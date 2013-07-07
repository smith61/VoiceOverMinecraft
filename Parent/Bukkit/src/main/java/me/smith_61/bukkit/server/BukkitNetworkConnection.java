package me.smith_61.bukkit.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import me.smith_61.VOMLogger;
import me.smith_61.network.Packet;
import me.smith_61.network.vom.NetworkConnection;

public class BukkitNetworkConnection extends NetworkConnection {

	private final Player bukkitPlayer;
	
	public BukkitNetworkConnection(Player bukkitPlayer) {
		if(bukkitPlayer == null) {
			throw new NullPointerException("bukkitPlayer");
		}
		
		this.bukkitPlayer = bukkitPlayer;
	}
	
	@Override
	public void sendPacket(Packet packet) {
		if(this.closed) {
			return;
		}
		
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			
			dataOut.writeByte(packet.getId());
			packet.writePacketData(dataOut);
			
			dataOut.close();
			
			byte[] data = byteOut.toByteArray();
			if(data.length >= Short.MAX_VALUE) {
				throw new IOException("Data array to long: " + data.length);
			}
			
			this.bukkitPlayer.sendPluginMessage(VOMPlugin.getInstance(), VOMPlugin.CHANNEL, data);
		}
		catch(Throwable t) {
			this.onNetworkError(t);
		}
	}

	@Override
	protected void onNetworkError(Throwable error) {
		this.diconnect("Network error: " + error.getClass() + ": " + error.getMessage());
		
		VOMLogger.logError(error, "Network error in connection for player: %s", this.getOwner() != null ? this.getOwner().getName() : "Not connected");
	}

	void onDataRecieved(byte[] packet) {
		if(packet == null) {
			return;
		}
		super.processPacket(packet);
	}
}
