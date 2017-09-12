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
public class LogoIntroScreen extends BaseScreen {

    protected static final String LOGO_PATH = "./data/logo/logo_screen.png";

    //logo image
    protected Texture logo = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load logo
        assetManager.load(LOGO_PATH, Texture.class);

        //force image loading
        assetManager.finishLoadingAsset(LOGO_PATH);

        //get image
        this.logo = assetManager.get(LOGO_PATH);

        //go to next screen after 1 second
        game.addTimerTask(2000l, () -> {
            //switch screen
            game.getScreenManager().leaveAllAndEnter("loading");

            //remove intro screen
            game.getScreenManager().removeScreen("logo_intro");
        });
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //we dont need to update this screen
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw logo
        batch.draw(this.logo, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {
        assetManager.unload(LOGO_PATH);
    }

}
