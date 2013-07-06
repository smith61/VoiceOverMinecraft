package me.smith_61.network.vom;

import me.smith_61.network.Packet;

public interface PacketHandler {

	void handlePacket(NetworkConnection connection, Packet packet) throws Exception;
}
