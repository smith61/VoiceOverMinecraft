package me.smith_61.bukkit.server;

import java.util.Map;
import java.util.logging.Level;

import me.smith_61.VOMLogger;
import me.smith_61.network.vom.PacketVersion;
import me.smith_61.server.network.ServerLoginHandler;
import me.smith_61.server.player.ServerPlayer;
import me.smith_61.server.player.ServerPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class VOMPlugin extends JavaPlugin implements Listener, PluginMessageListener {

	public static String CHANNEL = "VOM-Channel";
	
	private static VOMPlugin INSTANCE;
	
	public static VOMPlugin getInstance() {
		return VOMPlugin.INSTANCE;
	}
	
	private ServerPlayerManager playerManager;
	private Map<Player, BukkitNetworkConnection> connections;
	
	@Override
	public void onEnable() {
		VOMPlugin.INSTANCE = this;
		
		this.playerManager = new ServerPlayerManager();
		for(Player player : this.getServer().getOnlinePlayers()) {
			this.playerLoggedIn(player);
		}
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, VOMPlugin.CHANNEL);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, VOMPlugin.CHANNEL, this);
		
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		for(Player player : this.getServer().getOnlinePlayers()) {
			this.playerLoggedOut(player);
		}
		
		for(ServerPlayer player : this.playerManager.getPlayers()) {
			VOMLogger.logFormat(Level.WARNING, "Ghost player: %s.", player.getName());
		}
		VOMPlugin.INSTANCE = null;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerLoggedIn(PlayerLoginEvent event) {
		this.playerLoggedIn(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.playerLoggedOut(event.getPlayer());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equals(VOMPlugin.CHANNEL)) {
			return;
		}
		
		BukkitNetworkConnection connection = this.connections.get(player);
		if(connection == null) {
			return;
		}
		
		connection.onDataRecieved(message);
	}
	
	private void playerLoggedIn(Player player) {
		if(this.connections.containsKey(player)) {
			return;
		}
		
		BukkitNetworkConnection connection = new BukkitNetworkConnection(player);
		connection.setPacketHandler(new ServerLoginHandler(player.getName(), this.playerManager));
		connection.sendPacket(new PacketVersion());
		
		this.connections.put(player, connection);
	}
	
	private void playerLoggedOut(Player player) {
		if(!this.connections.containsKey(player)) {
			return;
		}
		
		this.playerManager.onPlayerDisconnect(player.getName());
		
		BukkitNetworkConnection connection = this.connections.remove(player);
		if(connection == null) {
			return;
		}
		connection.diconnect("Server shutdown.");
	}
}
