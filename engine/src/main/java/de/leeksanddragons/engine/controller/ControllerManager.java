package de.leeksanddragons.engine.controller;

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
    public List<IController> listConnectedControllers ();

}
