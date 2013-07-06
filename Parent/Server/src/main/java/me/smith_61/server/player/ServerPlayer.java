package me.smith_61.server.player;


import me.smith_61.VOMLogger;
import me.smith_61.network.vom.NetworkConnection;
import me.smith_61.network.vom.PacketAudioData;
import me.smith_61.player.NetworkPlayer;

public class ServerPlayer implements NetworkPlayer {

	private final String playerName;
	private final NetworkConnection connection;
	private final ServerPlayerManager playerManager;
	
	private boolean isMuted;
	
	public ServerPlayer(String playerName, NetworkConnection connection, ServerPlayerManager playerManager) {
		if(playerName == null) {
			throw new NullPointerException("playerName");
		}
		if(connection == null) {
			throw new NullPointerException("connection");
		}
		if(playerManager == null) {
			throw new NullPointerException("playerManager");
		}
		
		this.playerName = playerName;
		
		this.connection = connection;
		
		this.playerManager = playerManager;
		
		this.connection.setOwner(this);
	}
	
	public ServerPlayerManager getPlayerManager() {
		return this.playerManager;
	}
	
	@Override
	public NetworkConnection getConnection() {
		return this.connection;
	}
	
	@Override
	public String getName() {
		return this.playerName;
	}

	@Override
	public void setMuted(boolean muted) {
		this.isMuted = muted;
	}

	@Override
	public boolean isMuted() {
		return this.isMuted;
	}

	public void onAudioData(byte[] data) {
		if(this.isMuted()) {
			return;
		}
		
		VOMLogger.logInfo("Audio data recieved from: %s.", this.getName());
		
		PacketAudioData packet = new PacketAudioData(this.getName(), data);
		for(ServerPlayer player : this.getPlayerManager().getPlayers()) {
			if(player != this) {
				player.getConnection().sendPacket(packet);
			}
		}
	}
}
