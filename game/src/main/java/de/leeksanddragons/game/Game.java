package de.leeksanddragons.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.ScreenManager;
import de.leeksanddragons.engine.screen.impl.ScreenBasedGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 09.09.2017.
 */
public class Game extends ScreenBasedGame {

    public Game() {
        super("leeks-and-dragons");
    }

    @Override
    protected void onInit(ScreenManager<IScreen> screenManager) {
        //set log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    protected void onDispose() {

    }

}
