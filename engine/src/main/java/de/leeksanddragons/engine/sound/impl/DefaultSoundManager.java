package de.leeksanddragons.engine.sound.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.sound.SoundEffect;
import de.leeksanddragons.engine.sound.SoundManager;
import de.leeksanddragons.engine.sound.SoundTransition;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;
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

    //asset manager
    protected GameAssetManager assetManager = null;

    //list with sound effects
    protected List<SoundEffect> effectList = new ArrayList<>();

    //sound transition mode
    protected SoundTransition transition = SoundTransition.FADE_OUT_FADE_IN;

    //current sound track
    protected Music currentMusic = null;
    protected String currentMusicPath = "";

    //next music sound track
    protected Music nextMusic = null;
    protected String nextMusicPath = "";

    protected boolean looping = true;

    //elapsed fade-out time in ms
    protected float elapsedFadeOutTime = 0;
    protected float maxFadeoutTime = 3000;

    //elapsed fade-in time in ms
    protected float elapsedFadeInTime = 0;
    protected float maxFadeInTime = 3000;

    protected boolean isFadingOut = false;
    protected boolean isFadingIn = false;

    protected String currentLoadingSoundtrack = "";

    public DefaultSoundManager (GameAssetManager assetManager, GamePreferences prefs) {
        this.assetManager = assetManager;
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
        if (!this.nextMusicPath.isEmpty() && !isFadingOut && !isFadingIn) {
            //check, if soundtrack was loaded
            if (assetManager.isLoaded(this.nextMusicPath, Music.class)) {
                this.nextMusic = assetManager.get(this.nextMusicPath, Music.class);

                Gdx.app.debug("SoundManager", "soundtrack loaded successfully: " + this.nextMusicPath);

                this.currentLoadingSoundtrack = "";

                switch (this.transition) {
                    case STOP_AND_PLAY:
                        //stop old soundtrack
                        if (this.currentMusic != null) {
                            if (this.currentMusic.isPlaying()) {
                                this.currentMusic.stop();
                            }

                            this.currentMusic = null;

                            //unload music
                            this.assetManager.unload(this.currentMusicPath);
                        }

                        this.currentMusic = this.nextMusic;
                        this.currentMusicPath = nextMusicPath;
                        this.nextMusicPath = "";

                        //start new soundtrack
                        this.currentMusic.setVolume(getMusicVolume());
                        this.currentMusic.setLooping(this.looping);
                        this.currentMusic.play();

                        break;

                    case FADE_OUT_FADE_IN:

                        isFadingOut = true;

                        break;

                    default:
                        throw new UnsupportedOperationException("transition method " + transition.name() + " isnt supported yet.");
                }
            }
        }

        if (isFadingOut) {
            if (this.currentMusic != null) {
                this.elapsedFadeOutTime += time.getDeltaTime() * 1000;

                Gdx.app.debug("SoundManager", "fade in soundtrack.");

                float percent = this.elapsedFadeOutTime / this.maxFadeoutTime;
                Gdx.app.debug("Sound Manager", "fade out progress: " + percent);

                //calculate volume
                float volume = (1 - Math.max(1, percent)) * getMusicVolume();

                //set volume of current soundtrack
                this.currentMusic.setVolume(volume);

                if (volume == 0) {
                    //fading out has finished
                    this.isFadingOut = false;
                    this.isFadingIn = true;

                    //unload old soundtrack
                    assetManager.unload(this.currentMusicPath);

                    //set next music to current music
                    this.currentMusic = this.nextMusic;
                    this.currentMusicPath = this.nextMusicPath;

                    //reset values for next soundtrack
                    this.nextMusicPath = "";
                    this.nextMusic = null;

                    this.elapsedFadeInTime = 0;
                    this.elapsedFadeOutTime = 0;
                }
            } else {
                isFadingOut = false;
                this.isFadingIn = true;

                //set next music to current music
                this.currentMusic = this.nextMusic;
                this.currentMusicPath = this.nextMusicPath;

                //reset values for next soundtrack
                this.nextMusicPath = "";
                this.nextMusic = null;

                this.elapsedFadeInTime = 0;
                this.elapsedFadeOutTime = 0;
            }
        } else if (isFadingIn) {
            this.elapsedFadeInTime += time.getDeltaTime() * 1000;

            float percent = this.elapsedFadeInTime / this.maxFadeInTime;

            //calculate volume
            float volume = Math.min(1, percent) * getMusicVolume();

            //set volume of current soundtrack
            this.currentMusic.setVolume(volume);

            //start music, if music isnt playing already
            if (!this.currentMusic.isPlaying()) {
                this.currentMusic.setLooping(this.looping);
                this.currentMusic.play();
            }

            if (volume == 1) {
                Gdx.app.debug("SoundManager", "fade in was finished.");

                //fading in has finished
                this.isFadingIn = false;

                this.elapsedFadeInTime = 0;
            }
        } else {
            if (this.currentMusic != null && this.currentMusic.isPlaying()) {
                this.currentMusic.setVolume(getMusicVolume());
            }
        }

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

    @Override
    public void setMusicTransition(SoundTransition transition) {
        this.transition = transition;
    }

    @Override
    public SoundTransition getMusicTransition() {
        return this.transition;
    }

    @Override
    public void loadAndPlayBackgroundMusic(String musicPath, boolean looping) {
        if (this.currentMusicPath.equals(musicPath) || this.currentLoadingSoundtrack.equals(musicPath)) {
            return;
        }

        if (!new File(musicPath).exists()) {
            throw new IllegalArgumentException("music path doesnt exists.");
        }

        Gdx.app.debug("SoundManager", "load new soundtrack: " + musicPath);

        this.currentLoadingSoundtrack = musicPath;

        this.nextMusicPath = musicPath;

        //load soundtrack
        this.assetManager.load(musicPath, Music.class);
    }

    @Override
    public void stopBackgroundMusic() {
        if (this.currentMusic.isPlaying()) {
            //stop music
            this.currentMusic.stop();

            this.currentMusic = null;
        }

        if (this.nextMusic.isPlaying()) {
            //stop music
            this.nextMusic.stop();

            this.nextMusic = null;
        }

        //unload all soundtracks
        if (!this.currentMusicPath.isEmpty()) {
            this.assetManager.unload(this.currentMusicPath);

            this.currentMusicPath = "";
        }

        if (!this.nextMusicPath.isEmpty()) {
            this.assetManager.unload(this.nextMusicPath);

            this.nextMusicPath = "";
        }
    }

    @Override
    public void dispose() {
        this.clearAllEffects();
    }

}
