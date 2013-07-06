package me.smith_61.network.vom;

public class PacketPlayerDisconnect extends PacketPlayerBase {
	
	public PacketPlayerDisconnect() {
		super(NetworkConnection.PACKET_PLAYER_DISCONNECT);
	}
	
	public PacketPlayerDisconnect(String playerName) {
		super(NetworkConnection.PACKET_PLAYER_DISCONNECT, playerName);
	}
}
