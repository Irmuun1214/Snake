import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }  

    int boardWidth;
    int boardHeight;
    int numPlayers;
    int tileSize = 25;
    
    // First snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Second snake
    Tile snakeHead2;
    ArrayList<Tile> snakeBody2;

    // Food
    Tile food;
    Random random;

    // Game logic
    int velocityX;
    int velocityY;

    // Second snake logic
    int velocityX2;
    int velocityY2;

    Timer gameLoop;

    boolean gameOver = false;
    boolean gamePaused = true;

    GamePanel(int boardWidth, int boardHeight, int numPlayers) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.numPlayers = numPlayers;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        snakeBody.add(new Tile(4, 5));
        snakeBody.add(new Tile(3, 5));
        if(numPlayers ==2) {
        	 snakeHead2 = new Tile(15, 15);
             snakeBody2 = new ArrayList<Tile>();
             snakeBody2.add(new Tile(16, 15));
             snakeBody2.add(new Tile(17, 15));
        }

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;

        velocityX2 = -1;
        velocityY2 = 0;
        
        // Game timer
        gameLoop = new Timer(100, this); // How long it takes to start timer, milliseconds between frames 
        gameLoop.start();
    }   
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid Lines
        for(int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }

        // Food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        // Snake 1 Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        // Snake 1 Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }
        
        if(numPlayers ==2) {
        // Snake 2 Head
        g.setColor(Color.blue);
        g.fill3DRect(snakeHead2.x*tileSize, snakeHead2.y*tileSize, tileSize, tileSize, true);

        // Snake 2 Body
        for (int i = 0; i < snakeBody2.size(); i++) {
            Tile snakePart = snakeBody2.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }
        }
        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(numPlayers == 2) {
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("P1 Over: " + String.valueOf(snakeBody.size()-2), tileSize - 16, tileSize);
            
            g.drawString("P2 Over: " + String.valueOf( snakeBody2.size()-2), tileSize +475, tileSize);
            
            if(snakeBody.size() > snakeBody2.size()) {
            	g.drawString("(Sumaar udirdah)P1 WIN: " + String.valueOf(snakeBody.size()-2), tileSize + 200, tileSize + 200);
            }else if(snakeBody.size() < snakeBody2.size()) {
            	g.drawString("(wasd udridah)P2 WIN: " + String.valueOf(snakeBody.size()-2), tileSize + 200, tileSize + 200);
            }else if(snakeBody.size() == snakeBody2.size()) {
            	g.drawString("DRAW: " , tileSize + 200, tileSize + 200);
            }
            
        } else {
        	g.setColor(Color.green);
        	g.drawString("(Sumaar udirdah)P1 Score: " + String.valueOf(snakeBody.size()-2), tileSize - 16, tileSize);
        	g.setColor(Color.blue);
            g.drawString("(wasd udridah)P2 Score: " + String.valueOf(snakeBody2.size()-2), tileSize +380, tileSize);
        }
        
        }else {
            if (gameOver) {
                g.setColor(Color.red);
                g.drawString("Game Over: " + String.valueOf(snakeBody.size()-2), tileSize - 16, tileSize);
            } else {
                g.drawString("Score: " + String.valueOf(snakeBody.size()-2), tileSize - 16, tileSize);
            }
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Move snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { 
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            // Collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            // Collide with snake head
            if (collision(snakeHead2, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) {
            gameOver = true;
        }
    }

    public void move2() {
        // Eat food for the second snake
        if (collision(snakeHead2, food)) {
            snakeBody2.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Move snake body for the second snake
        for (int i = snakeBody2.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody2.get(i);
            if (i == 0) {
                snakePart.x = snakeHead2.x;
                snakePart.y = snakeHead2.y;
            } else {
                Tile prevSnakePart = snakeBody2.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move snake head for the second snake
        snakeHead2.x += velocityX2;
        snakeHead2.y += velocityY2;

        // Game over conditions for the second snake
        for (int i = 0; i < snakeBody2.size(); i++) {
            Tile snakePart = snakeBody2.get(i);

            // Collide with snake head
            if (collision(snakeHead2, snakePart)) {
                gameOver = true;
            }
        }
        
        for (int i = 0; i < snakeBody2.size(); i++) {
            Tile snakePart = snakeBody2.get(i);

            // Collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        
        if (snakeHead2.x * tileSize < 0 || snakeHead2.x * tileSize > boardWidth || 
            snakeHead2.y * tileSize < 0 || snakeHead2.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gamePaused) {
            move();
            if(numPlayers ==2) {
            move2(); // Move the second snake if needed
            }
            repaint();
            if (gameOver) {
                gameLoop.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        // Controls for the second snake
        if (e.getKeyCode() == KeyEvent.VK_W && velocityY2 != 1) {
            velocityX2 = 0;
            velocityY2 = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY2 != -1) {
            velocityX2 = 0;
            velocityY2 = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX2 != 1) {
            velocityX2 = -1;
            velocityY2 = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX2 != -1) {
            velocityX2 = 1;
            velocityY2 = 0;
        }
    }

    // Not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Method to resume the game
    public void resumeGame() {
        gamePaused = false;
        gameLoop.start();
    }

    // Method to restart the game
    public void restartGame() {
    	snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        snakeBody.add(new Tile(4, 5));
        snakeBody.add(new Tile(3, 5));
        if(numPlayers ==2) {
        	 snakeHead2 = new Tile(15, 15);
             snakeBody2 = new ArrayList<Tile>();
             snakeBody2.add(new Tile(16, 15));
             snakeBody2.add(new Tile(17, 15));
        }
        placeFood();
        velocityX = 1;
        velocityY = 0;
        velocityX2 = -1;
        velocityY2 = 0;
        gameOver = false;
        gamePaused = true;
        repaint();
        gameLoop.start();
    }
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
