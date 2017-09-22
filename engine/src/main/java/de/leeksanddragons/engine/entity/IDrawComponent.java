package de.leeksanddragons.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.entity.priority.ECSDrawPriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 10.02.2017.
 */
public interface IDrawComponent extends IComponent {

    /**
    * draw component
     *
     * @param game instance of game
    */
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch);

    /**
    * get draw order
     *
     * @return draw order
    */
    public ECSDrawPriority getDrawOrder();

}
