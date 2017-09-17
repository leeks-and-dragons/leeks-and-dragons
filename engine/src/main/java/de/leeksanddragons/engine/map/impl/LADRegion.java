package de.leeksanddragons.engine.map.impl;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Justin on 17.09.2017.
 */
public class LADRegion extends BaseRegion {

    //path to .lrf file
    protected String path = "";

    protected boolean hasPreloaded = false;

    /**
    * default constructor
    */
    public LADRegion () {
        //
    }

    /**
    * load region
    */
    @Override
    public void load (String path) throws IOException {
        this.path = path;

        //check, if file exists
        if (!new File(path).exists()) {
            throw new FileNotFoundException("Couldnt found region file: " + path);
        }

        //read file content
        String content = FileUtils.readFile(path, StandardCharsets.UTF_8);

        //create json object
        JSONObject json = new JSONObject(content);

        //get meta data
        JSONObject meta = json.getJSONObject("meta");
    }

    @Override
    public void update(IScreenGame game, GameTime time) {

    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {

    }

    @Override
    public void preload() {

    }

    @Override
    public boolean hasPreLoadingFinished() {
        return this.hasPreloaded;
    }

}
