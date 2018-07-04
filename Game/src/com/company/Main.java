package com.company;

import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Graphics2D g;
        Level level = new Level("maps/map1.txt");
        level.render(g);
    }
}
