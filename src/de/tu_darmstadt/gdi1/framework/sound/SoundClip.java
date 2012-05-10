/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * A SoundClip is a wrapper class for an mp3, ogg, wax, au or aiff 
 * audiofile. A SoundClip has an embedded SoundClipPlayer that can
 * playback the sound in it's own thread.
 * 
 * You shouldn't work with this class directly, an SoundManager will
 * fit your needs.
 * 
 * @author f_m
 */
public class SoundClip implements LineListener{
	private Mixer mixer;
	private File file;
	private AudioFormat decodedFormat;
	private DataLine.Info info;
	private SoundClipPlayer player;
	private float gain;
	private boolean loop;
	private List<LineListener> listeners;
	/**
	 * Constructor for an new SoundClip
	 * 
	 * @param file the AudioFile
	 * @param mixer the Mixer to use
	 * @throws java.io.IOException if the File couldn't be read successfully
	 * @throws javax.sound.sampled.UnsupportedAudioFileException if the audio file isn't supported.
	 */
	
	public SoundClip(final File file, final Mixer mixer) 
					throws IOException, UnsupportedAudioFileException{
		this.file = file;
		this.mixer = mixer;
		AudioFormat baseFormat = AudioSystem.getAudioFileFormat(file).getFormat();
		decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 
						16, 
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2, 
						baseFormat.getSampleRate(), 
						false);
		info = new DataLine.Info(SourceDataLine.class, decodedFormat);
		gain = 0.0F;
		listeners = new ArrayList<LineListener>();
		listeners.add((LineListener) this);
	}
	
	/**
	 * An audioplayer for this SoundClip.
	 * 
	 * @author f_m
	 */
	class SoundClipPlayer extends Thread{
		private byte[] data;
		private boolean stop;
		private SourceDataLine line;
		private AudioInputStream baseStream;
		private AudioInputStream decodedStream;
		
		/**
		 * Constructor for an SoundClipPlayer.
		 * 
		 * @throws javax.sound.sampled.LineUnavailableException if we couldn't get an line from the mixer.
		 */
		private SoundClipPlayer() throws LineUnavailableException {
			try {
				baseStream = AudioSystem.getAudioInputStream(file);
				decodedStream = AudioSystem.getAudioInputStream(decodedFormat, baseStream);
				data = new byte[decodedFormat.getFrameSize() * 100];
				stop = false;
				//request a new line for playing.
				line = (SourceDataLine) mixer.getLine(info);
				//register all listeners on the line.
				for(LineListener listener:listeners) {
					line.addLineListener(listener);
				}
				line.open();
				setGain(gain);
			} catch (UnsupportedAudioFileException e) {
				//shouldn't happen -> was checked before
				e.printStackTrace();
			} catch (IOException e) {
				//shouldn't happen -> was checked before
				e.printStackTrace();
			}
		}
		
		/**
		 * stops the playing.
		 */
		public void stopPlaying(){
			stop = true;
		}
		
		/**
		 * @param gain the gain to set.
		 */
		public void setGain(float gain) {
			FloatControl contrl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			contrl.setValue(gain);
		}
		
		@Override
		public void run() {
			if (line != null)
			  {
			    line.start();
			    int nBytesRead = 0, nBytesWritten = 0;
			    while (nBytesRead != -1 && !stop)
			    {
			        try {
						nBytesRead = decodedStream.read(data, 0, data.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
			    }
			    line.drain();
			    line.stop();
			    line.close();
				try {
					decodedStream.close();
					baseStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  } 
		}
	}
	
	/**
	 * Starts playing a the sound.
	 * @throws javax.sound.sampled.LineUnavailableException if the sound could not be played
	 * 	because no sound line could be allocated.
	 */
	public void play() throws LineUnavailableException {
		//@TODO currently it is allowed to play the same 
		//sound more then one time synchronously. Leave it
		//like that? The sound that was played first van't be
		//controlled longer in the current implementation. That
		//is no problem for clips like 1 second, but longer ones
		//can't be stopped again.
		
		//if(player == null || !player.isAlive()){
			player = new SoundClipPlayer();
			player.start();
		//}
	}
	
	/**
	 * @param gain the gain to set.
	 */
	public void setGain(float gain) {
		if(player == null || !player.isAlive()){
			this.gain = gain;
		} else {
			this.gain = gain;
			player.setGain(gain);
		}
	}
	
	/**
	 * @return true if this player is playing, otherwise false.
	 */
	public boolean isRunning() {
		if(player == null || !player.isAlive())
			return true;
		return false;
	}
	
	/**
	 * @param loop true for looping, false otherwise.
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	/**
	 * stops the playing.
	 */
	public void stop() {
		if(player != null && player.isAlive()){
			player.stopPlaying();
		}
	}
	
	/**
	 * Adds an listener for this sound. You'll be informed
	 * about Open, Close, Start and Stop events.
	 * @param listener
	 */
	public void addLineListener(LineListener listener) {
		listeners.add(listener);
	}

	/**
	 * Used for looping
	 */
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.STOP && loop) {
			try {
				player = new SoundClipPlayer();
				player.start();
			} catch (LineUnavailableException e) {
				// ignore. if it happens now, it probably happened before,
				// and there is no easy way to throw from here anyway.
			}
		}
	}
}
