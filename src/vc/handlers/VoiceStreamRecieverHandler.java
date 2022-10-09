package vc.handlers;

import vc.main.Main;
import vc.udp.UDPInputStream;
import vc.voice.StreamVoicePlayer;

public class VoiceStreamRecieverHandler implements Runnable {

	private UDPInputStream in;
	private StreamVoicePlayer player;
	
	/**
	 * Writes in stream data directly to speaker.
	 * Note: automatically discards packet if player isn't open.
	 * 
	 * Thread stops when Main.isEnabled != true
	 * 
	 * @param in UDPInputStream for recieving packet data
	 * @param player StreamVoicePlayer to write packet data to speakers
	 */
	public VoiceStreamRecieverHandler(UDPInputStream in, StreamVoicePlayer player) {
		this.in = in;
		this.player = player;
	}
	
	@Override
	public void run() {
		try {
			byte[] data;
			while (Main.main.isEnabled) {
				data = in.read();
				if (player.isEnabled) player.play(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
