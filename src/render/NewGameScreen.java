package traingame.render;

import java.util.*;

import traingame.engine.render.gui.*;
import traingame.engine.render.text.BitmapFont;
import traingame.Game;

public class NewGameScreen {
    final int INVALID = -1;

    TextButton ready;
    TextButton exit;

    ArrayList<GuiElement> selectable;
    int selected = INVALID;

    public NewGameScreen(Game game) {
        BitmapFont font = Fonts.getButtonFont();
        ready = new TextButton(font, "Ready", () -> game.enterWorld());
        exit = new TextButton(font, "Exit", () -> game.stop());

        selectable = new ArrayList<>();
        selectable.add(ready);
        selectable.add(exit);
    }

    private void select(GuiElement element) {
        for (int i = 0; i < selectable.size(); ++i) {
            if (selectable.get(i).equals(element)) {
                selected = i;
            }
        }
    }

    public void positionPointer(double xPos, double yPos) {
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
        // TODO
    }

    public void down() {
        // TODO
    }

    public void top() {
        // TODO
    }

    public void bottom() {
        select(ready);
    }

    public void blit(double screenWidth, double screenHeight)
    {
        BitmapFont font = Fonts.getButtonFont();
        BitmapFont titleFont = Fonts.getTitleFont();

        int height = 0;
        height += (int)(titleFont.getHeight() * 1.25f);
        height += (int)(font.getHeight() * 1.25f);
        height += font.getHeight();

        // TODO: Proper X positions for everything
        int yPos = ((int)screenHeight - height) / 2;
        GuiElement selected = getSelected();
        titleFont.blit("New Game", 0, yPos, 0, 0, 0);
        yPos += (int)(titleFont.getHeight() * 1.25f);
        font.blit("Select one color per player", 0, yPos, 0, 0, 0);
        yPos += (int)(font.getHeight() * 1.25f);
        exit.position(0, yPos);
        exit.blit(exit == selected);
        ready.position((int)(screenWidth / 2), yPos);
        ready.blit(ready == selected);
    }
}
