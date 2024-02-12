package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class main extends Application {

    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    private Pane gameRoot;
    private Rectangle player;

    @Override
    public void start(Stage primaryStage) {
        gameRoot = new Pane();
        gameRoot.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        Scene scene = new Scene(gameRoot, SCREEN_WIDTH, SCREEN_HEIGHT);

        //setting background image
        
        //
        primaryStage.setScene(scene);
        primaryStage.setTitle("KULT");
        primaryStage.show();


        scene();
        createPlayer();
        createPlatforms();
        

        // Start the game loop
        startGameLoop();
    }

    private void createPlayer() {
        player = new Rectangle(50, 50, Color.BLUE);
        player.setTranslateX(10);
        player.setTranslateY(SCREEN_HEIGHT - 140);
        gameRoot.getChildren().add(player);
    }

    private void createPlatforms() {
        Image terrain = new Image("main/images/Tile_02.png");

        Rectangle mainPlatform = new Rectangle(1920, 150, Color.RED);
        mainPlatform.setFill(new ImagePattern(terrain, 100, 100, 100, 150, true));

        mainPlatform.setTranslateX(0);
        mainPlatform.setTranslateY(SCREEN_HEIGHT - 250);
     
        gameRoot.getChildren().add(mainPlatform);

        
        // Rectangle platform2 = new Rectangle(800, 40, Color.YELLOW);
        // platform2.setTranslateX(800);
        // platform2.setTranslateY(800);
   
        // gameRoot.getChildren().addAll(mainPlatform, platform2);




    }


    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update game logic here
            }
        };
        gameLoop.start();
    }

    private void scene(){
        Image backgroundImage = new Image("main/images/1.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        gameRoot.getChildren().add(backgroundImageView);

    }

    public static void main(String[] args) {
        launch(args);
    }
}

