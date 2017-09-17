package de.leeksanddragons.game.screen.prototype;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.map.impl.DummyRegion;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.renderer.WaterRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 17.09.2017.
 */
public class DevRoomScreen extends BaseScreen {

    protected IRegion region = null;

    //water renderer
    protected WaterRenderer waterRenderer = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {

    }

    @Override
    public void onResume(IScreenGame game) {
        //initialize region, if neccessary
        if (this.region == null) {
            //TODO: create and load region
            this.region = new DummyRegion();

            //initialize region
            this.initRegion(game, this.region);
        }

        //initialize water renderer, if neccessary
        if (this.waterRenderer == null) {
            //create new water renderer
            this.createWaterRenderer();
        }
    }

    @Override
    public void onPause(IScreenGame game) {

    }

    /**
    * initialize region
    */
    protected void initRegion (IScreenGame game, IRegion region) {
        //set region position
        region.setPosition(0, 0);

        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //set camera bounds
        camera.setBounds(region.getX(), region.getY(), region.getX() + region.getWidth(), region.getY() + region.getHeight());
    }

    protected void createWaterRenderer () {
        //create new water renderer
        this.waterRenderer = new WaterRenderer(game);

        //get texture atlas
        TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("maingame_water_animation", TextureAtlas.class);

        //load water renderer
        this.waterRenderer.load(textureAtlas, "WaterLowestContrast", 200f);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //check, if region contains water
        if (region.hasWater()) {
            //update water renderer
            this.waterRenderer.update(game, time);
        }

        //update region
        this.region.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //check, if region contains water
        if (region.hasWater()) {
            //draw water
            this.waterRenderer.draw(game, time, batch);
        }

        //draw region
        this.region.draw(game, time, batch);
    }

    @Override
    public void dispose() {

    }

}
