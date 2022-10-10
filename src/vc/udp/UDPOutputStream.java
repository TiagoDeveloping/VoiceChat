package vc.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class UDPOutputStream {
	
	private SocketAddress addr;
	
	public DatagramSocket socket;
	
	public UDPOutputStream(DatagramSocket s) {
		this.addr = s.getRemoteSocketAddress();
		
		this.socket = s;
	}

	/**
	 * Ignores if socket isn't connected.
	 * 
	 * @param data data
	 * @throws SocketException when something fucks up
	 */
	public synchronized void write(byte[] data) throws SocketException {
		if (socket.isClosed()) throw new SocketException("Socket closed, please reopen.");
		
		try { // ioexception is sending problem, upper layer shouldn't have to worry
			socket.send(new DatagramPacket(data, data.length, addr));
		} catch (IOException e) {
			// TODO handle
			e.printStackTrace();
		}
		
		//System.out.println("write: " + new String(data) + "  -->  " + addr.toString());
	}
}
