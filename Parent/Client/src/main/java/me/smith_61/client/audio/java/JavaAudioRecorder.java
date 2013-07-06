package me.smith_61.client.audio.java;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

import me.smith_61.VOMLogger;
import me.smith_61.client.audio.AudioFrame;
import me.smith_61.client.audio.AudioOutputStream;
import me.smith_61.client.audio.AudioRecorder;

public class JavaAudioRecorder implements Runnable, AudioRecorder {
	
	public static final AudioFormat FORMAT = new AudioFormat(8000f, 8, 1, false, false);
	
	private final AtomicBoolean recording;
	
	private final AtomicBoolean shutdown;
	
	private final Thread recorderThread;
	
	private final AtomicReference<AudioOutputStream> outputStream;
	
	public JavaAudioRecorder() {
		this.recording = new AtomicBoolean(false);
		
		this.shutdown = new AtomicBoolean(false);
		
		this.recorderThread = new Thread(this, "AudioRecorderThread");
		this.recorderThread.start();
		
		this.outputStream = new AtomicReference<AudioOutputStream>();
	}
	
	public void shutdown() {
		if(!this.shutdown.getAndSet(true)) {
			VOMLogger.logInfo("Shutting down AudioRecorder.");
			
			this.stopRecording();
			
			this.recorderThread.interrupt();
		}
	}
	
	public boolean isRunning() {
		return !this.shutdown.get();
	}
	
	public boolean isRecording() {
		return this.recording.get();
	}
	
	public void startRecording() {
		if(this.isRunning() && !this.recording.getAndSet(true)) {
			VOMLogger.logInfo("Recording started.");
			this.recorderThread.interrupt();
		}
	}
	
	public void stopRecording() {
		if(this.recording.getAndSet(false)) {
			VOMLogger.logInfo("Recording stopped.");
		}
	}

	@Override
	public void setAudioOutputStream(AudioOutputStream newStream) {
		if(!this.isRunning()) {
			return;
		}
		AudioOutputStream stream = this.outputStream.getAndSet(newStream);
		if(stream != newStream && stream != null) {
			stream.flush();
		}
	}

	@Override
	public AudioOutputStream getAudioOutputStream() {
		return this.outputStream.get();
	}
	
	@Override
	public void run() {
		TargetDataLine line = null;
		AudioOutputStream output = null;
		
		try {
			line = AudioSystem.getTargetDataLine(AudioRecorder.FORMAT);
			
			int bufferSize = ((int)(AudioRecorder.FORMAT.getFrameSize() * AudioRecorder.FORMAT.getSampleRate())) / 2;
			byte[] buffer = new byte[bufferSize];
			int read = 0;
			
			VOMLogger.logInfo("Audio buffer size: %d.", bufferSize);
			
			while(this.isRunning()) {
				line.open(AudioRecorder.FORMAT);
				
				if(this.isRecording()) {
					line.start();
					
					while(this.isRecording()) {
						read = line.read(buffer, 0, buffer.length);
						if(read == 0) {
							continue;
						}
						
						output = this.getAudioOutputStream();
						if(output == null) {
							continue;
						}
						
						output.writeAudioFrame(new AudioFrame(buffer, 0, read));
					}
					
					line.stop();
					line.flush();
					
					output = this.getAudioOutputStream();
					if(output != null) {
						output.flush();
					}
				}
				else {
					try {
						Thread.sleep(1000);
					}
					catch(InterruptedException ie) {}
				}
			}
			
		}
		catch(Throwable t) {
			VOMLogger.logError(t, "Error in AudioRecorderThread");
			
			this.shutdown();
		}
		finally {
			if(line != null) {
				line.close();
			}
			this.outputStream.set(null);
		}
	}
}
