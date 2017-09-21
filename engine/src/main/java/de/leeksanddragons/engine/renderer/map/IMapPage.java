package de.leeksanddragons.engine.renderer.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.IPage;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

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
    public void setTileSize (float tileWidth, float tileHeight);

    public void generatePage (TiledMap map, int startTileX, int startTileY);

    /**
    * draw page on given position
     *
     * @param game instance of game
     * @param time game time
     * @param batch instance of sprite batch
    */
    public void draw (IScreenGame game, GameTime time, SpriteBatch batch);

    public boolean isPageLoaded ();

    public void unloadPage ();

    public void recreate (TiledMap map, int startTileX, int startTileY);

    /**
    * exclude layer from rendering
     *
     * @param layerName name of layer to exclude
    */
    public void addExcludedLayer (String layerName);

    /**
     * remove excluded layer from rendering, so layer will be rendered now
     *
     * @param layerName name of layer to exclude
     */
    public void removeExcludedLayer (String layerName);

    /**
    * check, if page is visible
    */
    public boolean isVisible (CameraHelper camera);

}
