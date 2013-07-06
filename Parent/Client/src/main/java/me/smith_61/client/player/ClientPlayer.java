package me.smith_61.client.player;

import me.smith_61.player.Player;

public interface ClientPlayer extends Player {

	/**
	 * Gets if the player is talking
	 * 
	 * @return If the player is talking
	 */
	boolean isTalking();
	
	/**
	 * Gets the manager for this player
	 * 
	 * @return The manager for this player
	 */
	ClientPlayerManager getManager();
}
