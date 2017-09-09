package de.leeksanddragons.engine.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import de.leeksanddragons.engine.camera.impl.Shake1CameraModification;
import de.leeksanddragons.engine.camera.impl.Shake2CameraModification;
import de.leeksanddragons.engine.camera.impl.Shake3CameraModification;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 09.09.2017.
 */
public class CameraHelper implements ModificationFinishedListener {

    //libGDX 2D orthographic camera
    protected OrthographicCamera camera = null;

    //camera position
    protected float x = 0;
    protected float y = 0;
    protected float zoom = 1;

    //camera offset
    protected float cameraOffsetX = 0;
    protected float cameraOffsetY = 0;

    //camera dimension
    protected int width = 0;
    protected int height = 0;

    //camera bounds
    protected float minX = Float.MIN_VALUE;
    protected float maxX = Float.MAX_VALUE;
    protected float minY = Float.MIN_VALUE;
    protected float maxY = Float.MAX_VALUE;

    //target camera position
    protected float targetX = 0;
    protected float targetY = 0;

    //camera mode
    protected CameraMode mode = CameraMode.DIRECT_CAMERA;

    //list with resize listeners
    protected List<ResizeListener> resizeListeners = new ArrayList<>();

    //temporary vector to avoid creation of new vector in gameloop
    protected Vector3 tmpScreenVector = new Vector3(0, 0, 0);

    //camera modifications
    protected Map<Class, CameraModification> cameraModificationMap = new ConcurrentHashMap<>();
    protected List<CameraModification> activeModifications = new ArrayList<>();

    //temporary camera params (to avoid override values)
    protected TempCameraParams tempCameraParams = null;

    /**
    * default constructor
     *
     * @param width viewport width
     * @param height viewport height
    */
    public CameraHelper (int width, int height) {
        this.width = width;
        this.height = height;

        //create new libGDX 2D orthographic camera
        this.camera = new OrthographicCamera(width, height);

        //move camera, so (0, 0) is on left bottom
        this.camera.translate(width / 2, height / 2, 0);

        //create new temp camera params
        this.tempCameraParams = new TempCameraParams(this.x, this.y, 1);

        //add some basic camera modifications
        this.registerMod(new Shake1CameraModification(), Shake1CameraModification.class);
        this.registerMod(new Shake2CameraModification(), Shake2CameraModification.class);
        this.registerMod(new Shake3CameraModification(), Shake3CameraModification.class);
    }

    public float getX () {
        return this.x;
    }

    public float getY () {
        return this.y;
    }

    public float getZoom () {
        return this.zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;

        this.syncPosToCamera();
    }

    /**
    * resize camera viewport (set new dimension)
     *
     * @param width viewport width
     * @param height viewport height
    */
    public void resize (int width, int height) {
        //set new width and height
        this.width = width;
        this.height = height;

        //call resize listeners
        this.resizeListeners.stream().forEach(consumer -> {
            consumer.onResize(width, height);
        });

    }

    public void addResizeListener(ResizeListener listener) {
        this.resizeListeners.add(listener);
    }

    public void removeResizeListener(ResizeListener listener) {
        this.resizeListeners.remove(listener);
    }

    /**
    * update camera
    */
    public void update (GameTime time) {
        //TODO: check, if camera can scroll

        //move camera to target position
        if (mode == CameraMode.DIRECT_CAMERA) {
            //set x and y position directly to target position
            this.x = this.targetX;
            this.y = this.targetY;
        } else if (mode == CameraMode.SMOOTH_CAMERA) {
            //TODO: add code here
        } else if (mode == CameraMode.FIXED_CAMERA) {
            //dont move camera
        } else if (mode == CameraMode.MOUSE_CAMERA) {
            //TODO: add code here
        } else {
            //throw exception
            throw new IllegalStateException("Unknown camera mode: " + mode.name());
        }

        //reset temporary camera position
        this.tempCameraParams.reset(getX(), getY(), getZoom());

        //update modifications first
        for (CameraModification mod : this.activeModifications) {
            mod.onUpdate(time, this.tempCameraParams, this);
        }

        //set libGDX camera dimension
        this.camera.viewportWidth = this.width;
        this.camera.viewportHeight = this.height;

        //set camera position to libGDX camera
        this.camera.position.x = this.tempCameraParams.getX() + cameraOffsetX;
        this.camera.position.y = this.tempCameraParams.getY() + cameraOffsetY;
        this.camera.zoom = this.tempCameraParams.getZoom();

        //update libGDX camera
        this.camera.update();
    }

