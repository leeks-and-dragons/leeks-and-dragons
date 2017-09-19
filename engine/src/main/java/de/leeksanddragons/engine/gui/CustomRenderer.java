package de.leeksanddragons.engine.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * An additional renderer to over draw widgets
 *
 * Created by Justin on 19.09.2017.
 */
public interface CustomRenderer<T extends HUDWidget> {

    public void update(IScreenGame game, T widget, GameTime time);

    public void drawLayer0(GameTime time, T widget, SpriteBatch batch);

}
