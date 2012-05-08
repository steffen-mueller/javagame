/**
 * 
 */
package de.tu_darmstadt.gdi1.framework.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.tu_darmstadt.gdi1.framework.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;


/**
 * <p>A SoundManager takes care of the sounds you want to play. It is your contact
 * to the SoundClip subsystem of the framework.</p>
 * 
 * <p>You should register every sound you want play in the run of your game. You can 
 * register as many sounds as you want, they'll not be loaded into the memory before
 * playing, so don't worry about the space. Each sound will ask the system for an 
 * mixer line once you start playing it and will close it directly after playing,
 * which gives you the freedom to register more than 32 sounds, the maximum of lines
 * the java sound audio engine can mix together. </p>
 * 
 * <p>For background music you might want to be informed about the end of an sound, just 
 * implement the LineListener interface to your class and register it via addEventListener.</p>
 * 
 * <p>Normally you should use the Simple SoundManager() constructor, that will build
 * an SoundManager that uses the sound mixer provided by the java platform. The java
 * mixer is capable to play 32 sounds simultaneously and mix them together.</p>
 * 
 * <p>The Java Sound Audio Engine mixer is at the moment only part of the Sun Java Platform,
 * alternative implementations may not have an own mixer. If the Java Sound Audio Engine 
 * can't be found on runtime by the constructor, it will fallback to the system default
 * mixer provided by the operating system.</p>
 * 
 * <p>The systems default mixer may provide a slightly better sound quality and reaction time
 * but also brings the risk of not beeing able to play sounds simultaneously.</p>
 * 
 * <p>If you want to force the SoundManager to use a special mixer, use the SoundManager(Mixer mixer)
 * constructor. Giving null as the mixer will construct an Soundmanager with the systems default 
 * mixer.</p>
 * 
 * <p>To support other filetypes, search for an Java Sound SPI that supports 
 * this audio format and simply add it to the classpath, this should be everything.</p>
 * 
 * <p>You'll be informed if the system couldn't open a line (i.e. is not able to play the sound)
 * with an SoundFailedException.</p>
 * 
 * @author f_m
 */
public class SoundManager {
	private HashMap<String, SoundClip> soundMap;
	private Mixer mixer;
	private boolean mute = false;
	
	
	/**
	 * Creates a new SoundManager with the Java Sound Audio Engine as mixer.
	 */
	public SoundManager() {
		this(SoundManager.findJavaSoundMixer());
	}
	
	/**
	 * Creates a new SoundManager that will use the given Mixer to play sounds.
	 * @param mixer the mixer to use, null for the systems default mixer.
	 */
	public SoundManager(Mixer mixer) {
		soundMap = new HashMap<String, SoundClip>();
		this.mixer = mixer;
	}
	
	/**
	 * Find the Java Sound Audio Engine Mixer.
	 * 
	 * @return the Java Sound mixer, if found, else: the systems default mixer.
	 */
	private static Mixer findJavaSoundMixer() {
		/* we need to get the Java SoundClip Audio Engine as our mixer,
		 * because starting with java5 we'd get the hardware mixer by default,
		 * that wouldn't give us more then one line if the hardware
		 * doesn't support hardware mixing.
		 */
		
		Mixer.Info[] mixInfo = AudioSystem.getMixerInfo();
		boolean found = false;
		Mixer mixer = null;
		
		for (Mixer.Info mix : mixInfo) {
			if (mix.getName().contains("Java Sound Audio Engine") && !found) {
				mixer = AudioSystem.getMixer(mix);
				found = true;
			}
		}
		
		if (!found) {
			/* we couldn't find the Java SoundClip mixer, maybe because
			 * we aren't using the Sun JRE, we use the system default 
			 * mixer instead.
			 */
			mixer = AudioSystem.getMixer(null);
		}
		
		return mixer;
	}
	
