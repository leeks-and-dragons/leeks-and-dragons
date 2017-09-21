package de.leeksanddragons.engine.renderer.map.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.map.IMapPage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.ColliderUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 21.09.2017.
 */
public class MapPage implements IMapPage {

    //page position
    protected float x = 0;
    protected float y = 0;

    //tile size
    protected float tileWidth = 32;
    protected float tileHeight = 32;

    //page size
    protected int tilesX = 10;
    protected int tilesY = 10;

    //page texture to draw
    protected Texture texture = null;

    //flag, if page was loaded
    protected boolean loaded = false;

    protected List<String> excludedLayers = new ArrayList<>();

    /**
    * default constructor
     *
     * @param x x position of page (in pixels)
     * @param y y position of page (in pixels)
    */
    public MapPage (float x, float y, int tilesX, int tilesY) {
        this.x = x;
        this.y = y;

        this.tilesX = tilesX;
        this.tilesY = tilesY;
    }

    @Override
    public float getWidth() {
        return this.tileWidth * this.tilesX;
    }

    @Override
    public float getHeight() {
        return this.tileHeight * this.tilesY;
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch, float x, float y) {
        if (this.texture == null) {
            return;
        }

        //check, if page is visible, if not, we dont need to draw page
        if (!isVisible(game.getCameraManager().getMainCamera())) {
            return;
        }

        //draw texture
        batch.draw(this.texture, x, y);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        this.draw(game, time, batch, this.x, this.y);
    }

    @Override
    public void dispose() {
        if (this.texture != null) {
            //dispose and delete texture
            this.texture.dispose();
            this.texture = null;
        }

        this.loaded = false;
    }

    @Override
    public void setTileSize(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public void generatePage(TiledMap map, int startTileX, int startTileY) {
        //first check, if texture already exists
        if (this.texture != null) {
            throw new IllegalStateException("cannot generate page, because texture already exists.");
        }

        //generate page

        //set flag
        this.loaded = true;
    }

    @Override
    public boolean isPageLoaded() {
        return this.loaded;
    }

    @Override
    public void unloadPage() {
        this.dispose();
    }

    @Override
    public void recreate(TiledMap map, int startTileX, int startTileY) {
        //unload page
        this.unloadPage();

        //generate page
        generatePage(map, startTileX, startTileY);
    }

    @Override
    public void addExcludedLayer(String layerName) {
        //add layer to list
        this.excludedLayers.add(layerName);
    }

    @Override
    public void removeExcludedLayer(String layerName) {
        //remove layer from list
        this.excludedLayers.add(layerName);
    }

    @Override
    public boolean isVisible(CameraHelper camera) {
        //left bottom corner
        float cameraX1 = camera.getX();
        float cameraY1 = camera.getY();

        //right top corner
        float cameraX2 = camera.getX() + camera.getViewportWidth();
        float cameraY2 = camera.getY() + camera.getViewportHeight();

        //check, if camera and page is overlaping
        return ColliderUtils.overlaping(this.x, this.x + getWidth(), cameraX1, cameraX2) && ColliderUtils.overlaping(this.y, this.y + getHeight(), cameraY1, cameraY2);
    }

}
