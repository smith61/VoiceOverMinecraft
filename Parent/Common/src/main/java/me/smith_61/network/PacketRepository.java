package me.smith_61.network;

public class PacketRepository {
	
	private final Class<? extends Packet>[] packetClasses;
	
	@SuppressWarnings("unchecked")
	public PacketRepository() {
		this.packetClasses = new Class[256];
	}
	
	/**
	 * Registers a Packet class with the given id
	 * 
	 * @param id The id of the packet
	 * @param packetClass The packet class
	 */
	public void registerPacket(int id, Class<? extends Packet> packetClass) {
		if(packetClass == null) {
			throw new NullPointerException("packetClass");
		}
		if(id < 0 || id >= this.packetClasses.length) {
			throw new IllegalArgumentException("Invalid packet id: " + id);
		}
		if(this.packetClasses[id] != null) {
			throw new IllegalStateException("Packet id already registered: " + id);
		}
		
		try {
			packetClass.newInstance();
			this.packetClasses[id] = packetClass;
		}
		catch(Throwable t) {
			throw new IllegalArgumentException("Invalid packet class: " + packetClass, t);
		}
	}
	
	/**
	 * Creates a packet with the given id
	 * 
	 * @param id The id of the packet to create
	 * 
	 * @return The created packet or null if no class is registered with the given id
	 */
	public Packet createPacket(int id) {
		if(id < 0 || id >= this.packetClasses.length) {
			return null;
		}
		if(this.packetClasses[id] == null) {
			return null;
		}
		else {
			try {
				return this.packetClasses[id].newInstance();
			}
			catch(Throwable t) {
				t.printStackTrace();
				return null;
			}
		}
	}
}
