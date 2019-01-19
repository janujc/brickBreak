package game;

// TODO
// TODO
// TODO
// TODO
// TODO
// TODO

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

/**
 * @author Januario Carreiro
 */
public class GameLoop extends Application{

    public static final String TITLE = "Breakout";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Color BACKGROUND = Color.ANTIQUEWHITE;
    public static final Color HIGHLIGHT = Color.TAN.darker();
    public static final Color BALL_COLOR = Color.DARKORANGE;
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = SIZE - 50.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 25.0;
    public static final double INITIAL_SPEED_X = 1.0;

    /**
     * These next few constants are used when creating the bricks
     */
    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 6;
    public static final double X_CHANGE = 10.0;
    public static final double Y_CHANGE = 20.0;
    public static final double BRICK_WIDTH = 80.0;
    public static final double BRICK_HEIGHT = 10.0;
    public static final double INITIAL_Y_POS = 0.0;
    public static final double Y_POS_DIFFICULTY = 25.0;
    public static final double INITIAL_X_POS =
            (SIZE - (NUM_COLS * BRICK_WIDTH) - ((NUM_COLS - 1) * X_CHANGE)) / 2.0;
    public static final Color BRICK_COLOR[] = {Color.PURPLE.brighter(), Color.LIGHTSKYBLUE, Color.LIGHTGREEN,
            Color.LIGHTSALMON, Color.ORANGERED};

    private Scene myScene;
    private Group root = new Group();
    private Stage myStage;
    private Timeline animation;
    private KeyFrame frame;
    private int myLevel = 1;
    private VBox vbox;
    private Circle myBall;
    private Rectangle myPlatform;
    private int myLives = 3;
    private Text myLivesDisplay;

    public Brick[][] myBrickConfig = new Brick[NUM_COLS][NUM_ROWS];

