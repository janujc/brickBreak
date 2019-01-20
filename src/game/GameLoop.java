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
    public static final Color BACKGROUND_C = Color.ANTIQUEWHITE;
    public static final Color HIGHLIGHT_C = Color.TAN.darker();
    public static final Color BALL_C = Color.DARKORANGE;
    public static final Color BRICK_C[] = {Color.PURPLE.brighter(), Color.LIGHTSKYBLUE, Color.LIGHTGREEN,
            Color.LIGHTSALMON, Color.ORANGERED};
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = SIZE - 50.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 25.0;
    public static final double REBOUND_SPEED_X = 1.0;
    public static final int NUM_LEVELS = 5;

    /**
     * These next few constants are used when creating the bricks
     */
    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 6;
    public static final double X_CHANGE = 10.0;
    public static final double Y_CHANGE = 20.0;
    public static final double BRICK_W = 80.0;
    public static final double BRICK_H = 10.0;
    public static final double INITIAL_Y_POS = 0.0;
    public static final double Y_POS_DIFFICULTY = 30.0;
    public static final double INITIAL_X_POS =
            (SIZE - (NUM_COLS * BRICK_W) - ((NUM_COLS - 1) * X_CHANGE)) / 2.0;

    private Scene myScene;
    private Group root = new Group();
    private Stage myStage;
    private Timeline animation;
    private KeyFrame frame;
    private int myLevel = 4;
    private VBox vbox;
    private VBox livesBox;
    private Circle myBall;
    private Rectangle myPlatform;
    private int myLives = 3;
    private Text myLivesDisplay;
    private int myNumBricks;

    public Brick[][] myBrickConfig = new Brick[NUM_COLS][NUM_ROWS];

    private double myBallSpeedX = 0.0;
    private double myBallSpeedY = 200.0;
    Scene levelScene = new Scene(root, SIZE, SIZE, BACKGROUND_C);

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
        Text t1 = new Text(10, 20, "BREAKOUT");
        Text t2 = new Text(10, 20, "INSTRUCTIONS:\n" +
                "USE THE A AND D KEYS TO MOVE THE PADDLE LEFT AND RIGHT, RESPECTIVELY.\n" +
                "THERE ARE " + NUM_LEVELS + " LEVELS AND YOU HAVE " + myLives + " LIVES.\n" +
                "DROPPING THE BALL RESULTS IN LOSING ONE LIFE AND HAVING TO RESTART THE LEVEL.\n" +
                "IF YOU DROP THE BALL AT 0 LIVES, YOU LOSE.\n" +
                "GOOD LUCK!");

        VBox textBox = new VBox(0, t1, t2);
        textBox.setPadding(new Insets(50));
        textBox.setAlignment(Pos.CENTER);
        pane.setCenter(textBox);

        Button startButton = new Button("Start Game");

        vbox = new VBox(0, startButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        startButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private void changeLevels(boolean bool) {
        this.animation.stop();
        root.getChildren().clear();
        if (bool) {
            myScene = betweenLevelsScreen();
            myStage.setScene(myScene);
        }
        else {
            myScene = droppedBallScreen();
            myStage.setScene(myScene);
        }
    }

    private Scene betweenLevelsScreen() {
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

    private Scene levelSelect(int level) {
        levelScene.setRoot(root);

        myLivesDisplay = new Text("LIVES: " + myLives);
        livesBox = new VBox(0, myLivesDisplay);
        livesBox.setPadding(new Insets(5));
        livesBox.setAlignment(Pos.TOP_LEFT);

        myBall = new Circle(BALL_RADIUS, BALL_C);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, 400);

        myPlatform = new Rectangle(SIZE / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT_C);

        myBrickConfig = makeBricks(NUM_ROWS, NUM_COLS, level);

        root.getChildren().add(livesBox);
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
        if (code == KeyCode.D && (myPlatform.getX() + myPlatform.getWidth() < myScene.getWidth())) {
            myPlatform.setX(myPlatform.getX() + PLATFORM_SPEED);
        }
        else if (code == KeyCode.A && (myPlatform.getX() > 0)) {
            myPlatform.setX(myPlatform.getX() - PLATFORM_SPEED);
        }
    }

    private Scene droppedBallScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t = new Text(10, 20, "YOU NOW HAVE " + myLives + " LIVES!\n" +
                "TO CONTINUE, PRESS CONTINUE");
        pane.setCenter(t);

        Button continueButton = new Button("CONTINUE");

        vbox = new VBox(0, continueButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        continueButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private void step (double elapsedTime) {
        myBall.setLayoutX(myBall.getLayoutX() + myBallSpeedX * elapsedTime);
        myBall.setLayoutY(myBall.getLayoutY() + myBallSpeedY * elapsedTime);

        if (myBall.getLayoutY() + myBall.getRadius() >= myScene.getHeight()) {
            myLives--;
            changeLevels(false);
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
                myBallSpeedX = - Math.abs(REBOUND_SPEED_X * (rand.nextDouble() * 100 + 50));
            }
            else myBallSpeedX = REBOUND_SPEED_X * (rand.nextDouble() * 100 + 50);
        }

        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                var brickBreak = Shape.intersect(myBall, myBrickConfig[x][y]);
                if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                    myBallSpeedY = -myBallSpeedY;
                    myBrickConfig[x][y].reduceHealth();
                    if (myBrickConfig[x][y].isDestroyed()) {
                        myNumBricks--;
                        root.getChildren().remove(myBrickConfig[x][y]);
                        myBrickConfig[x][y] = new Brick();
                    }
                }
            }
        }

        if (myNumBricks == 0) {
            myLevel++;
            changeLevels(true);
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
                        yPos += BRICK_H + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H, BRICK_C[levelSelect - 1]);
                        myBrickConfig[x][y].setHealth(2);
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    xPos += BRICK_W + X_CHANGE;
                }
                myNumBricks = NUM_COLS * NUM_ROWS;
                break;
            case 2:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < x + 1; y++) {
                        yPos += BRICK_H + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H, BRICK_C[levelSelect - 1]);
                        if (x == y) myBrickConfig[x][y].setHealth(3);
                        if (x > y) myBrickConfig[x][y].setHealth(2);
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    for (int y = x + 1; y < NUM_ROWS; y++) {
                        myBrickConfig[x][y] = new Brick();
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    xPos += BRICK_W + X_CHANGE;
                }
                myNumBricks = (NUM_COLS * (NUM_COLS + 1))/2;
                break;
            case 3:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < NUM_ROWS; y++) {
                        yPos += BRICK_H + Y_CHANGE;
                        myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H, BRICK_C[levelSelect - 1]);
                        if (y == 0 || x == 0 || y == NUM_ROWS - 1 || x == NUM_COLS - 1) {
                            myBrickConfig[x][y].setHealth(3);
                        }
                        else myBrickConfig[x][y].setHealth(2);
                        root.getChildren().add(myBrickConfig[x][y]);
                    }
                    xPos += BRICK_W + X_CHANGE;
                }
                myNumBricks = NUM_COLS * NUM_ROWS;
                break;
            case 4:
                for (int x = 0; x < NUM_COLS; x++) {
                    double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
                    for (int y = 0; y < NUM_ROWS; y++) {
                        yPos += BRICK_H + Y_CHANGE;
                        if (x == 0 || x == NUM_COLS - 1) {
                            myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H, BRICK_C[levelSelect - 1]);
                            myBrickConfig[x][y].setPermanent();
                        }
                        else {
                            myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H, BRICK_C[levelSelect - 1]);
                            myBrickConfig[x][y].setHealth(3);
                        }
                        root.getChildren().add(myBrickConfig[x][y]);

                    }
                    xPos += BRICK_W + X_CHANGE;
                }
                myNumBricks = NUM_COLS * NUM_ROWS;
                break;
            case 5:
        }
        return myBrickConfig;
    }
}
