package vc.handlers;

import java.io.ByteArrayOutputStream;

import vc.main.Main;
import vc.udp.UDPInputStream;
import vc.voice.BufferedVoicePlayer;

public class UDPBufferedVoiceHandler implements Runnable {

	private UDPInputStream in;
	private ByteArrayOutputStream out;
	
	public UDPBufferedVoiceHandler(UDPInputStream in) {
		this.in = in;
		out = new ByteArrayOutputStream();
	}
	
	@Override
	public void run() {
		try {
			while (Main.main.isEnabled) {
				out = new ByteArrayOutputStream();
				while (out.size() <= Main.main.transferSize) {
					out.write(in.read());
				}
				BufferedVoicePlayer vp = new BufferedVoicePlayer(out);
				vp.play(Main.main.transferSize);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
