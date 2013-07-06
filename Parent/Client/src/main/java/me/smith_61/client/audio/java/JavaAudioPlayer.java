package me.smith_61.client.audio.java;

import java.util.LinkedList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import me.smith_61.VOMLogger;
import me.smith_61.client.audio.AudioPlayer;
import me.smith_61.client.audio.AudioRecorder;
import me.smith_61.client.audio.AudioSource;
import me.smith_61.client.audio.NullAudioSource;

public class JavaAudioPlayer extends AudioPlayer {
	
	private final LinkedList<JavaAudioSource> sources;
	
	private JavaAudioPump audioPump;
	
	public JavaAudioPlayer() {
		this.sources = new LinkedList<JavaAudioSource>();
	}
	
	@Override
	protected AudioSource createSource(String name) {
		try {
			SourceDataLine dataLine = AudioSystem.getSourceDataLine(AudioRecorder.FORMAT);
			dataLine.open(AudioRecorder.FORMAT);
			dataLine.start();
			
			JavaAudioSource source = new JavaAudioSource(this, name, dataLine);
			
			synchronized(this.sources) {
				this.sources.add(source);
			}
			
			
			if(this.audioPump == null) {
				this.audioPump = new JavaAudioPump(this);
				new Thread(this.audioPump, "VOMJavaAudioPump").start();
			}
			
			return source;
		}
		catch(Throwable t) {
			VOMLogger.logError(t, "Error creating new audio source with name: %s.", name);
		}
		return new NullAudioSource(this, name);
	}

	@Override
	public boolean deleteSource(AudioSource source) {
		if(super.deleteSource(source)) {
			((JavaAudioSource)source).cleanup();
			
			synchronized(this.sources) {
				this.sources.remove(source);
				if(this.sources.size() == 0) {
					this.audioPump.stopPump();
					this.audioPump = null;
				}
			}
			return true;
		}
		return false;
	}
	
	void pumpOne() {
		JavaAudioSource audioSource;
		synchronized(this.sources) {
			audioSource = this.sources.peek();
		}
		
		if(audioSource == null) {
			return;
		}
		
		audioSource.pumpAudio();
		
		synchronized(this.sources) {
			if(this.sources.remove(audioSource)) {
				this.sources.add(audioSource);
			}
		}
	}
}
