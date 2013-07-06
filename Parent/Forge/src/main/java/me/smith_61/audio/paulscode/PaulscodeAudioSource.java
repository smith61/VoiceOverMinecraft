package me.smith_61.audio.paulscode;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.smith_61.client.audio.AudioFrame;
import me.smith_61.client.audio.AudioSource;

@SideOnly(Side.CLIENT)
public class PaulscodeAudioSource implements AudioSource {
	
	private final PaulscodeAudioPlayer audioPlayer;
	private final String name;
	
	PaulscodeAudioSource(PaulscodeAudioPlayer audioPlayer, String name) {
		if(audioPlayer == null) {
			throw new NullPointerException("audioPlayer");
		}
		if(name == null) {
			throw new NullPointerException("name");
		}
		
		this.audioPlayer = audioPlayer;
		this.name = name;
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
		if(start < 0 || length < 0 || start + length > audio.length) {
			throw new ArrayIndexOutOfBoundsException("Start: " + start + ". Length: " + length + ". Array: " + audio.length);
		}
		
		byte[] buffer = new byte[length];
		System.arraycopy(audio, start, buffer, 0, length);
		
		this.getAudioPlayer().migrateSources();
		this.getAudioPlayer().getSoundSystem().feedRawAudioData(this.getName(), buffer);
		
	}

	@Override
	public void feedRawAudio(AudioFrame audioFrame) {
		this.feedRawAudio(audioFrame.getData());
	}

	@Override
	public PaulscodeAudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}
	
	@Override
	public boolean isPlaying() {
		return this.getAudioPlayer().getSoundSystem().playing(this.getName());
	}
}
