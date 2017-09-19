package de.leeksanddragons.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import de.leeksanddragons.engine.mods.ModManager;
import de.leeksanddragons.engine.mods.impl.DefaultModManager;
import de.leeksanddragons.engine.preferences.GamePreferences;
import de.leeksanddragons.engine.preferences.WindowConfig;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.ScreenManager;
import de.leeksanddragons.engine.screen.impl.ScreenBasedGame;
import de.leeksanddragons.game.loading.tasks.LoadAssetsTask;
import de.leeksanddragons.game.loading.tasks.ModLoadingTask;
import de.leeksanddragons.game.screen.*;
import de.leeksanddragons.game.screen.prototype.DevRoomScreen;
import de.leeksanddragons.game.screen.prototype.WaterRenderScreen;
import de.leeksanddragons.game.shared.Shared;

/**
 * Created by Justin on 09.09.2017.
 */
public class Game extends ScreenBasedGame {

    public Game(WindowConfig windowConfig) {
        super(windowConfig, "leeks-and-dragons");
    }

    @Override
    protected void onInit(IScreenGame game, ScreenManager<IScreen> screenManager) {
        //check, if game is in dev mode
        if (isDevMode()) {
            //set log level
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        //create mod manager
        game.getSharedData().put(Shared.MOD_MANAGER, new DefaultModManager());

        //add screens
        screenManager.addScreen("jukusoft_intro", new JuKuSoftIntroScreen());
        screenManager.addScreen("logo_intro", new LogoIntroScreen());
        screenManager.addScreen("loading", new LoadingScreen());
        screenManager.addScreen("mainmenu", new MenuScreen());
        screenManager.addScreen("slot_selection", new SlotSelectionScreen());

        //prototype screens
        screenManager.addScreen("water_prototype", new WaterRenderScreen());
        screenManager.addScreen("dev_room", new DevRoomScreen());

        //get instance of loading screen
        LoadingScreen loading = (LoadingScreen) screenManager.getScreenByName("loading");

        //first get mod manager
        ModManager modManager = game.getSharedData().get(Shared.MOD_MANAGER, ModManager.class);

        if (modManager == null) {
            throw new NullPointerException("mod manager cannot be null.");
        }

        //add loading tasks
        this.addLoadingTasks(this, modManager, loading);

        //check, if preferences are available, else set default values
        GamePreferences prefs = game.getGeneralPreferences();
        prefs.putBooleanIfAbsent("engine_splash_screen", true);
        prefs.putBooleanIfAbsent("logo_splash_screen", true);
        prefs.putBooleanIfAbsent("dev_mode", false);
        prefs.putBooleanIfAbsent("sound_muted", false);
        prefs.putBooleanIfAbsent("music_muted", false);

        //save changes
        prefs.flush();

        //check, if engine splash screen is enabled
        if (prefs.getBoolean("engine_splash_screen", true)) {
            //push screen
            screenManager.push("jukusoft_intro");

            //push screen
            //screenManager.push("logo_intro");
        } else {
            //skip engine splash screen

            //check, if logo screen is enabled
            if (prefs.getBoolean("logo_splash_screen", true)) {
                //push screen
                screenManager.push("logo_intro");
            } else {
                //skip logo splash screen
                Gdx.app.debug("Game", "skip logo intro, caused by preferences.");

                //push screen
                screenManager.push("loading");
            }
        }
    }

    @Override
    protected void onDispose() {

    }

    protected void addLoadingTasks (IScreenGame game, ModManager modManager, LoadingScreen loading) {
        //add task to load mods
        loading.addTask(new ModLoadingTask(game.getAppName(), modManager), 0.15f);

        //load mod assets
        loading.addTask(new LoadAssetsTask(game.getAssetManager(), modManager), 0.85f);
    }

}
