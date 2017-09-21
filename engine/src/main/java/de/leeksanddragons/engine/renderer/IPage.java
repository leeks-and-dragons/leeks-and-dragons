package de.leeksanddragons.engine.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 15.09.2017.
 */
public interface IPage {

    public int getWidth ();

    public int getHeight ();

    public void draw (IScreenGame game, GameTime time, SpriteBatch batch, float x, float y);

    public void dispose ();

}
