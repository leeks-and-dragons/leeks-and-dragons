package de.leeksanddragons.engine.screen.impl;

import com.badlogic.gdx.Gdx;
import de.leeksanddragons.engine.exception.ScreenNotFoundException;
import de.leeksanddragons.engine.screen.IScreen;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.ScreenManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Justin on 06.02.2017.
 */
public class DefaultScreenManager implements ScreenManager<IScreen> {

    /**
     * map with all initialized screens
     */
    protected Map<String, IScreen> screens = new ConcurrentHashMap<>();

    /**
     * list with all active screens
     */
    protected Deque<IScreen> activeScreens = new ConcurrentLinkedDeque<>();

    /**
     * only for performance improvements!
     *
     * caching list
     */
    protected List<IScreen> cachedScreenList = new ArrayList<>();

    protected final IScreenGame game;

    public DefaultScreenManager(IScreenGame game) {
        this.game = game;
    }

    @Override
    public void addScreen(String name, IScreen screen) {
        // initialize screen first
        screen.init(game, game.getAssetManager());

        this.screens.put(name, screen);

        this.cachedScreenList.add(screen);

        Gdx.app.debug("Screens", "add screen: " + name);
    }

    @Override
    public void removeScreen(String name) {
        IScreen screen = this.screens.get(name);

        this.screens.remove(screen);

        if (screen != null) {
            screen.dispose();

            this.cachedScreenList.remove(screen);
        }

        this.screens.remove(name);

        Gdx.app.debug("Screens", "remove screen: " + name);
    }

    @Override
    public void push(String name) {
        IScreen screen = this.screens.get(name);

        if (screen == null) {
            throw new ScreenNotFoundException(
                    "Couldnt found initialized screen '" + name + "', add screen with method addScreen() first.");
        }

        screen.onResume();

        this.activeScreens.push(screen);

        Gdx.app.debug("Screens", "push screen: " + name);
    }

    @Override
    public void leaveAllAndEnter(String name) {
        // leave all active game states
        IScreen screen = pop();

        // pop and pause all active screens
        while (this.pop() != null) {
            screen = pop();
        }

        // push new screen
        this.push(name);
    }

    @Override
    public IScreen pop() {
        IScreen screen = this.activeScreens.poll();

        if (screen != null) {
            screen.onPause();
        }

        Gdx.app.debug("Screens", "pop screen.");

        return screen;
    }

    @Override
    public IScreen getScreenByName(String name) {
        return this.screens.get(name);
    }

    @Override
    public Collection<IScreen> listScreens() {
        return this.cachedScreenList;
    }

    @Override
    public Collection<IScreen> listActiveScreens() {
        return this.activeScreens;
    }

    @Override
    public void dispose() {
        //iterate through all screens
        for (Map.Entry<String,IScreen> entry : this.screens.entrySet()) {
            //remove screen
            this.removeScreen(entry.getKey());
        }
    }

}
