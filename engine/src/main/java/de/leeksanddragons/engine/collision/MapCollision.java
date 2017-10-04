package de.leeksanddragons.engine.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Justin on 04.10.2017.
 */
public interface MapCollision extends Disposable {

    /**
    * check, if player can pass / move to this tile
     *
     * @param tileX x index of map tile
     * @param tileY y index of map tile
     *
     * @return true, if player can pass / move to this tile
    */
    public boolean canPassTile (int tileX, int tileY);

    /**
    * check, if player can move to this position
     *
     * @param xPos x position
     * @param yPos y position
     *
     * @return true, if player can move to this position
    */
    public boolean canPass (float xPos, float yPos);

    /**
    * check, if map collisions are loaded
     *
     * @return true, if map collisions are loaded
    */
    public boolean isLoaded ();

    /**
    * draw debug overlay, so all unpassed tiles are red and all moveable tiles are green
    */
    public void drawDebugOverlay (SpriteBatch batch, Color passColor, Color collideColor);

}
