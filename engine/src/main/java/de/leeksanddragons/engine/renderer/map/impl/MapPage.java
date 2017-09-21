package de.leeksanddragons.engine.renderer.map.impl;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.map.IMapPage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.ColliderUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 21.09.2017.
 */
public class MapPage implements IMapPage {

    //page position
    protected float x = 0;
    protected float y = 0;

    //tile size in pixels
    protected int tileWidth = 32;
    protected int tileHeight = 32;

    //page size
    protected int tilesX = 10;
    protected int tilesY = 10;

    //page texture to draw
    protected Texture texture = null;

    //flag, if page was loaded
    protected volatile boolean loading = false;
    protected boolean loaded = false;

    protected List<String> excludedLayers = new ArrayList<>();

    //only for caching purposes while preparing page
    protected List<Texture> preparedTextures = new ArrayList<>();
    protected Map<Texture,Pixmap> pixmapMap = new HashMap<>();

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
    public boolean isPageLoading() {
        return this.loading;
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
    public void setTileSize(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    @Override
    public void setPageSizeInTiles(int tilesX, int tilesY) {
        if (isPageLoaded() || isPageLoading()) {
            throw new IllegalStateException("You cannot set page size after loading page. Call method before generating page.");
        }

        this.tilesX = tilesX;
        this.tilesY = tilesY;
    }

    @Override
    public void generatePage(TiledMap map, int startTileX, int startTileY) {
        if (isPageLoading()) {
            throw new IllegalStateException("Cannot generate page, because page is already loading.");
        }

        //first check, if texture already exists
        if (this.texture != null) {
            throw new IllegalStateException("cannot generate page, because texture already exists.");
        }

        //set flag
        this.loading = true;

        //create new pixmap
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);

        //iterate through all layers
        for (MapLayer layer : map.getLayers()) {
            //check, if layer name is excluded
            if (this.excludedLayers.contains(layer.getName())) {
                //dont draw this layer
                continue;
            }

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer, pixmap, startTileX, startTileY);
                } else if (layer instanceof TiledMapImageLayer) {
                    throw new UnsupportedOperationException("image layers arent supported yet.");
                    //renderImageLayer((TiledMapImageLayer)layer);
                } else {
                    //we dont need to draw objects

                    //renderObjects(layer);
                }
            }
        }

        //create texture from pixmap
        this.texture = new Texture(pixmap);

        //dispose generated pixmap
        pixmap.dispose();

        //dispose tile texture pixmap of tilesets
        for (Texture texture : this.preparedTextures) {
            //get pixmap
            Pixmap pixmap1 = pixmapMap.get(texture);

            //remove pixmap from map
            pixmapMap.remove(pixmap1);

            //dispose pixmap
            texture.getTextureData().disposePixmap();

            pixmap1.dispose();
        }

        //clear list
        this.preparedTextures.clear();

        //set flag
        this.loaded = true;
        this.loading = false;
    }

    protected void renderTileLayer (TiledMapTileLayer layer, Pixmap pixmap, int startTileX, int startTileY) {
        for (int x = startTileX; x < startTileX + tilesX; x++) {
            for (int y = startTileY; y < startTileY + tilesY; y++) {
                //get cell
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                //get tile
                TiledMapTile tile = cell.getTile();

                //only draw tile, if tile is set
                if (tile == null) {
                    continue;
                }

                //get texture region
                TextureRegion region = tile.getTextureRegion();

                //get texture
                Texture tileSetTexture = region.getTexture();

                //check, if texture is already prepared
                if (!tileSetTexture.getTextureData().isPrepared()) {
                    //prepare texture
                    tileSetTexture.getTextureData().prepare();

                    this.preparedTextures.add(tileSetTexture);
                }

                //get pixmap from tile texture
                Pixmap tilePixmap = tileSetTexture.getTextureData().consumePixmap();

                //calculate start position
                int startX = (x - startTileX) * tileWidth;
                int startY = (y - startTileY) * tileHeight;

                //draw tile
                pixmap.drawPixmap(tilePixmap, startX, startY, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
            }
        }
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
