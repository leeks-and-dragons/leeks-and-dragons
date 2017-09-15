package de.leeksanddragons.game.screen.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.renderer.WaterRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;

/**
 * Created by Justin on 15.09.2017.
 */
public class WaterRenderScreen extends BaseScreen {

    //water renderer
    protected WaterRenderer waterRenderer = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {

    }

    @Override
    public void onResume() {
        if (this.waterRenderer == null) {
            //create new water renderer
            this.waterRenderer = new WaterRenderer(this.game);

            File file = new File("mods/maingame/animation/water/WaterAnimation.atlas");
            Gdx.app.debug("WaterRenderer", "atlas file: " + file.getAbsolutePath());

            //load renderer
            this.waterRenderer.load(file.getAbsolutePath(), "WaterLowestContrast", 200f);
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //update renderer
        this.waterRenderer.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //draw renderer
        this.waterRenderer.draw(game, time, batch);
    }

    @Override
    public void dispose() {

    }

}
