package traingame;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import traingame.engine.DataFolders;
import traingame.engine.Gameloop;
import traingame.engine.Log;

public class Main {
    private static int DEFAULT_FRAME_CAP = 120;

    public static void main(String[] args) {
        DataFolders.init(Game.GAME_ID);
        Log.info("Starting " + Game.GAME_ID + " " + Game.VERSION + " with args: " + String.join(" ", args));
        Game game = new Game(true);
        new Gameloop(game, DEFAULT_FRAME_CAP).run();
    }
}
