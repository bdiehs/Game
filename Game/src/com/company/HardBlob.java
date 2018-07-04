package com.company;

import java.awt.*;

public class HardBlob {
    Level.HardBlobType type;
    HardBlob(Level.HardBlobType type) {
        this.type = type;
    }

    void render(Graphics2D g, int posX, int posY) {
        switch (type) {
            case BLANK:
            {

            }
            case BRICK:
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, Level.TILE_SIZE, Level.TILE_SIZE);
            }
            case CASTLE:
            {
                g.setColor(Color.GRAY);
                g.drawRect(posX, posY, 2 * Level.TILE_SIZE, 2 * Level.TILE_SIZE);
            }
        }
    }
}
