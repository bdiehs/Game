package com.company;

import java.awt.*;

public class Blob {

    private int posX;
    private int posY;
    private int height;
    private int width;
    char type;

    public Blob(int posX, int posY, char type) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.height = 1;
        this.width = 1;
    }

    void render(Graphics2D g) {
        switch (type) {
            case 'p': {
                g.setColor(Color.BLUE);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }

            default:
                System.err.println("Shouldn't happen");

        }
    }
}
