package codenames;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GridServer {
    private static JFrame frame;
    private static WordGrid wordGrid;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static JPanel gridPanel;
    private static JLabel clueLabel;
    private static ObjectInputStream inputStream;

    public static void main(String[] args) {
        wordGrid = new WordGrid();
        wordGrid.initializeGrid();
        wordGrid.assignColors();

        frame = new JFrame("Grid Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(5, 5));
        displayGrid();

        frame.add(gridPanel, BorderLayout.CENTER);

        clueLabel = new JLabel("Waiting for clue...");
        clueLabel.setFont(new Font(clueLabel.getFont().getName(), clueLabel.getFont().getStyle(), 20)); // set font size
        clueLabel.setHorizontalAlignment(JLabel.CENTER); // center the text
        frame.add(clueLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        setupServer();
        receiveClue();
    }

    private static void setupServer() {
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Waiting for client connection...");

            clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(wordGrid);
            
            inputStream = new ObjectInputStream(clientSocket.getInputStream());  // Add this line

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveClue() {
        new Thread(() -> {
            while (true) {  // Add this line
                try {
                    String clue = (String) inputStream.readObject();
                    int number = inputStream.readInt();

                    clueLabel.setText("The clue is: " + clue + " " + number);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static void displayGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton(wordGrid.getGridWords()[i][j]);
                button.setBackground(Color.WHITE);
                button.setOpaque(true); // Ensure the button's background is visible
                button.setBorder(new LineBorder(Color.BLACK, 1)); // Set a black border with 1px width
                button.addActionListener(new GridButtonListener(i, j));
                gridPanel.add(button);
            }
        }
    }


    static class GridButtonListener implements ActionListener {
        int row;
        int col;

        GridButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            Color cellColor = wordGrid.getGridColors()[row][col];
            source.setBackground(cellColor);
        }
    }
}