package com.wumpus;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Arrays;


public class Main extends Application {

    private Image keyUp;
    private Image keyDown;
    private Image keyLeft;
    private Image keyRight;
    private Image keyG;
    private Image keyT;
    private Image keyR;
    private Image keyQ;
    Cave cave = new Cave(10, 10);
    private int mouseX , mouseY;
    private int currentlySelectedTile = -1;
    Player player = new Player(cave, 9, 0);
    ArrayList<String> input = new ArrayList<String>();
    private boolean removePlayer = false;
    private long score=0;
    private int arrow = 1;
    private boolean killWumpus = false;
    private String direction="EAST";
    private int rowMatrice;
    private int colMatrice;
    private boolean isPerso = false;
    private GridPane lastGrid;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Wumpus World");
        Group root = new Group();
        Scene scene = new Scene(root);

        keyUp = new Image(Player.class.getResourceAsStream("/com/wumpus/images/keyUp.png"));
        keyDown = new Image(Player.class.getResourceAsStream("/com/wumpus/images/keyDown.png"));
        keyLeft = new Image(Player.class.getResourceAsStream("/com/wumpus/images/keyLeft.png"));
        keyRight = new Image(Player.class.getResourceAsStream("/com/wumpus/images/keyRight.png"));
        keyG = new Image(Player.class.getResourceAsStream("/com/wumpus/images/G.png"));
        keyT = new Image(Player.class.getResourceAsStream("/com/wumpus/images/T.png"));
        keyR = new Image(Player.class.getResourceAsStream("/com/wumpus/images/R.png"));
        keyQ = new Image(Player.class.getResourceAsStream("/com/wumpus/images/Q.png"));

        Canvas canvas = new Canvas(800,600);
        root.getChildren().add(canvas);
       final GraphicsContext gc = canvas.getGraphicsContext2D();

        lastGrid = new GridPane();
        lastGrid.setStyle("-fx-background-color: #F0E68C;");
        lastGrid.setPadding(new Insets(10,10,10,10));
        lastGrid.setVgap(25);
        lastGrid.setHgap(130);

        Label labelInvite = new Label("Voulez vous refaire une partie ?");
        labelInvite.setFont(new Font("Arial", 15));
        GridPane.setConstraints(labelInvite, 1,3);

        Button startNew = new Button("Nouvelle partie");
        GridPane.setConstraints(startNew, 1,4);

        Button quit = new Button("Quitter");
        GridPane.setConstraints(quit, 1,5);

        lastGrid.getChildren().addAll(labelInvite, startNew, quit);

        Scene lastScene = new Scene(lastGrid, 800,400);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #F0E68C;");
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(25);
        grid.setHgap(130);

        Label labelPerso = new Label("Partie personnalisée");
        labelPerso.setFont(new Font("Arial", 16));
        GridPane.setConstraints(labelPerso, 1,2);
        RadioButton buttonPerso = new RadioButton();
        GridPane.setConstraints(buttonPerso, 2,2);
        Label labelRandom = new Label("Partie Aléatoire");
        labelRandom.setFont(new Font("Arial", 16));
        GridPane.setConstraints(labelRandom, 1,3);
        RadioButton buttonRandom = new RadioButton();
        GridPane.setConstraints(buttonRandom, 2,3);
        Label labelRowMatrice = new Label("Nombre de lignes de la matrice : ");
        labelRowMatrice.setFont(new Font("Arial", 16));
        GridPane.setConstraints(labelRowMatrice, 1,4);
        Label labelColMatrice = new Label("Nombre de colonnes de la matrice : ");
        labelColMatrice.setFont(new Font("Arial", 16));
        GridPane.setConstraints(labelColMatrice, 1,5);
        TextField textColMatrice = new TextField();
        GridPane.setConstraints(textColMatrice, 2,5);
        TextField textRowMatrice = new TextField();
        GridPane.setConstraints(textRowMatrice, 2,4);
        Button start = new Button("Lancer la partie");
        GridPane.setConstraints(start, 2,6);

        ToggleGroup toggleGroup = new ToggleGroup();

        buttonPerso.setToggleGroup(toggleGroup);
        buttonRandom.setSelected(true);
        buttonRandom.setToggleGroup(toggleGroup);

