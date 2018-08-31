package com.company;

import java.awt.*;

public class Blob {

    protected Level level;

    protected static int ID = 0;
    protected static final int MAX_VERTICAL_SPEED = 30;
    private static final int BEE_SPEED = 12;
    private static final int MUSHROOM_SPEED = 6;
    private static final int FIREBALL_SPEED = 20;

    protected final int id;
    protected int posX;
    protected int posY;
    protected int velX;
    protected int velY;
    protected boolean alive;
    protected boolean hasGravity;
    protected int width;
    protected int height;
    protected char type;

    public Blob(int posX, int posY, char type, Level level) {
        this.id = ID;
        ID++;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.alive = true;
        this.level = level;
        switch (type) {
            case 'p':
            {
                velX = 0;
                velY = 0;
                width = 1;
                height = 2;
                hasGravity = true;
                break;
            }
            case 'm':
            {
                velX = MUSHROOM_SPEED;
                velY = 0;
                width = 1;
                height = 1;
                hasGravity = true;
                break;
            }
            case 'e':
            {
                velX = 0;
                velY = BEE_SPEED;
                width = 1;
                height = 1;
                hasGravity = false;
                break;
            }
            case 'c':
            {
                velX = 0;
                velY = 0;
                width = 2;
                height = 2;
                hasGravity = false;
                break;
            }
            case 'f':
            {
                velX = FIREBALL_SPEED;
                if (level.playerFacing() == Player.Facing.LEFT) {
                    velX *= -1;
                }
                velY = 0;
                width = 1;
                height = 1;
                hasGravity = true;
                break;
            }
            default:
            {
                throw new RuntimeException("Type " + type + " is not an allowed blob type");
            }

        }

    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public boolean isAlive() {
        return alive;
    }

    void timestep() {

        if (hasGravity) {
            velY += Level.GRAVITY;
        }
        if (Math.abs(velY) > MAX_VERTICAL_SPEED) {
            if (velY > 0) {
                velY = MAX_VERTICAL_SPEED;
            }
            else {
                velY = -MAX_VERTICAL_SPEED;
            }
        }
        posX += velX;
        posY += velY;
    }

    void handleHardCollision(Boolean[][] isBlock) {
        int row = posY / Level.TILE_SIZE;
        int col = posX / Level.TILE_SIZE;

        int lastPosX = posX;
        int lastPosY = posY;

        // check if two in the opposite corners
        if (isBlock[row + height][col] && isBlock[row][col + width]) {
            if (posX  - col * Level.TILE_SIZE > Level.TILE_SIZE / 2) {
                posX = (col + 1) * Level.TILE_SIZE;
                posY = (row + 1) * Level.TILE_SIZE;
            }
            else {
                posX = col * Level.TILE_SIZE;
                posY = row * Level.TILE_SIZE;
            }
        }
        else if (isBlock[row][col] && isBlock[row + height][col + width]) {
            if (posX  - col * Level.TILE_SIZE > Level.TILE_SIZE / 2) {
                posX = (col + 1) * Level.TILE_SIZE;
                posY = row * Level.TILE_SIZE;
            }
            else {
                posX = col * Level.TILE_SIZE;
                posY = (row + 1) * Level.TILE_SIZE;
            }
        }
        else {
            // count sides
            for (int i = 1; i < width; i++) {
                if (isBlock[row][col + i]) {
                    posY = (row + 1) * Level.TILE_SIZE;
                    break;
                }
                if (isBlock[row + height][col + i]) {
                    posY = row * Level.TILE_SIZE;
                    break;
                }
            }
            for (int i = 1; i < height; i++) {
                if (isBlock[row + i][col]) {
                    posX = (col + 1) * Level.TILE_SIZE;
                    break;
                }
                if (isBlock[row + i][col + width]) {
                    posX = col * Level.TILE_SIZE;
                    break;
                }
            }
            // check if there are two corners on one side
            if (isBlock[row][col] && isBlock[row][col + width]) {
                posY = (row + 1) * Level.TILE_SIZE;
            }
            else if (isBlock[row][col + width] && isBlock[row + height][col + width]) {
                posX = col * Level.TILE_SIZE;
            }
            else if (isBlock[row + height][col + width] && isBlock[row + height][col]) {
                posY = row * Level.TILE_SIZE;
            }
            else if (isBlock[row + height][col] && isBlock[row][col]) {
                posX = (col + 1) * Level.TILE_SIZE;
            }
            else {
                //only one or no corner
                if (isBlock[row][col]) {
                    posY = (row + 1) * Level.TILE_SIZE;
                }
                else if (isBlock[row][col + width]) {
                    if (posX > col * Level.TILE_SIZE) {
                        posY = (row + 1) * Level.TILE_SIZE;
                    }
                }
                else if (isBlock[row + height][col + width]) {
                    if (posX > col * Level.TILE_SIZE) {
                        posY = row * Level.TILE_SIZE;
                    }
                }
                else if (isBlock[row + height][col]) {
                    posY = row * Level.TILE_SIZE;
                }
                else {

                }
            }
        }

        switch (type) {
            case 'm':
            {
                if (posX != lastPosX) {
                    velX *= -1;
                }
                if (posY != lastPosY) {
                    velY = 0;
                }
                break;
            }
            case 'e':
            {
                if (posY != lastPosY) {
                    velY *= -1;
                }
                break;
            }
            case 'f':
            {
                if (posY != lastPosY) {
                    velY = 0;
                    if (posX != lastPosX) {
                        die();
                    }
                }
                break;
            }
        }

    }

    public void handleBlobCollision(Blob blob) {
        if (id == blob.id) {
            return;
        }
        Rectangle r1 = new Rectangle(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
        Rectangle r2 = new Rectangle(blob.posX, blob.posY, blob.width * Level.TILE_SIZE, blob.height * Level.TILE_SIZE);
        if (r1.intersects(r2)) {
            if (type == 'm' && blob.type == 'm') {
                posX = blob.posX + ((posX > blob.posX) ? Level.TILE_SIZE : -Level.TILE_SIZE);
                if (posY == blob.posY) {
                    velX = -velX;
                    blob.velX = -blob.velX;
                } else if (posY > blob.posY) {
                    velY = 0;
                    posY = blob.posY + Level.TILE_SIZE;
                } else {
                    blob.velY = 0;
                    blob.posY = posY + Level.TILE_SIZE;
                }
            } else if (type == 'f' && blob.type == 'm') {
                die();
                blob.die();
            } else if (type == 'f' && blob.type == 'e') {
                die();
                blob.die();
            }
        }
    }

    public void die() {
        alive = false;
    }

    void render(Graphics2D g) {
        switch (type) {
            case 'p':
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                g.setColor(Color.BLUE);
                g.fillRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }
            case 'm':
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                g.setColor(Color.ORANGE);
                g.fillRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }
            case 'e':
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                g.setColor(Color.YELLOW);
                g.fillRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }
            case 'c':
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                g.setColor(Color.GRAY);
                g.fillRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }
            case 'f':
            {
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                g.setColor(Color.RED);
                g.fillRect(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
                break;
            }
            default:
            {
                throw new RuntimeException(String.format("Type %c is not an allowed blob type", type));
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Blob of type %c at position (%d, %d) and velocity (%d, %d)", type, posX, posY, velX, velY);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Blob && ((Blob) other).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
