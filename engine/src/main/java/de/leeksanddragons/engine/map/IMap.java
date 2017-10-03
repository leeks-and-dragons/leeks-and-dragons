package de.leeksanddragons.engine.map;

import com.badlogic.gdx.audio.Sound;
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

    public float getWidth ();

    public float getHeight ();

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
    * check, if map has an global music path
     *
     * @return true, if map has an music path set in map properties
    */
    public boolean hasGlobalMusicPath ();

    /**
    * get global music path for map from map properties
     *
     * @return path to music file
    */
    public String getGlobalMusicPath ();

    /**
    * get footstep sound path by player position
     *
     * @param playerX x position of player
     * @param playerY y position of player
     *
     * @return path to footstep sound
    */
    public String getFootstepSoundPath (float playerX, float playerY);

    /**
    * get footstep sound by player position
     *
     * @param playerX x position of player
     * @param playerY y position of player
     *
     * @return instance of sound or null, if tile doesnt have an footstep sound effect
    */
    public Sound getFootstepSound (float playerX, float playerY);

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
