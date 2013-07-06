package me.smith_61.client.audio.java;

import javax.sound.sampled.SourceDataLine;

import me.smith_61.client.audio.AudioFrame;

public class JavaAudioFrame extends AudioFrame {
	
	private int index;
	
	JavaAudioFrame(byte[] data) {
		this(data, 0, data.length);
	}
	
	JavaAudioFrame(byte[] data, int start, int length) {
		super(data, start, length);
		
		this.index = 0;
	}
	
	boolean pushData(SourceDataLine dataLine) {
		byte[] data = this.getData();
		
		int written = dataLine.write(data, this.index, data.length - this.index);
		this.index += written;
		
		return this.index == data.length;
	}
}
