package de.leeksanddragons.game.loading;

import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 13.09.2017.
 */
public interface LoadingTask {

    public float getTaskPercentage ();

    public boolean isFinished ();

    public String getText ();

    public void update (IScreenGame game, GameTime time);

}
