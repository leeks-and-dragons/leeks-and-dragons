package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.map.IRegion;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 17.09.2017.
 */
public abstract class BaseRegion implements IRegion {

    //position
    protected float x = 0;
    protected float y = 0;

    //width & height of region in tiles
    protected float widthInTiles = 30;
    protected float heightInTiles = 30;

    //tile size in pixels
    protected float tileWidth = 32;
    protected float tileHeight = 32;

    //flag, if region contains water
    protected boolean hasWater = true;

    @Override
    public boolean hasWater() {
        return this.hasWater;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getWidth() {
        return this.widthInTiles * this.tileWidth;
    }

    @Override
    public float getHeight() {
        return this.heightInTiles * this.tileHeight;
    }

}
