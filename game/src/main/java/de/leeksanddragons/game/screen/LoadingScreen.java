package de.leeksanddragons.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 12.09.2017.
 */
public class LoadingScreen extends BaseScreen {

    protected static final String BG_PATH = "";

    //background image
    protected Texture bgTexture = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load background image first
        //assetManager.load();
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {

    }

    @Override
    public void dispose() {

    }

}
