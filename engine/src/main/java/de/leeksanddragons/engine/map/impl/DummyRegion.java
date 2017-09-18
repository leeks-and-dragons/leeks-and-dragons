package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.map.IMap;
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
    public IMap getCurrentMap(CameraHelper camera) {
        return null;
    }

    @Override
    public boolean hasPreLoadingFinished(CameraHelper camera) {
        return true;
    }

}
