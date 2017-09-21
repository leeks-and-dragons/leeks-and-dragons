package de.leeksanddragons.engine.gui.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.gui.BaseHUDWidget;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 09.02.2017.
 */
public class ImageWidget extends BaseHUDWidget<ImageWidget> {

    protected Texture texture = null;

    public ImageWidget(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //
    }

    @Override
    public void drawLayer0(IScreenGame game, GameTime time, SpriteBatch batch) {
        batch.draw(this.texture, getX(), getY());
    }
}
