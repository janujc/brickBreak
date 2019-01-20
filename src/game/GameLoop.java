package game;

// TODO ADD POWER-UPS
// TODO ADD CHEAT KEYS
// TODO ADD SOUND
// TODO ADD BOUNCE OFF SIDE OF BRICKS

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
    public static final int SIZE = 700;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    public static final Color BACKGROUND_C = Color.ANTIQUEWHITE;
    public static final Color HIGHLIGHT_C = Color.TAN.darker();
    public static final Color BALL_C = Color.DARKORANGE;
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y_POS = SIZE - 50.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 25.0;
    public static final double INITIAL_SPEED_Y = 200.0;
    public static final double INITIAL_SPEED_X = 0.0;
    public static final double SPEED_Y_CHANGE = 20.0;
    public static final double SPEED_X_CHANGE = 0.2;
    public static final double REBOUND_SPEED_X = 1.0;

    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 6;
    public static final double X_CHANGE = 10.0;
    public static final double Y_CHANGE = 25.0;
    public static final double BRICK_W = 90.0;
    public static final double BRICK_H = 10.0;
    public static final double INITIAL_Y_POS = 0.0;
    public static final double Y_POS_DIFFICULTY = 35.0;
    public static final double INITIAL_X_POS =
            (SIZE - (NUM_COLS * BRICK_W) - ((NUM_COLS - 1) * X_CHANGE)) / 2.0;

    public static final int NUM_LEVELS = 5;
    public static final int DISTANCE_BETWEEN_TEXT = 25;
    public static final int Y_OFFSET = 5;
    public static final int X_OFFSET_SCORE = 215;
    public static final int SCORE_PER_BRICK = 10;

    private Scene myScene;
    private Group root = new Group();
    private Stage myStage;
    private Timeline animation;
    private VBox vbox;
    private Circle myBall;
    private Rectangle myPlatform;
    private Text myScoreVal;
    private Brick[][] myBrickConfig = new Brick[NUM_COLS][NUM_ROWS];
    private int myLives = 3;
    private int myLevel = 1;
    private int myScore = 0;
    private int myNumBricks;
    private double myBallSpeedX = INITIAL_SPEED_X;
    private double myBallSpeedY = INITIAL_SPEED_Y;
    private double myReboundSpeedRatio = REBOUND_SPEED_X;
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
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene titleScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t1 = new Text(10, 20, "BREAKOUT");
        Text t2 = new Text(10, 20, "\n" + "INSTRUCTIONS:\n" +
                "USE THE A AND D KEYS TO MOVE THE PADDLE LEFT AND RIGHT, RESPECTIVELY.\n" +
                "THERE ARE " + NUM_LEVELS + " LEVELS AND YOU HAVE " + myLives + " LIVES.\n" +
                "DROPPING THE BALL RESULTS IN LOSING ONE LIFE AND RESETTING THE LEVEL.\n" +
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

    private Scene levelSelect(int level) {
        levelScene.setRoot(root);

        Text livesText = new Text("LIVES: " + myLives);
        Text levelText = new Text("LEVEL: " + myLevel);
        Text scoreText = new Text("SCORE: ");
        HBox displayBox = new HBox(DISTANCE_BETWEEN_TEXT, livesText, levelText, scoreText);
        displayBox.setPadding(new Insets(Y_OFFSET));

        myBall = new Circle(BALL_RADIUS, BALL_C);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, 400);

        myPlatform = new Rectangle(SIZE / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y_POS, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT_C);

        myBrickConfig = makeBricks(level);

        root.getChildren().add(displayBox);
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);

        levelScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        animationPlay();

        return levelScene;
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

    private void step() {
        myBall.setLayoutX(myBall.getLayoutX() + myBallSpeedX * GameLoop.SECOND_DELAY);
        myBall.setLayoutY(myBall.getLayoutY() + myBallSpeedY * GameLoop.SECOND_DELAY);

        if (myBall.getLayoutY() + myBall.getRadius() >= myScene.getHeight()) {
            myBallSpeedX = INITIAL_SPEED_X;
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
                myBallSpeedX = - Math.abs(myReboundSpeedRatio * (rand.nextDouble() * 100 + 50));
            }
            else myBallSpeedX = myReboundSpeedRatio * (rand.nextDouble() * 100 + 50);
        }

        brickBounce();

        updateScore();

        if (myNumBricks == 0) {
            nextLevel();
        }
    }

    private void brickBounce() {
        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                var brickBreak = Shape.intersect(myBall, myBrickConfig[x][y]);
                if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                    myBallSpeedY = -myBallSpeedY;
                    myBrickConfig[x][y].reduceHealth();
                    if (myBrickConfig[x][y].isDestroyed()) {
                        myNumBricks--;
                        myScore+= SCORE_PER_BRICK;
                        root.getChildren().remove(myBrickConfig[x][y]);
                        myBrickConfig[x][y] = new Brick();
                    }
                }
            }
        }
    }

    private void updateScore() {
        root.getChildren().remove(myScoreVal);
        myScoreVal = new Text("" + myScore);
        myScoreVal.relocate(X_OFFSET_SCORE, Y_OFFSET);
        root.getChildren().add(myScoreVal);
    }

    private void nextLevel() {
        myLevel++;
        myBallSpeedY += SPEED_Y_CHANGE;
        myReboundSpeedRatio += SPEED_X_CHANGE;
        changeLevels(true);
    }

    private Brick[][] makeBricks(int levelSelect) {
        double xPos = INITIAL_X_POS;
        switch(levelSelect){
            case 1:
                levelOneBuilder(xPos, levelSelect);
                break;
            case 2:
                levelTwoBuilder(xPos, levelSelect);
                break;
            case 3:
                levelThreeBuilder(xPos, levelSelect);
                break;
            case 4:
                levelFourBuilder(xPos, levelSelect);
                break;
            case 5:
                levelFiveBuilder(xPos, levelSelect);
                break;
        }
        return myBrickConfig;
    }

    private void levelOneBuilder(Double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < NUM_ROWS; y++) {
                yPos += BRICK_H + Y_CHANGE;
                myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                myBrickConfig[x][y].setHealth(1);
                root.getChildren().add(myBrickConfig[x][y]);
            }
            xPos += BRICK_W + X_CHANGE;
        }
        myNumBricks = NUM_COLS * NUM_ROWS;
    }

    private void levelTwoBuilder(double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < x + 1; y++) {
                yPos += BRICK_H + Y_CHANGE;
                myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                if (x == y) myBrickConfig[x][y].setHealth(4);
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
    }

    private void levelThreeBuilder(double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < NUM_ROWS; y++) {
                yPos += BRICK_H + Y_CHANGE;
                myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                if (y == 0 || x == 0 || y == NUM_ROWS - 1 || x == NUM_COLS - 1) {
                    myBrickConfig[x][y].setHealth(5);
                }
                else myBrickConfig[x][y].setHealth(3);
                root.getChildren().add(myBrickConfig[x][y]);
            }
            xPos += BRICK_W + X_CHANGE;
        }
        myNumBricks = NUM_COLS * NUM_ROWS;
    }

    private void levelFourBuilder(double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < NUM_ROWS; y++) {
                yPos += BRICK_H + Y_CHANGE;
                if (x == 0 || x == NUM_COLS - 1) {
                    myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                    myBrickConfig[x][y].setPermanent();
                }
                else {
                    myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                    myBrickConfig[x][y].setHealth(5);
                }
                root.getChildren().add(myBrickConfig[x][y]);

            }
            xPos += BRICK_W + X_CHANGE;
        }
        myNumBricks = (NUM_COLS - 2) * NUM_ROWS;
    }

    private void levelFiveBuilder(double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < NUM_ROWS; y++) {
                yPos += BRICK_H + Y_CHANGE;
                // TODO: ADD CONFIGURATION
                root.getChildren().add(myBrickConfig[x][y]);
            }
            xPos += BRICK_W + X_CHANGE;
        }
        myNumBricks = NUM_COLS * NUM_ROWS;
    }
}
