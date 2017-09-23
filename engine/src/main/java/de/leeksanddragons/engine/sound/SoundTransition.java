package de.leeksanddragons.engine.sound;

/**
 * Created by Justin on 23.09.2017.
 */
public enum SoundTransition {

    //music will stoped and new music will played directly
    STOP_AND_PLAY,

    //old music soundtrack will be faded out, new music soundtrack will be faded in
    FADE_OUT_FADE_IN,

    //2 music soundtracks will mixed together
    CROSSFADE

}
