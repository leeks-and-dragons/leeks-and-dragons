package de.leeksanddragons.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 09.09.2017.
 */
public class Game extends BaseGame {

    public Game() {
        super("leeks-and-dragons");
    }

    @Override
    protected void initGame() {
        //set log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    protected void update(GameTime time) {

    }

    @Override
    protected void draw(GameTime time, SpriteBatch batch) {

    }

    @Override
    protected void destroyGame() {

    }

}
