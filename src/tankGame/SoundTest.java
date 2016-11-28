package tankGame;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SoundTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		AeP apw = new AeP("c://123.wav");
		apw.start();
		

	}
	
	
}


class AeP extends Thread {
	private String fileName;
	
	public AeP(String wavName) {
		this.fileName = wavName;
	}
	
	public void run() {
			File soundFile = new File(fileName);
			AudioInputStream audioInputStream = null;
			
			try {
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			
			AudioFormat format = audioInputStream.getFormat();
			SourceDataLine sourceLine = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			
			try {
				sourceLine = (SourceDataLine) AudioSystem.getLine(info);
				sourceLine.open(format);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			}
			
			sourceLine.start();
			int nBytesRead = 0;
			byte[] abData = new byte[1024];
			
			try {
				while (nBytesRead != -1) {
					nBytesRead = audioInputStream.read(abData, 0,  abData.length);
					if (nBytesRead >= 0) {
		                @SuppressWarnings("unused")
		                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
		            }
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return;
			} finally {
				sourceLine.drain();
				sourceLine.close();
			}
		}
		
		
	
}