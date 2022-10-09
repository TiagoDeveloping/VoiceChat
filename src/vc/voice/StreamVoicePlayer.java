package vc.voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import vc.main.Main;

public class StreamVoicePlayer {

	private AudioFormat format = Main.main.format;
	
    private SourceDataLine sourceDataLine;
    private DataLine.Info dataLineInfo;
	
    public boolean isEnabled = false;
    
	public StreamVoicePlayer(boolean enabled) {
		this.isEnabled = enabled;
	}
	
	public void enable() {
		try {
			this.isEnabled = true;
	        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
	        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	        sourceDataLine.open(format);
	        sourceDataLine.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			//TODO handle
		}
	}
	
	public void disable() {
		this.isEnabled = false;
        sourceDataLine.drain();
        sourceDataLine.close();
	}
	
	/**
	 * Writes data to the speaker.
	 * Note: obviously only works if this.isEnabled;
	 * 
	 * @param data the data to write to the speakers
	 * @throws Exception when failed to write
	 */
	public synchronized void play(byte[] data) throws Exception {
        sourceDataLine.write(data, 0, data.length);
	}

}
