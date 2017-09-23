package de.leeksanddragons.engine.entity.impl;

import de.leeksanddragons.engine.entity.Entity;
import de.leeksanddragons.engine.entity.IComponent;
import de.leeksanddragons.engine.entity.annotation.SharableComponent;
import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * The base class for all components.
 * 
 * @since 1.0.0-PreAlpha
 */
public abstract class BaseComponent implements IComponent {

    //instance of game
    protected IScreenGame game = null;

    //entity, component belongs to
    protected Entity entity = null;

    @Override
    public final void init(IScreenGame game, Entity entity) {
        this.game = game;
        this.entity = entity;

        if (getClass().isAnnotationPresent(SharableComponent.class)) {
            // you cannot access entity on this way
            this.entity = null;
        }

        this.onInit(game, entity);
    }

    protected abstract void onInit(IScreenGame game, Entity entity);

    @Override
    public void onAddedToEntity(Entity entity) {

    }

    @Override
    public void onRemovedFromEntity(Entity entity) {

    }

}
