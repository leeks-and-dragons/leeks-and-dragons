package de.leeksanddragons.game.screen.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.camera.ResizeListener;
import de.leeksanddragons.engine.character.ICharacter;
import de.leeksanddragons.engine.character.impl.PlayerCharacter;
import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.EntityManager;
import de.leeksanddragons.engine.entity.impl.ECS;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.map.impl.LADRegion;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.renderer.water.WaterRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.shader.ShaderFactory;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.entity.factory.PlayerFactory;

import java.io.IOException;

/**
 * Created by Justin on 17.09.2017.
 */
public class DevRoomScreen extends BaseScreen implements ResizeListener {

    //path to loading wallpaper
    protected static final String LOADING_WALLPAPER_PATH = "./data/wallpaper/Loading_Screen.png";

    protected IRegion region = null;

    //water renderer
    protected WaterRenderer waterRenderer = null;

    //wallpaper, which is drawn, if
    protected Texture loadingTexture = null;

    //monochrome-filter shader
    protected ShaderProgram monochromeShader = null;

    //entity-component-system
    protected EntityManager ecs = null;

    //player entity
    protected Entity player = null;

    //player character
    protected ICharacter playerCharacter = null;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //create monochrome-filter shader
        try {
            this.monochromeShader = ShaderFactory.createShader("./data/shader/monochrome-filter/vertexShader.glsl", "./data/shader/monochrome-filter/pixelShader.glsl");
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    public void onResume(IScreenGame game) {
        //add resize listener
        game.addResizeListener(this);

        //check, if loading wallpaper is available
        if (this.loadingTexture == null) {
            //load loading wallpaper
            game.getAssetManager().load(LOADING_WALLPAPER_PATH, Texture.class);

            //wait, while texture is loading
            game.getAssetManager().finishLoadingAsset(LOADING_WALLPAPER_PATH);

            //get texture
            this.loadingTexture = game.getAssetManager().get(LOADING_WALLPAPER_PATH);
        }

        //initialize region, if neccessary
        if (this.region == null) {
            //create and load region
            this.region = new LADRegion();
            try {
                this.region.load(game, "./mods/maingame/maps/dev_room/dev_room.lrg");
            } catch (IOException e) {
                e.printStackTrace();
                Gdx.app.exit();
            }

            //initialize region
            this.initRegion(game, this.region);
        }

        //initialize water renderer, if neccessary
        if (this.waterRenderer == null) {
            //create new water renderer
            this.createWaterRenderer();
        }

        if (this.playerCharacter == null) {
            //create new player character
            this.playerCharacter = new PlayerCharacter();

            //load player character
            try {
                this.playerCharacter.load("./mods/maingame/character/cedric/cedric.json");
            } catch (IOException e) {
                e.printStackTrace();
                throw new GdxRuntimeException("IOException thrown while loading player character: " + e.getLocalizedMessage(), e);
            }
        }

        if (this.ecs == null) {
            //create new entity-component-system
            this.ecs = new ECS(game);

            //create new player entity
            this.player = PlayerFactory.createPlayer(this.ecs, this.playerCharacter, 100, 100);
            this.ecs.addEntity("player", this.player);
        }
    }

    @Override
    public void onPause(IScreenGame game) {
        //remove resize listener
        game.removeResizeListener(this);

        assetManager.unload(LOADING_WALLPAPER_PATH);
        this.loadingTexture = null;

        //dispose ecs
        this.ecs.dispose();
        this.ecs = null;
    }

    /**
    * initialize region
    */
    protected void initRegion (IScreenGame game, IRegion region) {
        //set region position
        region.setPosition(0, 0);

        //pre-load maps
        region.preloadMaps(0, 0);

        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //set camera bounds
        camera.setBounds(region.getX(), region.getX() + region.getWidth(), region.getY(), region.getY() + region.getHeight());

        //set target position
        camera.setTargetMiddlePos(camera.getViewportWidth() / 2, camera.getViewportHeight() / 2);
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
        //check, if region was pre-loaded
        if (!this.region.hasPreLoadingFinished(game.getCameraManager().getMainCamera())) {
            //update region to load map
            this.region.update(game, time);

            return;
        }

        /*if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            game.getCameraManager().getMainCamera().translate(-2, 0, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            game.getCameraManager().getMainCamera().translate(2, 0, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            game.getCameraManager().getMainCamera().translate(0, 2, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            game.getCameraManager().getMainCamera().translate(0, -2, 0);
        }*/

        //check, if region contains water
        if (region.hasWater()) {
            //update water renderer
            this.waterRenderer.update(game, time);
        }

        //update region
        this.region.update(game, time);

        //update entity-component-system
        this.ecs.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //check, if region was pre-loaded
        if (!this.region.hasPreLoadingFinished(game.getCameraManager().getMainCamera())) {
            //set shader
            batch.setShader(this.monochromeShader);
            this.monochromeShader.setUniformf("u_amount", 1.0f);

            //draw pre-loading text
            batch.draw(this.loadingTexture, camera.getX(), camera.getY(), camera.getViewportWidth(), camera.getViewportHeight());

            batch.flush();

            //reset shader
            batch.setShader(null);

            return;
        }

        //check, if region contains water
        if (region.hasWater()) {
            //draw water
            this.waterRenderer.draw(game, time, batch);
        }

        //draw region
        this.region.draw(game, time, batch);

        //draw entity-component-system
        this.ecs.draw(game, time, batch);
    }

    @Override
    public void onResize(int width, int height) {
        game.getCameraManager().getMainCamera().resize(width, height);
    }

    @Override
    public void dispose() {

    }

}
