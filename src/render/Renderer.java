package traingame.render;

import java.awt.Font;
import java.io.IOException;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;

import traingame.engine.render.*;
import traingame.engine.render.SpriteBatch;
import traingame.engine.render.Texture;
import traingame.World;
import shadowfox.math.*;
import java.lang.Math.*;
import traingame.Terrain;

public class Renderer implements IFramebufferSizeListener {
    private final ShaderProgram shader2D = ShaderProgram.load("/shaders/2d.vsh", "/shaders/2d.fsh");
    private final Vector2f uiDimensions = new Vector2f(1, 1);
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Texture mapBackground = new Texture("map_background.png");
    private final Texture terrainTexture = new Texture("tiles.png");
    private final Texture mainMenuBackground = new Texture("main_menu_background.png");
    private final int MAP_PIXELS_WIDE = mapBackground.getWidth();
    private final int MAP_PIXELS_HIGH = mapBackground.getHeight();
    private static final int HEX_WIDTH = 40;
    private static final int HEX_HEIGHT = 48;
    private int width;
    private int height;

    public Renderer() {
        // Enable alpha blending (over)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glClearColor(1f, 1f, 1f, 1f);

        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

    public int getPixelX(int x, int y) {
        int q = x - (y / 2);
        return (int)Math.round(HEX_WIDTH * (q + y / 2f));
    }

    public int getPixelY(int x, int y) {
        return y * (int)Math.round(HEX_WIDTH * Math.sqrt(3) / 2f);
    }

    public String getPrintableHexSize() {
        return "(" + HEX_WIDTH + ", " + HEX_HEIGHT + ")";
    }

    public void render(float lerp, World world, Overlay overlay) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Set up for two-dimensional rendering
        shader2D.use();
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Render the world
        if (world != null) {
            int reservedLeft = 0;
            int reservedBottom = 0;
            // Temporary test code to see what moving the UI location would look like
            if ((double)MAP_PIXELS_WIDE / MAP_PIXELS_HIGH < (double)width / height) {
                reservedLeft = 200;
            }
            else {
                reservedBottom = 200;
            }
            renderWorld(lerp, world, reservedLeft, reservedBottom);
        }
        else {
            spriteBatch.setTexture(mainMenuBackground);
            spriteBatch.blitScaled(0, 0, width, height, 0, 0, mainMenuBackground.getWidth(), mainMenuBackground.getHeight());
            spriteBatch.render();
        }

        // Render UI
        shader2D.setUniform("dimensions", uiDimensions);
        shader2D.setFloat("left_margin", 0);
        shader2D.setFloat("right_margin", 0);
        shader2D.setFloat("top_margin", 0);
        shader2D.setFloat("bottom_margin", 0);
        overlay.render(spriteBatch, uiDimensions);
    }

    private void renderWorld(float lerp, World world, int reservedLeft, int reservedBottom) {
        int mapScreenWidth = width - reservedLeft;
        int mapScreenHeight = height - reservedBottom;
        int widthDiff = 0;
        int heightDiff = 0;
        double aspectRatio = (double)MAP_PIXELS_WIDE / MAP_PIXELS_HIGH;
        if (aspectRatio > (double)mapScreenWidth / mapScreenHeight) {
            heightDiff = mapScreenHeight - (int)(mapScreenWidth / aspectRatio);
        }
        else {
            widthDiff = mapScreenWidth - (int)(aspectRatio * mapScreenHeight);
        }

        shader2D.setUniform("dimensions", new Vector2f(MAP_PIXELS_WIDE, MAP_PIXELS_HIGH));
        shader2D.setFloat("left_margin", (reservedLeft + widthDiff / 2) / uiDimensions.x);
        shader2D.setFloat("right_margin", (widthDiff - (widthDiff / 2)) / uiDimensions.x);
        shader2D.setFloat("top_margin", heightDiff / 2 / uiDimensions.y);
        shader2D.setFloat("bottom_margin", (heightDiff - heightDiff / 2 + reservedBottom) / uiDimensions.y);


        spriteBatch.setTexture(mapBackground);
        spriteBatch.blit(0, 0, MAP_PIXELS_WIDE, MAP_PIXELS_HIGH, mapBackground.getWidth(), mapBackground.getHeight());
        spriteBatch.render();

        spriteBatch.setTexture(terrainTexture);
        for (int y = 0; y < world.mapHeight; y++) {
            for (int x = 0; x < world.mapWidth; x++) {
                Terrain terrain = world.getTerrainXY(x,y);
                int locX = getPixelX(x, y);
                int locY = getPixelY(x, y);

                if (terrain == Terrain.MOUNTAIN){
                    spriteBatch.blit(locX, locY, HEX_WIDTH, HEX_HEIGHT, 247, 232);
                }
                else if (terrain == Terrain.PLAIN){
                    spriteBatch.blit(locX, locY, HEX_WIDTH, HEX_HEIGHT, 124, 232);
                }
                else if (terrain == Terrain.FOREST){
                    spriteBatch.blit(locX, locY, HEX_WIDTH, HEX_HEIGHT, 165, 232);
                }
                spriteBatch.render();
            }
        }
    }

    @Override
    public void windowResized(int width, int height) {
        // Ban 0 width or height
        width = Math.max(width, 1);
        height = Math.max(height, 1);

        this.width = width;
        this.height = height;

        // This is the size of UI's canvas, so the scale is inversely proportional to actual element size
        float uiScale = (float)Overlay.getScale();
        uiDimensions.x = width * uiScale;
        uiDimensions.y = height * uiScale;
    }

    public void enterWireframe() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public void exitWireframe() {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public void clean() {
        shader2D.delete();
    }
}
