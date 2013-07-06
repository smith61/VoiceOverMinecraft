package me.smith_61.network.vom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import me.smith_61.VOMLogger;
import me.smith_61.network.Packet;

public abstract class DelegatingPacketHandler implements PacketHandler {

	protected void onUnexpectedPacket(NetworkConnection connection, Packet packet) {
		String reason = "Unexpected packet: " + packet.getClass() + " in PacketHandler: " + this.getClass();
		VOMLogger.logFormat(Level.WARNING, reason);
		connection.diconnect(reason);
	}
	
	private Method[] handleMethods;
	
	protected DelegatingPacketHandler() {
		this.handleMethods = new Method[256];
		
		VOMLogger.logInfo("Parsing packet handler class: %s.", this.getClass());
		
		int totalRegisteredMethods = 0;
		
		for(Method method : this.getClass().getMethods()) {
			if(Modifier.isStatic(method.getModifiers()) || Modifier.isAbstract(method.getModifiers())) {
				continue;
			}
			
			PacketHandlerMethod anno = method.getAnnotation(PacketHandlerMethod.class);
			if(anno == null) {
				continue;
			}
			
			int id = anno.id();
			if(id < 0 || id >= this.handleMethods.length) {
				VOMLogger.logFormat(Level.WARNING, "Method: %s has invalid packet id: %d.", method, id);
				continue;
			}
			Class<?>[] params = method.getParameterTypes();
			if(params.length != 2 || (!NetworkConnection.class.isAssignableFrom(params[0]) || !Packet.class.isAssignableFrom(params[1]))) {
				VOMLogger.logFormat(Level.WARNING, "Invalid method signature: %s", method);
				continue;
			}
			
			if(this.handleMethods[id] != null) {
				VOMLogger.logFormat(Level.WARNING, "ID: %d is already handled by method: %s.", id, method);
				continue;
			}
			
			VOMLogger.logInfo("Registered method: %s for packet id: %d.", method, id);
			this.handleMethods[id] = method;
			totalRegisteredMethods++;
		}
		
		VOMLogger.logInfo("Found %d handler methods.", totalRegisteredMethods);
	}
	
	@Override
	public void handlePacket(NetworkConnection connection, Packet packet) throws Exception {
		int id = packet.getId();
		
		Method handle = this.handleMethods[id];
		if(handle != null) {
			handle.invoke(this, connection, packet);
		}
		else {
			this.onUnexpectedPacket(connection, packet);
		}
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface PacketHandlerMethod {

		int id();
		
	}
}
