package de.leeksanddragons.engine.screen.impl;

import com.badlogic.gdx.assets.AssetManager;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 06.02.2017.
 */
public abstract class BaseScreen implements IScreen {

    protected IScreenGame game;
    protected GameAssetManager assetManager;

    protected boolean touchable = true;

    @Override
    public final void init(IScreenGame game, GameAssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;

        this.onInit(game, assetManager);
    }

    protected abstract void onInit(IScreenGame game, GameAssetManager assetManager);

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    @Override
    public boolean isTouchable() {
        return this.touchable;
    }

}
