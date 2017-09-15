package de.leeksanddragons.engine.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 14.09.2017.
 */
public interface IRenderer {

    /**
     * update renderer
     */
    public void update(IScreenGame game, GameTime time);

    /**
     * draw renderer
     */
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch);

}
