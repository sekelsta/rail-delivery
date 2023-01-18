package traingame.render;

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

    private Deque<Screen> screenStack = new ArrayDeque<>();
    private double xPointer, yPointer;

    public Overlay(Game game) {
        screenStack.push(new NewGameScreen(game));
    }

    public void pushScreen(Screen screen) {
        screenStack.push(screen);
        screen.positionPointer(xPointer, yPointer);
    }

    public void popScreen() {
        screenStack.pop();
    }

    public void popScreenIfEquals(Screen screen) {
        if (hasScreen() && screenStack.peek().equals(screen)) {
            popScreen();
        }
    }

    public boolean hasScreen() {
        return screenStack.size() > 0;
    }

    public static double getScale() {
        return scale;
    }

    public void positionPointer(double xPos, double yPos) {
        xPointer = xPos * scale;
        yPointer = yPos * scale;
        if (hasScreen()) {
            screenStack.peek().positionPointer(xPointer, yPointer);
        }
    }

    public void escape(Game game) {
        if (screenStack.peek() instanceof NewGameScreen) {
            return;
        }

        if (hasScreen()) {
            popScreen();
        }
        else {
            pushScreen(new GameMenuScreen(game));
        }
    }

    public boolean trigger() {
        if (hasScreen()) {
            return screenStack.peek().trigger();
        }
        return false;
    }

    public boolean click() {
        if (hasScreen()) {
            return screenStack.peek().click(xPointer, yPointer);
        }
        return false;
    }

    public void up() {
        if (hasScreen()) {
            screenStack.peek().up();
        }
    }

    public void down() {
        if (hasScreen()) {
            screenStack.peek().down();
        }
    }

    public void top() {
        if (hasScreen()) {
            screenStack.peek().top();
        }
    }

    public void bottom() {
        if (hasScreen()) {
            screenStack.peek().bottom();
        }
    }

    public void render(SpriteBatch spriteBatch, Vector2f uiDimensions) {
        if (hasScreen()) {
            screenStack.peek().blit(spriteBatch, uiDimensions.x, uiDimensions.y);
        }
        Fonts.render();
    }
}
