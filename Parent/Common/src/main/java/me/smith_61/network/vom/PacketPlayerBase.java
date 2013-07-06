package me.smith_61.network.vom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.smith_61.network.Packet;

public abstract class PacketPlayerBase extends Packet {

	private String playerName;
	
	protected PacketPlayerBase(int id) {
		this(id, "");
	}
	
	protected PacketPlayerBase(int id, String playerName) {
		super(id);
		
		if(playerName == null) {
			throw new NullPointerException("playerName");
		}
		
		this.playerName = playerName;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	@Override
	public void readPacketData(DataInput in) throws IOException {
		int length = in.readInt();
		if(length < 0) {
			return;
		}
		char[] chars = new char[length];
		for(int i=0; i<length; i++) {
			chars[i] = in.readChar();
		}
		
		this.playerName = new String(chars);
	}

	@Override
	public void writePacketData(DataOutput out) throws IOException {
		char[] chars = this.playerName.toCharArray();
		
		out.writeInt(chars.length);
		for(int i=0; i<chars.length; i++) {
			out.writeChar(chars[i]);
		}
	}

}
