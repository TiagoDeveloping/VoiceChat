package vc.voice;

import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import vc.main.Main;
import vc.udp.UDPOutputStream;

public class StreamVoiceTransmitter {

	private AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
	private TargetDataLine microphone;
	private UDPOutputStream udpOut;
	
	private boolean enabled = false;
	
	/**
	 * Note: automatically: enabled=true
	 * 
	 * @param udpOut udp output stream.
	 */
	public StreamVoiceTransmitter(UDPOutputStream udpOut) {
		this.udpOut = udpOut;
		
		this.enable();
	}
	
	public void disable() {
		microphone.close();
		
		this.enabled = false;
	}
	
	byte[] data;
	
	public void enable() {
		try {
			microphone = AudioSystem.getTargetDataLine(format);
			
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			microphone = (TargetDataLine) AudioSystem.getLine(info);
			microphone.open(format);
			microphone.start();
			
			data = new byte[Main.main.chunkSize];
			
			this.enabled = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Note: only works if enabled.
	 * 
	 * @param bytes something with a buffer and a size
	 * @throws SocketException just whatever l0lz
	 */
	public void transmit() throws SocketException {
		microphone.read(data, 0, Main.main.chunkSize);
		udpOut.write(data);
	}
}
