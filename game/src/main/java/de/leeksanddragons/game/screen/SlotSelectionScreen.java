package de.leeksanddragons.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.font.BitmapFontFactory;
import de.leeksanddragons.engine.gui.HUD;
import de.leeksanddragons.engine.gui.widgets.MenuButton;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;

import java.io.File;

/**
 * Created by Justin on 18.09.2017.
 */
public class SlotSelectionScreen extends BaseScreen {

    //image paths
    protected static final String BG_PATH = "./data/wallpaper/Loading_Screen.png";
    protected static final String FG_PATH = "./data/wallpaper/Loading_Screen_layer_2.png";

    //background image
    protected Texture bgTexture = null;
    protected Texture fgTexture = null;

    //font
    protected BitmapFont font = null;

    //GUI
    protected HUD hud = null;

    //buttons
    protected MenuButton backToMenuButton = null;
    protected MenuButton slotButtons[] = new MenuButton[3];

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //create directory structure for saves, if neccessary
        this.checkSavesDirectory();
    }

    @Override
    public void onResume(IScreenGame game) {
        //load background image first
        assetManager.load(BG_PATH, Texture.class);
        assetManager.load(FG_PATH, Texture.class);

        //force finish asset loading
        assetManager.finishLoadingAsset(BG_PATH);
        assetManager.finishLoadingAsset(FG_PATH);

        //get texture
        this.bgTexture = assetManager.get(BG_PATH);
        this.fgTexture = assetManager.get(FG_PATH);

        if (this.hud == null) {
            //create new HUD
            this.hud = new HUD();

            //create new font
            this.font = BitmapFontFactory.createFont("./data/font/leekling/Leekling.ttf", 32, Color.WHITE);

            //add buttons
            this.addButtons();
        }
    }

    @Override
    public void onPause(IScreenGame game) {
        this.assetManager.unload(BG_PATH);
        this.assetManager.unload(FG_PATH);
        this.bgTexture = null;
        this.fgTexture = null;
    }

    protected void checkSavesDirectory () {
        String savesPath = FileUtils.getAppHomeDir(game.getAppName()) + "saves/";

        File saveDir = new File(savesPath);

        //create directory, if not exists
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        for (int i = 1; i <= 3; i++) {
            if (!new File(savesPath + "slot_" + i).exists()) {
                new File(savesPath + "slot_" + i).mkdirs();
            }
        }
    }

    /**
    * add buttons to HUD
    */
    protected void addButtons () {
        //first, get texture atlas
        TextureAtlas textureAtlas = game.getAssetManager().getAssetByName("menu_buttons", TextureAtlas.class);

        //get hover sound
        Sound hoverSound = game.getAssetManager().getAssetByName("menu_hover_sound", Sound.class);

        //create start button
        this.backToMenuButton = new MenuButton(textureAtlas, "button2", "button2_hovered", this.font, "MENU");
        this.backToMenuButton.setTextPadding(10, 50);
        this.backToMenuButton.setPosition(30, 30);
        this.backToMenuButton.setHoverSound(hoverSound, 0.5f);
        this.backToMenuButton.setClickListener(() -> {
            game.getScreenManager().leaveAllAndEnter("mainmenu");
        });
        this.hud.addWidget(backToMenuButton);

        float startX = 100;
        float startY = 340;

        float paddingTop = 260;

        //add slot buttons
        for (int i = 0; i < 3; i++) {
            this.slotButtons[i] = new MenuButton(textureAtlas, "slot", "slot_selected", this.font, "");
            this.slotButtons[i].setPosition(startX, startY);
            this.slotButtons[i].setHoverSound(hoverSound, 0.5f);

            //variable has to be final
            final int index = i;

            this.slotButtons[i].setClickListener(() -> {
                startGame(index);
            });
            this.hud.addWidget(slotButtons[i]);

            startY -= paddingTop;
        }
    }

    /**
    * start game
     *
     * @param slotIndex index of slot, begins with 0
    */
    protected void startGame (int slotIndex) {
        //increment index, so it starts with 1
        slotIndex = slotIndex + 1;

        Gdx.app.debug("Slots", "try to start game in slot " + slotIndex + ".");

        //TODO: add code here
    }

    @Override
    public void update(IScreenGame game, GameTime time) {
        //update HUD
        this.hud.update(game, time);
    }

    @Override
    public void draw(IScreenGame game, GameTime time, SpriteBatch batch) {
        //get camera
        CameraHelper camera = game.getCameraManager().getUICamera();

        //draw background
        batch.draw(this.bgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //draw foreground graphics
        batch.draw(this.fgTexture, 0, 0, camera.getViewportWidth(), camera.getViewportHeight());

        //draw GUI
        this.hud.drawLayer0(time, batch);
    }

    @Override
    public void dispose() {
        if (this.bgTexture != null) {
            //unload texture
            this.game.getAssetManager().unload(BG_PATH);

            this.bgTexture = null;
        }

        if (this.fgTexture != null) {
            //unload texture
            this.game.getAssetManager().unload(FG_PATH);

            this.fgTexture = null;
        }

        if (this.font != null) {
            this.font.dispose();
            this.font = null;
        }
    }

}
