/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_darmstadt.gdi1.bomberman.sound;

import de.tu_darmstadt.gdi1.framework.exceptions.SoundFailedException;
import de.tu_darmstadt.gdi1.framework.sound.SoundManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JUehV
 */
public class SoundManagerFactory {

    public static abstract class SoundLabel {

        public static final String BACKGROUND = "bg";
        public static final String BOMB = "bomb";
        public static final String DIE = "die";
        public static final String GAME_END = "game_end";
        public static final String WALK = "walk";
        public static final String POWER_UP = "power_up";

        private SoundLabel() {
            //empty
        }
    }

    public static abstract class SoundPath {

        private static final String RES_DIR = "resource/sound/";
        public static final String BACKGROUND = RES_DIR + "bg.wav";
        public static final String BOMB = RES_DIR + "bomb.wav";
        public static final String DIE = RES_DIR + "die.wav";
        public static final String GAME_END = RES_DIR + "game_end.wav";
        public static final String WALK = RES_DIR + "pacefulness.wav";
        public static final String POWER_UP = RES_DIR + "powerup.wav";

        private SoundPath() {
            //empty
        }
    }
    private static SoundManager INSTANCE = null;

    public static SoundManager getSoundManager() {
        if (INSTANCE == null) {
            INSTANCE = new SoundManager();
        }
        return INSTANCE;
    }

    public static void playWithoutAnnoyingExceptions(String label) {
        try {
            SoundManagerFactory.getSoundManager().playSound(label);
        } catch (SoundFailedException ex) {
            Logger.getLogger(SoundManagerFactory.class.getName()).log(Level.SEVERE, "Error while playing " + label, ex);
        }
    }
}
