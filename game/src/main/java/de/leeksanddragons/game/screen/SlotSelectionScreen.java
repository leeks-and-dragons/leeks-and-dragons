package de.leeksanddragons.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.leeksanddragons.engine.camera.CameraHelper;
import de.leeksanddragons.engine.font.BitmapFontFactory;
import de.leeksanddragons.engine.gui.HUD;
import de.leeksanddragons.engine.gui.widgets.MenuButton;
import de.leeksanddragons.engine.memory.GameAssetManager;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.screen.impl.BaseScreen;
import de.leeksanddragons.engine.utils.FileUtils;
import de.leeksanddragons.engine.utils.GameTime;
import de.leeksanddragons.game.saves.SlotInfo;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 18.09.2017.
 */
public class SlotSelectionScreen extends BaseScreen {

    //image paths
    protected static final String BG_PATH = "./data/wallpaper/Loading_Screen.png";
    protected static final String FG_PATH = "./data/wallpaper/Loading_Screen_layer_2.png";

    //icon paths
    protected static final String REMOVE_ICON = "./data/icon/remove/remove_64.png";
    protected static final String REMOVE_ICON_HOVERED = "./data/icon/remove/remove_64_hovered.png";

    //background image
    protected Texture bgTexture = null;
    protected Texture fgTexture = null;

    //font
    protected BitmapFont font = null;

    //GUI
    protected HUD hud = null;

    //icon textures
    protected Texture removeIcon = null;
    protected Texture removeHoveredIcon = null;

    //buttons
    protected MenuButton backToMenuButton = null;
    protected MenuButton slotButtons[] = new MenuButton[3];
    protected MenuButton removeSlotButtons[] = new MenuButton[3];

    //slots
    protected SlotInfo slotInfo[] = new SlotInfo[3];

    @Override
    protected void onInit(IScreenGame game, GameAssetManager assetManager) {
        //create directory structure for saves, if neccessary
        this.checkSavesDirectory();

        //load slots
        for (int i = 0; i < 3; i++) {
            //create and load slot
            slotInfo[i] = new SlotInfo(game, (i + 1));
            try {
                slotInfo[i].load();
            } catch (IOException e) {
                e.printStackTrace();
                throw new GdxRuntimeException("IOException while loading slot: " + e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public void onResume(IScreenGame game) {
        //load background image first
        assetManager.load(BG_PATH, Texture.class);
        assetManager.load(FG_PATH, Texture.class);
        assetManager.load(REMOVE_ICON, Texture.class);
        assetManager.load(REMOVE_ICON_HOVERED, Texture.class);

        //force finish asset loading
        assetManager.finishLoadingAsset(BG_PATH);
        assetManager.finishLoadingAsset(FG_PATH);
        assetManager.finishLoadingAsset(REMOVE_ICON);
        assetManager.finishLoadingAsset(REMOVE_ICON_HOVERED);

        //get texture
        this.bgTexture = assetManager.get(BG_PATH);
        this.fgTexture = assetManager.get(FG_PATH);
        this.removeIcon = assetManager.get(REMOVE_ICON);
        this.removeIcon = assetManager.get(REMOVE_ICON_HOVERED);

        if (this.hud == null) {
            //create new HUD
            this.hud = new HUD();

            //create new font
            this.font = BitmapFontFactory.createFont("./data/font/arial/arial.ttf", 26, Color.WHITE);

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

        this.assetManager.unload(REMOVE_ICON);
        this.assetManager.unload(REMOVE_ICON_HOVERED);
        this.removeIcon = null;
        this.removeHoveredIcon = null;
    }

    protected void checkSavesDirectory () {
        String savesPath = FileUtils.getAppHomeDir(game.getAppName()) + "saves/";

        File saveDir = new File(savesPath);

        //create directory, if not exists
        if (!saveDir.exists()) {
            saveDir.mkdirs();
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
        this.backToMenuButton = new MenuButton(textureAtlas, "Menu_normal", "Menu_hovered", this.font, "");
        this.backToMenuButton.setTextPadding(10, 50);
        this.backToMenuButton.setPosition(30, 30);
        this.backToMenuButton.setHoverSound(hoverSound, 0.5f);
        this.backToMenuButton.setClickListener(() -> {
            game.getScreenManager().leaveAllAndEnter("mainmenu");
        });
        this.hud.addWidget(backToMenuButton);

        float startX = 200;
        float startY = 325;

        //add slot buttons
        for (int i = 0; i < 3; i++) {
            //get slot info
            SlotInfo slot = this.slotInfo[i];

            this.slotButtons[i] = new MenuButton(textureAtlas, "slotSmall", "slotSmall_selected", this.font, slot.getName());
            this.slotButtons[i].setPosition(startX, startY);
            this.slotButtons[i].setTextPadding(70, 40);
            this.slotButtons[i].setHoverSound(hoverSound, 0.5f);

            //variable has to be final
            final int index = i;

            this.slotButtons[i].setClickListener(() -> {
                startGame(index);
            });
            this.hud.addWidget(slotButtons[i]);

            startY -= slotButtons[i].getHeight();//paddingTop;
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