	/**
	 * Registers the given sound to the sound system.
	 * 
	 * @param label the label with witch you can access the sound.
	 * @param file A mp3, ogg, wav, au or aiff audio file.
	 * @throws javax.sound.sampled.UnsupportedAudioFileException if the file type of the file is unsupported.
	 * @throws java.io.IOException if the file couldn't be read.
	 * @throws ParameterOutOfRangeException if the label is in use
	 */
	public void registerSound(final String label, final File file) throws ParameterOutOfRangeException, 
					UnsupportedAudioFileException, IOException {
		if (soundMap.containsKey(label)) {
			throw new ParameterOutOfRangeException("A sound with this label has already been registered.");
		} else {
			soundMap.put(label, new SoundClip(file, mixer));
		}
	}
	
	/**
	 * Unregisters the sound bound to the label and frees the resources.
	 * @param label the label for the sound
	 */
	public void unregisterSound(String label) {
		soundMap.remove(label).stop();
	}
	
	/**
	 * @param label the label for the sound
	 * @return whether a sound with this label is registered
	 */
	public boolean isSoundRegistered(String label) {
		return soundMap.containsKey(label);
	}
	
	/**
	 * Sets the gain for the given clip.
	 * @param label the label of the clip
	 * @param gain the gain level, as a positive or negative float,
	 * might be between -80 and +14
	 */
	public void setGain(String label, float gain) {
		if (soundMap.containsKey(label)) {
			soundMap.get(label).setGain(gain);
		}
	}
	
	/**
	 * Sets a loop on this sound.
	 * @param label
	 * @param loop true for loop mode, false otherwise.
	 */
	public void setLoop(String label, boolean loop) {
		if (soundMap.containsKey(label)) {
			soundMap.get(label).setLoop(loop);
		}
	}
	
	/**
	 * Starts playing the sound.
	 * @param label the sound to play.
	 * @throws SoundFailedException if the sound could not be played
	 * 	because no sound line could be allocated.
	 */
	public void playSound(String label) throws SoundFailedException {
		if (soundMap.containsKey(label) && mute == false) {
			try {
				soundMap.get(label).play();
			} catch (LineUnavailableException e) {
				throw new SoundFailedException(e);
			}
		}
	}
	
	/**
	 * Stops the given sound, i.e. pauses and sets the 
	 * sound back to beginning
	 * @param label the sound to stop
	 */
	public void stopSound(String label) {
		if (soundMap.containsKey(label)) {
			soundMap.get(label).stop();
		}
	}
	
	/**
	 * @param label the sound to check.
	 * @return whether the given sound is playing.
	 */
	public boolean isSoundPlaying(String label) {
		if (soundMap.containsKey(label)) {
			return soundMap.get(label).isRunning();
		}
		return false;
	}
	
	/**
	 * Adds the given implementer of LineListener to the listeners
	 * of the sound. The listener is informed about OPEN, START, STOP
	 * and CLOSE events. 
	 * 
	 * @param label the sound you want to listen to.
	 * @param listener the listener object.
	 */
	public void addEventListener(String label, LineListener listener) {
		if(soundMap.containsKey(label)) {
			soundMap.get(label).addLineListener(listener);
		}
	}
	
	/**
	 * Closes all sounds in the sound bank, i.e. frees their resources.
	 */
	public void closeSoundBank() {
		for (SoundClip soundClip : soundMap.values()) {
			soundClip.stop();
		}
		soundMap = new HashMap<String, SoundClip>();
	}
	
	/**
	 * Sets the mute state of the SoundManager. 
	 * 
	 * If set to true, all currently playing sounds will be stopped and no other
	 * incoming sounds will be played until the mute the is set back to false.
	 * 
	 * @param setMute true for mute, false for normal sound mode.
	 */
	public void setMute(final boolean setMute) {
		mute = setMute;
		
		if (setMute) {
			for (String sound : soundMap.keySet()) {
				stopSound(sound);
			}
		}
	}
	
	/**
	 * Returns the current mute state.
	 * @return true for mute, false for normal sound mode.
	 */
	public boolean getMute() {
		return mute;
	}

}
