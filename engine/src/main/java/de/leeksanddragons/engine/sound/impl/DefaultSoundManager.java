package de.leeksanddragons.engine.sound.impl;

import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.sound.SoundManager;

/**
 * Created by Justin on 17.09.2017.
 */
public class DefaultSoundManager implements SoundManager {

    //game preferences
    protected GamePreferences prefs = null;

    public DefaultSoundManager (GamePreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public float getSoundVolume() {
        return prefs.getFloat("sound_volume", 1f);
    }

    @Override
    public float getMusicVolume() {
        return prefs.getFloat("music_volume", 1f);
    }

    @Override
    public boolean isSoundMuted() {
        return prefs.getBoolean("sound_muted", false);
    }

    @Override
    public boolean isMusicMuted() {
        return prefs.getBoolean("music_muted", false);
    }

}
