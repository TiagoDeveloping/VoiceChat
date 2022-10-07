package vc.voice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class VoicePlayer {

	private AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true); // TODO extract format
	private InputStream stream;
	private ByteArrayOutputStream bStream;
	
    private AudioInputStream audioInputStream;
    private SourceDataLine sourceDataLine;
	
    public boolean occupied = false;
    
	public VoicePlayer(ByteArrayOutputStream bStream) {
		this.bStream = bStream;
		this.stream = new ByteArrayInputStream(bStream.toByteArray());
	}
	
	public void play(long bytes) throws Exception {
		occupied = true;
		
		audioInputStream = new AudioInputStream(stream,format, bStream.toByteArray().length / format.getFrameSize());
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(format);
        sourceDataLine.start();
        int cnt = 0;
        byte tempBuffer[] = new byte[(int) bytes];
        try {
            while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    sourceDataLine.write(tempBuffer, 0, cnt);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sourceDataLine.drain();
        sourceDataLine.close();
        
        occupied = true;
	}

}
