package me.smith_61.network.vom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketAudioData extends PacketPlayerBase {

	private byte[] data;
	
	public PacketAudioData() {
		this(new byte[0]);
	}
	
	public PacketAudioData(byte[] data) {
		this("", data);
	}
	
	public PacketAudioData(String playerName, byte[] data) {
		super(NetworkConnection.PACKET_AUDIO_DATA_ID, playerName);
		
		if(data == null) {
			throw new NullPointerException("data");
		}
		
		this.data = data;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	@Override
	public void readPacketData(DataInput in) throws IOException {
		super.readPacketData(in);
		
		int length = in.readInt();
		if(length < 0) {
			length = 0;
		}
		
		this.data = new byte[length];
		in.readFully(this.data);
	}

	@Override
	public void writePacketData(DataOutput out) throws IOException {
		super.writePacketData(out);
		
		out.writeInt(this.data.length);
		out.write(this.data);
	}

}
