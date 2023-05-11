package codenames;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(5, 5));
        displayGrid();

        frame.add(gridPanel, BorderLayout.CENTER);

        JPanel formPanel = createForm();
        frame.add(formPanel, BorderLayout.SOUTH);

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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void displayGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton(wordGrid.getGridWords()[i][j]);
                button.setBackground(wordGrid.getGridColors()[i][j]);
                button.setOpaque(true);
                button.setBorder(new LineBorder(Color.BLACK, 1));
                gridPanel.add(button);
            }
        }
    }

    private static JPanel createForm() {
        JPanel formPanel = new JPanel();
        JTextField clueTextField = new JTextField(10);
        JComboBox<Integer> numberComboBox = new JComboBox<>();
        for (int i = 0; i <= 9; i++) {
            numberComboBox.addItem(i);
        }
        JButton submitButton = new JButton("Submit");
        submitButton.setEnabled(false);

        clueTextField.getDocument().addDocumentListener(new ButtonEnableDocumentListener(clueTextField, numberComboBox, submitButton));
        numberComboBox.addActionListener(new ButtonEnableActionListener(clueTextField, numberComboBox, submitButton));

        submitButton.addActionListener(e -> {
            String clue = clueTextField.getText().trim();
            int number = (Integer) numberComboBox.getSelectedItem();

            sendClue(clue, number);

            clueTextField.setText("");
            numberComboBox.setSelectedIndex(0);
        });

        formPanel.add(clueTextField);
        formPanel.add(numberComboBox);
        formPanel.add(submitButton);
        return formPanel;
    }

    private static void sendClue(String clue, int number) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(clue);
            outputStream.writeInt(number);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class ButtonEnableDocumentListener implements DocumentListener {
        private JTextField textField;
        private JComboBox<Integer> comboBox;
        private JButton button;

        public ButtonEnableDocumentListener(JTextField textField, JComboBox<Integer> comboBox, JButton button) {
            this.textField = textField;
            this.comboBox = comboBox;
            this.button = button;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            enableButton();
        }

        private void enableButton() {
            button.setEnabled(!textField.getText().trim().isEmpty() && (Integer) comboBox.getSelectedItem() > 0);
        }
    }

    static class ButtonEnableActionListener implements ActionListener {
        private JTextField textField;
        private JComboBox<Integer> comboBox;
        private JButton button;

        public ButtonEnableActionListener(JTextField textField, JComboBox<Integer> comboBox, JButton button) {
            this.textField = textField;
            this.comboBox = comboBox;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.setEnabled(!textField.getText().trim().isEmpty() && (Integer) comboBox.getSelectedItem() > 0);
        }
    }
}
