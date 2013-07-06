package me.smith_61.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Packet {

	private final int id;
	
	protected Packet(int id) {
		if(id >= 256 || id < 0) {
			throw new IllegalArgumentException("Invalid packet id: " + id);
		}
		
		this.id = id;
	}
	
	public final int getId() {
		return this.id;
	}
	
	public abstract void readPacketData(DataInput in) throws IOException;
	
	public abstract void writePacketData(DataOutput out) throws IOException;
}
