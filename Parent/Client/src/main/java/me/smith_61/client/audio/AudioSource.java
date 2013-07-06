package me.smith_61.client.audio;

/**
 * 
 * @author Jacob
 *
 * This interface represents an audio source for playing.
 */
public interface AudioSource {
	
	String getName();

	void feedRawAudio(byte[] audio);
	
	void feedRawAudio(byte[] audio, int start, int length);
	
	void feedRawAudio(AudioFrame audioFrame);
	
	AudioPlayer getAudioPlayer();
	
	boolean isPlaying();
}
