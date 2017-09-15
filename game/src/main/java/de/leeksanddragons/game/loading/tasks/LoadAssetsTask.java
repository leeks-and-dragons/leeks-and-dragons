package de.leeksanddragons.game.loading.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.sun.org.apache.bcel.internal.generic.NEW;
import de.leeksanddragons.engine.memory.AssetInfo;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.memory.LoadingAssetParser;
import de.leeksanddragons.engine.mods.ModInfo;
import de.leeksanddragons.engine.mods.ModManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.loading.BaseLoadingTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loading task to load all required game assets
 *
 * Created by Justin on 14.09.2017.
 */
public class LoadAssetsTask extends BaseLoadingTask {

    //mod manager
    protected ModManager modManager = null;

    //elapsed time
    protected float elapsed = 0;

    protected enum LOADING_STATE {
            NEW_TASK, INITIALIZED, LOAD_ASSETS
    }

    //current loading state to avoid hanging up of application
    protected LOADING_STATE state = LOADING_STATE.NEW_TASK;

    //list with all mods, which supports asset pre-loading
    protected List<ModInfo> modList = null;

    //instance of asset manager
    protected GameAssetManager assetManager = null;

    //load_assets.json parser
    protected LoadingAssetParser parser = new LoadingAssetParser();

    //list all required assets (to check, if asset manager has finished)
    protected List<AssetInfo> assetList = new ArrayList<>();

    /**
    * default constructor
     *
     * @param assetManager instance of asset manager
     * @param modManager instance of mod manager
    */
    public LoadAssetsTask (GameAssetManager assetManager, ModManager modManager) {
        this.assetManager = assetManager;
        this.modManager = modManager;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        this.elapsed += time.getDeltaTime() * 1000;

        if (state == LOADING_STATE.NEW_TASK) {
            //initialize game
            init();

            return;
        } else if (state == LOADING_STATE.INITIALIZED) {
            //parse asset list
            parseAssetList();

            return;
        } else if (state == LOADING_STATE.LOAD_ASSETS) {
            //wait while all assets were loaded
            loadAssets();
        }
    }

    /**
    * initialize task and get all
    */
    protected void init () {
        this.percentageText = "Loading Assets";

        //get all mods
        this.modList = modManager.listAssetLoadingMods();

        Gdx.app.debug("Loading", "" + this.modList.size() + " mods supports asset pre-loading.");

        //set state
        this.state = LOADING_STATE.INITIALIZED;
    }

    protected void parseAssetList () {
        //parse load_assets.json in data directory
        try {
            this.parser.parseMod("./data/");
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.error("Loading", "Couldnt load data assets.", e);
        }

        //parse load_assets.json from every mod
        for (ModInfo mod : this.modList) {
            Gdx.app.debug("Loading", "parse load_assets.json from mod: " + mod.getPath());

            //parse load_assets.json
            try {
                this.parser.parseMod(mod.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                Gdx.app.error("Loading", "Couldnt load mod assets: " + mod.getPath(), e);
            }
        }

        //add all assets to asset manager
        for (AssetInfo asset : this.parser.listAllAssets()) {
            Gdx.app.debug("Loading", "load asset: " + asset.getPath());

            //load asset
            assetManager.load(asset.getPath(), asset.getLibGDXAssetClass());

            //block unloading of asset
            assetManager.addBlockingFile(asset.getPath());

            //add asset to list
            this.assetList.add(asset);
        }

        //set stage
        this.state = LOADING_STATE.LOAD_ASSETS;
    }

    protected void loadAssets () {
        float progress = assetManager.getProgress();

        if (progress >= 1f) {
            if (elapsed > 2000) {
                //check, if all assets were loaded
                for (AssetInfo asset : this.assetList) {
                    if (!assetManager.isLoaded(asset.getPath())) {
                        Gdx.app.log("LoadAssetsTask", "progress is 100%, but asset wasnt loaded yet: " + asset.getPath());

                        this.percentage = 0.99f;
                        return;
                    } else {
                        Gdx.app.log("LoadAssetsTask", "asset was loaded successfully: " + asset.getPath());

                        //check, if asset has an name
                        if (asset.hasName()) {
                            //store asset under name
                            assetManager.addAssetByName(asset.getName(), assetManager.get(asset.getPath(), asset.getLibGDXAssetClass()));

                            Gdx.app.debug("LoadAssetsTask", "stored asset under name '" + asset.getName() + "': " + asset.getPath());
                        }
                    }
                }

                this.percentage = progress;
            } else {
                this.percentage = 0.99f;
            }
        } else {
            this.percentage = progress;
        }
    }

}
