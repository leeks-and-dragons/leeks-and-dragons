package de.leeksanddragons.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 12.09.2017.
 */
public class LoadingScreen extends BaseScreen {

    protected static final String BG_PATH = "./data/wallpaper/Loading_Screen.png";

    //background image
    protected Texture bgTexture = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load background image first
        assetManager.load(BG_PATH, Texture.class);

        //force finish asset loading
        assetManager.finishLoadingAsset(BG_PATH);

        //get texture
        this.bgTexture = assetManager.get(BG_PATH);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {
        //unload image
        assetManager.unload(BG_PATH);
    }

    protected void gotoMainMenu () {
        game.getScreenManager().leaveAllAndEnter("mainmenu");

        //remove this screen
        game.getScreenManager().removeScreen("loading");
    }

}
