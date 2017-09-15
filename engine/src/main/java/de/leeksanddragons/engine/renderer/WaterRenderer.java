package de.leeksanddragons.engine.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 14.09.2017.
 */
public class WaterRenderer implements IRenderer {

    //path to current atlas file
    protected String currentAtlasPath = "";

    //name of current animation
    protected String animationName = "";

    //instance of game
    protected IScreenGame game = null;

    //instance of asset manager
    protected GameAssetManager assetManager = null;

    //texture atlas
    protected TextureAtlas textureAtlas = null;

    //flag, if renderer was loaded
    protected boolean loaded = false;

    //current animation
    protected Animation<TextureRegion> waterAnimation = null;

    //current frame
    protected TextureRegion frame = null;

    //position
    protected float x = 0;
    protected float y = 0;

    //dimension
    protected float width = 0;
    protected float height = 0;

    //frame duration in milliseconds
    protected float frameDuration = 200f;

    //elapsed time
    protected float elapsed = 0;

    public WaterRenderer (IScreenGame game) {
        this.game = game;

        //get asset manager
        this.assetManager = game.getAssetManager();
    }

    /**
    * load texture atlas
     *
     * IMPORTANT: This method doesnt work yet, an issue was commited to libGDX developers
     *
     * @param atlasFile path to atlas file
     * @param animation animation name
     * @param frameDurationInMillis frame duration in milliseconds
    */
    public void load (String atlasFile, String animation, float frameDurationInMillis) {
        //set texture atlas to null
        this.textureAtlas = null;
        this.waterAnimation = null;
        this.frame = null;
        this.elapsed = 0;

        //set frame duration in ms
        this.frameDuration = frameDurationInMillis;

        Gdx.app.debug("WaterRenderer", "try to load atlas file: " + atlasFile);

        //load asset asynchronous and get atlas in update() method
        assetManager.load(atlasFile, TextureAtlas.class);

        if (assetManager.isLoaded(atlasFile)) {
            Gdx.app.log("Water", "water animation is already loaded.");
        } else {
            Gdx.app.log("Water", "water animation isnt loaded yet.");
        }

        //finish loading
        //assetManager.finishLoadingAsset(atlasFile);

        Gdx.app.debug("WaterRenderer", "atlas file loaded: " + atlasFile);

        //unload old atlas file (for reference counting)
        if (!this.currentAtlasPath.isEmpty() && !this.currentAtlasPath.equals(atlasFile)) {
            //unload old asset
            assetManager.unload(this.currentAtlasPath);

            //log message
            Gdx.app.debug("Water", "unload old atlas file: " + this.currentAtlasPath);
        }

        //set new current file
        this.currentAtlasPath = atlasFile;

        //set current animation
        this.animationName = animation;

        //set flag
        this.loaded = true;
    }

    /**
     * load texture atlas
     *
     * @param textureAtlas current texture atlas
     * @param animationName animation name
     * @param frameDurationInMillis frame duration in milliseconds
     */
    public void load (TextureAtlas textureAtlas, String animationName, float frameDurationInMillis) {
        //set texture atlas to null
        this.textureAtlas = null;
        this.waterAnimation = null;
        this.frame = null;
        this.elapsed = 0;

        //set frame duration in ms
        this.frameDuration = frameDurationInMillis;

        //first, check if texture atlas is not null
        if (textureAtlas == null) {
            throw new NullPointerException("texture atlas cannot be null.");
        }

        if (animationName == null || animationName.isEmpty()) {
            throw new IllegalArgumentException("animation name cannot be null or empty.");
        }

        if (frameDurationInMillis <= 0) {
            throw new IllegalArgumentException("frameDurationInMillis has to be greater than 0.");
        }

        //save texture atlas
        this.textureAtlas = textureAtlas;

        //set current animation
        this.animationName = animationName;

        //set flag
        this.loaded = true;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //check, if renderer was already loaded
        if (!this.loaded) {
            throw new GdxRuntimeException("Cannot update water, because renderer wasnt loaded yet. Call load() method first update() call.");
        }

        //check, if texture atlas exists
        if (this.textureAtlas == null) {
            //check, if texture atlas was loaded by asset manager
            if (assetManager.isLoaded(this.currentAtlasPath)) {
                //Gdx.app.log("WaterUpdate", "water animation is already loaded.");

                //get atlas
                this.textureAtlas = assetManager.get(this.currentAtlasPath);
            } else {
                //Gdx.app.log("WaterUpdate", "water animation isnt loaded yet.");
            }

            return;
        }

        if (this.waterAnimation == null) {
            //get animation
            this.waterAnimation = new Animation<TextureRegion>(this.frameDuration / 1000, this.textureAtlas.findRegions(this.animationName), Animation.PlayMode.LOOP);
        }

        //increment elapsed time with delta time
        this.elapsed += time.getDeltaTime();

        //get current frame
        this.frame = this.waterAnimation.getKeyFrame(this.elapsed);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //check, if renderer was already loaded
        if (!this.loaded) {
            throw new GdxRuntimeException("Cannot renderer water, because renderer wasnt loaded yet. Call load() method first rendering.");
        }

        //while frame wasnt loaded dont draw animation
        if (this.frame == null) {
            return;
        }

        //get main camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw tile
        batch.draw(this.frame, this.x, this.y, camera.getViewportWidth(), camera.getViewportHeight());
    }

}
