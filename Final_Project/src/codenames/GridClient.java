package codenames;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.io.*;
import java.net.*;

public class GridClient {
    private static JFrame frame;
    private static WordGrid wordGrid;
    private static Socket socket;
    private static JPanel gridPanel;

    public static void main(String[] args) {
        connectToServer();
        initializeGridFromServer();

        frame = new JFrame("Grid Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        gridPanel = new JPanel(new GridLayout(5, 5));
        displayGrid();

        frame.add(gridPanel);
        frame.setVisible(true);
    }

    private static void connectToServer() {
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Connected to server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeGridFromServer() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            wordGrid = (WordGrid) inputStream.readObject();

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    private static void displayGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton(wordGrid.getGridWords()[i][j]);
                button.setBackground(wordGrid.getGridColors()[i][j]);
                button.setOpaque(true); // Ensure the button's background is visible
                button.setBorder(new LineBorder(Color.BLACK, 1));
                gridPanel.add(button);
            }
        }
    }

    


}
