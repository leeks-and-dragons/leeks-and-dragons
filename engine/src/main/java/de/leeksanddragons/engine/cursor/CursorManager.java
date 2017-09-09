package de.leeksanddragons.engine.cursor;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import de.leeksanddragons.engine.game.BaseGame;
import de.leeksanddragons.engine.utils.GameTime;

/**
 * Created by Justin on 15.02.2017.
 */
public interface CursorManager {

    public void setCursorTexture(Pixmap cursorImage);

    public void setSystemCursor(Cursor.SystemCursor cursor);

    public void update(BaseGame game, GameTime time);

    public void resetCursor();

}
