package de.leeksanddragons.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.leeksanddragons.engine.input.InputMapper;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 25.09.2017.
 */
public class DefaultInputMapper implements InputMapper {

    //instance of game
    protected IScreenGame game = null;

    /**
    * default constructor
     *
     * @param game instance of game
    */
    public DefaultInputMapper (IScreenGame game) {
        this.game = game;
    }

    public boolean isMouseMoving () {
        return Gdx.input.isKeyPressed(Input.Keys.M) || this.game.getControllerManager().getRightAxisDirection().len() != 0;
    }

    public boolean isMovingDown () {
        return Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN);
    }

    public boolean isMovingUp () {
        return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP);
    }

    public boolean isMovingLeft () {
        return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT);
    }

    public boolean isMovingRight () {
        return Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT);
    }

}
