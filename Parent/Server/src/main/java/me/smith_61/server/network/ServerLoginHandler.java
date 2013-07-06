package me.smith_61.server.network;

import java.util.logging.Level;

import me.smith_61.VOMLogger;
import me.smith_61.network.vom.DelegatingPacketHandler;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketVersion;
import me.smith_61.server.player.ServerPlayerManager;

public class ServerLoginHandler extends DelegatingPacketHandler {

	private final String playerName;
	private final ServerPlayerManager playerManager;
	
	public ServerLoginHandler(String playerName, ServerPlayerManager playerManager) {
		if(playerName == null) {
			throw new NullPointerException("playerName");
		}
		if(playerManager == null) {
			throw new NullPointerException("playerManager");
		}
		
		this.playerName = playerName;
		this.playerManager = playerManager;
	}
	
	@DelegatingPacketHandler.PacketHandlerMethod(id = NetworkConnection.PACKET_VERSION_ID)
	public void onVersion(NetworkConnection connection, PacketVersion packet) {
		VOMLogger.logInfo("Recieved version packet with version: %s.", packet.getVersion());
		
		if(packet.getVersion() != NetworkConnection.NETWORK_VERSION) {
			VOMLogger.logFormat(Level.WARNING, "Invalid network version recieved from: %s. Expected: %d. Got: %d.", this.playerName, NetworkConnection.NETWORK_VERSION, packet.getVersion());
			connection.diconnect("Invalid network version: " + packet.getVersion());
		}
		else {
			connection.setPacketHandler(new ServerPacketHandler());
			this.playerManager.onPlayerConnect(this.playerName, connection);
		}
	}

}
