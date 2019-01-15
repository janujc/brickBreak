package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

/**
 * @author Januario Carreiro
 */
public class Game extends Application{
    public static final String TITLE = "Breakout";
    public static final int SIZE = 500;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.ANTIQUEWHITE;
    public static final Paint HIGHLIGHT = Color.TAN;
    public static final Paint BALL_COLOR = Color.ORANGE;
    public static final Paint TARGET_COLOR = Color.VIOLET;
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = SIZE - 25.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 25.0;
    public static final double INITIAL_SPEED_X = 0.5;

    /**
     * These next few constants are used when creating the bricks
     */

    public static final int NUM_TARGETS_X = 5;
    public static final int NUM_TARGETS_Y = 5;
    public static final double X_CHANGE = 5.0;
    public static final double Y_CHANGE = 2.5;
    public static final double TARGET_WIDTH = 40.0;
    public static final double TARGET_HEIGHT = 10.0;
    public static final double INITIAL_Y_POS = 25.0;
    private double myXPos = 50.0;
    private double myYPos = INITIAL_Y_POS;


    private Scene myScene;
    private Circle myBall;
    private Rectangle myPlatform;
    private Rectangle myTarget;
    private double myBallSpeedX = INITIAL_SPEED_X;
    private double myBallSpeedY = 2.0;
    
    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Scene setupGame (int width, int height, Paint background) {
        var root = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // make some shapes and set their properties
        myBall = new Circle(BALL_RADIUS, BALL_COLOR);
        myBall.relocate(width / 2 - myBall.getRadius() / 2, height / 2 - myBall.getRadius() / 2);

        myPlatform = new Rectangle(width / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);
        myTarget = new Rectangle(width / 2 - (TARGET_WIDTH / 2), myYPos, TARGET_WIDTH, TARGET_HEIGHT);
        myTarget.setFill(TARGET_COLOR);

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);
        root.getChildren().add(myTarget);

        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
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
            if (myBall.getLayoutX() + (2.0 * myBall.getRadius()) <= myPlatform.getX() + (myPlatform.getWidth() / 2.0)) {
                myBallSpeedX = - Math.abs(INITIAL_SPEED_X * (rand.nextDouble() + 1.5));
            }
            else myBallSpeedX = INITIAL_SPEED_X * (rand.nextDouble() + 1.5);
        }

    }
}
