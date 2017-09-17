package de.leeksanddragons.engine.screen.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.preferences.WindowConfig;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.ScreenManager;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 12.09.2017.
 */
public abstract class ScreenBasedGame extends BaseGame implements IScreenGame {

    //instance of screen manager
    protected ScreenManager<IScreen> screenManager = null;

    public ScreenBasedGame(WindowConfig windowConfig, String appName) {
        super(windowConfig, appName);

        //create new screen manager
        this.screenManager = new DefaultScreenManager(this);
    }

    @Override
    protected final void initGame() {
        this.onInit(this, this.screenManager);
    }

    @Override
    protected final void update(GameTime time) {
        //update all screens
        for (IScreen screen : this.screenManager.listActiveScreens()) {
            // update screen
            screen.update(this, time);
        }
    }

    @Override
    protected final void draw(GameTime time, SpriteBatch batch) {
        //draw all screens
        for (IScreen screen : this.screenManager.listActiveScreens()) {
            // draw screen
            screen.draw(this, time, batch);
        }
    }

    @Override
    protected final void destroyGame() {
        // pause all active screens
        this.screenManager.listActiveScreens().forEach(screen -> {
            screen.onPause(this);
        });

        //dispose all screens
        this.screenManager.dispose();

        this.onDispose();
    }

    @Override
    public ScreenManager<IScreen> getScreenManager() {
        return this.screenManager;
    }

    /**
    * create screens
     *
     * @param screenManager instance of screen manager
    */
    protected abstract void onInit(IScreenGame game, ScreenManager<IScreen> screenManager);

    protected abstract void onDispose ();

}
