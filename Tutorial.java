package platformGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//images
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tutorial extends main {
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<>();
    
	private ArrayList<Node> pickups = new ArrayList<>();
    private ArrayList<Node> game_enemies = new ArrayList<>();
	private ArrayList<Node> goals = new ArrayList<>();
    
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private int levelWidth;
    private int point = 200;
	private Label hitPoint;
    private Stage primaryStage;
      
	private void initContent(){
        //game Backgroud
    	Image backgroundImage1 = new Image(getClass().getResource("/platformGame/7.jpg").toExternalForm());
    	Image backgroundImage2 = new Image(getClass().getResource("/platformGame/2.png").toExternalForm());
    	Image backgroundImage3 = new Image(getClass().getResource("/platformGame/3.png").toExternalForm());
    	Image backgroundImage4 = new Image(getClass().getResource("/platformGame/4.png").toExternalForm());
    	new Image(getClass().getResource("/platformGame/5.png").toExternalForm());

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

    	new Pane(bg1, bg2, bg3, bg4, bg5);
    	
        levelWidth = LevelData.LEVEL1[0].length() * 60;
        
        
        //Text Hit Point
        hitPoint=new Label();
        hitPoint.setText(String.format("HP: %d", point));
        hitPoint.setTextFill(Color.BLACK);
        hitPoint.setFont(Font.font("Algerian", FontWeight.NORMAL, FontPosture.REGULAR, 30));
        uiRoot.getChildren().add(hitPoint);
        
        //game loop
        for (int i=0; i< LevelData.TUTO.length; i++){
            String line = LevelData.TUTO[i];
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
                    	Node goal = createEntity(j*60, i *60, 60, 60, Color.web("FAB040",1.0));
                    	goals.add(goal);
                    	break;
                }
            }
        }
        
        //player position 
        Image characterSamurai = new Image(getClass().getResource("/platformGame/mainCharacter.png").toExternalForm());
        player = createEntity1(80, 600, 40, 40, characterSamurai);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 1280 / 2 && offset < levelWidth - 1280 / 2 ){
                gameRoot.setLayoutX(-(offset - 1280/2));
            }
        });
                
        Label move_mess=new Label();
        move_mess.setText(String.format("Use A,D'\n to move left and right and SPACE to jump"));
        move_mess.setAlignment(Pos.CENTER);
        move_mess.setTextFill(Color.BLACK);
        move_mess.setPadding(new Insets(300, 0, 0, 200));
        move_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(move_mess);
        
        Label coin_mess=new Label();
		coin_mess.setText(String.format("Do not touch witht he enemies\n or you will lose health"));
		coin_mess.setAlignment(Pos.CENTER);
		coin_mess.setTextFill(Color.BLACK);
		coin_mess.setPadding(new Insets(500, 0, 0, 400));
		coin_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(coin_mess);
		
		Label platform_mess=new Label();
		platform_mess.setText(String.format("Try to reach at the\n end of the level"));
		platform_mess.setAlignment(Pos.CENTER);
		platform_mess.setTextFill(Color.BLACK);
		platform_mess.setPadding(new Insets(200, 0, 0, 700));
		platform_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(platform_mess);
		
		Label obs_mess=new Label();
		obs_mess.setText(String.format("Dodge the Enemies"));
		obs_mess.setAlignment(Pos.CENTER);
		obs_mess.setTextFill(Color.BLACK);
		obs_mess.setPadding(new Insets(500, 0, 0, 2100));
		obs_mess.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
		gameRoot.getChildren().add(obs_mess);
        
        appRoot.getChildren().addAll(bg1, bg2, bg3, bg4, bg5, gameRoot, uiRoot);
    }
        
    // Key press for movement
    private void update(){
    	
    	if(point > 0) {
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
    			hitPoint.setText(String.format("HP: %d",point));
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
    			hitPoint.setText(String.format("HP: %d",point));
    		}
    	}
    	for (Iterator<Node> it = game_enemies.iterator(); it.hasNext(); ) {
    		Node enemies = it.next();
    		if (!(Boolean)enemies.getProperties().get("alive")) {
    			it.remove();
    			gameRoot.getChildren().remove(enemies);
    		}
    	}
    	
    	for (Node door : goals) {
    		if (player.getBoundsInParent().intersects(door.getBoundsInParent())){
    			door.getProperties().put("alive", false);
//    			level +=1;
                End_tuto();
            }
        }    
    }
    else{
        die();
    }
}

	private void End_tuto(){
        Rectangle Goal = new Rectangle(1280, 720,Color.BLACK);
    	
    	VBox goalBox = new VBox(50);
		goalBox.setPadding(new Insets(200, 0, 0, 370));
    	goalBox.setAlignment(Pos.CENTER);
    	
    	Label endMessage=new Label();
		endMessage.setText(String.format("Back to Main Menu"));
		endMessage.setTextFill(Color.WHITE);
		endMessage.setFont(Font.font("Algerian", FontWeight.BOLD, FontPosture.REGULAR, 40));
		goalBox.getChildren().add(endMessage);

		Button advanceButton = createButton("GO", Color.web("361d1d",1.0), StartScreen.class);
        goalBox.getChildren().addAll(advanceButton);
    	uiRoot.getChildren().addAll(Goal,goalBox);
    }
	 
    
    
	private void winner_board() {
	    	Rectangle win=new Rectangle(1280, 720,Color.LIGHTBLUE);
	    	
	    	VBox endBox=new VBox(50);
			endBox.setPadding(new Insets(140, 0, 0, 420));
	    	endBox.setAlignment(Pos.CENTER);
	    	
	    	Label endMessage=new Label();
			endMessage.setText(String.format("C'est Fini, Vous avez terminer le tutoriel."));
			endMessage.setTextFill(Color.web("443E33",1.0));
			endMessage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			endBox.getChildren().add(endMessage);
			
			Label final_score=new Label();
			final_score.setText(String.format("Votre score final est %d", point));
			final_score.setTextFill(Color.web("443E33",1.0));
			final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(final_score);
			
			Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), Tutorial.class);
	        Button menuButton = createButton("Menu", Color.web("A09CB0",1.0), StartScreen.class);
	        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
	        endBox.getChildren().addAll(RestartButton, menuButton,quitButton);
			
			
	    	uiRoot.getChildren().addAll(win,endBox);
		}
	 
	private void die() {
	    	
	    	Rectangle gameOverMask=new Rectangle(1280, 720,Color.LIGHTBLUE);
	    	
	    	VBox endBox=new VBox(30);
			endBox.setPadding(new Insets(120, 0, 0, 450));
	    	endBox.setAlignment(Pos.CENTER);
			
			Label endMessage=new Label();
			endMessage.setText(String.format("Vous êtes mort"));
			endMessage.setTextFill(Color.web("443E33",1.0));
			endMessage.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(endMessage);
			
			Label tip =new Label();
			tip.setText(String.format("Astuce: Essaie de sauter au dessus\n des blocks rouge pour ne pas mourrir."));
			tip.setTextFill(Color.web("443E33",1.0));
			tip.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(tip);
			
			Label final_score=new Label();
			final_score.setText(String.format("Votre score est %d", point));
			final_score.setTextFill(Color.web("443E33",1.0));
			final_score.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
			endBox.getChildren().add(final_score);
			
			Button RestartButton = createButton("Recommencer", Color.web("776D5A",1.0), Tutorial.class);
	        Button menuButton = createButton("Menu", Color.web("A09CB0",1.0), StartScreen.class);
	        Button quitButton = createButton("Quitter", Color.web("987D7C",1.0), null);
	        endBox.getChildren().addAll(RestartButton, menuButton, quitButton);
			
	    	uiRoot.getChildren().addAll(gameOverMask,endBox);
	    }
	
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
	    
	private void movePlayerX(int value){
		    boolean movingRight = value > 0;
		    for (int i=0; i < Math.abs(value);i++){
		        for (Node platform : platforms){
		            if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
		                if(movingRight){
		                    if (player.getTranslateX() + 40 == platform.getTranslateX()){
		                        return;
		                    }
		                }else {
		                    if (player.getTranslateX() == platform.getTranslateX() + 60) {
		                        return;
		                    }
		                }
		            }
		        }
		        player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
		        }
		    }
	
	private void movePlayerY(int value){
		        boolean movingDown = value > 0;
		        for (int i=0; i < Math.abs(value);i++){
		            for (Node platform : platforms){
		                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
		                    if(movingDown){
		                        if (player.getTranslateY() + 40 == platform.getTranslateY()){
		                            canJump = true;
		                            return;
		                        }
		                    }else {
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

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initContent();
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        primaryStage.setTitle("Jump - Tutoriel");
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

}