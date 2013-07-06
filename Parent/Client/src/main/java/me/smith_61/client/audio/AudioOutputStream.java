package me.smith_61.client.audio;

public interface AudioOutputStream {

	void writeAudioFrame(AudioFrame audioFrame);
	
	void flush();
}
