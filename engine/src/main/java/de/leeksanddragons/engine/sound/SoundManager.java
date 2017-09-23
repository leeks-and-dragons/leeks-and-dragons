package de.leeksanddragons.engine.sound;

import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 15.09.2017.
 */
public interface SoundManager {

    /**
    * get volume of sounds
     *
     * @return volume of sounds
    */
    public float getSoundVolume ();

    /**
     * get volume of music (soundtracks)
     *
     * @return volume of music (soundtracks)
     */
    public float getMusicVolume ();

    /**
     * check, if sound is muted
     *
     * @return true, if sound is muted
     */
    public boolean isSoundMuted ();

    /**
     * check, if music is muted
     *
     * @return true, if music is muted
     */
    public boolean isMusicMuted ();

    /**
    * set player target position
     *
     * @param x player x position
     * @param y player y position
    */
    public void setTargetPos (float x, float y);

    /**
    * update sound manager
    */
    public void udpate (GameTime time);

}
