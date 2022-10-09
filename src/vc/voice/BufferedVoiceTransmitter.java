package vc.voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import vc.main.Main;
import vc.udp.UDPOutputStream;

public class BufferedVoiceTransmitter {

	 private AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
	 private TargetDataLine microphone;
	 private UDPOutputStream udpOut;
	 
	 public BufferedVoiceTransmitter(UDPOutputStream udpOut) {
		 this.udpOut = udpOut;
	 }
	
	public void captureAndTransmit(long bytes) {
		try {
	        microphone = AudioSystem.getTargetDataLine(format);
	
	        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	        microphone = (TargetDataLine) AudioSystem.getLine(info);
	        microphone.open(format);
	
	        //ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int numBytesRead;
	        byte[] data = new byte[microphone.getBufferSize() / 5];
	        microphone.start();
	        
	        long bytesRead = 0;
	        
            while (bytesRead < bytes) {
                numBytesRead = microphone.read(data, 0, Main.main.chunkSize);
                bytesRead = bytesRead + numBytesRead;
                System.out.println(bytesRead);
                //out.write(data, 0, numBytesRead);
                udpOut.write(data);
            }
            
            //System.out.println(out.toString());
            microphone.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
