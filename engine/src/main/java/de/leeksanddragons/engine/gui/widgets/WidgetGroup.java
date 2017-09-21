package de.leeksanddragons.engine.gui.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.leeksanddragons.engine.gui.BaseHUDWidget;
import de.leeksanddragons.engine.gui.HUDWidget;
import de.leeksanddragons.engine.screen.IScreenGame;
import de.leeksanddragons.engine.utils.GameTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 09.02.2017.
 */
public class WidgetGroup extends BaseHUDWidget<WidgetGroup> {

    /**
     * list with all child widgets
     */
    protected List<HUDWidget> widgetList = new ArrayList<>();

    @Override
    public void update(IScreenGame game, GameTime time) {
        this.widgetList.forEach(widget -> {
            widget.update(game, time);
        });
    }

    @Override
    public void drawLayer0(IScreenGame game, GameTime time, SpriteBatch batch) {
        this.widgetList.forEach(widget -> {
            widget.drawLayer0(game, time, batch);
        });
    }

    @Override
    public void drawLayer1(IScreenGame game, GameTime time, ShapeRenderer shapeRenderer) {
        this.widgetList.forEach(widget -> {
            widget.drawLayer1(game, time, shapeRenderer);
        });
    }

    @Override
    public void drawLayer2(IScreenGame game, GameTime time, SpriteBatch batch) {
        this.widgetList.forEach(widget -> {
            widget.drawLayer2(game, time, batch);
        });
    }

    public void addWidget(HUDWidget widget) {
        // calculate absolute position
        widget.onMoveGroup(getX(), getY());

        this.widgetList.add(widget);
    }

    public void removeWidget(HUDWidget widget) {
        this.widgetList.remove(widget);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        // move widgets
        this.widgetList.forEach(widget -> {
            widget.onMoveGroup(x, y);
        });
    }

}
