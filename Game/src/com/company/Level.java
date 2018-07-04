package com.company;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Level {

    static final int TILE_SIZE = 32;
    static final int CASTLE_SIZE = 2;

    public enum HardBlobType {
        BLANK,
        BRICK,
        CASTLE
    }

    private final int width;
    private final int height;
    private List<Blob> allBlobs;
    private Player player;
    private HardBlob[][] hardBlobs;

    Level(String filename) throws IOException {
        allBlobs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String dims = reader.readLine();
        width = Integer.parseInt(dims.split("_")[0]);
        height = Integer.parseInt(dims.split("_")[1]);
        hardBlobs = new HardBlob[height][width];
        for (int i = 0; i < height; i++) {
            String line = reader.readLine();
            for (int j = 0; j < width; j++) {
                char blobType = line.charAt(j);
                if (blobType == '_') {
                    hardBlobs[i][j] = new HardBlob(HardBlobType.BLANK);
                }
                else if (blobType == '=') {
                    hardBlobs[i][j] = new HardBlob(HardBlobType.BRICK);
                }
                else if (blobType == 'c') {
                    for (int ii = 0; ii < CASTLE_SIZE; ii++) {
                        for (int jj = 0; jj < CASTLE_SIZE; jj++) {
                            hardBlobs[i+ii][j+jj] = new HardBlob(HardBlobType.CASTLE);
                        }
                    }
                }
                else if (blobType == 'p') {
                    player = new Player(j * TILE_SIZE, i * TILE_SIZE, blobType);
                }
                else {
                    allBlobs.add(new Blob(j * TILE_SIZE, i * TILE_SIZE, blobType));
                }
            }
        }
    }

    void timestep() {

    }

    void render(Graphics2D g) {
        for (Blob blob : allBlobs) {
            blob.render(g);
        }
        player.render(g);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                hardBlobs[i][j].render(g, j * TILE_SIZE, i * TILE_SIZE);
            }
        }
    }

}
