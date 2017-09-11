package de.leeksanddragons.engine.screen;

import de.leeksanddragons.engine.game.IGame;

/**
 * Created by Justin on 11.09.2017.
 */
public interface IScreenGame extends IGame {

    /**
    * get instance of screen manager
    */
    public <T extends IScreen> ScreenManager<T> getScreenManager ();

}
