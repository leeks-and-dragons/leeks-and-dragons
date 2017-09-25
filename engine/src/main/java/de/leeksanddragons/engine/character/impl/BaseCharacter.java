package de.leeksanddragons.engine.character.impl;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.character.ICharacter;
import de.leeksanddragons.engine.utils.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 25.09.2017.
 */
public abstract class BaseCharacter implements ICharacter {

    //name of character
    protected String name = "";

    //character attributes
    protected float baseSpeed = 0;

    //path to atlas file
    protected String atlasFile = "";

    /**
    * default constructor
    */
    public BaseCharacter() {
        //
    }

    /**
    * load character json file
    */
    public void load (String path) throws IOException {
        if (path == null) {
            throw new NullPointerException("path cannot be null.");
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException("path cannot be empty.");
        }

        if (!new File(path).exists()) {
            throw new IllegalStateException("character file doesnt exists: " + path);
        }

        //get content of file
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        //get name of character
        this.name = json.getString("name");

        //get atlas file
        this.atlasFile = json.getString("atlas");

        //get attributes
        JSONObject attributes = json.getJSONObject("attributes");
        this.baseSpeed = (float) attributes.getDouble("baseSpeed");
    }

    protected abstract void onLoad (JSONObject json);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getBaseSpeed() {
        return this.baseSpeed;
    }

    @Override
    public String getAtlasPath() {
        return this.atlasFile;
    }

}
