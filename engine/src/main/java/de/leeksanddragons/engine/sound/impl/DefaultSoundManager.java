package de.leeksanddragons.engine.sound.impl;

import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.sound.SoundEffect;
import de.leeksanddragons.engine.sound.SoundManager;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 17.09.2017.
 */
public class DefaultSoundManager implements SoundManager {

    //game preferences
    protected GamePreferences prefs = null;

    //target position (mostly players position)
    protected float targetX = 0;
    protected float targetY = 0;

    //list with sound effects
    protected List<SoundEffect> effectList = new ArrayList<>();

    public DefaultSoundManager (GamePreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public float getSoundVolume() {
        if (isSoundMuted()) {
            return 0;
        }

        return prefs.getFloat("sound_volume", 1f);
    }

    @Override
    public float getMusicVolume() {
        if (isMusicMuted()) {
            return 0;
        }

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

    @Override
    public void setTargetPos(float x, float y) {
        this.targetX = x;
        this.targetY = y;

        //update effects
        for (SoundEffect effect : this.effectList) {
            effect.setTargetPosition(targetX, targetY);
        }
    }

    @Override
    public void udpate(GameTime time) {
        //play background sound

        //iterate through all sound effects
        for (SoundEffect effect : this.effectList) {
            //update all effects
            effect.update(time, this.targetX, this.targetY);
        }
    }

    @Override
    public void addEffect(SoundEffect effect) {
        this.effectList.add(effect);
    }

    @Override
    public void removeEffect(SoundEffect effect) {
        this.effectList.remove(effect);
    }

    @Override
    public void clearAllEffects() {
        //dispose all sound effects
        for (SoundEffect effect : this.effectList) {
            effect.dispose();
        }

        this.effectList.clear();
    }

}
