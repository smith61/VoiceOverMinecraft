package me.smith_61.network.vom;

public class PacketPlayerConnect extends PacketPlayerBase {
	
	public PacketPlayerConnect() {
		super(NetworkConnection.PACKET_PLAYER_CONNECT);
	}
	
	public PacketPlayerConnect(String playerName) {
		super(NetworkConnection.PACKET_PLAYER_CONNECT, playerName);
	}
}
