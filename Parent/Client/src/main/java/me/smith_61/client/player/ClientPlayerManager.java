package me.smith_61.client.player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.smith_61.VOMLogger;
import me.smith_61.client.audio.AudioPlayer;
import me.smith_61.client.audio.AudioRecorder;
import me.smith_61.client.network.PacketAudioOutputStream;
import me.smith_61.network.vom.NetworkConnection;

public class ClientPlayerManager {

	private final AudioPlayer audioPlayer;
	
	private final AudioRecorder audioRecorder;
	private final LocalClientPlayer clientPlayer;
	
	private final Map<String, RemoteClientPlayer> remotePlayers;
	
	public ClientPlayerManager(String playerName, NetworkConnection serverConnection, AudioRecorder recorder, AudioPlayer audioPlayer) {
		if(playerName == null) {
			throw new NullPointerException("playerName");
		}
		if(serverConnection == null) {
			throw new NullPointerException("serverConnection");
		}
		if(recorder == null) {
			throw new NullPointerException("recorder");
		}
		if(audioPlayer == null) {
			throw new NullPointerException("audioPlayer");
		}
		
		this.audioPlayer = audioPlayer;
		
		this.audioRecorder = recorder;
		this.audioRecorder.setAudioOutputStream(new PacketAudioOutputStream(serverConnection));
		
		this.clientPlayer = new LocalClientPlayer(playerName, serverConnection, this.audioRecorder, this);
		
		this.remotePlayers = new HashMap<String, RemoteClientPlayer>();
	}
	
	public LocalClientPlayer getClientPlayer() {
		return this.clientPlayer;
	}
	
	public RemoteClientPlayer getRemotePlayer(String name) {
		return this.remotePlayers.get(name.trim().toLowerCase());
	}
	
	public RemoteClientPlayer[] getRemotePlayers() {
		return this.remotePlayers.values().toArray(new RemoteClientPlayer[0]);
	}
	
	public RemoteClientPlayer playerConnected(String name) {
		name = name.trim().toLowerCase();
		
		if(this.getRemotePlayer(name) != null) {
			VOMLogger.logFormat(Level.SEVERE, "Player already connected with name: %s", name);
			throw new IllegalStateException("Player already connected with name: " + name);
		}
		
		VOMLogger.logInfo("Player connected with name: %s", name);
		
		RemoteClientPlayer player = new RemoteClientPlayer(name, this.audioPlayer, this);
		
		this.remotePlayers.put(name, player);
		
		return player;
	}
	
	public void playerDisconnected(String name) {
		name = name.trim().toLowerCase();
		
		RemoteClientPlayer player = this.getRemotePlayer(name);
		if(player == null) {
			return;
		}
		
		VOMLogger.logInfo("Player disconnected with name: %s", name);
		
		this.audioPlayer.deleteSource(player.getPlayerSource());
		
		this.remotePlayers.remove(name);
	}
	
	public void shutdown() {
		VOMLogger.logInfo("Shutting down ClientPlayerManager");
		
		this.audioRecorder.shutdown();
		for(RemoteClientPlayer player : this.getRemotePlayers()) {
			this.playerDisconnected(player.getName());
		}
	}
}
