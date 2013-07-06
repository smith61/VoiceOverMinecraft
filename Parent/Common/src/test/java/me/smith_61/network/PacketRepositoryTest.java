package me.smith_61.network;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import me.smith_61.network.vom.PacketAudioData;

import org.junit.Test;

public class PacketRepositoryTest {

	@Test(expected = IllegalStateException.class)
	public void testDoubleRegistry() {
		PacketRepository repo = new PacketRepository();
		
		repo.registerPacket(0, PacketClass.class);
		repo.registerPacket(0, PacketClass.class);
	}
	
	@Test
	public void testRegistry() {
		PacketRepository repo = new PacketRepository();
		
		repo.registerPacket(0, PacketClass.class);
		
		Packet packet = repo.createPacket(0);
		
		assertNotNull(packet);
		assertEquals(PacketClass.class, packet.getClass());
		
		packet = repo.createPacket(1);
		assertNull(packet);
	}
	
	@Test
	public void audioDataTest() throws IOException {
		PacketAudioData packet = new PacketAudioData("testplayer", new byte[0]);
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(byteOut);
		
		dataOut.writeByte(packet.getId());
		packet.writePacketData(dataOut);
		
		dataOut.close();
		
		byte[] data = byteOut.toByteArray();
		
		System.out.println("Data length: " + data.length);
	}

	public static final class PacketClass extends Packet {

		protected PacketClass() {
			super(0);
		}

		@Override
		public void readPacketData(DataInput in) throws IOException {
		}

		@Override
		public void writePacketData(DataOutput out) throws IOException {
		}
		
	}
}
