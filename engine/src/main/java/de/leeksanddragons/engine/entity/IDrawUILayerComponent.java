package de.leeksanddragons.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.entity.priority.ECSDrawPriority;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 12.02.2017.
 */
public interface IDrawUILayerComponent extends IComponent {

    /**
    * draw UI layer
     *
     * @param game instance of game
     * @param time game time
     * @param batch sprite batch to draw
    */
    public void drawUILayer(IScreenGame game, GameTime time, SpriteBatch batch);

    /**
    * get draw priority
     *
     * @return draw priority
    */
    public ECSDrawPriority getUILayerDrawOrder();

}
