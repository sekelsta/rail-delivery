package traingame.render;

import java.awt.Font;
import java.io.IOException;
import java.util.*;

import org.lwjgl.opengl.GL11;

import traingame.engine.render.*;
import traingame.engine.render.gui.TextButton;
import traingame.Game;
import shadowfox.math.Vector2f;

// For rendering 2D GUI elements in front of the world
public class Overlay {
    private static final double scale = 1.0;

    private NewGameScreen screen;
    private double xPointer, yPointer;

    public Overlay(Game game) {
        screen = new NewGameScreen(game);
    }

    public void pushScreen(NewGameScreen screen) {
        this.screen = screen;
    }

    public void popScreen() {
        screen = null;
    }

    public boolean hasScreen() {
        return screen != null;
    }

    public static double getScale() {
        return scale;
    }

    public void positionPointer(double xPos, double yPos) {
        xPointer = xPos * scale;
        yPointer = yPos * scale;
        if (hasScreen()) {
            screen.positionPointer(xPointer, yPointer);
        }
    }

    public boolean trigger() {
        if (hasScreen()) {
            return screen.trigger();
        }
        return false;
    }

    public boolean click() {
        if (hasScreen()) {
            return screen.click(xPointer, yPointer);
        }
        return false;
    }

    public void up() {
        if (hasScreen()) {
            screen.up();
        }
    }

    public void down() {
        if (hasScreen()) {
            screen.down();
        }
    }

    public void top() {
        if (hasScreen()) {
            screen.top();
        }
    }

    public void bottom() {
        if (hasScreen()) {
            screen.bottom();
        }
    }

    public void render(Vector2f uiDimensions) {
        if (hasScreen()) {
            screen.blit(uiDimensions.x, uiDimensions.y);
        }

        Fonts.render();
    }
}
