package me.smith_61.client.network;

import me.smith_61.client.audio.AudioFrame;
import me.smith_61.client.audio.AudioOutputStream;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketAudioData;

public class PacketAudioOutputStream implements AudioOutputStream {

	private final NetworkConnection connection;
	
	private int index = 0;
	private final AudioFrame[] bufferedFrames;
	
	public PacketAudioOutputStream(NetworkConnection connection) {
		if(connection == null) {
			throw new NullPointerException("connection");
		}
		this.connection = connection;
		this.bufferedFrames = new AudioFrame[2];
	}
	
	@Override
	public void writeAudioFrame(AudioFrame audioFrame) {
		if(this.index == -1) {
			this.connection.sendPacket(new PacketAudioData(audioFrame.getData()));
		}
		else {
			this.bufferedFrames[this.index] = audioFrame;
			this.index++;
			
			if(this.index == this.bufferedFrames.length) {
				this.flushFrames();
			}
		}
	}

	@Override
	public void flush() {
		this.flushFrames();
		
		this.index = 0;
	}
	
	private void flushFrames() {
		if(this.index == -1) {
			return;
		}
		
		int curIndex = this.index;
		this.index = -1;
		
		for(int i=0; i<curIndex; i++) {
			this.writeAudioFrame(this.bufferedFrames[i]);
			this.bufferedFrames[i] = null;
		}
	}
}
