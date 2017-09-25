package de.leeksanddragons.engine.renderer.map.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.map.IMapPage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.ColliderUtils;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.ScreenshotUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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
    protected int tilesX = 20;
    protected int tilesY = 20;

    //page texture to draw
    protected Texture texture = null;

    //flag, if page was loaded
    protected volatile boolean loading = false;
    protected boolean asyncTask = false;
    protected boolean loaded = false;

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
    public int getWidth() {
        return this.tileWidth * this.tilesX;
    }

    @Override
    public int getHeight() {
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
        batch.draw(this.texture, x, y, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), false, true);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //System.out.println("draw page.");

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
    public void generatePage(IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY) {
        Gdx.app.debug("MapPage", "generatePage.");

        if (isPageLoading() && !asyncTask) {
            throw new IllegalStateException("Cannot generate page, because page is already loading.");
        }

        //first check, if texture already exists
        if (this.texture != null) {
            throw new IllegalStateException("cannot generate page, because texture already exists.");
        }

        //set flag
        this.loading = true;

        Gdx.app.debug("MapPage", "start generating map page, startTileX: " + startTileX + ", startTileY: " + startTileY);

        //get map render camera
        CameraHelper camera = game.getCameraManager().getCustomCamera(9);

        //first reset camera
        camera.reset();

        //set camera dimension
        camera.resize(getWidth(), getHeight());

        //move camera
        //camera.translate(-(tileWidth * startTileX), -(tileHeight * startTileY), 0);
        camera.translate((tileWidth * startTileX), (tileHeight * startTileY), 0);
        camera.update(GameTime.getInstance());

        //Gdx.app.debug("ManPage", "cameraX: " + camera.getX() + ", cameraY: " + camera.getY());

        //create new framebuffer
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, getWidth(), getHeight(), true);

        //Gdx.app.debug("FBO", "width: " + getWidth() + ", height: " + getHeight());

        //begin framebuffer, so everything will be drawn to framebuffer
        fbo.begin();

        //clear all color buffer bits and clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MapRenderer mapRenderer = new OrthogonalTiledMapRenderer(map);

        mapRenderer.setView(camera.getOriginalCamera());

        //render map to framebuffer
        mapRenderer.render();

        //dispose map renderer
        mapRenderer = null;

        //take screenshot in debug mode
        /*if (game.isDevMode()) {
            //check, if screenshot directory exists
            if (!new File(ScreenshotUtils.getScreenshotsHomeDir(game.getAppName()) + "/pages/").exists()) {
                new File(ScreenshotUtils.getScreenshotsHomeDir(game.getAppName()) + "/pages/").mkdirs();
            }

            String pagePath = ScreenshotUtils.getScreenshotsHomeDir(game.getAppName()) + "/pages/page_" + x + "_" + y + ".png";

            System.out.println("page screenshot path: " + pagePath);

            //delete file, if exists
            if (new File(pagePath).exists()) {
                new File(pagePath).delete();
            }

            try {
                ScreenshotUtils.takeScreenshot(pagePath, getWidth(), getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            };
        }*/

        //end framebuffer, so everything will no drawn to backbuffer again
        fbo.end();

        //we have to clear buffer, else it will also drawn to actual buffer instad only to framebuffer
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.texture = fbo.getColorBufferTexture();

        this.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //save screenshot
        //ScreenshotUtils.saveTexture(ScreenshotUtils.getScreenshotPath(game.getAppName()) + "/page_" + x + "_" + y + ".png", this.texture);

        //create new pixmap
        /*Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);

        //iterate through all layers
        for (MapLayer layer : map.getLayers()) {
            //check, if layer name is excluded
            if (excludedLayers.contains(layer.getName())) {
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
        }*/

        //create texture from pixmap
        //this.texture = new Texture(pixmap);

        //dispose generated pixmap
        //pixmap.dispose();

        //dispose tile texture pixmap of tilesets
        for (Texture texture : this.preparedTextures) {
            //get pixmap
            Pixmap pixmap1 = pixmapMap.get(texture);

            //remove pixmap from map
            pixmapMap.remove(pixmap1);

            //dispose pixmap
            texture.getTextureData().disposePixmap();
        }

        //clear list
        this.preparedTextures.clear();

        //set flag
        this.loaded = true;
        this.loading = false;

        Gdx.app.debug("MapPage", "map page generated finished.");
    }

    @Override
    @Deprecated
    public void generatePageAsync(ExecutorService executorService, IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY) {
        if (isPageLoading()) {
            return;
        }

        this.loading = true;
        this.asyncTask = true;

        Gdx.app.debug("MapPage", "submit page generation task to executor service.");

        //add task in another thread
        executorService.submit(() -> {
            try {
                Gdx.app.debug("MapPage", "start execute async map page generation.");

                generatePage(game, map, excludedLayers, startTileX, startTileY);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                asyncTask = false;
            }
        });
    }

    @Deprecated
    protected void renderTileLayer (TiledMapTileLayer layer, Pixmap pixmap, int startTileX, int startTileY) {
        for (int x = startTileX; x < startTileX + tilesX; x++) {
            for (int y = startTileY; y < startTileY + tilesY; y++) {
                //get cell
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell == null) {
                    continue;
                }

                Gdx.app.debug("MapPage", "get cell: " + x + ", y: " + y);

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

                Pixmap tilePixmap = null;

                //check, if texture is already prepared
                if (!tileSetTexture.getTextureData().isPrepared()) {
                    //prepare texture
                    tileSetTexture.getTextureData().prepare();

                    this.preparedTextures.add(tileSetTexture);

                    //get pixmap from tile texture
                    tilePixmap = tileSetTexture.getTextureData().consumePixmap();

                    this.pixmapMap.put(tileSetTexture, tilePixmap);
                } else {
                    tilePixmap = pixmapMap.get(tileSetTexture);
                }

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
    public void recreate(IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY) {
        Gdx.app.debug("MapPage", "recreate page, startTileX: " + startTileX + ", startTileY: " + startTileY);

        //unload page
        this.unloadPage();

        //generate page
        generatePage(game, map, excludedLayers, startTileX, startTileY);
    }

    @Override
    public float getMiddleX() {
        return this.x + (getWidth() / 2);
    }

    @Override
    public float getMiddleY() {
        return this.y + (getHeight() / 2);
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
