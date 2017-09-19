package de.leeksanddragons.engine.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;
import javafx.scene.shape.Rectangle;

/**
 * Interface for map
 *
 * Created by Justin on 17.09.2017.
 */
public interface IMap {

    /**
    * get x position of map
     *
     * @return x position (in pixels) of map
    */
    public float getX ();

    /**
     * get y position of map
     *
     * @return y position (in pixels) of map
     */
    public float getY ();

    /**
    * get path to tmx file
     *
     * @return path to tmx file
    */
    public String getMapPath ();

    /**
    * load map (if player is near map)
    */
    public void loadMap ();

    /**
     * check, if map is already in loading process
     *
     * @return true, if map is already in loading process
     */
    public boolean isLoading ();

    /**
    * check, if map is already loaded
     *
     * @return true, if map is already loaded
    */
    public boolean isLoaded ();

    /**
    * get bounding box
     *
     * @return bounding box
    */
    public BoundingBox getBoundingBox ();

    /**
     * update map
     */
    public void update (IScreenGame game, GameTime time);

    /**
     * draw map
     */
    public void draw (IScreenGame game, GameTime time, SpriteBatch batch);

    /**
    * dispose map
    */
    public void dispose ();

}