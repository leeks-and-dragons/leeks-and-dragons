package de.leeksanddragons.engine.controller.impl;

import de.leeksanddragons.engine.controller.ControllerManager;
import de.leeksanddragons.engine.controller.IController;

import java.util.List;

/**
 * Created by Justin on 20.09.2017.
 */
public class DefaultControllerManager implements ControllerManager {

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isXBoxControllerConnected() {
        return false;
    }

    @Override
    public boolean isOuyaControllerConnected() {
        return false;
    }

    @Override
    public List<IController> listConnectedControllers() {
        return null;
    }

}
