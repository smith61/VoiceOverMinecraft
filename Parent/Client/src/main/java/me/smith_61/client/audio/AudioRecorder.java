package me.smith_61.client.audio;

import javax.sound.sampled.AudioFormat;

public interface AudioRecorder {
	
	/**
	 * The format used when recording and playing audio
	 */
	public static final AudioFormat FORMAT = new AudioFormat(8000f, 8, 1, false, false);

	
	/**
	 * Starts recording
	 */
	void startRecording();
	
	/**
	 * Stops recording
	 */
	void stopRecording();
	
	/**
	 * Gets if the recorder is recording
	 * 
	 * @return If the recorder is recording
	 */
	boolean isRecording();
	
	/**
	 * Shutsdown the recorder cleaning up any resources
	 * 	used by it
	 */
	void shutdown();
	
	/**
	 * Gets if the recorder is running and has not been
	 * 	shutdown
	 * 
	 * @return If the recorder is running
	 */
	boolean isRunning();
	
	/**
	 * Sets the output stream recorded frames
	 * 	are written to. This will replace any
	 * 	existing output stream
	 * 
	 * @param outputStream The stream to write too
	 */
	void setAudioOutputStream(AudioOutputStream outputStream);
	
	/**
	 * Gets the current output stream audio frames
	 * 	are being written too.
	 * 
	 * @return The current stream audio is being written to
	 */
	AudioOutputStream getAudioOutputStream();
}
