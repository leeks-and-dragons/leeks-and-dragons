package de.leeksanddragons.engine.controller.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.controllers.mappings.Ouya;
import de.leeksanddragons.engine.controller.ControllerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 20.09.2017.
 */
public class DefaultControllerManager implements ControllerManager {

    //list with all connected controllers
    protected List<Controller> connectedControllers = new ArrayList<>();

    public DefaultControllerManager () {
        //detect connected controllers
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Controller detected", controller.getName());

            //add controller to list
            this.connectedControllers.add(controller);
        }

        Gdx.app.log("Controller Manager", "" + connectedControllers.size() + " connected controllers found.");

        //add listeners to receive events, if controllers connect or disconnect
        Controllers.addListener(new ControllerAdapter() {
            @Override
            public void connected(Controller controller) {
                Gdx.app.log("Controller connected: ", controller.getName());

                //add controller, if controller doesnt exists in connected controllers list
                if (!connectedControllers.contains(controller)) {
                    connectedControllers.add(controller);
                }
            }

            @Override
            public void disconnected(Controller controller) {
                Gdx.app.log("Controller disconnected", controller.getName());

                //remove controller from connected controllers list
                connectedControllers.remove(controller);
            }
        });
    }

    @Override
    public boolean isConnected() {
        return !this.connectedControllers.isEmpty();
    }

    @Override
    public boolean isXBoxControllerConnected() {
        for (Controller controller : connectedControllers) {
            if (controller.getName().contains("XBox")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isOuyaControllerConnected() {
        for (Controller controller : connectedControllers) {
            if (controller.getName().contains(Ouya.ID)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Controller> listConnectedControllers() {
        return null;
    }

}
