package me.smith_61.client.network;

import me.smith_61.VOMLogger;
import me.smith_61.client.player.ClientPlayerManager;
import me.smith_61.network.vom.DelegatingPacketHandler;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketVersion;

public class ClientLoginHandler extends DelegatingPacketHandler {

	private final ClientPlayerManager playerManager;
	
	public ClientLoginHandler(ClientPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@DelegatingPacketHandler.PacketHandlerMethod(id = NetworkConnection.PACKET_VERSION_ID)
	public void onVersionPacket(NetworkConnection connection, PacketVersion packet) {
		VOMLogger.logInfo("Recieved version packet with version: %d.", packet.getVersion());
		if(packet.getVersion() != NetworkConnection.NETWORK_VERSION) {
			VOMLogger.logInfo("Network versions do not match. Expected: %d. Got: %d.", NetworkConnection.NETWORK_VERSION, packet.getVersion());
			connection.diconnect("Non matching network versions.");
		}
		else {
			connection.sendPacket(new PacketVersion());
			connection.setPacketHandler(new ClientPacketHandler(this.playerManager));
		}
	}
}
