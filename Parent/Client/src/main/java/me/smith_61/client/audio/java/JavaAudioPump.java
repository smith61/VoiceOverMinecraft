package me.smith_61.client.audio.java;

import java.util.concurrent.atomic.AtomicBoolean;

public class JavaAudioPump implements Runnable {

	private final JavaAudioPlayer audioPlayer;
	
	private final AtomicBoolean stopping;
	
	JavaAudioPump(JavaAudioPlayer audioPlayer) {
		if(audioPlayer == null) {
			throw new NullPointerException("audioPlayer");
		}
		
		this.audioPlayer = audioPlayer;
		
		this.stopping = new AtomicBoolean(false);
	}
	
	void stopPump() {
		this.stopping.set(true);
	}
	
	@Override
	public void run() {
		while(!this.stopping.get()) {
			this.audioPlayer.pumpOne();
		}
	}

}
