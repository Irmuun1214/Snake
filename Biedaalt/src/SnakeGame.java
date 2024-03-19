import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeGame {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        // Create the JFrame
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the frame is closed
        frame.setSize(boardWidth+25, boardHeight+75); // Set the size of the frame
        frame.setResizable(true); // Disable frame resizing
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Create the game panel
        GamePanel snakeGame = new GamePanel(boardWidth, boardHeight, 2);

        // Create a control panel with play and restart buttons
        JPanel controlPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton restartButton = new JButton("Restart");
        JButton singlePlayerButton = new JButton("1 Player");
        JButton twoPlayerButton = new JButton("2 Players");

        // Add action listeners to the buttons
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeGame.requestFocus();
                snakeGame.resumeGame();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeGame.restartGame();
                snakeGame.requestFocus();
            }
        });
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeGame.setNumPlayers(1);
                snakeGame.restartGame();
                snakeGame.requestFocus();
            }
        });
        twoPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeGame.setNumPlayers(2);
                snakeGame.restartGame();
                snakeGame.requestFocus();
            }
        });

        controlPanel.add(playButton);
        controlPanel.add(restartButton);
        controlPanel.add(singlePlayerButton);
        controlPanel.add(twoPlayerButton);

        // Add components to the frame
        frame.add(snakeGame, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true); // Make the frame visible
    }
}
