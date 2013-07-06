package me.smith_61.client.audio;

import java.util.HashMap;
import java.util.Map;

import me.smith_61.VOMLogger;

public abstract class AudioPlayer {

	private Map<String, AudioSource> sources;
	
	protected AudioPlayer() {
		this.sources = new HashMap<String, AudioSource>();
	}
	
	protected abstract AudioSource createSource(String name);

	public AudioSource newSource(String name) {
		name = name.trim().toLowerCase();
		
		if(this.getSource(name) != null) {
			this.deleteSource(name);
		}
		
		VOMLogger.logInfo("Creating AudioSource: %s", name);
		
		AudioSource source = this.createSource(name);
		
		this.sources.put(name, source);
		
		return source;
	}
	
	public AudioSource getSource(String name) {
		return this.sources.get(name.trim().toLowerCase());
	}
	
	public AudioSource[] getSources() {
		return this.sources.values().toArray(new AudioSource[0]);
	}
	
	public boolean deleteSource(String name) {
		return this.deleteSource(this.getSource(name));
	}
	
	public boolean deleteSource(AudioSource source) {
		if(source == null || source.getAudioPlayer() != this) {
			return false;
		}
		
		String name = source.getName().trim().toLowerCase();
		VOMLogger.logInfo("Deleting AudioSource: %s", name);
		
		AudioSource other = this.getSource(name);
		if(other != source) {
			return false;
		}
		
		this.sources.remove(name);
		return true;
	}
	
	public void shutdown() {
		for(AudioSource source : this.getSources()) {
			this.deleteSource(source);
		}
	}
}
