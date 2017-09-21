package de.leeksanddragons.engine.renderer.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.IPage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Justin on 21.09.2017.
 */
public interface IMapPage extends IPage {

    /**
    * set size of a tile
     *
     * @param tileWidth width of a tile in pixels
     * @param tileHeight height of a tile in pixels
    */
    public void setTileSize (int tileWidth, int tileHeight);

    public void setPageSizeInTiles (int tilesX, int tilesY);

    public void generatePage (IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY);

    @Deprecated
    public void generatePageAsync (ExecutorService executorService, IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY);

    /**
    * draw page on given position
     *
     * @param game instance of game
     * @param time game time
     * @param batch instance of sprite batch
    */
    public void draw (IScreenGame game, GameTime time, SpriteBatch batch);

    public boolean isPageLoading ();

    public boolean isPageLoaded ();

    public void unloadPage ();

    public void recreate (IScreenGame game, TiledMap map, List<String> excludedLayers, int startTileX, int startTileY);

    public float getMiddleX ();

    public float getMiddleY ();

    /**
    * check, if page is visible
    */
    public boolean isVisible (CameraHelper camera);

}
