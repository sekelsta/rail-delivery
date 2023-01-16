package traingame.render;

import java.util.*;

import traingame.engine.render.gui.*;
import traingame.engine.render.text.BitmapFont;
import traingame.Game;

public class NewGameScreen extends Screen {
    TextButton ready;
    TextButton exit;

    public NewGameScreen(Game game) {
        BitmapFont font = Fonts.getButtonFont();
        ready = new TextButton(font, "Ready", () -> game.enterWorld());
        exit = new TextButton(font, "Exit", () -> game.stop());

        selectable.add(ready);
        selectable.add(exit);
    }

    @Override
    public void up() {
        // TODO
    }

    @Override
    public void down() {
        // TODO
    }

    @Override
    public void top() {
        // TODO
    }

    @Override
    public void bottom() {
        select(ready);
    }

    @Override
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