    /**
    * set camera mode
     *
     * @param mode camera mode
    */
    public void setMode (CameraMode mode) {
        this.mode = mode;
    }

    /**
    * get target camera positon x
     *
     * @return target x position
    */
    public float getTargetX () {
        return this.targetX;
    }

    /**
    * set target camera position x
     *
     * @param targetX target camera position x
    */
    public void setTargetX (float targetX) {
        this.targetX = targetX;
    }

    /**
     * get target camera positon y
     *
     * @return target y position
     */
    public float getTargetY () {
        return this.targetY;
    }

    /**
     * set target camera position y
     *
     * @param targetY target camera position y
     */
    public void setTargetY (float targetY) {
        this.targetY = targetY;
    }

    /**
    * set target camera position
     *
     * @param targetX target x position
     * @param targetY target y position
    */
    public void setTargetPos (float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
    * reset camera bounds
    */
    public void resetBounds () {
        this.minX = Float.MIN_VALUE;
        this.maxX = Float.MAX_VALUE;
        this.minY = Float.MIN_VALUE;
        this.maxY = Float.MAX_VALUE;
    }

    /**
    * sync camera helper position to libGDX original camera
    */
    protected void syncPosToCamera() {
        /*this.camera.position.x = x + cameraOffsetX;
        this.camera.position.y = y + cameraOffsetY;
        this.camera.zoom = zoom;*/

        //set camera position to libGDX camera
        this.camera.position.x = this.tempCameraParams.getX() + cameraOffsetX;
        this.camera.position.y = this.tempCameraParams.getY() + cameraOffsetY;
        this.camera.zoom = this.tempCameraParams.getZoom();
    }

    /**
    * get combined Matrix4 for libGDX SpriteBatch
     *
     * @return combined matrix
    */
    public Matrix4 getCombined() {
        return this.camera.combined;
    }

    /**
    * get frustum
     *
     * @return frustum
    */
    public Frustum getFrustum() {
        return this.camera.frustum;
    }

    /**
    * get mouse position in camera
     *
     * @return current mouse position
    */
    public Vector3 getMousePosition() {
        this.tmpScreenVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        return camera.unproject(this.tmpScreenVector);
    }

    @Override
    public <T extends CameraModification> void onModificationFinished(T mod, Class<T> cls) {
        if (mod == null) {
            throw new NullPointerException("mod cannot be null.");
        }

        this.deactivateMod(cls);
        // this.activeModifications.remove(mod);
    }

    public <T extends CameraModification> void registerMod(T mod, Class<T> cls) {
        if (mod == null) {
            throw new NullPointerException("mod cannot be null.");
        }

        if (cls == null) {
            throw new NullPointerException("class cannot be null.");
        }

        this.cameraModificationMap.put(cls, mod);
    }

    public <T extends CameraModification> void removeMod(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("class cannot be null.");
        }

        CameraModification mod = this.cameraModificationMap.get(cls);

        if (mod != null) {
            this.activeModifications.remove(mod);
        }

        this.cameraModificationMap.remove(cls);
    }

    public <T extends CameraModification> T getMod(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("class cannot be null.");
        }

        CameraModification mod = this.cameraModificationMap.get(cls);

        if (mod == null) {
            return null;
        }

        return cls.cast(mod);
    }

    public <T extends CameraModification> void activateMod(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("class cannot be null.");
        }

        CameraModification mod = this.cameraModificationMap.get(cls);

        if (mod == null) {
            throw new IllegalStateException("camera modification is not registered: " + cls.getName());
        }

        if (!this.activeModifications.contains(mod)) {
            this.activeModifications.add(mod);
        }
    }

    public <T extends CameraModification> void deactivateMod(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException("class cannot be null.");
        }

        CameraModification mod = this.cameraModificationMap.get(cls);

        if (mod == null) {
            throw new IllegalStateException("camera modification is not registered: " + cls.getName());
        }

        if (this.activeModifications.contains(mod)) {
            this.activeModifications.remove(mod);
        }
    }

    public int countActiveMods() {
        return this.activeModifications.size();
    }

}
