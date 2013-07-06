package me.smith_61.client.audio.java;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.SourceDataLine;

import me.smith_61.client.audio.AudioFrame;
import me.smith_61.client.audio.AudioSource;

public class JavaAudioSource implements AudioSource {

	private final JavaAudioPlayer audioPlayer;
	private final String name;
	
	private final SourceDataLine dataLine;
	
	private final LinkedList<JavaAudioFrame> audioFrames;
	
	private AtomicBoolean isClosed;
	
	JavaAudioSource(JavaAudioPlayer audioPlayer, String name, SourceDataLine dataLine) {
		if(audioPlayer == null) {
			throw new NullPointerException("audioPlayer");
		}
		if(name == null) {
			throw new NullPointerException("name");
		}
		if(dataLine == null) {
			throw new NullPointerException("dataLine");
		}
		
		this.audioPlayer = audioPlayer;
		this.name = name;
		
		this.dataLine = dataLine;
		
		this.audioFrames = new LinkedList<JavaAudioFrame>();
		this.isClosed = new AtomicBoolean(false);
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void feedRawAudio(byte[] audio) {
		this.feedRawAudio(audio, 0, audio.length);
	}

	@Override
	public void feedRawAudio(byte[] audio, int start, int length) {
		if(this.isClosed.get()) {
			return;
		}
		
		JavaAudioFrame audioFrame = new JavaAudioFrame(audio, start, length);
		
		synchronized(this.audioFrames) {
			this.audioFrames.add(audioFrame);
		}
	}
	
	@Override
	public void feedRawAudio(AudioFrame audioFrame) {
		this.feedRawAudio(audioFrame.getData());
	}

	@Override
	public JavaAudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}
	
	@Override
	public boolean isPlaying() {
		return this.dataLine.isRunning();
	}
	
	void cleanup() {
		if(this.isClosed.get()) {
			return;
		}
		this.isClosed.set(true);
		
		synchronized(this.audioFrames) {
			this.audioFrames.clear();
		}
		
		this.dataLine.close();
	}

	void pumpAudio() {
		if(this.isClosed.get()) {
			return;
		}
		JavaAudioFrame audioFrame;
		synchronized(this.audioFrames) {
			audioFrame = this.audioFrames.peek();
		}
		
		if(audioFrame == null) {
			return;
		}
		
		if(audioFrame.pushData(this.dataLine)) {
			synchronized(this.audioFrames) {
				this.audioFrames.poll();
			}
		}
	}
}
