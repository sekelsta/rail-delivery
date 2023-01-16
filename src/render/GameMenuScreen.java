package traingame.render;

import java.util.*;

import traingame.engine.render.gui.*;
import traingame.engine.render.text.BitmapFont;
import traingame.Game;

public class GameMenuScreen extends Screen {
    public GameMenuScreen(Game game) {
        BitmapFont font = Fonts.getButtonFont();
        selectable.add(new TextButton(font, "Resume", () -> game.escape()));
        selectable.add(new TextButton(font, "Quit to Main Menu", () -> game.exitWorld()));
    }

    @Override
    public void blit(double screenWidth, double screenHeight)
    {
        BitmapFont font = Fonts.getButtonFont();

        int height = 0;
        for (int i = 0; i < selectable.size(); ++i) {
            height += selectable.get(i).getHeight();
            if (i + 1 < selectable.size()) {
                height += selectable.get(i).getHeight() / 4;
            }
        }

        int yPos = ((int)screenHeight - height) / 2;
        GuiElement selected = getSelected();
        for (GuiElement item : selectable) {
            int xPos = ((int)screenWidth - item.getWidth()) / 2;
            item.position(xPos, yPos);
            item.blit(item == selected);
            yPos += (int)(1.25 * item.getHeight());
        }
    }
}
