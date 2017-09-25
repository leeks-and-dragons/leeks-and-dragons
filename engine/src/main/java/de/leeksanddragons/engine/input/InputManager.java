package de.leeksanddragons.engine.input;

/**
 * Created by Justin on 25.09.2017.
 */
public interface InputManager {

    //get input mapper by name
    public <T extends InputMapper> T getInputMapper (Class<T> cls);

    public <T extends InputMapper> void putInputMapper (T obj, Class<T> cls);

    public <T extends InputMapper> void removeInputMapper (Class<T> cls);

}
