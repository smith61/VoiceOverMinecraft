package me.smith_61.server.network;

import me.smith_61.network.vom.DelegatingPacketHandler;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketAudioData;
import me.smith_61.server.player.ServerPlayer;

public class ServerPacketHandler extends DelegatingPacketHandler {
	
	@DelegatingPacketHandler.PacketHandlerMethod(id=NetworkConnection.PACKET_AUDIO_DATA_ID)
	public void onAudioData(NetworkConnection connection, PacketAudioData packet) {
		((ServerPlayer)connection.getOwner()).onAudioData(packet.getData());
	}
}
