package de.leeksanddragons.game.loading;

import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 13.09.2017.
 */
public abstract class BaseLoadingTask implements LoadingTask {

    protected float percentage = 0;
    protected String percentageText = "";

    @Override
    public float getTaskPercentage() {
        return this.percentage;
    }

    @Override
    public boolean isFinished() {
        return this.percentage >= 1;
    }

    @Override
    public String getText() {
        return this.percentageText;
    }

}
