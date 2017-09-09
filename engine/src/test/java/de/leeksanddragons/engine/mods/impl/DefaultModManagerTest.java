package de.leeksanddragons.engine.mods.impl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import de.leeksanddragons.engine.exception.InvalideModJSONException;
import de.leeksanddragons.engine.mods.ModManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Justin on 09.09.2017.
 */
public class DefaultModManagerTest {

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
    public void testLoadInvalideMod () throws IOException {
        //create new instance of mod manager
        ModManager modManager = new DefaultModManager();

        //try to load invalide mod
        boolean res = modManager.loadMod("../junit-tests/mods/invalide-mod");
        assertEquals(false, res);
    }

    @Test
    public void testLoadValideMod () throws IOException {
        //create new instance of mod manager
        ModManager modManager = new DefaultModManager();

        //try to load valide mod
        boolean res = modManager.loadMod("../junit-tests/mods/mod1");
        assertEquals(true, res);
    }

    @Test
    public void testLoadMods () {
        //create new instance of mod manager
        ModManager modManager = new DefaultModManager();

        //load mods
        modManager.loadMods("../junit-tests/mods-2");

        assertEquals(2, modManager.countLoadedMods());
        assertEquals("mod1 wasnt detected.", true, modManager.listLoadedModNames().contains("mod1"));
        assertEquals(false, modManager.listLoadedModNames().contains("mod2.deactivated"));
        assertEquals(true, modManager.listLoadedModNames().contains("mod3"));
    }

}
