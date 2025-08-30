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
    private final int ALL_DOTS = 900;
    private final int DELAY = 140;

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

    public SnakeGameBoard(){
        this.xPosition = new int[ALL_DOTS];
        this.yPosition = new int[ALL_DOTS];
        initializeSnakeBoard();
    }

    private void initializeSnakeBoard(){
        addKeyListener(new UserClickAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH,BOARD_HEIGHT));
        loadAllImages();
        System.out.println("Ball image loaded? " + (ballImg != null));
        System.out.println("Head image loaded? " + (headImg != null));
        System.out.println("Apple image loaded? " + (appleImg != null));
        initializeGame();

    }
    private void loadAllImages(){
        ImageIcon ballIcon = new ImageIcon("src/resources/dot.png");
        ballImg = ballIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.jpg");
        appleImg = appleIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        headImg = headIcon.getImage();

    }
    private void initializeGame(){
        snakeSize = 3;
        for(int p = 0; p < snakeSize; p++){
            xPosition[p] = 50-p*10;
            yPosition[p] = 50;
        }
        locateNewApple();
        gameTimer = new Timer(DELAY,this);
        gameTimer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        if(gameON){
            g.drawImage(appleImg, apple_x_pos, apple_y_pos,this);
            for(int p = 0; p < snakeSize; p++){
                if(p == 0){
                    g.drawImage(headImg, xPosition[p], yPosition[p],this);
                }else{
                    g.drawImage(ballImg, xPosition[p], yPosition[p], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }

    }
    private void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("TimeRoman", Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_WIDTH-metrics.stringWidth("Game Over"))/2, BOARD_HEIGHT/2);

    }
    private void checkApple(){
        if((xPosition[0] == apple_x_pos) && (yPosition[0] == apple_y_pos)){
            if (snakeSize < ALL_DOTS - 1) {   // 👈 ensure max size not crossed
                snakeSize++;
            }
            locateNewApple();
        }

    }
    private void moveSnake(){
        for(int p = snakeSize; p>0; p--){
            xPosition[p] = xPosition[(p-1)];
            yPosition[p] = yPosition[(p-1)];
        }
        if(left){
            xPosition[0] -= DOT_SIZE;
        }
        if(right){
            xPosition[0] += DOT_SIZE;
        }
        if(up){
            yPosition[0] -= DOT_SIZE;
        }
        if(down){
            yPosition[0] += DOT_SIZE;
        }

    }
    private void checkSnakeCollision(){
        for(int p = snakeSize; p > 0; p++){
            if((p > 4) && (xPosition[0] == xPosition[p]) && (yPosition[0] == yPosition[p])){
                gameON = false;
            }
        }
        if(yPosition[0] >= BOARD_HEIGHT){
            gameON = false;
        }
        if(yPosition[0] < 0){
            gameON = false;
        }
        if(xPosition[0] >= BOARD_WIDTH){
            gameON = false;
        }
        if(xPosition[0] < 0){
            gameON = false;
        }
        if(!gameON){
            gameTimer.stop();
        }

    }
    private void locateNewApple(){
        int random = (int) (Math.random() * 29);
        apple_x_pos = ((random * DOT_SIZE));
        random = (int) (Math.random() * 29);
        apple_y_pos = ((random * DOT_SIZE));

    }
    private class UserClickAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if((key == KeyEvent.VK_LEFT) && (!right)){
                left = true;
                up = false;
                down = false;
            }
            if((key == KeyEvent.VK_RIGHT) && (!left)){
                right = true;
                up = false;
                down = false;
            }
            if((key == KeyEvent.VK_UP) && (!down)){
                up = true;
                right = false;
                left = false;
            }
            if((key == KeyEvent.VK_DOWN) && (!up)){
                down = true;
                right = false;
                left = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if(gameON){
                checkApple();
                checkSnakeCollision();
                moveSnake();
            }
            repaint();
    }
}
