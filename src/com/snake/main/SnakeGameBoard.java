package com.snake.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeGameBoard extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 600;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (DOT_SIZE * DOT_SIZE);

    // Speed control
    private final int INITIAL_DELAY = 200;   // Slow start
    private final int MIN_DELAY = 60;        // Fastest speed limit
    private int currentDelay;

    private final int xPosition[];
    private final int yPosition[];

    private int snakeSize;
    private int apple_x_pos;
    private int apple_y_pos;

    private boolean right = true;
    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
    private boolean gameON = true;

    private Timer gameTimer;
    private Image ballImg;
    private Image appleImg;
    private Image headImg;

    public SnakeGameBoard() {
        this.xPosition = new int[ALL_DOTS];
        this.yPosition = new int[ALL_DOTS];
        initializeSnakeBoard();
    }

    private void initializeSnakeBoard() {
        addKeyListener(new UserClickAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadAllImages();
        initializeGame();
    }

    private void loadAllImages() {
        ImageIcon ballIcon = new ImageIcon("src/resources/dot.png");
        ballImg = ballIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.jpg");
        appleImg = appleIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        headImg = headIcon.getImage();
    }

    private void initializeGame() {
        snakeSize = 3;
        for (int p = 0; p < snakeSize; p++) {
            xPosition[p] = 50 - p * 10;
            yPosition[p] = 50;
        }
        right = true; left = false; up = false; down = false;
        gameON = true;
        locateNewApple();

        currentDelay = INITIAL_DELAY;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(currentDelay, this);
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (gameON) {
            g.drawImage(appleImg, apple_x_pos, apple_y_pos, this);
            for (int p = 0; p < snakeSize; p++) {
                if (p == 0) {
                    g.drawImage(headImg, xPosition[p], yPosition[p], this);
                } else {
                    g.drawImage(ballImg, xPosition[p], yPosition[p], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over")) / 2, BOARD_HEIGHT / 2);

        // Restart text
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press R to Restart", (BOARD_WIDTH - 150) / 2, (BOARD_HEIGHT / 2) + 40);
    }

    private void checkApple() {
        if ((xPosition[0] == apple_x_pos) && (yPosition[0] == apple_y_pos)) {
            if (snakeSize < ALL_DOTS) {
                snakeSize++;
            }
            // Speed increase logic
            if (currentDelay > MIN_DELAY) {
                currentDelay -= 10; // every apple eaten -> speed up
                gameTimer.setDelay(currentDelay);
            }
            locateNewApple();
        }
    }

    private void moveSnake() {
        for (int p = snakeSize - 1; p > 0; p--) {
            if (p < ALL_DOTS) {
                xPosition[p] = xPosition[(p - 1)];
                yPosition[p] = yPosition[(p - 1)];
            }
        }
        if (left) xPosition[0] -= DOT_SIZE;
        if (right) xPosition[0] += DOT_SIZE;
        if (up) yPosition[0] -= DOT_SIZE;
        if (down) yPosition[0] += DOT_SIZE;
    }

    private void checkSnakeCollision() {
        for (int p = snakeSize - 1; p > 0; p--) {
            if ((p < ALL_DOTS) && (xPosition[0] == xPosition[p]) && (yPosition[0] == yPosition[p])) {
                gameON = false;
            }
        }
        if (yPosition[0] >= BOARD_HEIGHT || yPosition[0] < 0 ||
                xPosition[0] >= BOARD_WIDTH || xPosition[0] < 0) {
            gameON = false;
        }
        if (!gameON) {
            gameTimer.stop();
        }
    }

    private void locateNewApple() {
        int maxX = BOARD_WIDTH / DOT_SIZE;
        int maxY = BOARD_HEIGHT / DOT_SIZE;

        int randomX = (int) (Math.random() * maxX);
        int randomY = (int) (Math.random() * maxY);

        apple_x_pos = randomX * DOT_SIZE;
        apple_y_pos = randomY * DOT_SIZE;
    }

    private class UserClickAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }

            // Restart Game
            if (!gameON && key == KeyEvent.VK_R) {
                initializeGame();
                repaint();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameON) {
            checkApple();
            checkSnakeCollision();
            moveSnake();
        }
        repaint();
    }
}
