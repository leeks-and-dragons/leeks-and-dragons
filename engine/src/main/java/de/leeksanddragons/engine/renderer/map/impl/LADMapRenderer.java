package de.leeksanddragons.engine.renderer.map.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.map.IMapRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.ColliderUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Justin on 21.09.2017.
 */
public class LADMapRenderer implements IMapRenderer {

    //map position
    protected float mapX = 0;
    protected float mapY = 0;

    //size of pages, 10x10 tiles
    protected int pageTilesWidth = 20;
    protected int pageTilesHeight = 20;

    //size of tile
    protected int tileWidth = 32;
    protected int tileHeight = 32;

    //tiled map
    protected TiledMap map = null;

    protected MapPage[][] pages = null;

    protected int pagesX = 0;
    protected int pagesY = 0;

    protected List<String> excludedLayers = new ArrayList<>();

    protected IScreenGame game = null;

    public LADMapRenderer(IScreenGame game, TiledMap map, float mapX, float mapY) {
        this.game = game;

        this.map = map;

        this.mapX = mapX;
        this.mapY = mapY;

        //get properties
        MapProperties props = map.getProperties();
        int width = props.get("width", Integer.class);//get width in tiles
        int height = props.get("height", Integer.class);//get height in tiles

        this.tileWidth = props.get("tilewidth", Integer.class);
        this.tileHeight = props.get("tileheight", Integer.class);

        //calculate width and height of map in pixels
        float widthInPixels = width * tileWidth;
        float heightInPixels = height * tileHeight;

        //calculate required pages count
        this.pagesX = (int) (widthInPixels / (pageTilesWidth * tileWidth)) + 1;
        this.pagesY = (int) (heightInPixels / (pageTilesHeight * tileHeight)) + 1;

        System.out.println("required pages X: " + pagesX + ", required pages Y: " + pagesY);

        //System.out.println("pagesX: " + pagesX + ", pagesY: " + pagesY);

        this.pages = new MapPage[pagesX][pagesY];
    }

    @Override
    public void setPosition(float x, float y) {
        this.mapX = x;
        this.mapY = y;
    }

    @Override
    public void unload() {
        //iterate through all pages
        for (int x = 0; x < pages.length; x++) {
            for (int y = 0; y < pages[0].length; y++) {
                //dispose page
                MapPage page = pages[x][y];

                if (page != null) {
                    //dispose page
                    page.dispose();

                    pages[x][y] = null;
                }
            }
        }
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
        boolean val = ColliderUtils.overlaping(this.mapX, this.mapX + getWidth(), cameraX1, cameraX2) && ColliderUtils.overlaping(this.mapY, this.mapY + getHeight(), cameraY1, cameraY2);

        return val;
    }

    @Override
    public int getWidth() {
        return this.pageTilesWidth * tileWidth;
    }

    @Override
    public int getHeight() {
        return this.pageTilesHeight * tileHeight;
    }

    @Override
    public void addExcludedLayer(String layerName) {
        this.excludedLayers.add(layerName);

        //recreate pages, if neccessary
        this.recreateAllPages(this.game);
    }

    @Override
    public void removeExcludedLayer(String layerName) {
        this.excludedLayers.remove(layerName);

        //recreate pages, if neccessary
        this.recreateAllPages(this.game);
    }

    @Override
    public void dispose() {
        //unload all pages
        this.unload();

        this.excludedLayers.clear();

        this.map = null;
    }

    protected void recreateAllPages (IScreenGame game) {
        for (int x = 0; x < pages.length; x++) {
            for (int y = 0; y < pages[0].length; y++) {
                //dispose page
                MapPage page = pages[x][y];

                if (page != null) {
                    //check, if page was loaded
                    if (page.isPageLoaded()) {
                        //recreate page
                        page.recreate(game, this.map, this.excludedLayers, x * pageTilesWidth, y * pageTilesHeight);
                    }
                }
            }
        }
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //TODO: remove old pages with distance > maxDistance and load near pages
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //first check, if map is visible
        if (!isVisible(game.getCameraManager().getMainCamera())) {
            return;
        }

        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //calculate relative position in map
        float x2 = camera.getX() - this.mapX;
        float y2 = camera.getY() - this.mapY;

        System.out.println("x2: " + x2 + ", y2: " + y2);

        int requiredTilesToRenderX = (int) ((float) camera.getViewportWidth() / (float) (pageTilesWidth * tileWidth)) + 1;
        int requiredTilesToRenderY = (int) ((float) camera.getViewportHeight() / (float) (pageTilesHeight * tileHeight)) + 2;

        //calculate start page
        int startPageX = (int) (x2 / (float) (pageTilesWidth * tileWidth));
        int startPageY = (int) (y2 / (float) (pageTilesHeight * tileHeight));

        Gdx.app.debug("LADMapRenderer", "startPageX: " + startPageX + ", startPageY: " + startPageY + ", required width: " + requiredTilesToRenderX + ", required height: " + requiredTilesToRenderY);

        //draw pages
        for (int x = startPageX; x < startPageX + requiredTilesToRenderX; x++) {
            for (int y = startPageY; y < startPageY + requiredTilesToRenderY; y++) {
                //check, if page exists
                if (x >= this.pagesX) {
                    continue;
                }

                if (y >= this.pagesY) {
                    continue;
                }

                //get page
                MapPage page = getPage(x, y);

                //check, if page exists
                if (page == null) {
                    //create new page
                    MapPage newPage = new MapPage(this.mapX + (x * getPageWidthInPixels()), this.mapY + (y * getPageHeightInPixels()), pageTilesWidth, pageTilesHeight);

                    //add page to array
                    this.pages[x][y] = newPage;

                    //load page asynchronous
                    newPage.generatePage(game, this.map, this.excludedLayers, x * pageTilesWidth, y* pageTilesHeight);
                } else {
                    //check, if page is loaded
                    if (!page.isPageLoaded()) {
                        //check, if page isnt loading
                        if (!page.isPageLoading()) {
                            //load page asynchronous
                            page.generatePage(game, this.map, this.excludedLayers, x * pageTilesWidth, y* pageTilesHeight);
                        }
                    } else {
                        //render page
                        page.draw(game, time, batch);
                    }
                }
            }
        }
    }

    protected int getPageWidthInPixels () {
        return this.pageTilesWidth * tileWidth;
    }

    protected int getPageHeightInPixels () {
        return this.pageTilesWidth * tileWidth;
    }

    protected MapPage getPage (int x, int y) {
        if (x < 0 || x >= pages.length) {
            return null;
        }

        if (y < 0 || y >= this.pagesY) {
            return null;
        }

        return this.pages[x][y];
    }

}
