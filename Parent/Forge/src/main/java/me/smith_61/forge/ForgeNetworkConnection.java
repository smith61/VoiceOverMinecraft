package me.smith_61.forge;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import me.smith_61.VOMLogger;
import me.smith_61.network.Packet;
import me.smith_61.network.vom.NetworkConnection;

public class ForgeNetworkConnection extends NetworkConnection {

	private final INetworkManager manager;
	
	public ForgeNetworkConnection(INetworkManager manager) {
		if(manager == null) {
			throw new NullPointerException("manager");
		}
		this.manager = manager;
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
			
			this.manager.addToSendQueue(new Packet250CustomPayload(VOMMod.CHANNEL, data));
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

	public void dataRecieved(byte[] data) {
		super.processPacket(data);
	}
}
