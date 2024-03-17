package platformGame;
	
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
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;


import javafx.scene.shape.Circle;
import javafx.animation.Timeline;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class main extends Application {

    
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
    
    
//    Image enemyImage = new Image("file:///C:/POO2/PlatformJump/src/application/bg2.png");
//    //C:\Users\User\eclipse-workspaceJava\1. Java_udm\Cult_of_Kulve\src\platformGame
    

    
    private void initContent(){
    	
    	Image backgroundImage1 = new Image(getClass().getResource("/platformGame/7.jpg").toExternalForm());
    	Image backgroundImage2 = new Image(getClass().getResource("/platformGame/2.png").toExternalForm());
    	Image backgroundImage3 = new Image(getClass().getResource("/platformGame/3.png").toExternalForm());
    	Image backgroundImage4 = new Image(getClass().getResource("/platformGame/4.png").toExternalForm());
    	Image backgroundImage5 = new Image(getClass().getResource("/platformGame/5.png").toExternalForm());

    	Rectangle bg1 = new Rectangle(1280, 720);
    	bg1.setFill(new ImagePattern(backgroundImage1));

    	Rectangle bg2 = new Rectangle(1280, 720);
    	bg2.setFill(new ImagePattern(backgroundImage2));

    	Rectangle bg3 = new Rectangle(1280, 720);
    	bg3.setFill(new ImagePattern(backgroundImage3));

    	Rectangle bg4 = new Rectangle(1280, 720);
    	bg4.setFill(new ImagePattern(backgroundImage4));
    	
    	Rectangle bg5 = new Rectangle(1280, 720);
    	bg5.setFill(new ImagePattern(backgroundImage4));

    	Pane backgroundPane = new Pane(bg1, bg2, bg3, bg4, bg5);
    	
        levelWidth = LevelData.LEVEL1[0].length() * 60;

        score=new Label();
        score.setText(String.format("HP: %d", point));
        score.setTextFill(Color.BLACK);
        score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 40));
        uiRoot.getChildren().add(score);
        
        for (int i=0; i< LevelData.LEVEL1.length; i++){
            String line = LevelData.LEVEL1[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i *60, 60, 60, Color.web("000000",1.0));
                        platforms.add(platform);
                        break;
                    case '2':
                    	// Creating a circle and adding it to the pickups list
                        Image heathOrb = new Image(getClass().getResource("/platformGame/HP.png").toExternalForm());
                    	Node pickup = createEntity3(j * 60, i * 60, 60, 60, heathOrb);
                    	pickups.add(pickup);
                    	break;
                    case '3':
                        Image enemyImage = new Image(getClass().getResource("/platformGame/hurt.png").toExternalForm());
                    	Node enemies = createEntity3(j * 60, i * 60, 60, 60, enemyImage);
                    	game_enemies.add(enemies);
                    	break;
                    case '4':
                    	Node door = createEntity(j*60, i *60, 60, 60, Color.web("FAB040",1.0));
                    	doors.add(door);
                    	break;
                }
            }
        }

        Image characterSamurai = new Image(getClass().getResource("/platformGame/mainCharacter.png").toExternalForm());
        player = createEntity1(80, 600, 40, 40, characterSamurai);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 1280 / 2 && offset < levelWidth - 1280 / 2 ){
                gameRoot.setLayoutX(-(offset - 1280/2));
            }
        });
        
        appRoot.getChildren().addAll(bg1, bg2, bg3, bg4, bg5, gameRoot, uiRoot);
        
//        Image[] runningFrames = new Image[NUM_RUNNING_FRAMES];
//
//        for (int i = 0; i < NUM_RUNNING_FRAMES; i++) {
//            runningFrames[i] = new Image(getClass().getResource("/platformGame/runningFrame" + i + ".png").toExternalForm());
//        }
//
//        // Create an ImageView to display the character
//        ImageView playerView = new ImageView();
//        playerView.setFitWidth(40);
//        playerView.setFitHeight(40);
//        playerView.setImage(runningFrames[0]); // Set initial image
//
//        // Animation to switch between running frames
//        Timeline runningAnimation = new Timeline(
//            new KeyFrame(Duration.millis(100), event -> {
//                // Increment frame index or reset if reached the end
//                currentIndex = (currentIndex + 1) % NUM_RUNNING_FRAMES;
//                playerView.setImage(runningFrames[currentIndex]);
//            })
//        );
//        runningAnimation.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
//        runningAnimation.play();
//        // Add playerView to the gameRoot instead of player (if player is a Node)
//        gameRoot.getChildren().add(playerView);

    }
    
    // Key press for movement
    private void update(){
        if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 5){
            jumpPlayer();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5){
            movePlayerX(-5);
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-5){
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

		Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), main.class);
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
		
		Button RestartButton = createButton("Retry", Color.web("361d1d",1.0), main.class);
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
    
    private Node createEntity1(int x, int y, int w, int h, Image image){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        
        // Creating an ImageView with the specified image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        
        // Adding the ImageView to the rectangle
        entity.setClip(imageView);
        
        entity.getProperties().put("alive", true);
        gameRoot.getChildren().add(entity);
        
        return entity;
    }
    
//    private Node createEntity2(int x, int y, int radius, Color color){
//        Circle entity = new Circle(radius);
//        entity.setCenterX(x);
//        entity.setCenterY(y);
//        entity.setFill(color);
//        entity.getProperties().put("alive", true);
//        gameRoot.getChildren().add(entity);
//        return entity;
//    }
    
    private Node createEntity3(int x, int y, int w, int h, Image image){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        
        // Creating an ImageView with the specified image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        
        // Adding the ImageView to the rectangle
        entity.setClip(imageView);
        
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