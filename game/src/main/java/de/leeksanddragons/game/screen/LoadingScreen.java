package de.leeksanddragons.game.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.font.BitmapFontFactory;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.engine.utils.SpriteBatcherUtils;

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

    protected float percentage = 0;

    protected BitmapFont percentageFont = null;
    protected static final int PERCENTAGE_FONT_SIZE = 32;

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
        percentage += time.getDeltaTime() * 0.5f;

        if (percentage > 1f) {
            percentage = 0;
        }
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getMainCamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //TODO: draw loading bar

        //get maximum width of bar
        float maxWidth = camera.getViewportWidth();

        //draw bar
        SpriteBatcherUtils.fillRectangle(batch, startX, startY, maxWidth * percentage, barHeight, barColor);

        //draw percentage text
        this.percentageFont.draw(batch, "" + Math.round((percentage * 100)) + "%", (camera.getViewportWidth() / 2) - 32, 60);

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

}
