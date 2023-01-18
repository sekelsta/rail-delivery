package traingame.render;

import java.util.*;

import traingame.engine.render.SpriteBatch;
import traingame.engine.render.gui.*;
import traingame.engine.render.text.BitmapFont;
import traingame.Game;

public abstract class Screen {
    final int INVALID = -1;

    ArrayList<GuiElement> selectable = new ArrayList<>();
    int selected = INVALID;

    protected void select(GuiElement element) {
        for (int i = 0; i < selectable.size(); ++i) {
            if (selectable.get(i).equals(element)) {
                selected = i;
            }
        }
    }

    public void positionPointer(double xPos, double yPos) {
        selected = INVALID;
        for (int i = 0; i < selectable.size(); ++i) {
            if (selectable.get(i).containsPoint(xPos, yPos)) {
                selected = i;
            }
        }
    }

    public GuiElement getSelected() {
        if (selected == INVALID) {
            return null;
        }
        return selectable.get(selected);
    }

    public boolean trigger() {
        GuiElement selected = getSelected();
        if (selected != null) {
            return selected.trigger();
        }
        return false;
    }

    public boolean click(double xPos, double yPos) {
        positionPointer(xPos, yPos);
        return trigger();
    }

    public void up() {
        if (selected > 0) {
            selected -= 1;
        }
        else {
            top();
        }
    }

    public void down() {
        if (selected + 1 < selectable.size()) {
            selected += 1;
        }
    }

    public void top() {
        if (selectable.size() > 0) {
            selected = 0;
        }
    }

    public void bottom() {
        if (selectable.size() > 0) {
            selected = selectable.size() - 1;
        }
    }

    public abstract void blit(SpriteBatch spriteBatch, double screenWidth, double screenHeight);
}
