package de.leeksanddragons.engine.gui.widgets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.gui.BaseHUDWidget;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 20.09.2017.
 */
public class LabelWidget extends BaseHUDWidget<LabelWidget> {

    //font
    protected BitmapFont font = null;

    //text
    protected String text = "";

    /**
    * default constructor
     *
     * @param font font
     * @param text text to draw
    */
    public LabelWidget (BitmapFont font, String text) {
        if (font == null) {
            throw new NullPointerException("font cannot be null.");
        }

        this.font = font;
        this.text = text;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void drawLayer0(GameTime time, SpriteBatch batch) {

    }
}
