package me.smith_61.client.player;

import me.smith_61.client.audio.AudioPlayer;
import me.smith_61.client.audio.AudioSource;

public class RemoteClientPlayer implements ClientPlayer {

	private final String playerName;
	private final ClientPlayerManager playerManager;
	
	private boolean muted;
	
	private final AudioSource playerSource;
	
	public RemoteClientPlayer(String playerName, AudioPlayer player, ClientPlayerManager playerManager) {
		if(playerName == null) {
			throw new NullPointerException("playerName");
		}
		if(player == null) {
			throw new NullPointerException("player");
		}
		if(playerManager == null) {
			throw new NullPointerException("playerManager");
		}
		
		this.playerName = playerName;
		this.playerManager = playerManager;
		
		this.muted = false;
		
		this.playerSource = player.newSource(this.getName());
	}
	
	@Override
	public ClientPlayerManager getManager() {
		return this.playerManager;
	}
	
	@Override
	public String getName() {
		return this.playerName;
	}

	@Override
	public boolean isTalking() {
		return this.playerSource.isPlaying();
	}

	@Override
	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	@Override
	public boolean isMuted() {
		return this.muted;
	}

	public void feedRawAudio(byte[] audio) {
		this.feedRawAudio(audio, 0, audio.length);
	}

	public void feedRawAudio(byte[] audio, int start, int length) {
		if(this.isMuted()) {
			return;
		}
		
		this.getPlayerSource().feedRawAudio(audio, start, length);
	}
	
	public AudioSource getPlayerSource() {
		return this.playerSource;
	}
}
