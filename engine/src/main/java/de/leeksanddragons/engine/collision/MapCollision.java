package de.leeksanddragons.engine.collision;

/**
 * Created by Justin on 04.10.2017.
 */
public interface MapCollision {

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

}
