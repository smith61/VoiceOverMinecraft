package me.smith_61.network.vom;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import me.smith_61.VOMLogger;
import me.smith_61.network.Packet;
import me.smith_61.network.PacketRepository;
import me.smith_61.player.NetworkPlayer;

public abstract class NetworkConnection {

	public static final int NETWORK_VERSION = 1;
	
	public static final int PACKET_DISCONNECT_ID = 0;
	public static final int PACKET_VERSION_ID = 1;
	public static final int PACKET_PLAYER_CONNECT = 2;
	public static final int PACKET_PLAYER_DISCONNECT = 3;
	public static final int PACKET_AUDIO_DATA_ID = 4;
	
	public static final PacketRepository repo = new PacketRepository();
	
	private PacketHandler packetHandler;
	private NetworkPlayer owner;
	
	protected boolean closed;
	
	protected NetworkConnection() {
		this.packetHandler = null;
		this.owner = null;
		
		this.closed = false;
	}
	
	public NetworkPlayer getOwner() {
		return this.owner;
	}
	
	public void setOwner(NetworkPlayer owner) {
		if(this.closed || owner == null) {
			return;
		}
		
		if(this.owner != null) {
			throw new IllegalStateException("Owner already set.");
		}
		
		this.owner = owner;
	}
	
	/**
	 * Sends a packet
	 * 
	 * @param packet The packet to send
	 */
	public abstract void sendPacket(Packet packet);
	
	protected abstract void onNetworkError(Throwable error);
	
	/**
	 * Disconnects this connection with the given reason
	 * 
	 * @param reason The reason for the disconnection
	 */
	public void diconnect(String reason) {
		if(this.closed) {
			return;
		}
		this.sendPacket(new PacketDisconnect(reason));
		
		this.close();
	}
	
	/**
	 * Gets the PacketHandler for this connection
	 * 
	 * @return This connections PacketHandler
	 */
	public PacketHandler getPacketHandler() {
		return this.packetHandler;
	}
	
	/**
	 * Sets the PacketHandler for this connection
	 * 
	 * @param handler The new PacketHandler
	 */
	public void setPacketHandler(PacketHandler handler) {
		this.packetHandler = handler;
	}
	
	/**
	 * Gets if this connection is closed
	 * 
	 * @return If the connection is closed
	 */
	public boolean isClosed() {
		return this.closed;
	}
	
	protected void close() {
		this.closed = true;
	}
	
	protected final void processPacket(byte[] packetData) {
		if(this.closed) {
			return;
		}
		if(packetData == null) {
			throw new NullPointerException("packetData");
		}
		ByteArrayInputStream byteIn = new ByteArrayInputStream(packetData);
		DataInputStream dataIn = new DataInputStream(byteIn);
		
		try {
			int id = dataIn.readUnsignedByte();
			
			Packet packet = NetworkConnection.repo.createPacket(id);
			if(packet == null) {
				throw new IOException("Invalid packet id: " + id);
			}
			
			packet.readPacketData(dataIn);
			
			if(packet.getId() == NetworkConnection.PACKET_DISCONNECT_ID) {
				this.close();
				
				VOMLogger.logInfo("Closing connection. Reason: %s.", ((PacketDisconnect)packet).getReason());
			}
			
			if(this.packetHandler != null) {
				this.packetHandler.handlePacket(this, packet);
			}
		}
		catch(Throwable t) {
			this.onNetworkError(t);
		}
	}
	
	static {
		NetworkConnection.repo.registerPacket(PACKET_DISCONNECT_ID, PacketDisconnect.class);
		NetworkConnection.repo.registerPacket(PACKET_VERSION_ID, PacketVersion.class);
		NetworkConnection.repo.registerPacket(PACKET_PLAYER_CONNECT, PacketPlayerConnect.class);
		NetworkConnection.repo.registerPacket(PACKET_PLAYER_DISCONNECT, PacketPlayerDisconnect.class);
		NetworkConnection.repo.registerPacket(PACKET_AUDIO_DATA_ID, PacketAudioData.class);
	}
}
