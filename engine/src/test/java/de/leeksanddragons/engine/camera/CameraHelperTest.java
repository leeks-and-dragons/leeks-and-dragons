package de.leeksanddragons.engine.camera;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
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
        //create new camera
        CameraHelper camera = new CameraHelper(800, 600);

        //test viewvport dimension
        assertEquals(800, camera.getViewportWidth());
        assertEquals(600, camera.getViewportHeight());
    }

}
