package me.smith_61.player;

public interface Player {

	/**
	 * Gets the name of the player
	 * 
	 * @return The name of the player
	 */
	String getName();
	
	/**
	 * Gets if the player is muted
	 * 
	 * @return If the player is muted
	 */
	boolean isMuted();
	
	/**
	 * Sets if the player should be muted
	 * 
	 * @param mute If the player should be muted
	 */
	void setMuted(boolean mute);
}
