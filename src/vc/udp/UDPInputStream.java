package vc.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPInputStream {
	
	public DatagramSocket socket;
	private int chunkSize;
	
	public UDPInputStream(DatagramSocket s, int chunkSize) {
		this.socket = s;
		this.chunkSize = chunkSize;
	}

	/**
	 * Ignores if socket isn't connected.
	 * 
	 * @return byte[] data
	 * 
	 * @throws SocketException when something fucks up
	 */
	public synchronized byte[] read() throws SocketException {
		if (socket.isClosed()) throw new SocketException("Socket closed, please reopen.");
		
		DatagramPacket packet = new DatagramPacket(new byte[chunkSize], chunkSize);
		
		try { // io exception should be fixed here, upper layer shouldn't have to worry. (lucky me its udp ;) )
			socket.receive(packet);
		} catch (IOException e) {
			// TODO handle
			e.printStackTrace();
		}

		System.out.println("read: " + packet.getAddress() + ":" + packet.getPort()  + "  -->  " + new String(packet.getData()));
		
		return packet.getData();
	}
}
