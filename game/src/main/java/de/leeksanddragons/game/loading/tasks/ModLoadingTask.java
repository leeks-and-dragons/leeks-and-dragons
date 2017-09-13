package de.leeksanddragons.game.loading.tasks;

import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.loading.LoadingTask;

/**
 * Created by Justin on 13.09.2017.
 */
public class ModLoadingTask implements LoadingTask {

    @Override
    public float getTaskPercentage() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

}
