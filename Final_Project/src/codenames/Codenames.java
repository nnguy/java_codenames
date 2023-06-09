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

public class Codenames {
    private static JFrame frame;
    private static WordGrid wordGrid;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static JPanel gridPanel;
    private static JLabel clueLabel;
    private static JLabel remainingWordsLabel; // New label to display remaining words
    private static int redRemaining; // Counter for remaining red words
    private static int blueRemaining; // Counter for remaining blue words
    private static ObjectInputStream inputStream;
    private static JLabel turnLabel;
    private static JButton passButton;
    private static String currentTurn;
    private static int clueNumber; // Store the last received clue number
    private static int consecutiveClicks; // Store the number of consecutive clicks of the current team's color


    //new main method 
    public static void main(String[] args) {
        frame = new JFrame("Codenames - Teams");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        startMenu();
    }
    //startmenu method 
    private static void startMenu() {
    	Dimension preferredSize = new Dimension(100, 40);
    	
        JLabel welcomeLabel = new JLabel("Welcome to Codenames! Please select an option.");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), welcomeLabel.getFont().getStyle(), 20)); // set font size
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER); // center the text
        
        JLabel messageLabel = new JLabel("After choosing one of these options, run Spymaster.java to begin the game!", SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), messageLabel.getFont().getStyle(), 20)); // set font size

        JButton uploadButton = new JButton("Custom words! (Upload my own words - each word should be in a separate line)");
        uploadButton.setPreferredSize(preferredSize);
        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    ArrayList<String> words = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        words.add(line);

                    }
                    if(words.size() < 25) {
                        // Show an error message
                        JOptionPane.showMessageDialog(frame, "The file must contain at least 25 words!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        wordGrid = new WordGrid(words.toArray(new String[0]));
                        messageLabel.setVisible(true);
                        startGame(words.toArray(new String[0]));
                        //startGame();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        JButton randomButton = new JButton("Any words! (Get words from a hardcoded dictionary of over 500 words)");
        randomButton.setPreferredSize(preferredSize);
        randomButton.addActionListener(e -> {
            wordGrid = new WordGrid();
            messageLabel.setVisible(true);
            startGame();
        });

        JPanel menuPanel = new JPanel(new GridLayout(4, 1));
        menuPanel.add(welcomeLabel);
        menuPanel.add(uploadButton);
        menuPanel.add(randomButton);
        menuPanel.add(messageLabel);

        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    //startgame method for using built-in words 

    private static void startGame() {
        frame.getContentPane().removeAll();
        frame.repaint();
        

        JLabel messageLabel = new JLabel("Run Spymaster.java to begin the game!", SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), messageLabel.getFont().getStyle(), 20)); // set font size
        
        
        frame.add(messageLabel);
        frame.validate(); // This will make the label appear immediately
        frame.repaint(); // Update the frame
        
        wordGrid.initializeGrid();
        wordGrid.assignColors();

        redRemaining = 0;
        blueRemaining = 0;
        clueNumber = 0;
        consecutiveClicks = 0;
        
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Color color = wordGrid.getGridColors()[i][j];
                if (color.equals(new Color(255, 171, 171, 255))) {
                    redRemaining++;
                } else if (color.equals(new Color(171, 171, 255, 255))) {
                    blueRemaining++;
                }
            }
        }
        
        //removing the new Jframe because I think it is preventing the messageLabel from appearing
        //frame = new JFrame("Codenames");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(5, 5));
        displayGrid();

        frame.add(gridPanel, BorderLayout.CENTER);
        
        remainingWordsLabel = new JLabel("Red Words Remaining: " + redRemaining + "     Blue Words Remaining: " + blueRemaining);
        remainingWordsLabel.setFont(new Font(remainingWordsLabel.getFont().getName(), remainingWordsLabel.getFont().getStyle(), 20)); // set font size
        remainingWordsLabel.setHorizontalAlignment(JLabel.CENTER); // center the text
        frame.add(remainingWordsLabel, BorderLayout.NORTH); // add the label to the frame
        

        turnLabel = new JLabel("");
        turnLabel.setFont(new Font(turnLabel.getFont().getName(), turnLabel.getFont().getStyle(), 20)); // set font size
        turnLabel.setHorizontalAlignment(JLabel.CENTER); // center the text

        passButton = new JButton("Pass");
        passButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchTurn();
            }
        });

        JPanel turnPanel = new JPanel();
        turnPanel.add(turnLabel);
        turnPanel.add(passButton);
        
        //create a new panel to hold both labels 
        JPanel northPanel = new JPanel(new GridLayout(2, 1)); // GridLayout to place components vertically
        northPanel.add(remainingWordsLabel);
        northPanel.add(turnPanel);


        frame.add(northPanel, BorderLayout.NORTH);

        int redCount = getRedCount();
        int blueCount = getBlueCount();
        currentTurn = redCount > blueCount ? "Red" : "Blue";
        updateTurnLabel();

        
        clueLabel = new JLabel("Waiting for clue...");
        clueLabel.setFont(new Font(clueLabel.getFont().getName(), clueLabel.getFont().getStyle(), 20)); // set font size
        clueLabel.setHorizontalAlignment(JLabel.CENTER); // center the text
        frame.add(clueLabel, BorderLayout.SOUTH);

        frame.setVisible(true);

        setupServer();
        receiveClue();
    }
    
    //overload startGame with words array 
    private static void startGame(String [] words) {
        frame.getContentPane().removeAll();
        frame.repaint();
        

        JLabel messageLabel = new JLabel("Run Spymaster.java to begin the game!", SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), messageLabel.getFont().getStyle(), 20)); // set font size
        
        
        frame.add(messageLabel);
        frame.validate(); // This will make the label appear immediately
        frame.repaint(); // Update the frame
        
        wordGrid.initializeGrid(words);
        wordGrid.assignColors();

        redRemaining = 0;
        blueRemaining = 0;
        clueNumber = 0;
        consecutiveClicks = 0;
        
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Color color = wordGrid.getGridColors()[i][j];
                if (color.equals(new Color(255, 171, 171, 255))) {
                    redRemaining++;
                } else if (color.equals(new Color(171, 171, 255, 255))) {
                    blueRemaining++;
                }
            }
        }
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(5, 5));
        displayGrid();

        frame.add(gridPanel, BorderLayout.CENTER);
        
        remainingWordsLabel = new JLabel("Red Words Remaining: " + redRemaining + "     Blue Words Remaining: " + blueRemaining);
        remainingWordsLabel.setFont(new Font(remainingWordsLabel.getFont().getName(), remainingWordsLabel.getFont().getStyle(), 20)); 
        remainingWordsLabel.setHorizontalAlignment(JLabel.CENTER); // center the text
        frame.add(remainingWordsLabel, BorderLayout.NORTH); // add the label to the frame
        

        turnLabel = new JLabel("");
        turnLabel.setFont(new Font(turnLabel.getFont().getName(), turnLabel.getFont().getStyle(), 20)); 
        turnLabel.setHorizontalAlignment(JLabel.CENTER); // center the text

        passButton = new JButton("Pass");
        passButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchTurn();
            }
        });

        JPanel turnPanel = new JPanel();
        turnPanel.add(turnLabel);
        turnPanel.add(passButton);
        
        //create a new panel to hold both labels 
        JPanel northPanel = new JPanel(new GridLayout(2, 1)); // GridLayout to place components vertically
        northPanel.add(remainingWordsLabel);
        northPanel.add(turnPanel);


        frame.add(northPanel, BorderLayout.NORTH);

        int redCount = getRedCount();
        int blueCount = getBlueCount();
        currentTurn = redCount > blueCount ? "Red" : "Blue";
        updateTurnLabel();

        
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
            
            inputStream = new ObjectInputStream(clientSocket.getInputStream());  

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveClue() {
        new Thread(() -> {
            while (true) {  
                try {
                    String clue = (String) inputStream.readObject();
                    
                    
                    clueNumber = inputStream.readInt();
                    
                    
                    clueLabel.setText("The clue is: " + clue + " " + clueNumber); 
                } catch (IOException | ClassNotFoundException e) {
                    //e.printStackTrace();
                	//the IO exception occurs when the GridClient closes 
                	System.exit(0);
                	
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
    
    private static void updateTurnLabel() {
        turnLabel.setText("It is " + currentTurn + "'s turn");
    }

    private static void switchTurn() {
        if ("Red".equals(currentTurn)) {
            currentTurn = "Blue";
        } else {
            currentTurn = "Red";
        }

        updateTurnLabel();
    }

    private static int getRedCount() {
        int count = 0;
        for (Color[] row : wordGrid.getGridColors()) {
            for (Color color : row) {
                if (color.equals(new Color(255, 171, 171, 255))) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int getBlueCount() {
        int count = 0;
        for (Color[] row : wordGrid.getGridColors()) {
            for (Color color : row) {
                if (color.equals(new Color(171, 171, 255, 255))) {
                    count++;
                }
            }
        }
        return count;
    }


    
    //new GridButonListener class that contains turn switching and win conditions 
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
            source.setEnabled(false); //make the button unclickable 
            
            boolean switchTurn = false;
            
            if (cellColor.equals(new Color(255, 171, 171, 255))) {
                redRemaining--;
                if ("Red".equals(currentTurn)) {
                    consecutiveClicks++;
                } else {
                    switchTurn = true;
                }
            } else if (cellColor.equals(new Color(171, 171, 255, 255))) {
                blueRemaining--;
                if ("Blue".equals(currentTurn)) {
                    consecutiveClicks++;
                } else {
                    switchTurn = true;
                }
            } else if (cellColor.equals(new Color(171, 171, 171, 255))) { // Dark gray color
                if ("Red".equals(currentTurn)) {
                    turnLabel.setText("Blue wins! Red revealed the assassin");
                } else {
                    turnLabel.setText("Red wins! Blue revealed the assassin");
                }
                endGame();
                return;
            } else { 
                switchTurn = true;
            }
            remainingWordsLabel.setText("Red Words Remaining: " + redRemaining + "     Blue Words Remaining: " + blueRemaining);

            if (consecutiveClicks == clueNumber + 1 || switchTurn) {
                switchTurn();
                consecutiveClicks = 0; // reset the consecutive clicks
            }

            if (redRemaining == 0) {
                turnLabel.setText("Red wins! All of the Red codewords have been found");
                endGame();
            } else if (blueRemaining == 0) {
                turnLabel.setText("Blue wins! All of the Blue codewords have been found");
                endGame();
            }
        }
      
    }
    
    private static void endGame() {
        Component[] components = gridPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(false);
            }
        }
        passButton.setEnabled(false);
    }
        
}
