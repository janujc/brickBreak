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
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.ANTIQUEWHITE;
    public static final Paint HIGHLIGHT = Color.TAN;
    public static final Paint BALL_COLOR = Color.ORANGE;
    public static final Paint BRICK_COLOR = Color.VIOLET;
    public static final double BALL_RADIUS = 8.0;
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = SIZE - 50.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 30.0;
    public static final double INITIAL_SPEED_X = 1.0;

    /**
     * These next few constants are used when creating the bricks
     */
    public static final int NUM_BRICKS_X = 5;
    public static final int NUM_BRICKS_Y = 5;
    public static final double X_CHANGE = 10.0;
    public static final double Y_CHANGE = 5.0;
    public static final double BRICK_WIDTH = 80.0;
    public static final double BRICK_HEIGHT = 20.0;
    public static final double INITIAL_Y_POS = 25.0;
    public static final double INITIAL_X_POS =
            (SIZE - (NUM_BRICKS_X * BRICK_WIDTH) - ((NUM_BRICKS_X - 1) * X_CHANGE)) / 2.0;
    private double myXPos = INITIAL_X_POS;
    private double myYPos = INITIAL_Y_POS;

    private Scene myScene;
    private Circle myBall;
    private Rectangle myPlatform;
    private Brick myBricks[] = new Brick[NUM_BRICKS_Y * NUM_BRICKS_X];
    private double myBallSpeedX = INITIAL_SPEED_X;
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

    private Scene setupGame (int width, int height, Paint background) {
        var root = new Group();
        Group brickConfig = new Group();
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // make some shapes and set their properties
        myBall = new Circle(BALL_RADIUS, BALL_COLOR);
        myBall.relocate(width / 2 - myBall.getRadius() / 2, height / 2 - myBall.getRadius() / 2);

        myPlatform = new Rectangle(width / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);

        double xPos = myXPos;
        for (int x = 0; x < NUM_BRICKS_X; x++) {
            double yPos = INITIAL_Y_POS;
            myBricks[NUM_BRICKS_X * x] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR);
            brickConfig.getChildren().add(myBricks[NUM_BRICKS_X * x]);
            for (int y = 1; y < NUM_BRICKS_Y; y++) {
                yPos += BRICK_HEIGHT + Y_CHANGE;
                myBricks[NUM_BRICKS_X * x + y] = new Brick(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT, BRICK_COLOR);
                brickConfig.getChildren().add(myBricks[NUM_BRICKS_X * x + y]);
            }
            xPos += BRICK_WIDTH + X_CHANGE;
        }

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);
        root.getChildren().add(brickConfig);

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

        for (int i = 0; i < NUM_BRICKS_X * NUM_BRICKS_Y; i++) {
            var brickBreak = Shape.intersect(myBall, myBricks[i].myBrick);
            if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                myBallSpeedY = -myBallSpeedY;
            }
        }
    }
}
