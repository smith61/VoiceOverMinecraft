package me.smith_61.forge;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import me.smith_61.VOMLogger;
import me.smith_61.client.player.ClientPlayerManager;
import me.smith_61.server.player.ServerPlayerManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "VOM-Forge", name="VoiceOverMinecraft-Forge", version="1.0.0")
public class VOMMod {
	
	public static String CHANNEL = "VOM-Channel";
	
	@Instance
	public static VOMMod INSTANCE;
	
	
	// Start SERVER ONLY Fields
	
	@SideOnly(Side.SERVER)
	private ServerPlayerManager serverPlayerManager;
	
	// END SERVER ONLY Fields
	
	
	
	
	// Start CLIENT ONLY Fields
	
	@SideOnly(Side.CLIENT)
	private ClientPlayerManager clientPlayerManager;
	
	// End CLIENT ONLY Fields

	
	// Start Proxies
	
	@SidedProxy(clientSide = "me.smith_61.forge.client.ClientNetworkManager", serverSide = "me.smith_61.forge.server.ServerNetworkManager")
	public static NetworkManager networkManager;
	
	@SidedProxy(clientSide = "me.smith_61.forge.client.ClientKeyBindingManager", serverSide = "me.smith_61.forge.KeyBindingManager")
	public static KeyBindingManager keybindingManager;
	
	// End Proxies
	
	@EventHandler
	public void setupLogger(FMLPreInitializationEvent event) {
		Logger logger = VOMLogger.VOM_LOGGER;
		
		logger.setParent(event.getModLog());
		
		try {
			File logsDir = new File(event.getModConfigurationDirectory().getParentFile(), "logs");
			logsDir.mkdirs();
			
			File logFile = new File(logsDir, "VOM.log");
			VOMLogger.logInfo("Saving log to: %s", logFile);
			
			FileHandler handler = new FileHandler(logFile.getAbsolutePath());
			
			logger.addHandler(handler);
		}
		catch(Throwable t) {
			VOMLogger.logError(t, "Error creating VOM Log File.");
		}
	}
	
	@EventHandler
	public void initMod(FMLInitializationEvent event) {
		
		VOMLogger.logInfo("Starting VOMMod-Forge on side: %s.", event.getSide());
		
		VOMLogger.logInfo("Initializing NetworkManager.");
		VOMMod.networkManager.initialize();
		
		VOMLogger.logInfo("Registering packet listener on channel: %s.", VOMMod.CHANNEL);
		
		NetworkRegistry instance = NetworkRegistry.instance();
		
		instance.registerConnectionHandler(VOMMod.networkManager);
		instance.registerChannel(VOMMod.networkManager, VOMMod.CHANNEL);
		
		VOMMod.keybindingManager.load();
	}

	// Start SERVER ONLY Methods
	
	@SideOnly(Side.SERVER)
	public ServerPlayerManager getServerPlayerManager() {
		return this.serverPlayerManager;
	}
	
	@SideOnly(Side.SERVER)
	public void setServerPlayerManager(ServerPlayerManager manager) {
		this.serverPlayerManager = manager;
	}
	
	// End SERVER ONLY Methods
	
	
	
	// Start CLIENT ONLY Methods
	
	@SideOnly(Side.CLIENT)
	public ClientPlayerManager getClientPlayerManager() {
		return this.clientPlayerManager;
	}
	
	@SideOnly(Side.CLIENT)
	public void setClientPlayerManager(ClientPlayerManager manager) {
		this.clientPlayerManager = manager;
	}
	
	// End CLIENT ONLY Methods
}
