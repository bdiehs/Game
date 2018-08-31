package com.company;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPanel extends JPanel implements ActionListener {

    private static final int DELAY = 50;
    private static final int NUM_LEVELS = 2;
    private static List<String> allMaps;
    private int levelIndex;

    private Level level;
    private Timer timer;

    public MyPanel() {
        levelIndex = 0;
        allMaps = new ArrayList<>();
        for (int i = 0; i < NUM_LEVELS; i++) {
            allMaps.add(String.format("src/com/company/maps/map%d.txt", i));
        }
        level = new Level(allMaps.get(levelIndex));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                level.handleKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                level.handleKeyReleased(e);
            }
        });

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        level.render(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        level.timestep();
        if (level.getGameState() == Level.GameState.DEFEAT) {
            level = new Level(allMaps.get(levelIndex));
            System.out.println("==================\nNew Level");
        }
        else if (level.getGameState() == Level.GameState.VICTORY) {
            levelIndex += 1;
            level = new Level(allMaps.get(levelIndex));
        }
        repaint();
    }

}
