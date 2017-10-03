package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IMap;
import de.leeksanddragons.engine.renderer.map.impl.LADMapRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    protected RENDER_METHOD renderMethod = RENDER_METHOD.LAD_RENDERER;

    //map renderer
    protected MapRenderer mapRenderer = null;

    protected LADMapRenderer ladMapRenderer = null;

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

    //tile dimension
    protected int tileWidth = 0;
    protected int tileHeight = 0;

    //bounding box of map
    protected Vector3 minVector = new Vector3(0, 0, 0);
    protected Vector3 maxVector = new Vector3(100, 100, 0);
    protected BoundingBox boundingBox = new BoundingBox(minVector, maxVector);

    //map specific music information (global)
    protected String musicPath = "";
    protected boolean hasMusic = false;

    //footstep sonds
    protected List<String> requiredFootstepSounds = new ArrayList<>();
    protected int[][] footsteps = null;
    protected String[] footstepSounds = null;

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
    public float getWidth() {
        return this.mapWidth;
    }

    @Override
    public float getHeight() {
        return this.mapHeight;
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
        } else if (this.renderMethod == RENDER_METHOD.LAD_RENDERER) {
            //create new LAD map renderer
            this.ladMapRenderer = new LADMapRenderer(this.game, this.map, getX(), getY());
        } else {
            throw new UnsupportedOperationException("render method isnt supported yet: " + this.renderMethod.name());
        }

        Gdx.app.debug("LADMap", "map loading finished: " + this.tmxPath);

        //get music path
        if (this.mapProperties.containsKey("music_path")) {
            this.hasMusic = true;
            this.musicPath = this.mapProperties.get("music_path", String.class);
        } else {
            this.hasMusic = false;
            this.musicPath = "";
        }

        this.loadFootsteps();

        //TODO: find object layers for sound and so on

        //TODO: add code here

        this.loading_state = LOADING_STATE.LOADING_FINISHED;
    }

    protected void loadFootsteps () {
        List<MapObject> mapObjects = new ArrayList<>();

        //temporary map which contains sound path to sound id
        Map<String,Integer> tmpMap = new HashMap<>();

        //iterate through all map layers
        for (MapLayer layer : map.getLayers()) {
            //iterate through all layer objects
            for (MapObject obj : layer.getObjects()) {
                //check, if object has attribute "footstep_sound"
                if (obj.getProperties().containsKey("footstep_sound")) {
                    String footstepPath = obj.getProperties().get("footstep_sound", String.class);

                    //check, if path is null or empty
                    if (footstepPath == null || footstepPath.isEmpty()) {
                        Gdx.app.error("LADMap", "map '" + this.tmxPath + "' contains a footstep layer with wrong footstep definition: " + layer.getName());

                        continue;
                    }

                    //add footstep sound object to list
                    mapObjects.add(obj);

                    //check, if footstep path is already in list
                    if (!this.requiredFootstepSounds.contains(footstepPath)) {
                        Gdx.app.debug("LADMap", "new footstep sound path found: " + footstepPath);

                        //add sound to list, so it will be loaded
                        this.requiredFootstepSounds.add(footstepPath);
                    }
                }
            }
        }

        //create new array for all footstep sounds
        this.footstepSounds = new String[this.requiredFootstepSounds.size() + 1];

        //start with 1, because 0 means "no footstep sound"
        int i = 1;

        //load footsteps
        for (String path : this.requiredFootstepSounds) {
            this.game.getAssetManager().load(path, Sound.class);

            this.footstepSounds[i] = path;

            //add path to temporary map to find sound ID later
            tmpMap.put(path, i);

            i++;
        }

        //get properties
        MapProperties props = map.getProperties();
        int width = props.get("width", Integer.class);//get width in tiles
        int height = props.get("height", Integer.class);//get height in tiles

        //get width and hight of an single tile
        this.tileWidth = props.get("tilewidth", Integer.class);
        this.tileHeight = props.get("tileheight", Integer.class);

        //create new integer array
        this.footsteps = new int[width][height];

        //fill array
        for (MapObject obj : mapObjects) {
            /*obj.getProperties().getKeys().forEachRemaining((String key) -> {
                System.out.println("object property: " + key);
            });*/

            //calculate start tile
            int startX = (int) (obj.getProperties().get("x", Float.class) / tileWidth);
            int startY = (int) (obj.getProperties().get("y", Float.class) / tileHeight);

            //get width and height of object
            float objWidth = obj.getProperties().get("width", Float.class);
            float objHeight = obj.getProperties().get("height", Float.class);

            float x2 = obj.getProperties().get("x", Float.class) + objWidth;
            float y2 = obj.getProperties().get("y", Float.class) + objHeight;

            if (x2 % tileWidth != 0) {
                x2 += (x2 % tileWidth);
            }

            if (y2 % tileHeight != 0) {
                y2 += (y2 % tileHeight);
            }

            //get sound ID
            int soundID = tmpMap.get(obj.getProperties().get("footstep_sound", String.class));

            //calculate end tile
            int endX = (int) (x2 / tileWidth);
            int endY = (int) (y2 / tileHeight);

            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    this.footsteps[x][y] = soundID;
                }
            }
        }
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
    public boolean hasGlobalMusicPath() {
        return this.hasMusic;
    }

    @Override
    public String getGlobalMusicPath() {
        return this.musicPath;
    }

    @Override
    public String getFootstepSound(float playerX, float playerY) {
        //calculate relative position on map
        float relX = playerX - this.x;
        float relY = playerY - this.y;

        //get cell
        int x = (int) relX / this.tileWidth;
        int y = (int) relY / this.tileHeight;

        //check, if cell is in bounds
        if (x < 0 || x > this.getWidth()) {
            Gdx.app.error("LADMap", "getFootstepSound: cell (" + x + ", " + y + ") isnt in bounds.");

            return null;
        }

        if (y < 0 || y > this.getHeight()) {
            Gdx.app.error("LADMap", "getFootstepSound: cell (" + x + ", " + y + ") isnt in bounds.");

            return null;
        }

        //get footstep number of cell
        int k = this.footsteps[x][y];

        if (k <= 0) {
            return null;
        }

        //get path to footstep sound
        return this.footstepSounds[k];
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

        //update map
        if (this.renderMethod == RENDER_METHOD.LAD_RENDERER) {
            //update (pre-load) map
            this.ladMapRenderer.update(game, time);
        }
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
            //this.mapRenderer.setView(camera.getCombined(), /*this.x - */camera.getX(), /*this.y - */camera.getY() , camera.getViewportWidth(), camera.getViewportHeight());
            this.mapRenderer.setView(camera.getOriginalCamera());

            //end batch
            batch.flush();
            batch.end();

            //render map
            this.mapRenderer.render();

            //begin batch
            batch.begin();
        } else if (this.renderMethod == RENDER_METHOD.LAD_RENDERER) {
            //draw map
            this.ladMapRenderer.draw(game, time, batch);
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

        if (this.ladMapRenderer != null) {
            //dispose map renderer with all pages
            this.ladMapRenderer.dispose();

            this.ladMapRenderer = null;
        }

        //unload footstep sounds
        for (String path : this.requiredFootstepSounds) {
            this.game.getAssetManager().unload(path);
        }
    }

}
