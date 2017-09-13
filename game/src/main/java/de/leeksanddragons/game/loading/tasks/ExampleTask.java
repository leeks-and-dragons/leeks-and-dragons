package de.leeksanddragons.game.loading.tasks;

import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.loading.LoadingTask;

/**
 * Created by Justin on 13.09.2017.
 */
public class ExampleTask implements LoadingTask {

    protected float percentage = 0;

    @Override
    public float getTaskPercentage() {
        if (percentage >= 1) {
            return 1;
        } else {
            return percentage;
        }
    }

    @Override
    public boolean isFinished() {
        return percentage >= 1;
    }

    @Override
    public String getText() {
        return "Example";
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        this.percentage += time.getDeltaTime() * 0.5f;
    }

}
