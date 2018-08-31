package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here


        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.getContentPane().add(new MyPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280,800);
            frame.setVisible(true);
        });

    }
}