        grid.getChildren().addAll(labelPerso,labelColMatrice,labelRandom,labelRowMatrice, buttonPerso, buttonRandom, start, textColMatrice, textRowMatrice);
        
        Scene indexScene = new Scene(grid, 800,400);
        primaryStage.setScene(indexScene);

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textRowMatrice.getText().trim().isEmpty() || textColMatrice.getText().trim().isEmpty()){
                    removeNodeByRowColumnIndex(1,1,grid);
                    Label labelErreur = new Label("Veuillez specifier la taille de la matrice");
                    labelErreur.setTextFill(Color.web("#CD5C5C"));
                    labelErreur.setFont(new Font("Arial", 13));
                    GridPane.setConstraints(labelErreur, 1,1);
                    grid.getChildren().add(labelErreur);
                }else {
                    rowMatrice = Integer.parseInt(textRowMatrice.getText());
                    colMatrice = Integer.parseInt(textColMatrice.getText());
                    if(rowMatrice > 8 || colMatrice > 8){
                        removeNodeByRowColumnIndex(1,1,grid);
                        Label labelErreur = new Label("La matrice ne doit pas depasser 8 lignes et 8 colonnes");
                        labelErreur.setTextFill(Color.web("#CD5C5C"));
                        labelErreur.setFont(new Font("Arial", 13));
                        GridPane.setConstraints(labelErreur, 1,1);
                        grid.getChildren().add(labelErreur);
                    }else if (rowMatrice < 4 || colMatrice < 4){
                        removeNodeByRowColumnIndex(1,1,grid);
                        Label labelErreur = new Label("La matrice ne doit pas être au dessous de 4 lignes et 4 colonnes");
                        labelErreur.setTextFill(Color.web("#CD5C5C"));
                        labelErreur.setFont(new Font("Arial", 13));
                        GridPane.setConstraints(labelErreur, 1,1);
                        grid.getChildren().add(labelErreur);
                    }else {
                        if (buttonRandom.isSelected()){
                            isPerso = false;
                            if (rowMatrice != colMatrice){
                                removeNodeByRowColumnIndex(1,1,grid);
                                Label labelErreur = new Label("Le nombre de lignes doit être egal au nombre de colonnes pour les parties aléatoires");
                                labelErreur.setTextFill(Color.web("#CD5C5C"));
                                labelErreur.setFont(new Font("Arial", 9));
                                GridPane.setConstraints(labelErreur, 1,1);
                                grid.getChildren().add(labelErreur);
                            }else {
                                cave = new Cave(rowMatrice, colMatrice);
                                cave.drawRandom(rowMatrice, colMatrice);
                                player = new Player(cave, rowMatrice-1, 0);
                                primaryStage.setScene(scene);
                            }
                        }else {
                            isPerso = true;
                            cave = new Cave(rowMatrice, colMatrice);
                            player = new Player(cave, rowMatrice-1, 0);
                            primaryStage.setScene(scene);
                        }

                    }
                }
            }
        });


        startNew.setOnAction(new EventHandler<ActionEvent>() {
                              @Override
                              public void handle(ActionEvent event) {
                                  removeNodeByRowColumnIndex(1,1,grid);
                                  direction = "EAST";
                                  killWumpus = false;
                                  removePlayer = false;
                                  score = 0;
                                  arrow = 1;
                                  primaryStage.setScene(indexScene);
                              }
                          });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });


       scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
           @Override
           public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                if(!input.contains(code)){
                    if(!(player.getLoc().getRow()==0 && code.equals("UP")) && !(player.getLoc().getCol()==colMatrice-1 && code.equals("RIGHT"))
                    && !(player.getLoc().getRow()==rowMatrice-1 && code.equals("DOWN")) && !(player.getLoc().getCol()==0 && code.equals("LEFT")))
                    {
                        input.add(code);
                    }else{
                        Media sound = new Media(getClass().getResource("/com/wumpus/media/wall.mp3").toExternalForm());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    }
                }
               if(code.equals("G") && player.getLoc().getRow() == rowMatrice-1 && player.getLoc().getCol() == 0 ){
                   removePlayer = true;
               }
               if(code.equals("Q")){
                   removePlayer = true;
               }
               if (code.equals("T") && !killWumpus){
                   for (Location loc: cave.getLocWumpus()){
                       Location location = new Location(loc.getRow(), loc.getCol());
                       loc= convertClickToLocation(loc.getRow(),loc.getCol());
                       if(loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()+1 && direction =="SOUTH" || loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()-1 && direction =="NORTH" || loc.getCol() == player.getLoc().getCol()+1 && loc.getRow() == player.getLoc().getRow() && direction =="EAST" || loc.getCol() == player.getLoc().getCol()-1 && loc.getRow() == player.getLoc().getRow() && direction =="WEST"){
                           int[][] tiles = Cave.getTiles();
                           if (tiles[loc.getRow()][loc.getCol()]== Cave.WUMPUS){
                               for (int i =0; i < cave.getLocWumpus().size(); i++){
                                   if (cave.getLocWumpus().get(i).getRow() == location.getRow() && cave.getLocWumpus().get(i).getCol() == location.getCol()){
                                       cave.getLocWumpus().remove(i);
                                       break;
                                   }
                               }
                               cave.setTile(loc, Cave.GROUND);
                               killWumpus = true;
                               arrow = 0;
                               score = score -10;

                           }
                           Media sound = new Media(getClass().getResource("/com/wumpus/media/wumpusScream.wav").toExternalForm());
                           MediaPlayer mediaPlayer = new MediaPlayer(sound);
                           mediaPlayer.play();
                           break;
                       }
                   }
               }
              if(code.equals("R")){
                   for(Location loc : cave.getLocGold()){
                       loc= convertClickToLocation(loc.getRow(),loc.getCol());
                       int[][] tiles = Cave.getTiles();
                       if (tiles[loc.getRow()][loc.getCol()]== Cave.GOLD){
                           cave.setTile(loc, Cave.GROUND);
                           score = score + 1000;

                           removeNodeByRowColumnIndex(1,1,lastGrid);

                           Label labelSuccess = new Label("Felicitations!! Vous avez gagner la partie!!");
                           labelSuccess.setTextFill(Color.web("#1E8449"));
                           labelSuccess.setFont(new Font("Arial", 23));
                           GridPane.setConstraints(labelSuccess, 1,1);

                           removeNodeByRowColumnIndex(2,1,lastGrid);

                           Label labelScore = new Label("Votre score est : " + score);
                           labelScore.setFont(new Font("Arial", 15));
                           GridPane.setConstraints(labelScore, 1,2);

                           lastGrid.getChildren().addAll(labelScore, labelSuccess);
                           primaryStage.setScene(lastScene);
                       }
                   }
               }
           }
       });

            scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (isPerso){
                        mouseX = (int) event.getX();
                        mouseY = (int) event.getY();

                        if(mouseX >= 650 && mouseX <= 700 && mouseY >= 80 && mouseY < 130){
                            currentlySelectedTile = Cave.PIT;
                        }
                        else if(mouseX >= 650 && mouseX <= 700 && mouseY >= 130 && mouseY < 180){
                            currentlySelectedTile = Cave.SPIDER;
                        }
                        else if(mouseX >= 650 && mouseX <= 700 && mouseY >= 180 && mouseY < 230){
                            currentlySelectedTile = Cave.WUMPUS;
                        }
                        else if(mouseX >= 650 && mouseX <= 700 && mouseY >= 230 && mouseY < 280){
                            currentlySelectedTile = Cave.GOLD;
                        }
                        else if(mouseX >= 650 && mouseX <= 700 && mouseY >= 280 && mouseY < 330){
                            currentlySelectedTile = Cave.GROUND;
                        }
                        if(currentlySelectedTile != -1) {
                            Location clickLoc = convertClickToLocation(mouseX,mouseY);
                            System.out.println(clickLoc);
                            if(cave.isValid(clickLoc)){
                                cave.setTile(clickLoc , currentlySelectedTile);
                                switch (currentlySelectedTile){
                                    case 1 :
                                        Location pitLocation = new Location(cave.xOffset+(clickLoc.getCol()*50),cave.yOffset+(clickLoc.getRow()*50));
                                        if (cave.locPit.isEmpty()){
                                            cave.locPit.add(pitLocation);
                                        }else {
                                            boolean locationExists = false;
                                            for (Location location : cave.locPit){
                                                if ((location.getRow() == pitLocation.getRow() && location.getCol() == pitLocation.getCol())){
                                                    locationExists = true;
                                                    break;
                                                }
                                            }
                                            if (!locationExists){
                                                cave.locPit.add(pitLocation);
                                            }
                                        }
                                        break;
                                    case 2 :
                                        Location spiderLocation = new Location(cave.xOffset+(clickLoc.getCol()*50),cave.yOffset+(clickLoc.getRow()*50));
                                        if (cave.locSpider.isEmpty()){
                                            cave.locSpider.add(spiderLocation);
                                        }else {
                                            boolean locationExists = false;
                                            for (Location location : cave.locSpider){
                                                if ((location.getRow() == spiderLocation.getRow() && location.getCol() == spiderLocation.getCol())){
                                                    locationExists = true;
                                                    break;
                                                }
                                            }
                                            if (!locationExists){
                                                cave.locSpider.add(spiderLocation);
                                            }
                                        }
                                        break;
                                    case 3 :
                                        Location wumpusLocation = new Location(cave.xOffset+(clickLoc.getCol()*50),cave.yOffset+(clickLoc.getRow()*50));
                                        if (cave.locWumpus.isEmpty()){
                                            cave.locWumpus.add(wumpusLocation);
                                        }else {
                                            boolean locationExists = false;
                                            for (Location location : cave.locWumpus){
                                                if ((location.getRow() == wumpusLocation.getRow() && location.getCol() == wumpusLocation.getCol())){
                                                    locationExists = true;
                                                    break;
                                                }
                                            }
                                            if (!locationExists){
                                                cave.locWumpus.add(wumpusLocation);
                                            }
                                        }
                                        break;
                                    case 4 :
                                        Location goldLocation = new Location(cave.xOffset+(clickLoc.getCol()*50),cave.yOffset+(clickLoc.getRow()*50));
                                        if (cave.locGold.isEmpty()){
                                            cave.locGold.add(goldLocation);
                                        }else {
                                            boolean locationExists = false;
                                            for (Location location : cave.locGold){
                                                if ((location.getRow() == goldLocation.getRow() && location.getCol() == goldLocation.getCol())){
                                                    locationExists = true;
                                                    break;
                                                }
                                            }
                                            if (!locationExists){
                                                cave.locGold.add(goldLocation);
                                            }
                                        }
                                        break;
                                }
                                currentlySelectedTile=-1;
                            }
                        }
                    }
                }
            });

            scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mouseX = (int) event.getX();
                    mouseY = (int) event.getY();
                }
            });

       new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.KHAKI);
                gc.fillRect(0,0,800,600);

                cave.draw(gc);
                drawToolbar(gc);
                if(!removePlayer){
                    processInput();
                    player.draw(gc, direction);
                }else {
                    removeNodeByRowColumnIndex(1,1,lastGrid);

                    Label labelSuccess = new Label("Pas de chance !!");
                    labelSuccess.setTextFill(Color.web("#EC7063"));
                    labelSuccess.setFont(new Font("Arial", 23));
                    GridPane.setConstraints(labelSuccess, 1,1);

                    removeNodeByRowColumnIndex(2,1,lastGrid);

                    Label labelScore = new Label("Votre score est : " + score);
                    labelScore.setFont(new Font("Arial", 15));
                    GridPane.setConstraints(labelScore, 1,2);

                    lastGrid.getChildren().addAll(labelScore, labelSuccess);
                    primaryStage.setScene(lastScene);
                }



            }
        }.start();


        primaryStage.show();

    }

    private void removeNodeByRowColumnIndex(final int row,final int column,GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for(Node node : children) {
            if(node instanceof Label && gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
    }

    public void processInput(){
        for (int i=0; i<input.size(); i++){
            if(input.get(i).equals("RIGHT")){
                player.moveRight();
                cave.assignVisible(player.getLoc());
                direction = "EAST";
                score = score -1;
                checkCollision();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("LEFT")){
                player.moveLeft();
                cave.assignVisible(player.getLoc());
                direction = "WEST";
                score = score -1;
                checkCollision();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("UP")){
                player.moveUp();
                cave.assignVisible(player.getLoc());
                direction = "NORTH";
                score = score -1;
                checkCollision();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("DOWN")){
                player.moveDown();
                cave.assignVisible(player.getLoc());
                direction = "SOUTH";
                score = score -1;
                checkCollision();
                input.remove(i);
                i--;
            }
            else {
                input.remove(i);
                i--;
            }
        }
    }

    private void checkCollision(){
        for (Location loc: cave.getLocSpider()){
            Location location = new Location(loc.getRow(), loc.getCol());
            loc= convertClickToLocation(loc.getRow(),loc.getCol());
            if(loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()){
                int[][] tiles = Cave.getTiles();
                if (tiles[loc.getRow()][loc.getCol()]== Cave.SPIDER){
                    for (int i =0; i < cave.getLocSpider().size(); i++){
                        if (cave.getLocSpider().get(i).getRow() == location.getRow() && cave.getLocSpider().get(i).getCol() == location.getCol()){
                            cave.getLocSpider().remove(i);
                            cave.setTile(player.getLoc(), Cave.GROUND);
                            score = score - 500;
                            break;
                        }
                    }
                }
                break;
            }
        }
        for (Location loc : cave.getLocWumpus()){
            loc = convertClickToLocation(loc.getRow(),loc.getCol());
            if(loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()){
                removePlayer = true;
                break;
            }
        }
        for (Location loc : cave.getLocPit()){
            loc = convertClickToLocation(loc.getRow(),loc.getCol());
            if(loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()){
                removePlayer = true;
                score = score - 1000;
                break;
            }
        }
        for (Location loc : cave.getLocGold()){
            loc = convertClickToLocation(loc.getRow(),loc.getCol());
            if(loc.getCol() == player.getLoc().getCol() && loc.getRow() == player.getLoc().getRow()){

            }
        }
    }



    public void drawToolbar(GraphicsContext gc){
        Font myFont = new Font(53);
        gc.setFont(myFont.font("Verdana", FontWeight.BOLD, 25));
        gc.setFill(Color.BLACK);
        gc.fillText("Score : "+score , 630,480);
        gc.fillText("Arrow : "+arrow, 630,520);
        gc.setFill(Color.RED);
        gc.fillText(direction,680,560);
        gc.setFill(Color.BLACK);
        gc.fillText(player.getLoc().toString(), 550,590);
        gc.fillText("Commands :", 10,480);
        gc.drawImage(keyUp,40,490);
        gc.drawImage(keyLeft,10,520);
        gc.drawImage(keyDown,40,520);
        gc.drawImage(keyRight,70,520);
        gc.setFont(myFont.font("Verdana", 25));
        gc.fillText(" : move", 105,540);
        gc.drawImage(keyG,220,520);
        gc.fillText(" : grimper", 255,540);
        gc.drawImage(keyT,400,520);
        gc.fillText(" : tirer", 440,540);
        gc.drawImage(keyR,10,570);
        gc.fillText(" : ramasser", 50,590);
        gc.drawImage(keyQ,220,570);
        gc.fillText(" : quitter", 260,590);

        if (isPerso){
            gc.fillText("Toolbar", 650,60);
            gc.drawImage(cave.getPitImage(),650,80);
            gc.drawImage(cave.getSpiderImage(),650,130);
            gc.drawImage(cave.getWumpusImage(),650,180);
            gc.drawImage(cave.getGoldImage(),640,230);
            gc.drawImage(cave.getGroundImage(),650,280);
            if(currentlySelectedTile == Cave.PIT){
                gc.drawImage(cave.getPitImage(),mouseX-25,mouseY-25 );
            }
            else if(currentlySelectedTile == Cave.SPIDER){
                gc.drawImage(cave.getSpiderImage(),mouseX-25,mouseY-25 );
            }
            else if(currentlySelectedTile == Cave.WUMPUS){
                gc.drawImage(cave.getWumpusImage(),mouseX-25,mouseY-25 );
            }
            else if(currentlySelectedTile == Cave.GOLD){
                gc.drawImage(cave.getGoldImage(),mouseX-25,mouseY-25 );
            }
            else if(currentlySelectedTile == Cave.GROUND){
                gc.drawImage(cave.getGroundImage(),mouseX-25,mouseY-25 );
            }
        }
    }



    public Location convertClickToLocation(int x,int y){
        int row =(y-Cave.xOffset)/50;
        int col =(x-Cave.yOffset)/50;

        Location loc = new Location(row,col);

        return loc;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
