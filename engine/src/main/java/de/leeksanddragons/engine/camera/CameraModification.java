package de.leeksanddragons.engine.camera;

import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 11.02.2017.
 */
public interface CameraModification {

    public void onUpdate(GameTime time, TempCameraParams position, ModificationFinishedListener listener);

    public void dispose();

}
