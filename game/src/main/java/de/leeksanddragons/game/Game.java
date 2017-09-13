package de.leeksanddragons.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.preferences.WindowConfig;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.ScreenManager;
import de.leeksanddragons.engine.screen.impl.ScreenBasedGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.screen.JuKuSoftIntroScreen;
import de.leeksanddragons.game.screen.LoadingScreen;
import de.leeksanddragons.game.screen.LogoIntroScreen;
import de.leeksanddragons.game.screen.MainMenuScreen;

/**
 * Created by Justin on 09.09.2017.
 */
public class Game extends ScreenBasedGame {

    public Game(WindowConfig windowConfig) {
        super(windowConfig, "leeks-and-dragons");
    }

    @Override
    protected void onInit(IScreenGame game, ScreenManager<IScreen> screenManager) {
        //set log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //add screens
        screenManager.addScreen("jukusoft_intro", new JuKuSoftIntroScreen());
        screenManager.addScreen("logo_intro", new LogoIntroScreen());
        screenManager.addScreen("loading", new LoadingScreen());
        screenManager.addScreen("mainmenu", new MainMenuScreen());

        //check, if preferences are available
        if (!game.getGeneralPreferences().contains("engine_splash_screen")) {
            //set preferences and save
            game.getGeneralPreferences().putBoolean("engine_splash_screen", true);
            game.getGeneralPreferences().flush();
        }

        //check, if engine splash screen is enabled
        if (game.getGeneralPreferences().getBoolean("engine_splash_screen", true)) {
            //push screen
            screenManager.push("jukusoft_intro");
        } else {
            //skip engine splash screen

            //check, if logo screen is enabled
            if (game.getGeneralPreferences().getBoolean("logo_splash_screen", true)) {
                //push screen
                screenManager.push("logo_intro");
            } else {
                //skip logo splash screen

                //push screen
                screenManager.push("loading");
            }
        }
    }

    @Override
    protected void onDispose() {

    }

}
