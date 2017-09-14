package de.leeksanddragons.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.font.BitmapFontFactory;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.mods.ModManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;
import de.leeksanddragons.game.loading.LoadingTask;
import de.leeksanddragons.game.loading.tasks.ExampleTask;
import de.leeksanddragons.game.loading.tasks.ModLoadingTask;
import de.leeksanddragons.game.shared.Shared;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Justin on 12.09.2017.
 */
public class LoadingScreen extends BaseScreen {

    protected static final String BG_PATH = "./data/wallpaper/Loading_Screen.png";
    protected static final String FG_PATH = "./data/wallpaper/Loading_Screen_layer_2.png";
    protected static final String PERCENTAGE_FONT_PATH = "./data/font/arial/arial.ttf";//"./data/font/leekling/Leekling.ttf";

    //background image
    protected Texture bgTexture = null;
    protected Texture fgTexture = null;

    //loading bar
    protected final float startX = 20;
    protected final float startY = 20;
    protected final float barHeight = 10;
    protected final Color barColor = new Color(0xd8d982ff);//#d8d982

    //percentage and progress text of loading bar
    protected float percentage = 0;
    protected String progressText = "";

    //font
    protected BitmapFont percentageFont = null;
    protected static final int PERCENTAGE_FONT_SIZE = 18;

    //queue with all loading tasks
    protected Queue<LoadingTask> taskQueue = new LinkedBlockingQueue<>();

    //current loading task
    protected LoadingTask currentLoadingTask = null;

    //map with percentage portion
    protected Map<LoadingTask,Float> percentageMap = new HashMap<>();

    //percentage amount of last task
    protected float lastTaskPercentage = 0;

    //flag, if loading was finished and screen should be switched after 1 second
    protected boolean finished = false;

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //load background image first
        assetManager.load(BG_PATH, Texture.class);
        assetManager.load(FG_PATH, Texture.class);

        //force finish asset loading
        assetManager.finishLoadingAsset(BG_PATH);
        assetManager.finishLoadingAsset(FG_PATH);

        //get texture
        this.bgTexture = assetManager.get(BG_PATH);
        this.fgTexture = assetManager.get(FG_PATH);

        //create font
        this.percentageFont = BitmapFontFactory.createFont(PERCENTAGE_FONT_PATH, PERCENTAGE_FONT_SIZE, this.barColor);
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        if (this.currentLoadingTask == null) {
            if (!finished) {
                //go to mainmenu after 1 second
                game.addTimerTask(1000, () -> {
                    loadingFinished();
                });

                finished = true;
            }

            return;
        }

        //check, if task queue is empty
        if (this.currentLoadingTask.isFinished()) {
            LoadingTask nextTask = taskQueue.poll();

            //this.minPercent += (percentageMap.get(currentLoadingTask) / 100f);

            if (nextTask != null) {
                this.lastTaskPercentage += (percentageMap.get(currentLoadingTask));
            }

            //goto next task
            this.currentLoadingTask = nextTask;
        } else {
            //update task
            this.currentLoadingTask.update(game, time);

            //calculate percentage
            this.percentage = lastTaskPercentage + (percentageMap.get(currentLoadingTask) * currentLoadingTask.getTaskPercentage());
        }

        if (currentLoadingTask != null && !currentLoadingTask.getText().isEmpty()) {
            this.progressText = "" + Math.round((percentage * 100)) + "% - " + this.currentLoadingTask.getText();
        } else {
            this.progressText = "" + Math.round((percentage * 100)) + "%";
        }
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //get maximum width of bar
        float maxWidth = camera.getViewportWidth();

        //draw bar
        SpriteBatcherUtils.fillRectangle(batch, startX, startY, maxWidth * percentage, barHeight, barColor);

        //draw percentage text
        this.percentageFont.draw(batch, this.progressText, (camera.getViewportWidth() / 2) - 32, 60);

        //draw foreground graphics
        batch.draw(this.fgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());
    }

    @Override
    public void dispose() {
        //unload image
        assetManager.unload(BG_PATH);
    }

    protected void gotoMainMenu () {
        game.getScreenManager().leaveAllAndEnter("mainmenu");

        //remove this screen
        game.getScreenManager().removeScreen("loading");
    }

    protected void loadingFinished () {
        Gdx.app.log("Loading", "Loading finished.");

        //switch screen
        game.getScreenManager().leaveAllAndEnter("mainmenu");
    }

    public void addTask (LoadingTask task, float percentagePortion) {
        this.taskQueue.add(task);

        //check, if it is first task
        if (this.currentLoadingTask == null) {
            this.currentLoadingTask = this.taskQueue.poll();
        }

        //this.loadingTaskList.add(task);
        this.percentageMap.put(task, percentagePortion);
    }

    public void removeTask (LoadingTask task) {
        this.taskQueue.remove(task);

        //this.loadingTaskList.remove(task);
        this.percentageMap.remove(task);
    }

}
