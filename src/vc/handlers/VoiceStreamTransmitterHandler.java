package vc.handlers;

import java.net.SocketException;

import vc.main.Main;
import vc.voice.StreamVoiceTransmitter;

public class VoiceStreamTransmitterHandler implements Runnable {

	private StreamVoiceTransmitter trans;
	
	public VoiceStreamTransmitterHandler(StreamVoiceTransmitter trans) {
		this.trans = trans;
	}

	@Override
	public void run() {
		try {
			while (Main.main.isEnabled) {
				trans.transmit();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
