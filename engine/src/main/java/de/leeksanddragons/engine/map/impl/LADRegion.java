package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IMap;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 17.09.2017.
 */
public class LADRegion extends BaseRegion {

    //path to .lrf file
    protected String path = "";

    //directory of region
    protected String dir = "";

    //flag, if region was preloaded
    protected boolean hasPreloaded = false;

    //maps in loading process
    protected List<IMap> loadingMaps = new ArrayList<>();

    //maps which are visible
    protected List<IMap> visibleMaps = new ArrayList<>();

    //temporary list for performance optimization
    protected List<IMap> tmpList = new ArrayList<>();

    //offset
    protected float offsetX = 0;
    protected float offsetY = 0;

    //value, how many maps near current maps are visible
    protected int visibleMapDistance = 1;

    /**
    * default constructor
    */
    public LADRegion () {
        //
    }

    /**
    * load region
    */
    @Override
    public void load (IScreenGame game, String path) throws IOException {
       //save path to region file
        this.path = path;

        //check, if file exists
        if (!new File(path).exists()) {
            throw new FileNotFoundException("Couldnt found region file: " + path);
        }

        //save directory of file
        this.dir = new File(path).getParent().replace("\\", "/") + "/";

        Gdx.app.debug("LADRegion", "load region: " + path + ", directory: " + this.dir);

        //read file content
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);

        //create json object
        JSONObject json = new JSONObject(content);

        //get meta data
        JSONObject meta = json.getJSONObject("meta");

        //flag, if water has to be rendered
        this.hasWater = meta.getBoolean("containsWater");

        //parse tile width and height
        this.tileWidth = meta.getInt("tileWidth");
        this.tileHeight = meta.getInt("tileHeight");

        //parse meta data
        this.xTilesPerMap = meta.getInt("xTilesPerMap");
        this.yTilesPerMap = meta.getInt("yTilesPerMap");

        //parse bounds
        int minX = meta.getInt("minX");
        int maxX = meta.getInt("maxX");
        int minY = meta.getInt("minY");
        int maxY = meta.getInt("maxY");

        //set bounds and calculate width and height of region
        this.setBounds(minX, maxX, minY, maxY);

        //get array with maps
        JSONArray array = json.getJSONArray("maps");

        //iterate through maps
        for (int i = 0; i < array.length(); i++) {
            //parse and add map
            this.parseMap(game, array.getJSONObject(i));
        }
    }

    protected void parseMap (IScreenGame game, JSONObject json) {
        //get x and y coordinates
        int x = json.getInt("x");
        int y = json.getInt("y");

        //get file to tmx map
        String tmxFile = json.getString("tmx_file");

        //get full tmx file
        tmxFile = this.dir + tmxFile;

        //create map
        IMap map = new LADMap(x, y, getMapWidth(), getMapHeight(), game, tmxFile);

        //add map to array
        this.maps[getXIndex(x)][getYIndex(y)] = map;
    }

    protected void preloadMapIfNeccessary (int x, int y) {
        //first, get map
        IMap map = getMap(x, y);

        //check, if map is already loading or loaded
        if (map.isLoading() || map.isLoaded()) {
            //we dont need to pre-load map
        } else {
            Gdx.app.debug("LADRegion", "try to pre-load map (" + x + ", " + y + "): " + map.getMapPath());

            //start loading map (async)
            map.loadMap();

            //add loading map to list
            this.loadingMaps.add(map);
        }
    }

    @Override
    public void update (IScreenGame game, GameTime time) {
        //clear temporary list
        this.tmpList.clear();

        //update all maps, which are loading
        for (IMap map : this.loadingMaps) {
            //check, if map has finish loading
            if (map.isLoaded()) {
                //add map to temporary list, so it will be removed from loading list after iteration
            }

            //update map, so map can load
            map.update(game, time);
        }

        //remove all entries from temporary list
        this.loadingMaps.removeAll(this.tmpList);

        //calculate visible maps
        this.calculateVisibleMaps(game);

        //update all visible maps
        for (IMap map : this.visibleMaps) {
            map.update(game, time);
        }
    }

    @Override
    public void draw (IScreenGame game, GameTime time, SpriteBatch batch) {
        //draw all visible maps
        for (IMap map : this.visibleMaps) {
            map.draw(game, time, batch);
        }
    }

    /**
    * check, if all visible maps are already visible and load new maps
    */
    protected void calculateVisibleMaps (IScreenGame game) {
        //first, get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //get mittle coordinates of camera
        float middleX = camera.getX() + (camera.getViewportWidth() / 2) - this.offsetX;
        float middleY = camera.getY() + (camera.getViewportHeight() / 2) - this.offsetY;

        //clear temporary list
        this.tmpList.clear();

        //get map in middle
        int mapX = (int) (middleX / getMapWidth());
        int mapY = (int) (middleY / getMapHeight());

        //add current map, if exists
        addMapToListIfExists(mapX, mapY, camera, this.tmpList);

        //add maps near current maps
        for (int i = 0; i < this.visibleMapDistance; i++) {
            //add neighbour maps
            addMapToListIfExists(mapX - i, mapY, camera, this.tmpList);
            addMapToListIfExists(mapX + i, mapY, camera, this.tmpList);
            addMapToListIfExists(mapX, mapY - i, camera, this.tmpList);
            addMapToListIfExists(mapX, mapY + i, camera, this.tmpList);

            addMapToListIfExists(mapX - i, mapY - i, camera, this.tmpList);
            addMapToListIfExists(mapX - i, mapY + i, camera, this.tmpList);
            addMapToListIfExists(mapX + i, mapY - i, camera, this.tmpList);
            addMapToListIfExists(mapX + i, mapY - i, camera, this.tmpList);
        }

        //clear visible maps list
        this.visibleMaps.clear();

        //add maps
        this.visibleMaps.addAll(this.tmpList);
    }

    protected void addMapToListIfExists (int x, int y, CameraHelper camera, List<IMap> list) {
        //first, check, if map is in bounds
        if (!isInBounds(x, y)) {
            //we dont need to add map
            return;
        }

        //check, if map is visible
        if (isMapVisible(x, y, camera)) {
            //add map to list
            list.add(getMap(x, y));

            this.preloadMapIfNeccessary(x, y);
        }
    }

    @Override
    public void preloadMaps (float currentX, float currentY) {
        //TODO: add code here

        this.hasPreloaded = true;
    }

    @Override
    public IMap getCurrentMap(CameraHelper camera) {
        //get mittle coordinates of camera
        float middleX = camera.getX() + (camera.getViewportWidth() / 2) - this.offsetX;
        float middleY = camera.getY() + (camera.getViewportHeight() / 2) - this.offsetY;

        //get map in middle
        int mapX = (int) (middleX / getMapWidth());
        int mapY = (int) (middleY / getMapHeight());

        //check, if map is in bounds
        if (!isInBounds(mapX, mapY)) {
            return null;
        }

        return getMap(mapX, mapY);
    }

    @Override
    public boolean hasPreLoadingFinished () {
        return this.hasPreloaded;
    }

}
