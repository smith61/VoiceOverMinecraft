package me.smith_61.client.audio;

public class AudioFrame {

	private final byte[] data;
	
	public AudioFrame(byte[] data) {
		this(data, 0, data.length);
	}
	
	public AudioFrame(byte[] data, int start, int length) {
		if(data == null) {
			throw new NullPointerException("data");
		}
		
		if(start < 0 || length < 0 || start + length > data.length) {
			throw new ArrayIndexOutOfBoundsException("Start: " + start + ". Length: " + length + ". Array: " + data.length);
		}
		
		this.data = new byte[length];
		System.arraycopy(data, start, this.data, 0, length);
	}
	
	public byte[] getData() {
		return this.data;
	}
}
