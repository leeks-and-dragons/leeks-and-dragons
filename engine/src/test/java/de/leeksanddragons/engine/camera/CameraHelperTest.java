package de.leeksanddragons.engine.camera;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import de.leeksanddragons.engine.utils.GameTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 10.09.2017.
 */
public class CameraHelperTest {

    //test application
    private static Application application;

    //initialize application with the headless backend before running any tests
    @BeforeClass
    public static void init() {
        System.out.println("init app.");

        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    //clean up application after executing tests
    @AfterClass
    public static void cleanUp() {
        // Exit the application first
        application.exit();
        application = null;
    }

    @Test
    public void testConstructor () {
        int width = 800;
        int height = 600;

        //create new camera
        CameraHelper camera = new CameraHelper(width, height);

        //test viewvport dimension
        assertEquals(width, camera.getViewportWidth());
        assertEquals(height, camera.getViewportHeight());

        //test position
        assertEquals(0, camera.getX(), 0);
        assertEquals(0, camera.getY(), 0);

        //test zoom
        assertEquals(1, camera.getZoom(), 0);

        //test mode
        assertEquals(CameraMode.DIRECT_CAMERA, camera.getMode());

        //test target position
        assertEquals(0, camera.getTargetX(), 0);
        assertEquals(0, camera.getTargetY(), 0);

        //test offset
        assertEquals(width / 2, camera.getOffsetX(), 0);
        assertEquals(height / 2, camera.getOffsetY(), 0);

        //test original camera position
        assertEquals(width / 2, camera.getOriginalCamera().position.x, 0);
        assertEquals(height / 2, camera.getOriginalCamera().position.y, 0);
    }

    @Test
    public void testResize () {
        int width = 800;
        int height = 600;

        //create new camera
        CameraHelper camera = new CameraHelper(width, height);

        int newWidth = 1280;
        int newHeight = 912;

        //resize camera
        camera.resize(newWidth, newHeight);

        //test dimension
        assertEquals(newWidth, camera.getViewportWidth());
        assertEquals(newHeight, camera.getViewportHeight());

        //update camera
        camera.update(GameTime.getInstance());

        //test dimension
        assertEquals(newWidth, camera.getViewportWidth());
        assertEquals(newHeight, camera.getViewportHeight());
    }

    @Test
    public void testSetTargetPosition () {
        int width = 800;
        int height = 600;

        //create new camera
        CameraHelper camera = new CameraHelper(width, height);

        //test position
        assertEquals(0, camera.getX(), 0);
        assertEquals(0, camera.getY(), 0);

        //set target position
        camera.setTargetPos(10, 20);

        //update camera
        camera.update(GameTime.getInstance());

        //test position
        assertEquals(10, camera.getX(), 0);
        assertEquals(20, camera.getY(), 0);

        //test original camera position
        assertEquals((width / 2 + 10), camera.getOriginalCamera().position.x, 0);
        assertEquals((height / 2 + 20), camera.getOriginalCamera().position.y, 0);

    }

}