    private double myBallSpeedX = 0.0;
    private double myBallSpeedY = 150.0;
    Scene levelScene = new Scene(root, SIZE, SIZE, BACKGROUND);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.myStage = stage;
        myScene = titleScreen();
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
    }

    private void animationPlay() {
        frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene titleScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t = new Text(10, 20, "BREAKOUT");
        pane.setCenter(t);

        Button startButton = new Button("Start Game");

        vbox = new VBox(0, startButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        startButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private Scene betweenLevelsScene() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t = new Text(10, 20, "CONGRATULATIONS ON BEATING THAT LEVEL!\n" +
                "ARE YOU READY FOR THE NEXT ONE?");
        pane.setCenter(t);

        Button startButton = new Button("START NEXT LEVEL");

        vbox = new VBox(0, startButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        startButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private void changeLevels() {
        this.animation.stop();
        root.getChildren().removeAll();
        myScene = betweenLevelsScene();
        myStage.setScene(myScene);
    }

    private Scene levelSelect(int level) {
        levelScene.setRoot(root);

        myLivesDisplay = new Text("LIVES: " + myLives);
        VBox vbox = new VBox(0, myLivesDisplay);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.TOP_LEFT);

        myBall = new Circle(BALL_RADIUS, BALL_COLOR);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, SIZE / 2 - myBall.getRadius() / 2);

        myPlatform = new Rectangle(SIZE / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);

        myBrickConfig = makeBricks(NUM_ROWS, NUM_COLS, level);

        root.getChildren().add(vbox);
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);

        levelScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        animationPlay();

        return levelScene;
    }

    private void buttonClick(int level) {
        myScene = levelSelect(level);
        myStage.setScene(myScene);
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && (myPlatform.getX() + myPlatform.getWidth() < myScene.getWidth())) {
            myPlatform.setX(myPlatform.getX() + PLATFORM_SPEED);
        }
        else if (code == KeyCode.LEFT && (myPlatform.getX() > 0)) {
            myPlatform.setX(myPlatform.getX() - PLATFORM_SPEED);
        }
    }

    private void resetBall() {
        this.animation.stop();
        root.getChildren().remove(vbox);
        root.getChildren().remove(myBall);
        root.getChildren().remove(myPlatform);
        myScene = droppedBallScreen();
        myStage.setScene(myScene);
    }

    private Scene droppedBallScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t = new Text(10, 20, "YOU NOW HAVE " + myLives + " LIVES!\n" +
                "TO CONTINUE, PRESS CONTINUE");
        pane.setCenter(t);

        Button startButton = new Button("CONTINUE");

        vbox = new VBox(0, startButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        startButton.setOnAction(e -> continueLevel(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private void continueLevel(int level) {
        myLivesDisplay = new Text("LIVES: " + myLives);
        vbox = new VBox(0, myLivesDisplay);
        vbox.setPadding(new Insets(5));
        vbox.setAlignment(Pos.TOP_LEFT);

        myBall = new Circle(BALL_RADIUS, BALL_COLOR);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, SIZE / 2 - myBall.getRadius() / 2);

        myPlatform = new Rectangle(SIZE / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);

        root.getChildren().add(vbox);
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);

        levelScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        animationPlay();

        myStage.setScene(levelScene);
    }

    private void step (double elapsedTime) {
        myBall.setLayoutX(myBall.getLayoutX() + myBallSpeedX * elapsedTime);
        myBall.setLayoutY(myBall.getLayoutY() + myBallSpeedY * elapsedTime);

        if (myBall.getLayoutY() + myBall.getRadius() >= myScene.getHeight()) {
            myLives--;
            resetBall();
        }

        if (myBall.getLayoutX() <= myBall.getRadius() || myBall.getLayoutX() + myBall.getRadius() >= myScene.getWidth()) {
            myBallSpeedX = - myBallSpeedX;
        }

        if (myBall.getLayoutY() <= myBall.getRadius()) {
            myBallSpeedY = - myBallSpeedY;
        }

        var intersect = Shape.intersect(myBall, myPlatform);
        Random rand = new Random();
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myBallSpeedY = - myBallSpeedY;
            if (myBall.getLayoutX() + myBall.getRadius() <= myPlatform.getX() + (myPlatform.getWidth() / 2.0)) {
                myBallSpeedX = - Math.abs(INITIAL_SPEED_X * (rand.nextDouble() * 100 + 50));
            }
            else myBallSpeedX = INITIAL_SPEED_X * (rand.nextDouble() * 100 + 50);
        }

        int brokenBricks = 0;
        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                var brickBreak = Shape.intersect(myBall, myBrickConfig[x][y]);
                if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                    myBallSpeedY = -myBallSpeedY;
                    myBrickConfig[x][y].reduceHealth();
                    if (myBrickConfig[x][y].isDestroyed()) {
                        brokenBricks++;
                        root.getChildren().remove(myBrickConfig[x][y]);
                        myBrickConfig[x][y] = new Brick();
                    }
                    if (brokenBricks == NUM_ROWS*NUM_COLS) {
                        myLevel++;
                        changeLevels();
                    }
                }
            }
        }
    }

    public Brick[][] makeBricks(int rows, int cols, int levelSelect) {
        Brick[][] myBrickConfig = new Brick[rows][cols];
        double xPos = INITIAL_X_POS;
        switch(levelSelect){
            case 1:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < NUM_ROWS; y++) {
                        yPos += BRICK_HEIGHT + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[levelSelect - 1]);
                        myBrickConfig[x][y].setHealth(1);
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    xPos += BRICK_WIDTH + X_CHANGE;
                }
                break;
            case 2:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < x + 1; y++) {
                        yPos += BRICK_HEIGHT + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[levelSelect - 1]);
                        if (x == y) myBrickConfig[x][y].setHealth(3);
                        if (x > y) myBrickConfig[x][y].setHealth(2);
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    for (int y = x + 1; y < NUM_ROWS; y++) {
                        myBrickConfig[x][y] = new Brick();
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    xPos += BRICK_WIDTH + X_CHANGE;
                }
                break;
            case 3:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < NUM_ROWS; y++) {
                        yPos += BRICK_HEIGHT + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[levelSelect - 1]);
                    }
                    xPos += BRICK_WIDTH + X_CHANGE;
                }
                break;
            case 4:
            case 5:
        }
        return myBrickConfig;
    }
}
