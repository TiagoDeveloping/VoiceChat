package vc.handlers;

import java.io.ByteArrayOutputStream;

import vc.main.Main;
import vc.udp.UDPInputStream;
import vc.voice.VoicePlayer;

public class UDPInStreamHandler implements Runnable {

	private UDPInputStream in;
	private ByteArrayOutputStream out;
	
	public UDPInStreamHandler(UDPInputStream in) {
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
				VoicePlayer vp = new VoicePlayer(out);
				vp.play(Main.main.transferSize);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
