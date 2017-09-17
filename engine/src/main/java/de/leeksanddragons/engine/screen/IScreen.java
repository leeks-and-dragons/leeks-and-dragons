package de.leeksanddragons.engine.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.game.IGame;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 06.02.2017.
 */
public interface IScreen {

    /**
     * initialize game screen
     */
    public void init(IScreenGame game, GameAssetManager assetManager);

    /**
     * update game screen
     */
    public void update(IScreenGame game, GameTime time);

    /**
     * draw game screen
     */
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch);

    /**
    * set flag, if screen can handle input
     *
     * @param touchable flag, if screen can handle input or another screen is handling input
    */
    public void setTouchable (boolean touchable);

    /**
    * check, if screen is touchable and can process input
     *
     * @return true, if screen can process input
    */
    public boolean isTouchable ();

    /**
     * pause screen and switch to another screen
     */
    public void onPause(IScreenGame game);

    /**
     * screen was pushed, so we have to resume this game screen
     */
    public void onResume(IScreenGame game);

    /**
     * destroy game screen
     */
    public void dispose();

}
