package me.smith_61.player;

import me.smith_61.network.vom.NetworkConnection;

public interface NetworkPlayer extends Player {

	
	/**
	 * Gets the connection for this player
	 * 
	 * @return The connection for this player
	 */
	NetworkConnection getConnection();
}
