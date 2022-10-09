package vc.main;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import vc.handlers.UDPBufferedVoiceHandler;
import vc.udp.UDPInputStream;
import vc.udp.UDPOutputStream;

public class SocketConnector {

	private DatagramSocket socketIn, socketOut;
	private int inport, outport, chunkSize;
	private String outAddr;
	
	public UDPInputStream udpInStream;
	public UDPOutputStream udpOutStream;
	
	public SocketConnector(int listenChunkSize ,int listeningPort, int sendingPort, String sendingAddr) {
		this.inport = listeningPort;
		this.outport = sendingPort;
		this.outAddr = sendingAddr;
		this.chunkSize = listenChunkSize;
	}
	
	public synchronized void connect() {
		/* Register listening socket */
		try {
			socketIn = new DatagramSocket(inport);
			udpInStream = new UDPInputStream(socketIn, chunkSize);
			Main.main.registerThread(new Thread(new UDPBufferedVoiceHandler(udpInStream)), "UDPInStreamHandler"); // TODO cleanup traintrack
			
			System.out.println("listening on: " + socketIn.getLocalSocketAddress() + " | " + socketIn.isBound());
		} catch (SocketException listenExc) {
			listenExc.printStackTrace(); // TODO handle
		}
		
		/* Register sending socket */
		try {
			socketOut = new DatagramSocket();
			socketOut.connect(new InetSocketAddress(outAddr, outport));
			udpOutStream = new UDPOutputStream(socketOut);
			
			System.out.println("sending to: " + socketOut.getRemoteSocketAddress() + " | " + socketOut.isConnected());
		} catch (SocketException sendExc) {
			sendExc.printStackTrace(); // TODO handle
		}

	}
	
	public boolean isConnected() {
		return ( (this.socketOut.isConnected() == this.socketIn.isBound()) && this.socketOut.isConnected());
	}
}
