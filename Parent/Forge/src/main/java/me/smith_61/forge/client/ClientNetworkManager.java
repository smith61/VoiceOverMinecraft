package me.smith_61.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.smith_61.VOMLogger;
import me.smith_61.client.audio.AudioPlayer;
import me.smith_61.client.audio.java.JavaAudioPlayer;
import me.smith_61.client.audio.java.JavaAudioRecorder;
import me.smith_61.client.network.ClientLoginHandler;
import me.smith_61.client.player.ClientPlayerManager;
import me.smith_61.forge.ForgeNetworkConnection;
import me.smith_61.forge.NetworkManager;
import me.smith_61.forge.VOMMod;

@SideOnly(Side.CLIENT)
public class ClientNetworkManager extends NetworkManager {
	
	private final AudioPlayer audioPlayer;
	
	public ClientNetworkManager() {
		this.audioPlayer = new JavaAudioPlayer();
	}
	
	private ForgeNetworkConnection networkConnection;
	
	@Override
	public void connectionOpened(NetHandler netHandler, String server, int port, INetworkManager netManager) {
		String playerName = Minecraft.getMinecraft().func_110432_I().func_111285_a();
		VOMLogger.logInfo("Connetion opened to server. Connecting with player name: %s. Initializing client.", playerName);
		
		this.networkConnection = new ForgeNetworkConnection(netManager);
		
		ClientPlayerManager playerManager = new ClientPlayerManager(playerName, this.networkConnection, new JavaAudioRecorder(), this.audioPlayer);
		this.networkConnection.setPacketHandler(new ClientLoginHandler(playerManager));
		
		VOMMod.INSTANCE.setClientPlayerManager(playerManager);
	}
	
	@Override
	public void connectionClosed(INetworkManager manager) {
		if(this.networkConnection == null) {
			return;
		}
		
		VOMLogger.logInfo("Connection closed to server. Shutting down client.");
		
		VOMMod.INSTANCE.getClientPlayerManager().shutdown();
		VOMMod.INSTANCE.setClientPlayerManager(null);
		
		this.networkConnection = null;
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.data == null) {
			return;
		}
		this.networkConnection.dataRecieved(packet.data);
	}

}
