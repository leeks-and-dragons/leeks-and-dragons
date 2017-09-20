package de.leeksanddragons.engine.controller.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector3;
import de.leeksanddragons.engine.controller.ControllerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 20.09.2017.
 */
public class DefaultControllerManager implements ControllerManager {

    //list with all connected controllers
    protected List<Controller> connectedControllers = new ArrayList<>();

    public enum CONTROLLER_TYPE {
        XBOX, OUYA, PS4, UNKNOWN
    }

    //active controller (game only supports one active controller)
    protected Controller activeController = null;
    protected CONTROLLER_TYPE activeControllerType = null;

    public DefaultControllerManager () {
        //detect connected controllers
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Controller detected", controller.getName());

            //add listener for debug options
            controller.addListener(new ControllerAdapter() {
                @Override
                public boolean buttonDown(Controller controller, int buttonCode) {
                    Gdx.app.log("Controller", "button down: " + buttonCode);

                    return false;
                }

                @Override
                public boolean buttonUp(Controller controller, int buttonCode) {
                    return false;
                }

                @Override
                public boolean axisMoved(Controller controller, int axisCode, float value) {
                    Gdx.app.debug("Controller", "axis moved: " + axisCode + ", value: " + value);

                    return false;
                }

                @Override
                public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                    return false;
                }

                @Override
                public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
                    return false;
                }

                @Override
                public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
                    return false;
                }
            });

            //add controller to list
            this.connectedControllers.add(controller);
        }

        Gdx.app.log("Controller Manager", "" + connectedControllers.size() + " connected controllers found.");

        //choose active controller
        this.chooseControllerIfNeccessary();

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

    protected void chooseControllerIfNeccessary () {
        if (this.activeController == null) {
            if (this.connectedControllers.size() > 0) {
                //set first controller in list active
                this.activeController = connectedControllers.get(0);

                //get controller type
                if (activeController.getName().equals(Ouya.ID)) {
                    this.activeControllerType = CONTROLLER_TYPE.OUYA;
                } else if (activeController.getName().contains("XBox")) {
                    this.activeControllerType = CONTROLLER_TYPE.XBOX;
                } else if (activeController.getName().contains("PS") || activeController.getName().contains("Play")) {
                    this.activeControllerType = CONTROLLER_TYPE.PS4;
                } else {
                    this.activeControllerType = CONTROLLER_TYPE.UNKNOWN;
                }

                Gdx.app.log("Controller Manager", "choose active controller: " + this.activeController.getName() + ", type: " + activeControllerType.name());
            }

            Gdx.app.debug("Controller Manager", "Couldn't choose active controller, because no controller is detected.");
        }
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
        return this.connectedControllers;
    }

    @Override
    public float getRightXAxis() {
        if (!isConnected()) {
            return 0;
        }

        switch (this.activeControllerType) {
            case XBOX:
                return activeController.getAxis(Xbox.R_STICK_HORIZONTAL_AXIS);
            case OUYA:
                return activeController.getAxis(Ouya.AXIS_RIGHT_X);
            case PS4:
                throw new UnsupportedOperationException("PS4 controller isnt supported yet. Please contact developers.");
            default:
                throw new UnsupportedOperationException("Controller isnt supported: " + activeController.getName() + ", type: " + activeControllerType.name());
        }
    }

    @Override
    public float getRightYAxis() {
        if (!isConnected()) {
            return 0;
        }

        switch (this.activeControllerType) {
            case XBOX:
                return activeController.getAxis(Xbox.R_STICK_VERTICAL_AXIS);
            case OUYA:
                return activeController.getAxis(Ouya.AXIS_RIGHT_Y);
            case PS4:
                throw new UnsupportedOperationException("PS4 controller isnt supported yet. Please contact developers.");
            default:
                throw new UnsupportedOperationException("Controller isnt supported: " + activeController.getName() + ", type: " + activeControllerType.name());
        }
    }

}
