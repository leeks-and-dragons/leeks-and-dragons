package de.leeksanddragons.engine.entity.impl;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IComponent;
import de.leeksanddragons.engine.entity.listener.ComponentListener;
import de.leeksanddragons.engine.screen.IScreenGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 10.02.2017.
 */
public class ECS extends BaseECS {

    // list with all component listeners
    protected List<ComponentListener> componentListenerList = new ArrayList<>();

    public ECS(IScreenGame game) {
        super(game);
    }

    @Override
    protected void onEntityAdded(Entity entity) {

    }

    @Override
    protected void onEntityRemoved(Entity entity) {

    }

    @Override
    public <T extends IComponent> void onComponentAdded(Entity entity, T component, Class<T> cls) {
        for (ComponentListener listener : this.componentListenerList) {
            listener.onComponentAdded(entity, component, cls);
        }
    }

    @Override
    public <T extends IComponent> void onComponentRemoved(Entity entity, T component, Class<T> cls) {
        for (ComponentListener listener : this.componentListenerList) {
            listener.onComponentRemoved(entity, component, cls);
        }
    }

    @Override
    public void registerComponentListener(ComponentListener listener) {
        this.componentListenerList.add(listener);
    }

    @Override
    public void removeComponentListener(ComponentListener listener) {
        this.componentListenerList.remove(listener);
    }
}
