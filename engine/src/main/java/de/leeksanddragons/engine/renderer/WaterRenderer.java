package de.leeksanddragons.engine.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.HashMap;
import java.util.Map;

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

    //last frame
    protected TextureRegion lastFrame = null;

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

    //map for paging
    protected Map<TextureRegion,WaterRenderPage> pageMap = new HashMap<>();

    //current page
    protected IPage currentPage = null;

    //page width & height
    protected static final int PAGE_WIDTH = 256;
    protected static final int PAGE_HEIGHT = 256;

    public WaterRenderer (IScreenGame game) {
        this.game = game;

        //get asset manager
        this.assetManager = game.getAssetManager();
    }

    /**
    * load texture atlas
     *
     * @param atlasFile path to atlas file
     * @param animation animation name
     * @param frameDurationInMillis frame duration in milliseconds
    */
    public void load (String atlasFile, String animation, float frameDurationInMillis) {
        //replace windows backslashes with slashes to fix app hanging up, see also libGDX issue: https://github.com/libgdx/libgdx/issues/4888
        atlasFile = atlasFile.replace("\\", "/");

        //dispose all old pages
        this.disposeAllPages();

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
        assetManager.finishLoadingAsset(atlasFile);

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
        //dispose all old pages
        this.disposeAllPages();

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

            //generate pages
            this.generatePages(this.waterAnimation);
        }

        //increment elapsed time with delta time
        this.elapsed += time.getDeltaTime();

        //get current frame
        this.frame = this.waterAnimation.getKeyFrame(this.elapsed);

        //update page, if frame has changed
        if (this.lastFrame != this.frame) {
            //get current page
            this.currentPage = pageMap.get(this.frame);

            this.lastFrame = this.frame;
        }

        //this.frame.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //frame.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
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

        //Texture texture = this.frame.getTexture();

        //texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //draw tile
        //batch.draw(texture, this.x, this.y, /*camera.getViewportWidth(), camera.getViewportHeight()*/texture.getWidth(), texture.getHeight(), frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight());

        //draw frame
        //batch.draw(frame, 0, 0, 32, 32);

        if (this.currentPage != null) {
            //calculate startX and startY
            float w1 = camera.getX() / this.frame.getRegionWidth() - 1;
            float h1 = camera.getY() / this.frame.getRegionHeight() - 1;

            //get start position of first page on bottom left viewport
            float startX = w1 * this.frame.getRegionWidth();
            float startY = h1 * this.frame.getRegionHeight();

            //get camera position on top right border
            //float cameraXRight = camera.getX() + camera.getViewportWidth();
            //float cameraYTop = camera.getY() + camera.getViewportHeight();

            int requiredPagesX = (int) (camera.getViewportWidth() / this.currentPage.getWidth() + 1);
            int requiredPagesY = (int) (camera.getViewportHeight() / this.currentPage.getHeight() + 1);

            float offsetX = 0;
            float offsetY = 0;

            //fill screen with water animation
            for (int x = 0; x < requiredPagesX; x++) {
                for (int y = 0; y < requiredPagesY; y++) {
                    this.currentPage.draw(game, time, batch, startX + offsetX, startY + offsetY);

                    //increment offset y
                    offsetY += this.currentPage.getHeight();
                }

                //increment offset x
                offsetX += this.currentPage.getWidth();

                //reset offset y
                offsetY = 0;
            }
        }
    }

    protected void generatePages (Animation<TextureRegion> animation) {
        Object[] regions = animation.getKeyFrames();

        //generate pages for every frame
        for (Object region1 : regions) {
            TextureRegion region = (TextureRegion) region1;

            //create new page
            WaterRenderPage page = new WaterRenderPage(PAGE_WIDTH, PAGE_HEIGHT, region);

            //add frame to map
            this.pageMap.put(region, page);
        }
    }

    protected void disposeAllPages () {
        //iterate through all available pages
        for (Map.Entry<TextureRegion,WaterRenderPage> entry : this.pageMap.entrySet()) {
            //get page
            IPage page = entry.getValue();

            //remove page from map
            this.pageMap.remove(entry.getKey());

            //dispose page
            page.dispose();
        }

        //set current page to null
        this.currentPage = null;
    }

}
