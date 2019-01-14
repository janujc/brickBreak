package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Januario Carreiro
 */
public class Game extends Application{
    public static final String TITLE = "Breakout";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.ANTIQUEWHITE;
    public static final Paint HIGHLIGHT = Color.TAN;
    public static final Paint BALL_COLOR = Color.ORANGE;
    public static final Paint TARGET_COLOR = Color.VIOLET;
    public static final String BALL_IMAGE = "ball.gif";
    public static final double PLATFORM_WIDTH = 100.0;
    public static final double PLATFORM_Y = 375.0;
    public static final double PLATFORM_HEIGHT = 10.0;
    public static final double PLATFORM_SPEED = 20.0;
    public static final double TARGET_WIDTH = 50.0;
    public static final double TARGET_Y = 25.0;
    public static final double TARGET_HEIGHT = 10.0;

    private Scene myScene;
    private ImageView myBall;
    private Rectangle myPlatform;
    private Rectangle myTarget;
    private double myBallSpeedX;
    private double myBallSpeedY;
    private int myDirectionX = 1;
    private int myDirectionY = 1;

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
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBall = new ImageView(image);
        // x and y represent the top left corner, so center it

        myBall.setX(width / 2 - myBall.getBoundsInLocal().getWidth() / 2);
        myBall.setY(height / 2 - myBall.getBoundsInLocal().getHeight() / 2);
        myBall.setX(200.0);
        myBall.setY(200.0);
        myPlatform = new Rectangle(width / 2 - (PLATFORM_WIDTH / 2), PLATFORM_Y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT);
        myTarget = new Rectangle(width / 2 - (TARGET_WIDTH / 2), TARGET_Y, TARGET_WIDTH, TARGET_HEIGHT);
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
        if (code == KeyCode.RIGHT) {
            myPlatform.setX(myPlatform.getX() + PLATFORM_SPEED);
        }
        else if (code == KeyCode.LEFT) {
            myPlatform.setX(myPlatform.getX() - PLATFORM_SPEED);
        }
    }

    private void step (double elapsedTime) {
        // update attributes
        myBall.setX(myBall.getX() + myBallSpeedX * elapsedTime);
        myBall.setY(myBall.getY() + myBallSpeedY * elapsedTime);

        if (myBall.getX() <= 0 || myBall.getX() >= myScene.getWidth()) {
            myDirectionX *= -1;
        }

        if (myBall.getY() <= 0 || myBall.getY() >= myScene.getHeight()) {
            myDirectionY *= -1;
        }
    }
}
