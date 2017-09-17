package de.leeksanddragons.engine.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.IOException;

/**
 * Created by Justin on 17.09.2017.
 */
public interface IRegion {

    /**
    * load region file
     *
     * @param path path to region file
    */
    public void load (String path) throws IOException;

    /**
    * update maps of region
    */
    public void update (IScreenGame game, GameTime time);

    /**
    * draw maps of region
    */
    public void draw (IScreenGame game, GameTime time, SpriteBatch batch);

    /**
     * check, if region requires water rendering
     *
     * @return true, if water has to be drawn
     */
    public boolean hasWater ();

    /**
    * set region
     *
     * @param x x coordinate of region
     * @param y y coordinate of region
    */
    public void setPosition (float x, float y);

    /**
    * get x position of region
     *
     * @return x position of region
    */
    public float getX ();

    /**
     * get y position of region
     *
     * @return y position of region
     */
    public float getY ();

    /**
    * get complete width of region in pixels
     *
     * @return width of region in pixels
    */
    public float getWidth ();

    /**
     * get complete height of region in pixels
     *
     * @return height of region in pixels
     */
    public float getHeight ();

    /**
    * pre-load region, if possible
    */
    public void preload ();

    /**
    * check, if region has finished pre-loading
     *
     * @return true, if region has finished pre-loading
    */
    public boolean hasPreLoadingFinished ();

}
