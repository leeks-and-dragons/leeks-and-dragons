package de.leeksanddragons.engine.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;

/**
 * Created by Justin on 23.09.2017.
 */
public class SoundEffect {

    //sound position
    protected float x = 0;
    protected float y = 0;

    //sound id
    protected long soundID = 0;

    public enum SOUND_EFFECT_TYPE {
        //sound is listen everywhere with same volume
        GLOBAL,

        //sound is listen locally, volume dependend on distance
        ENVIRONMENT
    };

    protected SOUND_EFFECT_TYPE type = SOUND_EFFECT_TYPE.GLOBAL;

    //temporary vector to avoid GC pressure
    protected Vector2 tmpVec = new Vector2(0, 0);

    //max distance to player to play sound
    protected float maxDistance = 100;

    //sound path
    protected String soundPath = "";

    //instance of sound
    protected Sound sound = null;

    //flag, if sound asset is managed by asset manager
    protected boolean isManaged = false;

    //instance of game
    protected IScreenGame game = null;

    //flag, if sound is playing
    protected boolean isPlaying = false;

    //flag, if sound was playing last rendering cycle
    protected boolean wasPlaying = false;

    //flag, if sound is looping
    protected boolean looping = false;

    //pitch
    protected float pitch = 1;

    //panorama
    protected float pan = 0;

    //last target position
    protected float targetX = 0;
    protected float targetY = 0;

    /**
    * default constructor
     *
     * @param x x position of sound effect
     * @param y y position of sound effect
    */
    public SoundEffect (IScreenGame game, float x, float y) {
        this.game = game;

        this.x = x;
        this.y = y;
    }

    public void load (String soundPath, SOUND_EFFECT_TYPE type) {
        if (sound != null) {
            //dispose old sound
            this.unload();
        }

        this.type = type;
        this.isManaged = true;

        //check, if sound path is null
        if (soundPath == null) {
            throw new NullPointerException("sound path cannot be null.");
        }

        //check, if sound path is empty
        if (soundPath.isEmpty()) {
            throw new IllegalArgumentException("sound cannot be empty.");
        }

        //check, if sound path doesnt exists
        if (!new File(soundPath).exists()) {
            throw new IllegalArgumentException("sound file doesnt exists, path: " + soundPath);
        }

        //load sound with asset manager
        game.getAssetManager().load(soundPath, Sound.class);
    }

    public void load (Sound sound, SOUND_EFFECT_TYPE type) {
        if (sound != null) {
            //dispose old sound
            this.unload();
        }

        this.type = type;
        this.isManaged = false;

        this.sound = sound;
    }

    /**
    * set position of sound effect
     *
     * @param x x position of sound effect
     * @param y y position of sound effect
    */
    public void setPos (float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
    * calculate distance between player and sound effect
     *
     * @return distance between player and sound effect
    */
    public float calculateDistance (float targetX, float targetY) {
        //set vector
        tmpVec.set(targetX - this.x, targetY - this.y);

        //calculate length of vector
        return tmpVec.len();
    }

    /**
    * calculate sound volume, depend on player position
     *
     * @param distance distance to player
     *
     * @return sound volume in percent
    */
    public float calculateVolume (float distance) {
        //for global sounds we dont need to calculate distance
        if (type == SOUND_EFFECT_TYPE.GLOBAL) {
            return game.getSoundManager().getSoundVolume();
        }

        if (distance > this.maxDistance) {
            return 0;
        }

        return (distance / this.maxDistance) * game.getSoundManager().getSoundVolume();
    }

    /**
    * get maximum distance between sound effect and player in pixels
     *
     * @return maximum distance between sound effect and player
    */
    public float getMaxDistance () {
        return this.maxDistance;
    }

    /**
    * set maximum distance between sound effect and player in pixels
     *
     * @param maxDistance maximum distance between sound effect and player
    */
    public void setMaxDistance (float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setTargetPosition (float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void update (GameTime time, float targetX, float targetY) {
        //update target position
        this.targetX = targetX;
        this.targetY = targetY;

        //if sound isnt playing, we dont need to update sound effect volume
        if (!isPlaying) {
            return;
        }

        //update volume of sound effect

        //calculate distance between player and sound effect
        float distance = this.calculateDistance(targetX, targetY);

        //calculate sound volume
        float volume = this.calculateVolume(distance);

        if (volume > 0) {
            //play sound, update volume
            this.sound.setVolume(this.soundID, calculateVolume(distance));
        } else {
            this.sound.setVolume(this.soundID, 0);
        }
    }

    public void play (boolean looping) {
        if (this.sound == null) {
            if (isManaged) {
                //force loading
                game.getAssetManager().finishLoadingAsset(this.soundPath);

                this.sound = game.getAssetManager().get(this.soundPath);
            } else {
                throw new NullPointerException("sound cannot be null.");
            }
        }

        if (isPlaying) {
            this.sound.stop(this.soundID);
        }

        this.looping = looping;

        this.isPlaying = true;

        //calculate distance between player and sound effect
        float distance = this.calculateDistance(targetX, targetY);

        //calculate sound volume
        float volume = this.calculateVolume(distance);

        //start playing
        this.soundID = this.sound.play(volume, this.pitch, this.pan);

        //set looping
        this.sound.setLooping(this.soundID, looping);
    }

    public void pause () {
        if (isPlaying) {
            this.sound.pause(this.soundID);
        }
    }

    public void resume (boolean looping) {
        if (!isPlaying) {
            this.play(looping);
        } else {
            this.sound.resume(this.soundID);
        }
    }

    public void stop () {
        this.isPlaying = false;
        this.sound.stop(this.soundID);

        this.soundID = -1;
    }

    public boolean isPlaying () {
        return this.isPlaying;
    }

    public void unload () {
        if (sound != null) {
            if (isManaged) {
                this.game.getAssetManager().unload(this.soundPath);
            } else {
                this.sound.dispose();
            }

            this.sound = null;
        }
    }

    public void dispose () {
        this.unload();
    }

}
