package me.smith_61.client.player;

import me.smith_61.client.audio.AudioRecorder;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.player.NetworkPlayer;

public class LocalClientPlayer implements ClientPlayer, NetworkPlayer {
	
	private final String playerName;
	private final NetworkConnection connection;
	private final AudioRecorder recorder;
	private final ClientPlayerManager playerManager;
	
	private boolean muted;
	
	protected LocalClientPlayer(String name, NetworkConnection connection, AudioRecorder recorder, ClientPlayerManager playerManager) {
		if(name == null) {
			throw new NullPointerException("name");
		}
		if(connection == null) {
			throw new NullPointerException("connection");
		}
		if(recorder == null) {
			throw new NullPointerException("recorder");
		}
		if(playerManager == null) {
			throw new NullPointerException("playerManager");
		}
		
		this.playerName = name;
		this.connection = connection;
		this.recorder = recorder;
		this.playerManager = playerManager;
		
		this.muted = false;
		
		this.connection.setOwner(this);
	}
	
	@Override
	public ClientPlayerManager getManager() {
		return this.playerManager;
	}

	@Override
	public boolean isTalking() {
		return this.recorder.isRecording();
	}

	@Override
	public void setMuted(boolean muted) {
		if(muted) {
			this.recorder.stopRecording();
		}
		
		this.muted = muted;
	}

	@Override
	public boolean isMuted() {
		return this.muted;
	}

	@Override
	public String getName() {
		return this.playerName;
	}
	
	@Override
	public NetworkConnection getConnection() {
		return this.connection;
	}
	
	public void startTalking() {
		if(!this.isMuted()) {
			this.recorder.startRecording();
		}
	}
	
	public void stopTalking() {
		this.recorder.stopRecording();
	}
}
