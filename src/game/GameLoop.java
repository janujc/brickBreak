package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
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
    public static final Color HIGHLIGHT = Color.TAN;
    public static final Color BALL_COLOR = Color.ORANGE;
    public static final Color BRICK_COLOR[] = {Color.VIOLET, Color.LIGHTSKYBLUE, Color.LIGHTGREEN,
            Color.LIGHTSALMON, Color.ORANGERED};
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = SIZE - 50.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 25.0;
    public static final double INITIAL_SPEED_X = 1.0;

    /**
     * These next few constants are used when creating the bricks
     */
    public static final int NUM_BRICKS_X = 5;
    public static final int NUM_BRICKS_Y = 5;
    public static final double X_CHANGE = 10.0;
    public static final double Y_CHANGE = 20.0;
    public static final double BRICK_WIDTH = 90.0;
    public static final double BRICK_HEIGHT = 15.0;
    public static final double INITIAL_Y_POS = 25.0;
    public static final double INITIAL_X_POS =
            (SIZE - (NUM_BRICKS_X * BRICK_WIDTH) - ((NUM_BRICKS_X - 1) * X_CHANGE)) / 2.0;

    private Scene myScene;
    private Group root = new Group();
    private Scene titleScreen, levelOne, levelTwo, levelThree, levelFour;

    private Circle myBall;
    private Rectangle myPlatform;
    private Brick myBricks[] = new Brick[NUM_BRICKS_Y * NUM_BRICKS_X];
    private double myBallSpeedX = 0.0;
    private double myBallSpeedY = 3.0;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        myScene = titleScreen();
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        if (myScene != titleScreen) {
            KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
            Timeline animation = new Timeline();
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.getKeyFrames().add(frame);
            animation.play();
        }
    }

    private Scene titleScreen() {
        titleScreen = new Scene(root, SIZE, SIZE, BACKGROUND);
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> buttonClick());
        root.getChildren().add(startButton);

        return titleScreen;
    }

    private void buttonClick() {
        myScene = setupGame();
    }

    private Scene setupGame() {
        var setUpLevel = new Scene(root, SIZE, SIZE, BACKGROUND);

        // make some shapes and set their properties
        myBall = new Circle(BALL_RADIUS, BALL_COLOR);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, SIZE / 2 - myBall.getRadius() / 2);

        myPlatform = new Rectangle(SIZE / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);

        double xPos = INITIAL_X_POS;
        for (int x = 0; x < NUM_BRICKS_X; x++) {
            double yPos = INITIAL_Y_POS;
            myBricks[NUM_BRICKS_X * x] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[4]);
            root.getChildren().add(myBricks[NUM_BRICKS_X * x]);
            for (int y = 1; y < NUM_BRICKS_Y; y++) {
                yPos += BRICK_HEIGHT + Y_CHANGE;
                myBricks[NUM_BRICKS_X * x + y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[4]);
                root.getChildren().add(myBricks[NUM_BRICKS_X * x + y]);
            }
            xPos += BRICK_WIDTH + X_CHANGE;
        }

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);

        // respond to input
        setUpLevel.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return setUpLevel;
    }

    private Scene levelOne() {
        var levelOne = new Scene(root, SIZE, SIZE, BACKGROUND);

        double xPos = INITIAL_X_POS;
        for (int x = 0; x < NUM_BRICKS_X; x++) {
            double yPos = INITIAL_Y_POS;
            myBricks[NUM_BRICKS_X * x] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[0]);
            root.getChildren().add(myBricks[NUM_BRICKS_X * x]);
            for (int y = 1; y < NUM_BRICKS_Y; y++) {
                yPos += BRICK_HEIGHT + Y_CHANGE;
                myBricks[NUM_BRICKS_X * x + y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR[0]);
                root.getChildren().add(myBricks[NUM_BRICKS_X * x + y]);
            }
            xPos += BRICK_WIDTH + X_CHANGE;
        }

        return levelOne;
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && (myPlatform.getX() + myPlatform.getWidth() < myScene.getWidth())) {
            myPlatform.setX(myPlatform.getX() + PLATFORM_SPEED);
        }
        else if (code == KeyCode.LEFT && (myPlatform.getX() > 0)) {
            myPlatform.setX(myPlatform.getX() - PLATFORM_SPEED);
        }
    }

    private void step (double elapsedTime) {
        myBall.setLayoutX(myBall.getLayoutX() + myBallSpeedX);
        myBall.setLayoutY(myBall.getLayoutY() + myBallSpeedY);

        /**
         * Below we will define how the ball bounces off different surfaces
         */

        if (myBall.getLayoutX() <= myBall.getRadius() || myBall.getLayoutX() + myBall.getRadius() >= myScene.getWidth()) {
            myBallSpeedX = - myBallSpeedX;
        }

        if (myBall.getLayoutY() <= myBall.getRadius() || myBall.getLayoutY() + myBall.getRadius() >= myScene.getHeight()) {
            myBallSpeedY = - myBallSpeedY;
        }

        var intersect = Shape.intersect(myBall, myPlatform);
        Random rand = new Random();
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myBallSpeedY = - myBallSpeedY;
            if (myBall.getLayoutX() + myBall.getRadius() <= myPlatform.getX() + (myPlatform.getWidth() / 2.0)) {
                myBallSpeedX = - Math.abs(INITIAL_SPEED_X * (rand.nextDouble() * 2 + 1.5));
            }
            else myBallSpeedX = INITIAL_SPEED_X * (rand.nextDouble() * 2 + 1.5);
        }

        for (int i = 0; i < NUM_BRICKS_X * NUM_BRICKS_Y; i++) {
            var brickBreak = Shape.intersect(myBall, myBricks[i]);
            if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                myBallSpeedY = -myBallSpeedY;
                myBricks[i].reduceHealth();
                if (myBricks[i].isDestroyed()) {
                    root.getChildren().remove(myBricks[i]);
                    myBricks[i] = new Brick();
                }
            }
        }
    }
}
