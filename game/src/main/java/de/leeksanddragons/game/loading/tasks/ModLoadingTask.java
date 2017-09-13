package de.leeksanddragons.game.loading.tasks;

import com.badlogic.gdx.Gdx;
import de.leeksanddragons.engine.mods.ModManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.loading.BaseLoadingTask;
import de.leeksanddragons.game.loading.LoadingTask;

/**
 * Created by Justin on 13.09.2017.
 */
public class ModLoadingTask extends BaseLoadingTask {

    //application name
    protected String appName = "";

    //instance of mod manager
    protected ModManager modManager = null;

    public ModLoadingTask (String appName, ModManager modManager) {
        this.appName = appName;
        this.modManager = modManager;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        if (percentage == 0) {
            percentageText = "Load mods in app directory";

            percentage = 0.2f;
        } else if (percentage == 0.2f) {
            //add all mods in local directory
            modManager.loadMods("./mods/");

            percentage = 0.5f;
        } else if (percentage == 0.5f) {
            percentageText = "Load mods from user.home directory";

            percentage = 0.7f;
        } else if (percentage == 0.7f) {
            //add all mods from home directory
            modManager.loadMods(FileUtils.getHomeModsDir(appName));

            Gdx.app.log("Loading", "" + modManager.countLoadedMods() + " mods loaded.");

            percentage = 1f;
        }
    }

}
