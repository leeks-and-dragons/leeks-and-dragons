package de.leeksanddragons.engine.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.List;

/**
 * Created by Justin on 20.09.2017.
 */
public interface ControllerManager {

    /**
    * check, if one or more controllers are connected
     *
     * @return true, if one or more controllers are connected
    */
    public boolean isConnected ();

    /**
    * check, if an XBox controller is connected
     *
     * @return true, if an XBox controller is connected
    */
    public boolean isXBoxControllerConnected ();

    /**
     * check, if an Ouya controller is connected
     *
     * @return true, if an XBox controller is connected
     */
    public boolean isOuyaControllerConnected ();

    /**
    * list all connected controllers
     *
     * @return list with all connected controllers
    */
    public List<Controller> listConnectedControllers ();

    /**
    * get value of right x axis between -1 and 1
     *
     * @return value of x axis
    */
    public float getRightXAxis ();

    /**
     * get value of right y axis between -1 and 1
     *
     * @return value of y axis
     */
    public float getRightYAxis ();

    /**
    * get direction of right axis
    */
    public Vector2 getRightAxisDirection ();

}
