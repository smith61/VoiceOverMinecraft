package me.smith_61.forge.server;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.smith_61.VOMLogger;
import me.smith_61.forge.ForgeNetworkConnection;
import me.smith_61.forge.NetworkManager;
import me.smith_61.forge.VOMMod;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketVersion;
import me.smith_61.server.network.ServerLoginHandler;
import me.smith_61.server.player.ServerPlayerManager;

@SideOnly(Side.SERVER)
public class ServerNetworkManager extends NetworkManager {
	
	private final Map<INetworkManager, ForgeNetworkConnection> networkConnections;
	
	private boolean initialized;
	
	public ServerNetworkManager() {
		this.networkConnections = new HashMap<INetworkManager, ForgeNetworkConnection>();
		
		this.initialized = false;
	}
	
	@Override
	protected void initialize() {
		if(this.initialized) {
			return;
		}
		
		VOMMod.INSTANCE.setServerPlayerManager(new ServerPlayerManager());
		this.initialized = true;
	}
	
	@Override
	public void playerLoggedIn(Player player, NetHandler handler, INetworkManager manager) {
		EntityPlayer ePlayer = (EntityPlayer)player;
		VOMLogger.logInfo("Played: %s logged in. Sending version packet with version: %d.", ePlayer.getEntityName(), NetworkConnection.NETWORK_VERSION);
		
		ForgeNetworkConnection connection = new ForgeNetworkConnection(manager);
		this.networkConnections.put(manager, connection);
		
		connection.setPacketHandler(new ServerLoginHandler(ePlayer.getEntityName(), VOMMod.INSTANCE.getServerPlayerManager()));
		
		connection.sendPacket(new PacketVersion());
	}
	
	@Override
	public void connectionClosed(INetworkManager manager) {
		ForgeNetworkConnection connection = this.networkConnections.remove(manager);
		
		if(connection == null || connection.getOwner() == null) {
			return;
		}
		
		String playerName = connection.getOwner().getName();
		VOMLogger.logInfo("Player: %s closed connection.", playerName);
		
		VOMMod.INSTANCE.getServerPlayerManager().onPlayerDisconnect(playerName);
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.data == null) {
			return;
		}
		this.networkConnections.get(manager).dataRecieved(packet.data);
	}

}
