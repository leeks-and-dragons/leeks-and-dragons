package de.leeksanddragons.game.saves;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class to get information for an load / save slot
 *
 * Created by Justin on 20.09.2017.
 */
public class SlotInfo {

    //path to slot
    protected String slotPath = "";

    //flag, if slot was loaded
    protected boolean loaded = false;

    //flag, if it is an new game
    protected boolean newGame = false;

    //name of slot
    protected String name = "";

    //location description
    protected String locDesc = "";

    //played time ("Spielzeit")
    protected long playedTime = 0;

    /**
    * default constructor
     *
     * @param game instance of game
     * @param slotIndex index of slot
    */
    public SlotInfo (IScreenGame game, int slotIndex) {
        if (slotIndex <= 0) {
            throw new IllegalArgumentException("slot index has to be greater than 0.");
        }

        //generate saves path
        String savesPath = FileUtils.getAppHomeDir(game.getAppName()) + "saves/";
        this.slotPath = savesPath + "slot_" + slotIndex + "/";

        //create directory if absent
        if (!new File(this.slotPath).exists()) {
            new File(this.slotPath).mkdirs();
        }
    }

    /**
    * load slot, if possible
    */
    public void load () throws IOException {
        //generate save path
        String filePath = this.slotPath + "game.save";

        //check, if game.save exists
        if (!new File(filePath).exists()) {
            //slot doesnt exists --> new slot

            //set flags
            this.newGame = true;
            this.loaded = true;

            return;
        }

        //read file content and convert to json object
        String content = FileUtils.readFile(filePath, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(content);

        //parse json
        this.name = json.getString("name");
        this.locDesc = json.getString("locDesc");
        this.playedTime = json.getLong("playedTime");

        //set flags
        this.newGame = false;
        this.loaded = true;
    }

    /**
    * get path to slot directory
     *
     * @return path to slot directory
    */
    public String getPath () {
        return this.slotPath;
    }

    /**
    * check, if slot was loaded
     *
     * @return true, if slot was loaded
    */
    public boolean isLoaded () {
        return this.loaded;
    }

    /**
    * check, if game is a new game or an saved game
     *
     * @return true, if game is a new game
    */
    public boolean isNewGame () {
        if (!isLoaded()) {
            throw new IllegalStateException("load SlotInfo before check, if it is an new game.");
        }

        return this.newGame;
    }

    /**
    * get name of slot
     *
     * @return name of slot or "New Game", if it is an new game
    */
    public String getName () {
        if (isNewGame()) {
            return "New Game";
        }

        return this.name;
    }

    /**
    * get location description where player's character stands, e.q. "Marios Shop - Insel 5"
     *
     * @return location descprition
    */
    public String getLocationDescription () {
        if (isNewGame()) {
            return "Tutorial";
        }

        return this.locDesc;
    }

    public String getPlayedTimeString () {
        if (isNewGame()) {
            return "Not played yet.";
        }

        return this.playedTime + " minutes";
    }

}
