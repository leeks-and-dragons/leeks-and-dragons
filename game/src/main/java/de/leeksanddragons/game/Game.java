package de.leeksanddragons.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.ScreenManager;
import de.leeksanddragons.engine.screen.impl.ScreenBasedGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.screen.JuKuSoftIntroScreen;
import de.leeksanddragons.game.screen.LogoIntroScreen;

/**
 * Created by Justin on 09.09.2017.
 */
public class Game extends ScreenBasedGame {

    public Game() {
        super("leeks-and-dragons");
    }

    @Override
    protected void onInit(IScreenGame game, ScreenManager<IScreen> screenManager) {
        //set log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //add screens
        screenManager.addScreen("jukusoft_intro", new JuKuSoftIntroScreen());
        screenManager.addScreen("logo_intro", new LogoIntroScreen());

        //push screen
        screenManager.push("jukusoft_intro");
    }

    @Override
    protected void onDispose() {

    }

}
