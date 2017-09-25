package de.leeksanddragons.engine.input.impl;

import de.leeksanddragons.engine.input.InputManager;
import de.leeksanddragons.engine.input.InputMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 25.09.2017.
 */
public class DefaultInputManager implements InputManager {

    //map with all input mappers
    protected Map<Class<? extends InputMapper>, InputMapper> inputMapper = new HashMap();

    @Override
    public <T extends InputMapper> T getInputMapper(Class<T> cls) {
        if (!inputMapper.containsKey(cls)) {
            throw new IllegalStateException("input mapper doesnt exists: " + cls.getName());
        }

        return cls.cast(this.inputMapper.get(cls));
    }

    @Override
    public <T extends InputMapper> void putInputMapper(T obj, Class<T> cls) {
        this.inputMapper.put(cls, obj);
    }

    @Override
    public <T extends InputMapper> void removeInputMapper(Class<T> cls) {
        this.inputMapper.remove(cls);
    }

}
