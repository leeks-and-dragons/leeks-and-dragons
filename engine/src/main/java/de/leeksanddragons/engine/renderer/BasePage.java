package de.leeksanddragons.engine.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 15.09.2017.
 */
public abstract class BasePage implements IPage {

    //width & height
    protected int width = 0;
    protected int height = 0;

    /**
    * default constructor
     *
     * @param width page width
     * @param height page height
    */
    public BasePage (int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

}
