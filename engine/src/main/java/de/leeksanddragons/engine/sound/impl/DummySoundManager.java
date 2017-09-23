package de.leeksanddragons.engine.sound.impl;

import de.leeksanddragons.engine.sound.SoundEffect;
import de.leeksanddragons.engine.sound.SoundManager;
import de.leeksanddragons.engine.sound.SoundTransition;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 15.09.2017.
 */
public class DummySoundManager implements SoundManager {

    @Override
    public float getSoundVolume() {
        return 1f;
    }

    @Override
    public float getMusicVolume() {
        return 1f;
    }

    @Override
    public boolean isSoundMuted() {
        return false;
    }

    @Override
    public boolean isMusicMuted() {
        return false;
    }

    @Override
    public void setTargetPos(float x, float y) {

    }

    @Override
    public void udpate(GameTime time) {

    }

    @Override
    public void addEffect(SoundEffect effect) {

    }

    @Override
    public void removeEffect(SoundEffect effect) {

    }

    @Override
    public void clearAllEffects() {

    }

    @Override
    public void setMusicTransition(SoundTransition transition) {

    }

    @Override
    public SoundTransition getMusicTransition() {
        return null;
    }

    @Override
    public void loadAndPlayBackgroundMusic(String musicPath, boolean looping) {

    }

    @Override
    public void stopBackgroundMusic() {

    }

    @Override
    public void dispose() {

    }

}
