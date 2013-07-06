package me.smith_61.forge.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.smith_61.VOMLogger;
import me.smith_61.forge.KeyBindingManager;
import me.smith_61.forge.VOMMod;

@SideOnly(Side.CLIENT)
public class ClientKeyBindingManager extends KeyBindingManager {

	@Override
	public void load() {
		VOMLogger.logInfo("Loading Client KeyBindings.");
		
		KeyBindingRegistry.registerKeyBinding(new TalkKeyHandler());
	}
	
	@SideOnly(Side.CLIENT)
	private static class TalkKeyHandler extends KeyHandler {
		
		private TalkKeyHandler() {
			super(new KeyBinding[] { new KeyBinding("Talk", 19) }, new boolean[] { false });
		}

		@Override
		public String getLabel() {
			return "VOM Talk";
		}

		@Override
		public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
			if(tickEnd || Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
				return;
			}
			
			VOMLogger.logInfo("Talk key pressed.");
			
			if(VOMMod.INSTANCE.getClientPlayerManager() == null) {
				return;
			}
			VOMMod.INSTANCE.getClientPlayerManager().getClientPlayer().startTalking();
		}

		@Override
		public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
			if(tickEnd || Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()) {
				return;
			}
			
			VOMLogger.logInfo("Talk key released.");
			
			if(VOMMod.INSTANCE.getClientPlayerManager() == null) {
				return;
			}
			VOMMod.INSTANCE.getClientPlayerManager().getClientPlayer().stopTalking();
		}

		@Override
		public EnumSet<TickType> ticks() {
			return EnumSet.of(TickType.CLIENT);
		}
	}
}
