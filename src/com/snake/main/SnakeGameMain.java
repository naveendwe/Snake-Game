package com.snake.main;

import javax.swing.*;
import java.awt.*;

public class SnakeGameMain extends JFrame {

    public SnakeGameMain() {   // 👈 constructor add karo
        initializeUI();
    }

    private void initializeUI(){
        add(new SnakeGameBoard());
        setResizable(false);
        pack();
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
          JFrame snakeFrame = new SnakeGameMain();
          snakeFrame.setVisible(true);
        });
    }
}
