package me.smith_61.server.player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.smith_61.VOMLogger;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketPlayerConnect;
import me.smith_61.network.vom.PacketPlayerDisconnect;

public class ServerPlayerManager {
	
	private final Map<String, ServerPlayer> players;
	
	public ServerPlayerManager() {
		this.players = new HashMap<String, ServerPlayer>();
	}
	
	public ServerPlayer getPlayer(String name) {
		return this.players.get(name.trim().toLowerCase());
	}
	
	public ServerPlayer[] getPlayers() {
		return this.players.values().toArray(new ServerPlayer[0]);
	}
	
	public void onPlayerConnect(String name, final NetworkConnection connection) {
		name = name.trim().toLowerCase();
		if(this.getPlayer(name) != null) {
			VOMLogger.logFormat(Level.WARNING, "Player already connected: %s.", name);
			throw new IllegalStateException("Player already connected: " + name);
		}
		
		VOMLogger.logInfo("Player connected: %s.", name);
		
		ServerPlayer player = new ServerPlayer(name, connection, this);
		
		PacketPlayerConnect playerConnect = new PacketPlayerConnect(player.getName());
		
		for(ServerPlayer other : this.getPlayers()) {
			other.getConnection().sendPacket(playerConnect);
			player.getConnection().sendPacket(new PacketPlayerConnect(other.getName()));
		}
		
		this.players.put(name, player);
	}
	
	public void onPlayerDisconnect(String name) {
		name = name.trim().toLowerCase();
		if(this.getPlayer(name) == null) {
			return;
		}
		
		VOMLogger.logInfo("Player disconnected: %s.", name);
		
		ServerPlayer player = this.players.remove(name);
		PacketPlayerDisconnect playerDisconnect = new PacketPlayerDisconnect(player.getName());
		
		for(ServerPlayer other : this.getPlayers()) {
			other.getConnection().sendPacket(playerDisconnect);
		}
	}
}
