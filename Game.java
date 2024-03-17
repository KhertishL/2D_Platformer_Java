package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import java.util.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Game extends Application {

    
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<>();
    private ArrayList<Node> pickups = new ArrayList<>();
    private ArrayList<Node> game_enemies = new ArrayList<>();
    private ArrayList<Node> doors = new ArrayList<>();
    
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int levelWidth;
    private int point = 200;
    private Label score;
    private Stage primaryStage;
    
    
    
    private void initContent(){
    	
    	Random random = new Random(); 	//random object
        String[] selectedLevel;			//variable taking value string from LevelData
        
        int levelChoice = random.nextInt(3) ; // Generates a random number between 0 - 2
        
        switch (levelChoice) {
            case 0:
                selectedLevel = LevelData.LEVEL1;
                break;
            case 1:
                selectedLevel = LevelData.LEVEL2;
                break;
            case 2:
                selectedLevel = LevelData.LEVEL3;
                break;
            default:
                selectedLevel = LevelData.LEVEL1; // Default to LEVEL1 if something goes wrong
                break;
        }
   
    	Image backgroundImage = new Image(getClass().getResource("/application/bg2.png").toExternalForm()); //relative path

        // Create the rectangle with the image as its background
        Rectangle bg = new Rectangle(1280, 720);
        bg.setFill(new ImagePattern(backgroundImage));
        
        		
        levelWidth = selectedLevel[0].length() * 60;			// selected level = level chosen from LevelData class 
        
        score=new Label();
        score.setText(String.format("HP: %d", point));
        score.setTextFill(Color.BLACK);
        score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 40));
        uiRoot.getChildren().add(score);
        
        for (int i = 0; i < selectedLevel.length; i++) {		// changed old variable from Level1.length to selectedlevel.length
            String line = selectedLevel[i];
            for (int j = 0; j < line.length(); j++) {			// changed old variable from Level1.length to selectedlevel.length
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.web("00B9AE",1.0));
                        platforms.add(platform);
                        break;
                    case '2':
                    	Node pickup = createEntity(j*60, i *60, 60, 60, Color.web("F0E052",1.0));
                    	pickups.add(pickup);
                    	break;
                    case '3':
                    	Node enemies = createEntity(j*60, i *60, 60, 60, Color.web("F00000",1.0));
                    	game_enemies.add(enemies);
                    	break;
                    case '4':
                    	Node door = createEntity(j*60, i *60, 60, 60, Color.web("FAB040",1.0));
                    	doors.add(door);
                    	break;
                }
            }
        }

        
        player = createEntity(80, 600, 40, 40, Color.web("03312E",1.0));
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 1280 / 2 && offset < levelWidth - 1280 / 2 ){
                gameRoot.setLayoutX(-(offset - 1280 / 2));
            }
        });
        
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
    
    // Key press for movement
    private void update(){
        if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 2){
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >= 2){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-2){
            movePlayerX(5);
        }
        if (playerVelocity.getY() < 10){
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());
    
        //Update when touching with pickups
    	for (Node pickup : pickups) {
    		if (player.getBoundsInParent().intersects(pickup.getBoundsInParent())){
    			pickup.getProperties().put("alive", false);
    			point+=50;
    			score.setText(String.format("HP: %d",point));
    		}
    	}
    	for (Iterator<Node> it = pickups.iterator(); it.hasNext(); ) {
    		Node pickup = it.next();
    		if (!(Boolean)pickup.getProperties().get("alive")) {
    			it.remove();
    			gameRoot.getChildren().remove(pickup);
    		}
    	}
    	
    	for (Node enemies : game_enemies) {
    		if (player.getBoundsInParent().intersects(enemies.getBoundsInParent())){
    			enemies.getProperties().put("alive", false);
    			point-=50;
    			score.setText(String.format("HP: %d",point));
    		}
    	}
    	for (Iterator<Node> it = game_enemies.iterator(); it.hasNext(); ) {
    		Node enemies = it.next();
    		if (!(Boolean)enemies.getProperties().get("alive")) {
    			it.remove();
    			gameRoot.getChildren().remove(enemies);
    		}
    	}
    	
    	for (Node door : doors) {
    		if (player.getBoundsInParent().intersects(door.getBoundsInParent())){
    			door.getProperties().put("alive", false);
    			winner_board();
    		}
    	}
    }

    //Fucntion for Win
    private void winner_board() {
    	Rectangle win=new Rectangle(1280, 720,Color.LIGHTBLUE);
    	
    	VBox endBox=new VBox(50);
		endBox.setPadding(new Insets(100, 0, 0, 500));
    	endBox.setAlignment(Pos.CENTER);
    	
    	Label endMessage=new Label();
		endMessage.setText(String.format("C'est Fini, Vous avez fini le jeu!!"));
		endMessage.setTextFill(Color.WHITE);
		endMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		endBox.getChildren().add(endMessage);

		Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), Game.class);
        Button menuButton = createButton("Menu", Color.web("A09CB0",1.0), StartScreen.class);
        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
        endBox.getChildren().addAll(RestartButton, menuButton, quitButton);
		
		
    	uiRoot.getChildren().addAll(win,endBox);
	}
    
   //Function for death
	private void die() {
    	
    	Rectangle gameOverMask=new Rectangle(1280, 720,Color.BLACK);
    	
    	VBox endBox=new VBox(50);
		endBox.setPadding(new Insets(100, 0, 0, 480));
    	endBox.setAlignment(Pos.CENTER);
		
		Label endMessage=new Label();
		endMessage.setText(String.format("YOU DIED!"));
		endMessage.setTextFill(Color.web("4f0202",1.0));
		endMessage.setFont(Font.font("Algerian", FontWeight.BOLD, FontPosture.REGULAR, 50));
		endBox.getChildren().add(endMessage);
		
		Button RestartButton = createButton("Retry", Color.web("361d1d",1.0), Game.class);
        Button menuButton = createButton("Menu", Color.web("361d1d",1.0), StartScreen.class);
        Button quitButton = createButton("Quit", Color.web("361d1d",1.0), null);
        endBox.getChildren().addAll(RestartButton, menuButton, quitButton);
		
		
    	uiRoot.getChildren().addAll(gameOverMask,endBox);
    }
    
    //Creation of Buttons 
	private Button createButton(String buttonText, Color backgroundColor, Class<? extends Application> appClass) {
        Button button = new Button(buttonText);
        button.setPrefSize(200, 50);
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        button.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, null)));
 
        if (appClass != null) {
            button.setOnAction(e -> {
                try {
                    Application appInstance = appClass.getDeclaredConstructor().newInstance();
                    appInstance.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                primaryStage.close();
            });
        } else {
            button.setOnAction(e -> primaryStage.close());
        }
 
        return button;
    }
	
    //Movement of player in X axis
    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    

     //Movement of player in Y axis
     private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    

    private void jumpPlayer(){
    if(canJump){
        playerVelocity = playerVelocity.add(0, -30);
        canJump = false;
        }
    }
    

    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);
        gameRoot.getChildren().add(entity);
        return entity;

    }
    
    
    private boolean isPressed(KeyCode key){
    return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
    	this.primaryStage = primaryStage;
    	
        initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        primaryStage.setTitle("Kult");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(720);
        primaryStage.setMaxWidth(1280);
        primaryStage.setResizable(false);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                	update();
            }
        };
        
        timer.start();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}