package de.leeksanddragons.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.entity.listener.ComponentListener;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 10.02.2017.
 */
public interface EntityManager {

    /**
    * add entity with an unique name
     *
     * @param uniqueName unique name of entity
     * @param entity instance of entity
    */
    public void addEntity(final String uniqueName, final Entity entity);

    /**
     * add entity
     *
     * @param entity instance of entity
     */
    public void addEntity(Entity entity);

    /**
     * remove entity
     *
     * @param entity instance of entity
     */
    public void removeEntity(Entity entity);

    /**
    * remove entity by ID
     *
     * @param entityID entity ID
    */
    public void removeEntity(final long entityID);

    /**
    * remove entity by unique name
     *
     * @param uniqueName unique name of entity
    */
    public void removeEntity(final String uniqueName);

    /**
    * remove all entites from ECS
    */
    public void removeAllEntities();

    /**
    * update entity with all its components
     *
     * @param game instance of game
     * @param time game time
    */
    public void update(IScreenGame game, GameTime time);

    /**
     * daw entity with all its components
     *
     * @param game instance of game
     * @param time game time
     * @param batch sprite batch to draw
     */
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch);

    /**
     * daw UI layer of entity with all its components
     *
     * @param game instance of game
     * @param time game time
     * @param batch sprite batch to draw
     */
    public void drawUILayer(IScreenGame game, GameTime time, SpriteBatch batch);

    /**
    * listener, if entity update order was changed
    */
    public void onEntityUpdateOrderChanged();

    /**
     * listener, if entity draw order was changed
     */
    public void onEntityDrawOrderChanged();

    /**
     * listener, if entity UI layer draw order was changed
     */
    public void onEntityUILayerDrawOrderChanged();

    /**
    * listener, if component was added to entity
     *
     * @param entity instance of entity, component was added to
     * @param component instance of component
     * @param cls class of component
    */
    public <T extends IComponent> void onComponentAdded(Entity entity, T component, Class<T> cls);

    /**
     * listener, if component was removed from entity
     *
     * @param entity instance of entity, component was added to
     * @param component instance of component
     * @param cls class of component
     */
    public <T extends IComponent> void onComponentRemoved(Entity entity, T component, Class<T> cls);

    /**
    * register a new component listener
     *
     * @param listener instance of component listener
    */
    public void registerComponentListener(ComponentListener listener);

    /**
     * remove a component listener
     *
     * @param listener instance of component listener
     */
    public void removeComponentListener(ComponentListener listener);

    /**
    * get instance of game
     *
     * @return instance of game
    */
    @Deprecated
    public IScreenGame getGame();

    /**
    * dispose entity
    */
    public void dispose();

}
