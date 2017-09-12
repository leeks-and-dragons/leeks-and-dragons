package de.leeksanddragons.game.screen;

import com.badlogic.gdx.assets.AssetManager;
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
public class JuKuSoftIntroScreen extends BaseScreen {

    protected static final String LOGO_PATH = "./data/logo/jukusoft_engine_logo.png";

    //jukusoft logo
    protected Texture jukusoftLogo = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load logo
        assetManager.load(LOGO_PATH, Texture.class);

        //force image loading
        assetManager.finishLoadingAsset(LOGO_PATH);

        //get image
        this.jukusoftLogo = assetManager.get(LOGO_PATH);

        //go to next screen after 500ms
        game.addTimerTask(500l, () -> {
            //switch screen
            game.getScreenManager().leaveAllAndEnter("logo_intro");

            //remove intro screen
            game.getScreenManager().removeScreen("jukusoft_intro");
        });
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw logo
        batch.draw(this.jukusoftLogo, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {
        //unload logo
        this.assetManager.unload(LOGO_PATH);
    }

}
