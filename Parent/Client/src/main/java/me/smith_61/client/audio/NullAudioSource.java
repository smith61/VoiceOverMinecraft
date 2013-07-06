package me.smith_61.client.audio;

public class NullAudioSource implements AudioSource {

	private final AudioPlayer audioPlayer;
	private final String name;
	
	public NullAudioSource(AudioPlayer audioPlayer, String name) {
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
	public void feedRawAudio(byte[] audio) {}

	@Override
	public void feedRawAudio(byte[] audio, int start, int length) {}

	@Override
	public void feedRawAudio(AudioFrame audioFrame) {}

	@Override
	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}
	
	@Override
	public boolean isPlaying() {
		return false;
	}
}
