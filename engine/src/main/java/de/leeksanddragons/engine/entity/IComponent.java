package de.leeksanddragons.engine.entity;

import de.leeksanddragons.engine.screen.IScreenGame;

/**
 * Created by Justin on 10.02.2017.
 */
public interface IComponent {

    /**
    * initialize component
     *
     * @param game instance of game
     * @param entity entity, this components belongs to
    */
    public void init(IScreenGame game, Entity entity);

    /**
    * method which is called, if component was added to an entity
     *
     * @param entity entity, component belongs to
    */
    public void onAddedToEntity(Entity entity);

    /**
     * method which is called, if component was removed from an entity
     *
     * @param entity entity, component belonged to
     */
    public void onRemovedFromEntity(Entity entity);

    /**
    * dispose component
    */
    public void dispose();

}
