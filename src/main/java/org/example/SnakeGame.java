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
        setBackground(Color.black);
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

        for(int i=0; i<boardWidth/tileSize;i++) {
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }

        //Food
        g.setColor(Color.red);
        g.fillRect(food.getX() * tileSize, food.getY() * tileSize, tileSize, tileSize);

        //Snake Head
        g.setColor(Color.green);
        g.fillRect(snakeHead.getX() * tileSize, snakeHead.getY() * tileSize, tileSize, tileSize);

        //Snake Body
        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.getX() * tileSize, snakePart.getY() * tileSize, tileSize, tileSize);
        }

    }

    public void placeFood() {
        food.setX(random.nextInt(boardWidth/tileSize));
        food.setY(random.nextInt(boardHeight/tileSize));
    }

    public void move() {

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

        for (Tile snakePart : snakeBody) {
            if (meeting(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if(snakeHead.getX()*tileSize < 0 || snakeHead.getX()*tileSize > boardWidth ||
                snakeHead.getY()*tileSize <0 || snakeHead.getY() > boardHeight) {
            gameOver=true;
        }



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
