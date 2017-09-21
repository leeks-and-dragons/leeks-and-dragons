package de.leeksanddragons.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.leeksanddragons.engine.camera.CameraHelper;

/**
 * Created by Justin on 12.02.2017.
 */
public class MouseUtils {

    protected static Vector3 tmpVector = new Vector3(0, 0, 0);
    protected static Vector2 tmpVector2 = new Vector2(0, 0);

    public static float getMouseX(float x, CameraHelper camera) {
        return (x + camera.getX()) * 1
                / camera.getZoom();/* - (viewportWidth / 2) */
    }

    public static float getMouseY(float y, CameraHelper camera) {
        // y = camera.getOriginalCamera().viewportHeight - y;
        return (y + camera.getY()) * 1 / camera.getZoom();
    }

    @Deprecated
    public static Vector3 getMousePositionWithCamera(Camera camera) {
        tmpVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        return camera.unproject(tmpVector);
    }

    public Vector3 getMousePositionWithCamera(CameraHelper camera) {
        return camera.getMousePosition();
    }

    public static float getRelativeMouseAngleInRadians(CameraHelper camera, float entityX, float entityY) {
        return (float) Math.toRadians(getRelativeMouseAngle(camera, entityX, entityY));
    }

    public static float getRelativeMouseAngle(CameraHelper camera, float entityX, float entityY) {
        // get mouse position relative to entity
        Vector2 relPos = getRelativePositionToEntity(camera, entityX, entityY);

        // calculate mouse angle relative to entity
        double angleRadians = (float) Math.atan2(relPos.y, relPos.x);
        float angle = (float) Math.toDegrees(angleRadians);

        while (angle > 360) {
            angle -= 360;
        }

        while (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public static Vector2 getRelativePositionToEntity(CameraHelper camera, float entityX, float entityY) {
        // get mouse position relative to camera
        Vector3 mousePos = camera.getMousePosition();

        // calculate mouse position relative to entity
        float relX = mousePos.x - entityX;
        float relY = mousePos.y - entityY;

        tmpVector2.set(relX, relY);

        return tmpVector2;
    }

    /*public static float getCorrectWindowY () {
        //first, check if fullscreen mode
        if (Gdx.graphics.isFullscreen()) {
            return Gdx.input.getY();
        } else {
            //TODO: check, if it is an decorated window

            return Gdx.input.getY() + 31;
        }
    }*/

    public static float correctDecoredMousePositionY (float y) {
        float a = (((float) Gdx.graphics.getHeight() - 31f) / (float) Gdx.graphics.getHeight());
        return y * a;
    }

}
