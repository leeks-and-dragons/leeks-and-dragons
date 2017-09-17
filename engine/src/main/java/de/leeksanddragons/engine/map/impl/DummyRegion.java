package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 17.09.2017.
 */
public class DummyRegion extends BaseRegion {

    public DummyRegion () {
        this.widthInTiles = 30;
        this.heightInTiles = 30;
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {

    }

}
