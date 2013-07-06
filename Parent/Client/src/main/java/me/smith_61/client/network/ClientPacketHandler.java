package me.smith_61.client.network;


import me.smith_61.VOMLogger;
import me.smith_61.client.player.ClientPlayerManager;
import me.smith_61.client.player.RemoteClientPlayer;
import me.smith_61.network.vom.DelegatingPacketHandler;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketAudioData;
import me.smith_61.network.vom.PacketPlayerConnect;
import me.smith_61.network.vom.PacketPlayerDisconnect;

public class ClientPacketHandler extends DelegatingPacketHandler {

	private final ClientPlayerManager playerManager;
	
	public ClientPacketHandler(ClientPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@DelegatingPacketHandler.PacketHandlerMethod(id = NetworkConnection.PACKET_AUDIO_DATA_ID)
	public void onAudioData(NetworkConnection connection, PacketAudioData packet) {
		RemoteClientPlayer player = this.playerManager.getRemotePlayer(packet.getPlayerName());
		if(player == null) {
			return;
		}
		
		VOMLogger.logInfo("Player audio recieved: %s", player.getName());
		
		player.feedRawAudio(packet.getData());
	}
	
	@DelegatingPacketHandler.PacketHandlerMethod(id = NetworkConnection.PACKET_PLAYER_CONNECT)
	public void onPlayerConnect(NetworkConnection connection, PacketPlayerConnect packet) {
		this.playerManager.playerConnected(packet.getPlayerName());
	}
	
	@DelegatingPacketHandler.PacketHandlerMethod(id = NetworkConnection.PACKET_PLAYER_DISCONNECT)
	public void onPlayerDisconnect(NetworkConnection connection, PacketPlayerDisconnect packet) {
		this.playerManager.playerDisconnected(packet.getPlayerName());
	}
}
