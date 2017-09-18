package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.IOException;

/**
 * Created by Justin on 17.09.2017.
 */
public class DummyRegion extends BaseRegion {

    public DummyRegion () {
        //
    }

    @Override
    public void load(IScreenGame game, String path) throws IOException {
        //
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {

    }

    @Override
    public void preloadMaps(float currentX, float currentY) {

    }

    @Override
    public boolean hasPreLoadingFinished() {
        return true;
    }

}
