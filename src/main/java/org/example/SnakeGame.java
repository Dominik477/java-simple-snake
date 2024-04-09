package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;

    Timer gameLoop;
    int velocityX, velocityY;
    boolean gameOver = false;


    SnakeGame(int boardWith, int boardHeight) {

        this.boardWidth=boardWith;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(3, 3);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(7,7);
        random = new Random();
        placeFood();

        velocityX=0;
        velocityY=0;

        gameLoop = new Timer(100,this);
        gameLoop.start();

    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if(gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 48));
            g.setColor(Color.red);
            g.drawString("GAME OVER" , 150, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 200, 200);
        } else {
            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 532, 18);
        }

        //Food
        g.setColor(Color.orange);
        g.fill3DRect(food.getX() * tileSize, food.getY() * tileSize, tileSize, tileSize, true);

        //Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.getX() * tileSize, snakeHead.getY() * tileSize, tileSize, tileSize, true);

        //Snake Body
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.getX() * tileSize, snakePart.getY() * tileSize, tileSize, tileSize, true);
        }

    }

    public void placeFood() {
        food.setX(random.nextInt(boardWidth/tileSize));
        food.setY(random.nextInt(boardHeight/tileSize));
    }

    public void move() {

        for (Tile snakePart : snakeBody) {
            if (meeting(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if(snakeHead.getX()*tileSize < 0 || snakeHead.getX()*tileSize > boardWidth ||
                snakeHead.getY()*tileSize < 0 || snakeHead.getY()*tileSize > boardHeight) {
            gameOver=true;
        }

        if(meeting(snakeHead, food)) {
            snakeBody.add(new Tile(food.getX(),food.getY()));
            placeFood();
        }

        for(int i = snakeBody.size()-1; i>=0; i--) {
            Tile snakePart = snakeBody.get(i);
            if(i==0) {
                snakePart.setX(snakeHead.getX());
                snakePart.setY(snakeHead.getY());
            } else {
                Tile previousSnakePart = snakeBody.get(i-1);
                snakePart.setX(previousSnakePart.getX());
                snakePart.setY(previousSnakePart.getY());
            }
        }

        snakeHead.setX(snakeHead.getX()+velocityX);
        snakeHead.setY(snakeHead.getY()+velocityY);

    }

    public boolean meeting(Tile tile1, Tile tile2) {
        return tile1.getX() == tile2.getX() && tile1.getY() == tile2.getY();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //do not need them
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
