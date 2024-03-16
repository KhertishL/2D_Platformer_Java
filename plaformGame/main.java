package plaformGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class main extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();

    private Pane appRoot;
    @FXML
    private Pane gameRoot;
    @FXML
    private Pane uiRoot;

    @FXML
    private Node player;

    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;

    private Config config;

    @FXML
    private SimpleIntegerProperty score;
    @FXML
    private Text textScore;


    //Update player when key is pressed
    private void update() {
        if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 2) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 2) {
            movePlayerX(-2);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + config.getPlayerSize() <= levelWidth - 2) {
            movePlayerX(2);
        }

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }

        movePlayerY((int)playerVelocity.getY());


        //Coins pickup - score  adding on pickup
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                coin.getProperties().put("alive", false);
                score.set(score.get() + 100);
            }
        }

        //Removal of pickup after interaction 
        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
    }

    //Movement of player in X axis
    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + config.getPlayerSize() == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + config.getBlockSize()) {
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
                        if (player.getTranslateY() + config.getPlayerSize() == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + config.getBlockSize()) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    //Player jumps
    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }

    //Creation of Player
    private Node createEntity(int x, int y, int w, int h, Color color) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void init() throws Exception {
        config = Config.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        loader.setController(this);
        appRoot = loader.load();

        textScore.textProperty().bind(score.asString());

        levelWidth = LevelData.LEVEL1[0].length() * config.getBlockSize();

        //Game Loop

        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    //Creation of platforms
                    case '1':
                        Node platform = createEntity(j*config.getBlockSize(), i*config.getBlockSize(), config.getBlockSize(), config.getBlockSize(), Color.YELLOWGREEN);
                        platforms.add(platform);
                        break;
                    //Creation of pickups
                    case '2':
                        Node coin = createEntity(j*config.getBlockSize(), i*config.getBlockSize(), config.getBlockSize(), config.getBlockSize(), Color.GOLD);
                        coins.add(coin);
                        break;
                }
            }
        }

        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > config.getAppWidth() / 2 && offset < levelWidth - config.getAppWidth() / 2) {
                gameRoot.setLayoutX(-(offset - config.getAppWidth() / 2));
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Platformer Game");
        primaryStage.setScene(scene);
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
