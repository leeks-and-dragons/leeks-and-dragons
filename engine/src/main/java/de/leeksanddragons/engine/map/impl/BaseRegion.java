package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IMap;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 17.09.2017.
 */
public abstract class BaseRegion implements IRegion {

    //position
    protected float x = 0;
    protected float y = 0;

    //width & height of map in tiles
    protected int xTilesPerMap = 30;
    protected int yTilesPerMap = 30;

    //tile size in pixels
    protected int tileWidth = 32;
    protected int tileHeight = 32;

    //width and height in maps
    private int widthInMaps = 1;
    private int heightInMaps = 1;

    //flag, if region contains water
    protected boolean hasWater = true;

    //bounds
    protected int minX = 0;
    protected int maxX = 0;
    protected int minY = 0;
    protected int maxY = 0;

    //offset for array index calculation
    private int offsetX = 0;
    private int offsetY = 0;

    //array with all maps
    protected IMap[][] maps = null;

    @Override
    public boolean hasWater() {
        return this.hasWater;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
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
        return getMapWidth() * this.widthInMaps;
    }

    @Override
    public float getHeight() {
        return getMapHeight() * this.heightInMaps;
    }

    /**
     * get width of a single map
     *
     * @return width of a single map
     */
    public float getMapWidth () {
        return this.xTilesPerMap * this.tileWidth;
    }

    /**
     * get height of a single map
     *
     * @return height of a single map
     */
    public float getMapHeight () {
        return this.yTilesPerMap * this.tileHeight;
    }

    /**
     * check, if map index is in bounds
     *
     * @return true, if map index is in bounds
     */
    public boolean isInBounds (int xIndex, int yIndex) {
        return (xIndex >= minX && yIndex <= maxX) && (yIndex >= minY && yIndex <= maxY);
    }

    protected void setBounds (int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        //calculate width and height of map
        this.widthInMaps = maxX - minX + 1;
        this.heightInMaps = maxY - minY + 1;

        //check, if bounds contains (0, 0)
        if (minX > 0 || maxX < 0) {
            throw new IllegalArgumentException("minX has to be <= 0 and maxX has to be >= 0.");
        }

        if (minY > 0 || maxY < 0) {
            throw new IllegalArgumentException("minY has to be <= 0 and maxY has to be >= 0.");
        }

        //calculate offset
        this.offsetX = Math.abs(minX);
        this.offsetY = Math.abs(minY);

        //create new map array
        this.maps = new IMap[widthInMaps][heightInMaps];
    }

    /**
     * check, if map is visible
     *
     * @return true, if map is visible
     */
    public boolean isMapVisible (int xIndex, int yIndex, CameraHelper camera) {
        if (!isInBounds(xIndex, yIndex)) {
            throw new ArrayIndexOutOfBoundsException("map (" + xIndex + ", " + yIndex + ") doesnt exists in this region.");
        }

        IMap map = getMap(xIndex, yIndex);

        if (map == null) {
            throw new NullPointerException("map cannot be null.");
        }

        //check if map is in frustum
        return camera.getFrustum().boundsInFrustum(map.getBoundingBox());
    }

    protected int getXIndex (int x) {
        return x + this.offsetX;
    }

    protected int getYIndex (int y) {
        return y + this.offsetY;
    }

    public int getWidthInMaps () {
        return this.widthInMaps;
    }

    public int getHeightInMaps () {
        return this.heightInMaps;
    }

    @Override
    public IMap getMap (int x, int y) {
        //check array length
        return this.maps[getXIndex(x)][getYIndex(y)];
    }

}
