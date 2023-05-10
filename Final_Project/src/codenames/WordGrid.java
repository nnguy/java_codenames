package codenames;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WordGrid implements Serializable{
    private static final String[] WORDS = {
            "apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "nectarine",
            "orange", "papaya", "pear", "plum", "quince", "raspberry", "strawberry", "tangerine", "ugli", "vanilla",
            "watermelon", "xigua", "yuzu", "zinfandel", "jujube"
    };
    private ArrayList<String> words;
    private Color[][] gridColors;
    private String[][] gridWords;

    public WordGrid() {
    	/*
    	 * The wordGrid serves as the grid that will be used in the client and in the server
    	 * */
        words = new ArrayList<>();
        gridColors = new Color[5][5];
        gridWords = new String[5][5];
    }

    public void initializeGrid() {
        ArrayList<String> wordList = new ArrayList<>();
        Collections.addAll(wordList, WORDS);
        Collections.shuffle(wordList);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridWords[i][j] = wordList.get(i * 5 + j);
            }
        }
    }

    public void assignColors() {
        ArrayList<Color> colors = new ArrayList<>();
        Random random = new Random();
        int redCount = random.nextBoolean() ? 8 : 7;
        int blueCount = 15 - redCount;

        for (int i = 0; i < redCount; i++) {
            colors.add(new Color(255, 171, 171, 255)); // Red with 50% opacity
        }
        for (int i = 0; i < blueCount; i++) {
            colors.add(new Color(171, 171, 255, 255)); // Blue with 50% opacity
        }

        for (int i = 0; i < 9; i++) {
            colors.add(new Color(245, 245, 220, 255)); // Beige color with 50% opacity
        }
        colors.add(new Color(171, 171, 171, 255)); // Dark Gray with 50% opacity

        Collections.shuffle(colors);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridColors[i][j] = colors.get(i * 5 + j);
            }
        }
    }



    public Color[][] getGridColors() {
        return gridColors;
    }

    public String[][] getGridWords() {
        return gridWords;
    }
}
