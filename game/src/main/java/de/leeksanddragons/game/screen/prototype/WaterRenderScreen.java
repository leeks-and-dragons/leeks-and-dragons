package de.leeksanddragons.game.screen.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

            //get (pre-loaded) texture atlas
            TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("maingame_water_animation", TextureAtlas.class);

            //load water renderer
            this.waterRenderer.load(textureAtlas, "WaterLowestContrast", 200f);
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
