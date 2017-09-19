package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IMap;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 18.09.2017.
 */
public class LADMap implements IMap {

    //path to tmx map file
    protected String tmxPath = "";

    public enum LOADING_STATE {
        NOT_LOADED, LOADING, LOADING_FINISHED
    }

    public enum RENDER_METHOD {
        LIBGDX_RENDERER, LAD_RENDERER
    }

    //flag, if map is loading or loaded
    protected LOADING_STATE loading_state = LOADING_STATE.NOT_LOADED;

    //renderer
    protected RENDER_METHOD renderMethod = RENDER_METHOD.LIBGDX_RENDERER;

    //map renderer
    protected MapRenderer mapRenderer = null;

    //instance of game
    protected IScreenGame game = null;

    //tiled map
    protected TiledMap map = null;
    protected MapProperties mapProperties = null;

    //map position
    protected float x = 0;
    protected float y = 0;

    //map dimension
    protected float mapWidth = 0;
    protected float mapHeight = 0;

    //bounding box of map
    protected Vector3 minVector = new Vector3(0, 0, 0);
    protected Vector3 maxVector = new Vector3(100, 100, 0);
    protected BoundingBox boundingBox = new BoundingBox(minVector, maxVector);

    /**
    * default constructor
     *
     * @param x x position of map
     * @param y y position of map
     * @param mapWidth width of map in pixels
     * @param mapHeight height of map in pixels
     * @param game instance of game
     * @param tmxPath path to tmx map
    */
    public LADMap (float x, float y, float mapWidth, float mapHeight, IScreenGame game, String tmxPath) {
        this.x = x;
        this.y = y;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.game = game;
        this.tmxPath = tmxPath;

        //set bounding box
        this.updateBoundingBox();
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public String getMapPath() {
        return this.tmxPath;
    }

    /**
    * update values of bounding box
    */
    protected void updateBoundingBox () {
        this.boundingBox.min.x = getX();
        this.boundingBox.min.y = getY();
        this.boundingBox.max.x = getX() + mapWidth;
        this.boundingBox.max.y = getY() + mapHeight;
    }

    @Override
    public void loadMap() {
        if (isLoading() || isLoaded()) {
            //map is already loading or loaded
            return;
        }

        //load tiled map with asset manager
        game.getAssetManager().load(this.tmxPath, TiledMap.class);

        //set loading flag
        this.loading_state = LOADING_STATE.LOADING;
    }

    /**
    * finish loading after asset manager has loaded tiled map
    */
    protected void finishLoading (SpriteBatch batch) {
        //get properties
        this.mapProperties = this.map.getProperties();

        if (this.renderMethod == RENDER_METHOD.LIBGDX_RENDERER) {
            //create new libgdx map renderer
            this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, batch);
        }

        Gdx.app.debug("LADMap", "map loading finished: " + this.tmxPath);

        //TODO: find object layers for sound and so on

        //TODO: add code here

        this.loading_state = LOADING_STATE.LOADING_FINISHED;
    }

    @Override
    public boolean isLoading() {
        return this.loading_state == LOADING_STATE.LOADING;
    }

    @Override
    public boolean isLoaded() {
        return this.loading_state == LOADING_STATE.LOADING_FINISHED;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //check, if map is in loading process
        if (isLoading()) {
            //check, if tiled map was loaded
            if (game.getAssetManager().isLoaded(this.tmxPath, TiledMap.class)) {
                //get tiled map from asset manager
                this.map = game.getAssetManager().get(this.tmxPath);

                //finish loading
                this.finishLoading(game.getSpriteBatch());
            }
        }

        //only update, if map was already loaded
        if (!isLoaded()) {
            return;
        }

        //TODO: update map
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //only draw, if map was already loaded
        if (!isLoaded()) {
            //Gdx.app.error("LADMap", "Cannot draw map, because map isnt loaded yet.");

            return;
        }

        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw map
        if (this.renderMethod == RENDER_METHOD.LIBGDX_RENDERER) {
            //set camera
            this.mapRenderer.setView(camera.getCombined(), this.x - camera.getX(), this.y - camera.getY(), camera.getViewportWidth(), camera.getViewportHeight());

            //end batch
            batch.flush();
            batch.end();

            //render map
            this.mapRenderer.render();

            //begin batch
            batch.begin();
        } else {
            throw new UnsupportedOperationException("other renderer methods arent supported yet.");
        }
    }

    @Override
    public void dispose() {
        if (this.map != null) {
            //unload map
            game.getAssetManager().unload(this.tmxPath);

            this.map = null;

            //reset loading state
            this.loading_state = LOADING_STATE.NOT_LOADED;
        }
    }

}