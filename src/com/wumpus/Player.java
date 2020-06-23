package com.wumpus;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Player {
    private Location loc;
    private Image playerEast;
    private Image playerWest;
    private Image playerNorth;
    private Image playerSouth;
    private Image groundImage;
    private Cave cave;

    public Player(Cave cave, int row, int col){
        this.cave = cave;
        loc = new Location(row,col);
        playerEast = new Image(Player.class.getResourceAsStream("/com/wumpus/images/east.png"));
        playerWest = new Image(Player.class.getResourceAsStream("/com/wumpus/images/west.png"));
        playerNorth = new Image(Player.class.getResourceAsStream("/com/wumpus/images/north.png"));
        playerSouth = new Image(Player.class.getResourceAsStream("/com/wumpus/images/south.png"));
        groundImage = new Image(Player.class.getResourceAsStream("/com/wumpus/images/groundTile.png"));
    }

    public void moveRight(){
        loc.setCol(loc.getCol()+1);
    }

    public void moveLeft(){
        loc.setCol(loc.getCol()-1);
    }

    public void moveUp(){
        loc.setRow(loc.getRow()-1);
    }
    public void moveDown(){
        loc.setRow(loc.getRow()+1);
    }

    private void drawNorth(GraphicsContext gc){
        gc.drawImage(groundImage , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
        gc.drawImage(playerNorth , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
    }

    private void drawSouth(GraphicsContext gc){
        gc.drawImage(groundImage , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
        gc.drawImage(playerSouth, loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
    }
    private void drawEast(GraphicsContext gc){
        gc.drawImage(groundImage , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
        gc.drawImage(playerEast, loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
    }
    private void drawWest(GraphicsContext gc){
        gc.drawImage(groundImage , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
        gc.drawImage(playerWest , loc.getCol()*50+Cave.xOffset , loc.getRow()*50+Cave.yOffset);
    }
    public void draw(GraphicsContext gc, String direction){
        if(direction.equals("EAST")){
            drawEast(gc);
        }
        else if(direction.equals("WEST")){
            drawWest(gc);
        }
        else if(direction.equals("NORTH")){
            drawNorth(gc);
        }
        else if(direction.equals("SOUTH")){
            drawSouth(gc);
        }
    }






    public Location getLoc() {
        return loc;
    }
}
