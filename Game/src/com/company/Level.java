package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level {

    static final int TILE_SIZE = 32;
    public static final int GRAVITY = 4;

    enum GameState {ONGOING, VICTORY, DEFEAT}

    private GameState gameState;
    private final int width;
    private final int height;
    private List<Blob> incomingBlobs;
    private Set<Blob> allBlobs;
    private Player player;
    private Boolean[][] isBlock;

    Level(String filename) {
        gameState = GameState.ONGOING;
        incomingBlobs = new ArrayList<>();
        allBlobs = new HashSet<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(filename)));
        } catch (FileNotFoundException e) {}
        String dims = null;
        try {
            dims = reader.readLine();
        } catch (IOException e) {}
        width = Integer.parseInt(dims.split(" ")[0]);
        height = Integer.parseInt(dims.split(" ")[1]);
        isBlock = new Boolean[height][width];
        for (int i = 0; i < height; i++) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {}
            for (int j = 0; j < width; j++) {
                char blobType = line.charAt(j);
                // empty
                if (blobType == '_') {
                    isBlock[i][j] = false;
                }
                // block
                else if (blobType == '=') {
                    isBlock[i][j] = true;
                }
                // blob
                else {
                    isBlock[i][j] = false;
                    if (blobType == 'p') {
                        player = new Player(j * TILE_SIZE, i * TILE_SIZE, blobType, this);
                    }
                    else {
                        allBlobs.add(new Blob(j * TILE_SIZE, i * TILE_SIZE, blobType, this));
                    }
                }

            }
        }
    }

    public Player.Facing playerFacing() {
        return player.facing();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newState) {
        gameState = newState;
    }

    void handleKeyPressed(KeyEvent e) {
        player.handleKeyPressed(e);
    }

    void handleKeyReleased(KeyEvent e) {
        player.handleKeyReleased(e);
    }

    void timestep() {

        for (Blob blob : incomingBlobs) {
            allBlobs.add(blob);
        }
        incomingBlobs.clear();

        for (Blob blob : allBlobs) {
            blob.timestep();
        }
        player.timestep();

        for (Blob blob : allBlobs) {
            blob.handleHardCollision(isBlock);
        }

        player.handleHardCollision(isBlock);

        for (Blob blob1 : allBlobs) {
            for (Blob blob2 : allBlobs) {
                blob1.handleBlobCollision(blob2);
            }
            player.handleBlobCollision(blob1);
        }

        List<Blob> deadBlobs = new ArrayList<>();
        for (Blob blob: allBlobs) {
            if (!blob.alive) {
                deadBlobs.add(blob);
            }
        }
        for (Blob blob : deadBlobs) {
            allBlobs.remove(blob);
        }
        if (!player.isAlive()) {
            gameState = GameState.DEFEAT;
        }
    }

    public void addBlobToQueue(int posX, int posY, char blobType) {
        incomingBlobs.add(new Blob(posX, posY, blobType, this));
    }

    void render(Graphics2D g) {
        for (Blob blob : allBlobs) {
            blob.render(g);
        }
        player.render(g);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isBlock[i][j]) {
                    g.setColor(Color.BLACK);
                    g.drawRect(j * TILE_SIZE, i * TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
                    g.setColor(Color.GREEN);
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
                }
            }
        }
    }

}
