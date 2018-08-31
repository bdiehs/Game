package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Blob {

    enum Facing {LEFT, RIGHT}

    private static final int DRAG = 2;
    private static final int MAX_HORIZONTAL_SPEED = 15;
    private static final int HORIZONTAL_ACCELERATION = 6;
    private static final int JUMP_SPEED = 30;
    private static final int JUMP_DURATION = 3;
    private static final int BOUNCE_SPEED = 34;
    private static final int HELICOPTER_DURATION = 100;
    private static final int BORED_THRESHOLD = 60;
    private static final int SUN_POWER = 500;

    private int jumpState;
    private int numFireballs;
    private Facing facing;

    private boolean upEvent;
    private boolean rightEvent;
    private boolean leftEvent;
    private boolean fireEvent;

    Player(int posX, int posY, char type, Level level) {
        super(posX, posY, type, level);
        jumpState = -1;
        numFireballs = SUN_POWER;
        upEvent = false;
        rightEvent = false;
        leftEvent = false;
        fireEvent = false;
        facing = Facing.RIGHT;
    }

    public Facing facing() {
        return facing;
    }

    void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            {
                upEvent = true;
                break;
            }
            case KeyEvent.VK_LEFT:
            {
                leftEvent = true;
                break;
            }
            case KeyEvent.VK_RIGHT:
            {
                rightEvent = true;
                break;
            }
            case KeyEvent.VK_DOWN:
            {
                if (height == 2) {
                    posY += Level.TILE_SIZE;
                }
                height = 1;
                break;
            }
        }
    }

    void handleKeyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            {
                upEvent = false;
                break;
            }
            case KeyEvent.VK_LEFT:
            {
                leftEvent = false;
                break;
            }
            case KeyEvent.VK_RIGHT:
            {
                rightEvent = false;
                break;
            }
            case KeyEvent.VK_X:
            {
                fireEvent = true;
                break;
            }
            case KeyEvent.VK_DOWN:
            {
                if (height == 1) {
                    posY -= Level.TILE_SIZE;
                }
                height = 2;
                break;
            }
        }
    }

    @Override
    void timestep() {

        if (upEvent) {
            if (jumpState == -1) {
                jumpState = 0;
                velY -= JUMP_SPEED;
            }
        }
        if (leftEvent) {
            facing = Facing.LEFT;
            velX -= HORIZONTAL_ACCELERATION;
        }
        if (rightEvent) {
            facing = Facing.RIGHT;
            velX += HORIZONTAL_ACCELERATION;
        }
        if (fireEvent) {
            if (numFireballs >= 1) {
                level.addBlobToQueue(posX, posY, 'f');
                fireEvent = false;
                numFireballs -= 1;
            }
        }

        if (jumpState >= 0 && jumpState < JUMP_DURATION) {
            jumpState += 1;
        }


        if (Math.abs(velX) > MAX_HORIZONTAL_SPEED) {
            if (velX > 0) {
                velX = MAX_HORIZONTAL_SPEED;
            }
            else {
                velX = -MAX_HORIZONTAL_SPEED;
            }
        }

        if (velX > 0) {
            velX -= Math.min(velX, DRAG);
        }
        else {
            velX -= Math.max(velX, -DRAG);
        }

        super.timestep();
    }

    @Override
    void handleHardCollision(Boolean[][] isBlock) {
        int lastPosX = posX;
        int lastPosY = posY;
        super.handleHardCollision(isBlock);
        if (posX != lastPosX) {
            velX = 0;
        }
        if (posY != lastPosY) {
            velY = 0;
            if (posY < lastPosY) {
                jumpState = -1;
            }
        }
        // System.out.println(String.format("player went from (%d, %d) to (%d, %d)", lastPosX, lastPosY, posX, posY));
    }


    public void handleBlobCollision(Blob blob) {
        Rectangle r1 = new Rectangle(posX, posY, width * Level.TILE_SIZE, height * Level.TILE_SIZE);
        Rectangle r2 = new Rectangle(blob.posX, blob.posY, blob.width * Level.TILE_SIZE, blob.height * Level.TILE_SIZE);
        if (r1.intersects(r2)) {
            if (blob.type == 'm') {
                System.out.println(this + "\n" + blob);
                if (velY > 0) {
                    blob.die();
                    velY -= BOUNCE_SPEED;
                    posY = blob.posY - height * Level.TILE_SIZE;
                } else {
                    die();
                }
            }
            if (blob.type == 'c') {
                level.setGameState(Level.GameState.VICTORY);
            }
            if (blob.type == 'e') {
                die();
            }
        }
    }
}
