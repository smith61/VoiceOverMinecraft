package me.smith_61.audio.paulscode;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.smith_61.VOMLogger;
import me.smith_61.client.audio.AudioPlayer;
import me.smith_61.client.audio.AudioRecorder;
import me.smith_61.client.audio.AudioSource;

@SideOnly(Side.CLIENT)
public class PaulscodeAudioPlayer extends AudioPlayer {

	private SoundSystem soundSystem;
	
	public PaulscodeAudioPlayer() {
		this.soundSystem = Minecraft.getMinecraft().sndManager.sndSystem;
	}
	
	public SoundSystem getSoundSystem() {
		return this.soundSystem;
	}
	
	@Override
	protected AudioSource createSource(String name) {
		this.migrateSources();
		
		PaulscodeAudioSource source = new PaulscodeAudioSource(this, name);
		
		this.soundSystem.rawDataStream(AudioRecorder.FORMAT, false, name, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
		this.soundSystem.setVolume(name, 1.0f);
		
		return source;
	}

	@Override
	public boolean deleteSource(AudioSource source) {
		if(super.deleteSource(source)) {
			if(!this.soundSystemChanged()) {
				this.soundSystem.removeSource(source.getName());
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteSource(String name) {
		if(super.deleteSource(name)) {
			if(!this.soundSystemChanged()) {
				this.soundSystem.removeSource(name);
			}
			return true;
		}
		return false;
	}
	
	private boolean soundSystemChanged() {
		return this.soundSystem != Minecraft.getMinecraft().sndManager.sndSystem;
	}
	
	void migrateSources() {
		if(!this.soundSystemChanged()) {
			return;
		}
		
		VOMLogger.logInfo("Migrating sounds. Sound system changed.");
		
		this.soundSystem = Minecraft.getMinecraft().sndManager.sndSystem;
		
		for(AudioSource source : this.getSources()) {
			this.soundSystem.rawDataStream(AudioRecorder.FORMAT, false, source.getName(), 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
			this.soundSystem.setVolume(source.getName(), 1.0f);
			
			VOMLogger.logInfo("Migrated audio source: %s.", source.getName());
		}
	}
}
