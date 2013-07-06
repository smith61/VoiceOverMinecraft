package me.smith_61.network.vom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.smith_61.network.Packet;

public class PacketVersion extends Packet {

	private int version;
	
	public PacketVersion() {
		super(NetworkConnection.PACKET_VERSION_ID);
		
		this.version = NetworkConnection.NETWORK_VERSION;
	}
	
	public int getVersion() {
		return this.version;
	}

	@Override
	public void readPacketData(DataInput in) throws IOException {
		this.version = in.readInt();
	}

	@Override
	public void writePacketData(DataOutput out) throws IOException {
		out.writeInt(this.version);
	}
}
