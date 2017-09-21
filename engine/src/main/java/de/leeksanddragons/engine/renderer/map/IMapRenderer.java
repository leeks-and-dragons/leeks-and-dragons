package de.leeksanddragons.engine.renderer.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.renderer.IRenderer;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Interface to draw tiled maps
 *
 * Created by Justin on 21.09.2017.
 */
public interface IMapRenderer extends IRenderer {

    /**
    * set position in game world
     *
     * @param x x position
     * @param y y position
    */
    public void setPosition (float x, float y);

    /**
    * unload map renderer
    */
    public void unload ();

    /**
    * check, if map is visible
     *
     * @param camera instance of game camera
     *
     * @return true, if map is visible
    */
    public boolean isVisible (CameraHelper camera);

    public int getWidth ();

    public int getHeight ();

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

    public void dispose ();

}
