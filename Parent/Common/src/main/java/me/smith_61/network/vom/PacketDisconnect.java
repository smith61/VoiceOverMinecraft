package me.smith_61.network.vom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.smith_61.network.Packet;

public class PacketDisconnect extends Packet {

	private String reason;
	
	public PacketDisconnect(String reason) {
		this();
		
		this.reason = reason;
	}
	
	public PacketDisconnect() {
		super(NetworkConnection.PACKET_DISCONNECT_ID);
	}
	
	public String getReason() {
		return this.reason;
	}
	
	@Override
	public void readPacketData(DataInput in) throws IOException {
		this.reason = in.readUTF();
	}

	@Override
	public void writePacketData(DataOutput out) throws IOException {
		out.writeUTF(this.reason);
	}

}
