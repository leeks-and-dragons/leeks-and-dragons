package de.leeksanddragons.engine.memory;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Proxy Design pattern for libGDX Asset Manager to prevent unloading of specific assets
 *
 * @see AssetManager
 * @link https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/assets/AssetManager.html
 * @link https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/assets/AssetManager.java
 * @link https://www.gamedevelopment.blog/asset-manager-libgdx-tutorial/
 *
 * Created by Justin on 11.09.2017.
 */
public class GameAssetManager extends AssetManager {

    //https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/assets/AssetManager.html

    //list with all assets, which cannot be blocked
    protected List<String> blockList = new ArrayList<>();

    //time limit for every asset manager update() execution
    protected int max_Loading_Millis = 10;

    @Override
    public void unload (String fileName) {
        //check, if asset file is blocked
        if (this.blockList.contains(fileName)) {
            //dont unload file
            return;
        }

        super.unload(fileName);
    }

    /**
    * add file which cannot be unloaded
     *
     * @param fileName asset file name which cannot be unloaded
    */
    public void addBlockingFile (String fileName) {
        this.blockList.add(fileName);
    }

    /**
    * remove file which cannot be unloaded, so its allowed to unload this file now
     *
     * @param fileName asset file name to allow unload
    */
    public void removeBlockingFile (String fileName) {
        this.blockList.remove(fileName);
    }

    /**
    * get update time limit
     *
     * @return update time limit
    */
    public int getUpdateTimeLimit () {
        return this.max_Loading_Millis;
    }

    public boolean isUpdateTimeLimited () {
        return this.max_Loading_Millis == -1;
    }

    public void disableUpdateTimeLimit () {
        this.max_Loading_Millis = -1;
    }

    public void enableUpdateTimeLimit (int millis) {
        this.max_Loading_Millis = millis;
    }

    public void setUpdateTimeLimit (int max_Loading_Millis) {
        this.max_Loading_Millis = max_Loading_Millis;
    }

    public boolean updateLoading () {
        if (this.max_Loading_Millis > 0) {
            return super.update(this.max_Loading_Millis);
        } else {
            return super.update();
        }
    }

}
