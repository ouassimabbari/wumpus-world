package com.wumpus;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Cave {
    public static int[][] getTiles() {
        return tiles;
    }

    private static int tiles [][];

    private boolean visible [][];

    public static final int xOffset = 20;
    public static final int yOffset = 20;

    public static final int GROUND=0, PIT=1, SPIDER=2, WUMPUS=3, GOLD=4,
                            WIND=10, WEB=20, STINK=30, GLITTER=40 , SUP=50;
    private Image groundImage , blackImage , glitterImage ,goldImage, pitImage, spiderImage,
                    stinkImage, webImage , windImage , wumpusImage, darkImage;

    ArrayList<Location> locPit = new ArrayList<Location>();
    ArrayList<Location> locWumpus = new ArrayList<Location>();
    ArrayList<Location> locSpider = new ArrayList<Location>();
    ArrayList<Location> locGold = new ArrayList<Location>();


    public Cave(int row, int col) {
        tiles = new int[row][col];
        visible = new boolean[row][col];
        visible[row-2][0] = true;
        visible[row-1][1] = true;

        groundImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/groundTile.png"));
        blackImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/stinkTile.png"));
        glitterImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/glitterTile.png"));
        goldImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/goldTile.png"));
        pitImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/pitTile.png"));
        spiderImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/spiderTile.png"));
        stinkImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/windTile.png"));
        webImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/webTile.png"));
        windImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/stinkTile.png"));
        wumpusImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/wumpusTile.png"));
        darkImage = new Image(Cave.class.getResourceAsStream("/com/wumpus/images/blackImage.png"));


    }

    public void draw(GraphicsContext gc){
        for(int row=0; row<tiles.length ; row++){
            for(int col=0; col<tiles[0] .length;col++){
                if (!(visible[row][col])){
                    gc.drawImage(darkImage , xOffset+(col*50),yOffset+(row*50));
                }else {
                    if(tiles[row][col]== GROUND){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== SPIDER){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(spiderImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== GOLD){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(goldImage, xOffset+(col*50),yOffset+(row*50));

                    }
                    else if(tiles[row][col]== WUMPUS){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(wumpusImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== PIT){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(pitImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== WEB){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(webImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== GLITTER){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(glitterImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== STINK){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(stinkImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    else if(tiles[row][col]== WIND){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                        gc.drawImage(windImage, xOffset+(col*50),yOffset+(row*50));
                    }
                    if(tiles[row][col]== SUP){
                        gc.drawImage(groundImage , xOffset+(col*50),yOffset+(row*50));
                    }
                }
            }
        }
    }

    public Image getGroundImage() {
        return groundImage;
    }

    public Image getGoldImage() {
        return goldImage;
    }

    public Image getPitImage() {
        return pitImage;
    }

    public Image getSpiderImage() {
        return spiderImage;
    }

    public Image getWumpusImage() {
        return wumpusImage;
    }

    public ArrayList<Location> getLocSpider(){
        return locSpider;
    }
    public ArrayList<Location> getLocWumpus(){
        return locWumpus;
    }
    public ArrayList<Location> getLocPit(){
        return locPit;
    }
    public ArrayList<Location> getLocGold(){
        return locGold;
    }

    public void setTile(Location loc , int tileID){
        if(isValid(loc)){
            tiles[loc.getRow()][loc.getCol()] = tileID;
            updateTileHints(tileID , loc.getRow(),loc.getCol());
        }
    }
    private void updateTileHints(int tileId, int row, int col){
        Location above = new Location(row-1,col);
        Location below = new Location(row+1,col);
        Location left = new Location(row, col-1);
        Location right = new Location(row,col+1);

        if(isValid(above) && !(tiles[above.getRow()][above.getCol()] > 0 && tiles[above.getRow()][above.getCol()] < 5)){
            tiles[above.getRow()][above.getCol()] = tileId*10;
        }
        if(isValid(below) && !(tiles[below.getRow()][below.getCol()] > 0 && tiles[below.getRow()][below.getCol()] < 5)){
            tiles[below.getRow()][below.getCol()] = tileId*10;
        }
        if(isValid(left) && !(tiles[left.getRow()][left.getCol()] > 0 && tiles[left.getRow()][left.getCol()] < 5)){
            tiles[left.getRow()][left.getCol()] = tileId*10;
        }
        if(isValid(right) && !(tiles[right.getRow()][right.getCol()] > 0 && tiles[right.getRow()][right.getCol()] < 5)){
            tiles[right.getRow()][right.getCol()] = tileId*10;
        }
    }

    public boolean isValid(Location loc){
        return loc.getRow() >=0 && loc.getRow() < tiles.length &&
                 loc.getCol() >= 0 && loc.getCol() < tiles[loc.getRow()].length;
    }

    public boolean isValidRow(int row){
        return row >=0 && row < tiles.length;
    }

    public boolean isValidCol(int col, int row){
        if (isValidRow(row)){
            return col >= 0 && col < tiles[row].length;
        }else {
            return false;
        }
    }

    public void assignVisible(Location loc){
        if (isValidCol(loc.getCol(), loc.getRow()+1) && isValidRow(loc.getRow()+1)){
            visible[loc.getRow()+1][loc.getCol()] = true;
        }
        if (isValidCol(loc.getCol(), loc.getRow()-1) && isValidRow(loc.getRow()-1)){
            visible[loc.getRow()-1][loc.getCol()] = true;
        }
        if (isValidCol(loc.getCol()+1, loc.getRow()) && isValidRow(loc.getRow())){
            visible[loc.getRow()][loc.getCol()+1] = true;
        }
        if (isValidCol(loc.getCol()-1, loc.getRow()) && isValidRow(loc.getRow())){
            visible[loc.getRow()][loc.getCol()-1] = true;
        }
    }

    public void drawRandom(int row , int col){

        boolean gold = false;
        boolean wumpus = false;
        boolean pit = false;
        boolean spider = false;

        while(!gold){
            int randomGoldCol;
            int randomGoldRow = (int)(Math.random() * ((row-1) + 1));
            if (randomGoldRow == row - 2){
                randomGoldCol =(int) (Math.random() * ((col-1 - 1) + 1)) + 1;
            }else if (randomGoldRow == row - 1){
                randomGoldCol =(int) (Math.random() * ((col - 1 - 2) + 1)) + 2;
            }else {
                randomGoldCol =(int) (Math.random() * ((col-1) + 1));
            }
            Location locGold1 = new Location(randomGoldRow,randomGoldCol);
            if(     isValid(locGold1) && tiles[locGold1.getRow()][locGold1.getCol()]!=WUMPUS && tiles[locGold1.getRow()][locGold1.getCol()]!=PIT
                    && tiles[locGold1.getRow()][locGold1.getCol()]!=SPIDER
            ){
                setTile(locGold1,4);
                locGold1 = new Location(xOffset+(randomGoldCol*50),yOffset+(randomGoldRow*50));
                locGold.add(locGold1);
                gold=true;
            }

        }

        while(!wumpus){
            int randomWumpusCol;
            int randomWumpusRow = (int)(Math.random() * ((row-1) + 1));
            if (randomWumpusRow == row - 2){
                randomWumpusCol =(int) (Math.random() * ((col-1 - 1) + 1)) + 1;
            }else if (randomWumpusRow == row - 1){
                randomWumpusCol =(int) (Math.random() * ((col - 1 - 2) + 1)) + 2;
            }else {
                randomWumpusCol =(int) (Math.random() * ((col-1) + 1));
            }
            Location locWumpus1 = new Location(randomWumpusRow,randomWumpusCol);
            if( isValid(locWumpus1) && tiles[locWumpus1.getRow()][locWumpus1.getCol()]!=GOLD && tiles[locWumpus1.getRow()][locWumpus1.getCol()]!=PIT
                    &&tiles[locWumpus1.getRow()][locWumpus1.getCol()]!=SPIDER
            )
            {
                setTile(locWumpus1,3);
                locWumpus1 = new Location(xOffset+(randomWumpusCol*50),yOffset+(randomWumpusRow*50));
                locWumpus.add(locWumpus1);
                wumpus=true;
            }

        }
        int randomPits = (int) (Math.random() * ((row - 1) + 1)) + 1;
        System.out.println(randomPits);
        while(!pit){
            for(int i = 0 ; i < randomPits ; i++){
                int randomPitCol;
                int randomPitRow = (int)(Math.random() * ((row-1) + 1));
                if (randomPitRow == row - 2){
                    randomPitCol =(int) (Math.random() * ((col-1 - 1) + 1)) + 1;
                }else if (randomPitRow == row - 1){
                    randomPitCol =(int) (Math.random() * ((col - 1 - 2) + 1)) + 2;
                }else {
                    randomPitCol =(int) (Math.random() * ((col-1) + 1));
                }
                Location locPit1 = new Location(randomPitRow,randomPitCol);
                if( isValid(locPit1) && tiles[locPit1.getRow()][locPit1.getCol()]!=SPIDER && tiles[locPit1.getRow()][locPit1.getCol()]!=WUMPUS
                        && tiles[locPit1.getRow()][locPit1.getCol()]!=GOLD
                )
                {
                    setTile(locPit1,1);
                    locPit1 = new Location(xOffset+(randomPitCol*50),yOffset+(randomPitRow*50));
                    locPit.add(locPit1);
                }else {
                    i--;
                }
            }
            pit = true;
        }

        if(col>5 && row>5){
            while(!spider){
                int randomSpiderCol;
                int randomSpiderRow = (int)(Math.random() * ((row-1) + 1));
                if (randomSpiderRow == row - 2){
                    randomSpiderCol =(int) (Math.random() * ((col-1 - 1) + 1)) + 1;
                }else if (randomSpiderRow == row - 1){
                    randomSpiderCol =(int) (Math.random() * ((col - 1 - 2) + 1)) + 2;
                }else {
                    randomSpiderCol =(int) (Math.random() * ((col-1) + 1));
                }
                Location locSpider1 = new Location(randomSpiderRow,randomSpiderCol);
                if( isValid(locSpider1) && tiles[locSpider1.getRow()][locSpider1.getCol()]!=GOLD && tiles[locSpider1.getRow()][locSpider1.getCol()]!=WUMPUS
                        && tiles[locSpider1.getRow()][locSpider1.getCol()]!=PIT
                )
                {
                    setTile(locSpider1,2);
                    locSpider1 = new Location(xOffset+(randomSpiderCol*50),yOffset+(randomSpiderRow*50));
                    locSpider.add(locSpider1);
                    spider=true;
                }

            }
        }


    }


}
