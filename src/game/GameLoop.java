package game;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.Random;
import java.util.List;

/**
 * @author Januario Carreiro
 * This class runs the game from start to finish. Dependencies: Brick and PowerUp classes.
 * <p>
 * To use, simply run this class.
 * <p>
 * Methods sorted by order of appearance. Most changes one makes should be limited to the constants and instance
 * variables. Would not recommend changes to methods after and including step. All other changes that break the game
 * should be easy to fix/modify. DO NOT modify BRICK_W without also modifying SIZE.
 */
public class GameLoop extends Application {
    private static final String TITLE = "Breakout";
    private static final int SIZE = 700;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Color BACKGROUND_C = Color.ANTIQUEWHITE;
    private static final Color HIGHLIGHT_C = Color.TAN.darker();
    private static final Color BALL_C = Color.DARKORANGE;
    private static final Color[] POWERUP_COLOR =
            {Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.RED, Color.BLACK};
    private static final double BALL_RADIUS = 8.0;
    private static final double PLATFORM_Y_POS = SIZE - 50.0;
    private static final double PLATFORM_WIDTH_INITIAL = 100.0;
    private static final double PLATFORM_WIDTH_INCREASE = 50.0;
    private static final double PLATFORM_HEIGHT = 10.0;
    private static final double PLATFORM_SPEED = 25.0;
    private static final double INITIAL_SPEED_Y = 200.0;
    private static final double POWER_SPEED_Y = 100.0;
    private static final double INITIAL_SPEED_X = 0.0;
    private static final double SPEED_Y_CHANGE = 20.0;
    private static final double REBOUND_SPEED_X_CHANGE = 0.2;
    private static final double REBOUND_SPEED_X = 1.0;
    private static final double X_CHANGE = 10.0;
    private static final double Y_CHANGE = 25.0;
    private static final double BRICK_W = 90.0;
    private static final double BRICK_H = 10.0;
    private static final double INITIAL_Y_POS = 0.0;
    private static final double Y_POS_DIFFICULTY = 35.0;
    private static final int NUM_COLS = 6;
    private static final int NUM_ROWS = 6;
    private static final double INITIAL_X_POS =
            (SIZE - (NUM_COLS * BRICK_W) - ((NUM_COLS - 1) * X_CHANGE)) / 2.0;
    private static final int NUM_LEVELS = 5;
    private static final int DISTANCE_BETWEEN_TEXT = 25;
    private static final int Y_OFFSET = 5;
    private static final int X_OFFSET_SCORE = 215;
    private static final int SCORE_PER_BRICK = 10;
    private static final int POWERUP_RADIUS = 4;
    private static final Media sound1 = new Media(new File("BounceBrick.wav").toURI().toString());
    private static final Media sound2 = new Media(new File("PlatformBounce.wav").toURI().toString());

    private Scene myScene;
    private Group root = new Group();
    private Stage myStage;
    private Timeline animation;
    private VBox vbox;
    private HBox myDisplayBox;
    private Circle myBall;
    private Rectangle myPlatform;
    private Text myScoreVal;
    private Brick[][] myBrickConfig = new Brick[NUM_COLS][NUM_ROWS];
    private PowerUp[] myPower = new PowerUp[NUM_COLS * NUM_ROWS];
    private double myPlatformWidth = PLATFORM_WIDTH_INITIAL;
    private int myPowerNumber = 0;
    private int myLives = 3;
    private int myLevel = 1;
    private int myScore = 0;
    private int myNumBricks;
    private double myBallSpeedX = INITIAL_SPEED_X;
    private double myBallSpeedY = INITIAL_SPEED_Y;
    private double myReboundSpeedRatio = REBOUND_SPEED_X;
    private Scene levelScene = new Scene(root, SIZE, SIZE, BACKGROUND_C);

    /**
     * Launches game. DO NOT modify.
     * @param args is used by JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Necessary for game to work. DO NOT modify. We have made an instance variable myStage in order to be able to
     * change which scene we are in later on by using stage.setScene() again.
     * @param stage is used by JavaFX.
     */
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

    private void buttonClick(int level) {
        myScene = levelSelect(level);
        myStage.setScene(myScene);
    }

    private Scene levelSelect(int level) {
        levelScene.setRoot(root);

        makeDisplayBox();

        myBall = new Circle(BALL_RADIUS, BALL_C);
        myBall.relocate(SIZE / 2 - myBall.getRadius() / 2, 400);

        myPlatform = new Rectangle(SIZE / 2 - (myPlatformWidth / 2), PLATFORM_Y_POS, myPlatformWidth, PLATFORM_HEIGHT);
        myPlatform.setFill(HIGHLIGHT_C);

        myBrickConfig = makeBricks(level);

        root.getChildren().add(myBall);
        root.getChildren().add(myPlatform);

        levelScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        animationPlay();

        return levelScene;
    }

    private void makeDisplayBox() {
        Text livesText = new Text("LIVES: " + myLives);
        Text levelText = new Text("LEVEL: " + myLevel);
        Text scoreText = new Text("SCORE: ");
        myDisplayBox = new HBox(DISTANCE_BETWEEN_TEXT, livesText, levelText, scoreText);
        myDisplayBox.setPadding(new Insets(Y_OFFSET));
        root.getChildren().add(myDisplayBox);
    }

    private Brick[][] makeBricks(int levelSelect) {
        double xPos = INITIAL_X_POS;
        switch (levelSelect) {
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
        myNumBricks = (NUM_COLS * (NUM_COLS + 1)) / 2;
    }

    private void levelThreeBuilder(double xPos, int levelSelect) {
        for (int x = 0; x < NUM_COLS; x++) {
            double yPos = INITIAL_Y_POS + Y_POS_DIFFICULTY * levelSelect;
            for (int y = 0; y < NUM_ROWS; y++) {
                yPos += BRICK_H + Y_CHANGE;
                myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                if (y == 0 || x == 0 || y == NUM_ROWS - 1 || x == NUM_COLS - 1) {
                    myBrickConfig[x][y].setHealth(5);
                } else myBrickConfig[x][y].setHealth(3);
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
                } else {
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
                if (y == NUM_COLS - 1) {
                    myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                    myBrickConfig[x][y].setPermanent();
                } else {
                    myBrickConfig[x][y] = new Brick(xPos, yPos, BRICK_W, BRICK_H);
                    myBrickConfig[x][y].setHealth(3);
                }
                root.getChildren().add(myBrickConfig[x][y]);
            }
            xPos += BRICK_W + X_CHANGE;
        }
        myNumBricks = NUM_COLS * (NUM_ROWS - 1);
    }

    private void handleKeyInput(KeyCode code) {
        checkForCheats(code);
        if (code == KeyCode.D && (myPlatform.getX() + myPlatform.getWidth() < myScene.getWidth())) {
            myPlatform.setX(myPlatform.getX() + PLATFORM_SPEED);
        } else if (code == KeyCode.A && (myPlatform.getX() > 0)) {
            myPlatform.setX(myPlatform.getX() - PLATFORM_SPEED);
        }
    }

    private void checkForCheats(KeyCode code) {
        if (code == KeyCode.DIGIT1) {
            myLevel = 1;
            changeLevels(0);
        }
        if (code == KeyCode.DIGIT2) {
            myLevel = 2;
            changeLevels(0);
        }
        if (code == KeyCode.DIGIT3) {
            myLevel = 3;
            changeLevels(0);
        }
        if (code == KeyCode.DIGIT4) {
            myLevel = 4;
            changeLevels(0);
        }
        if (code == KeyCode.DIGIT5) {
            myLevel = 5;
            changeLevels(0);
        }
        if (code == KeyCode.J) {
            if (myBallSpeedY < 0) myBallSpeedY = (-1) * INITIAL_SPEED_Y / 2;
            else myBallSpeedY = INITIAL_SPEED_Y / 2;
        }
        if (code == KeyCode.K) {
            extendPlatform(true);
        }
        if (code == KeyCode.L) {
            myLives++;
            root.getChildren().remove(myDisplayBox);
            makeDisplayBox();
        }
    }

    private void extendPlatform(boolean bool) {
        root.getChildren().remove(myPlatform);
        if (bool) {
            myPlatformWidth += PLATFORM_WIDTH_INCREASE;
            myPlatform.setX(myPlatform.getX() - PLATFORM_WIDTH_INCREASE / 2);
        } else {
            myPlatformWidth -= PLATFORM_WIDTH_INCREASE;
            myPlatform.setX(myPlatform.getX() + PLATFORM_WIDTH_INCREASE / 2);
        }
        myPlatform.setWidth(myPlatformWidth);
        root.getChildren().add(myPlatform);
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

    private void changeLevels(int num) {
        this.animation.stop();
        myPlatformWidth = PLATFORM_WIDTH_INITIAL;
        myBallSpeedX = INITIAL_SPEED_X;
        myBallSpeedY = INITIAL_SPEED_Y + SPEED_Y_CHANGE * myLevel;
        myReboundSpeedRatio = REBOUND_SPEED_X + REBOUND_SPEED_X_CHANGE * myLevel;
        root.getChildren().clear();
        if (num == 0) {
            myScene = betweenLevelsScreen();
            myStage.setScene(myScene);
        } else if (num == 1) {
            myScene = droppedBallScreen();
            myStage.setScene(myScene);
        } else if (num == 2) {
            myScene = gameOverScreen();
            myStage.setScene(myScene);
        } else if (num == 3) {
            myScene = endGameScreen();
            myStage.setScene(myScene);
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

    private Scene gameOverScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t1 = new Text(10, 20, "GAME OVER");
        VBox textBox = new VBox(0, t1);
        textBox.setPadding(new Insets(50));
        textBox.setAlignment(Pos.CENTER);
        pane.setCenter(textBox);

        Button restartButton = new Button("Restart Game");

        vbox = new VBox(0, restartButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        myLives = 3;
        myLevel = 1;
        myScore = 0;

        restartButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    private Scene endGameScreen() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: ANTIQUEWHITE");
        Text t1 = new Text(10, 20, "CONGRATULATIONS, YOU'VE WON!");
        VBox textBox = new VBox(0, t1);
        textBox.setPadding(new Insets(50));
        textBox.setAlignment(Pos.CENTER);
        pane.setCenter(textBox);

        Button restartButton = new Button("Restart Game");

        vbox = new VBox(0, restartButton);
        vbox.setPadding(new Insets(100));
        vbox.setAlignment(Pos.CENTER);

        pane.setBottom(vbox);

        myLives = 3;
        myLevel = 1;
        myScore = 0;

        restartButton.setOnAction(e -> buttonClick(myLevel));
        return new Scene(pane, SIZE, SIZE);
    }

    /**
     * Most important method in GameLoop. Most objects are removed from root after this method, and PowerUp objects are
     * added to game through this method. This method is also used to determine when lives are lost and when the game
     * ends. Be very careful when changing---could break game.
     */
    private void step() {
        myBall.setLayoutX(myBall.getLayoutX() + myBallSpeedX * GameLoop.SECOND_DELAY);
        myBall.setLayoutY(myBall.getLayoutY() + myBallSpeedY * GameLoop.SECOND_DELAY);

        if (myBall.getLayoutY() + myBall.getRadius() >= myScene.getHeight()) {
            if (myLives == 0) {
                changeLevels(2);
                return;
            }
            myLives--;
            changeLevels(1);
        }

        if (myBall.getLayoutX() <= myBall.getRadius() || myBall.getLayoutX() + myBall.getRadius() >= myScene.getWidth()) {
            myBallSpeedX = -myBallSpeedX;
            MediaPlayer mediaPlayer2 = new MediaPlayer(sound2);
            mediaPlayer2.play();
        }

        if (myBall.getLayoutY() <= myBall.getRadius()) {
            myBallSpeedY = -myBallSpeedY;
            MediaPlayer mediaPlayer2 = new MediaPlayer(sound2);
            mediaPlayer2.play();
        }

        var intersect = Shape.intersect(myBall, myPlatform);
        Random rand = new Random();
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myBallSpeedY = -myBallSpeedY;
            MediaPlayer mediaPlayer2 = new MediaPlayer(sound2);
            mediaPlayer2.play();
            if (myBall.getLayoutX() + myBall.getRadius() <= myPlatform.getX() + (myPlatform.getWidth() / 2.0)) {
                myBallSpeedX = -Math.abs(myReboundSpeedRatio * (rand.nextDouble() * 100 + 50));
            } else myBallSpeedX = myReboundSpeedRatio * (rand.nextDouble() * 100 + 50);
        }

        powerUpStep();

        brickBounce();

        updateScore();

        if (myNumBricks == 0) {
            if (myLevel == 5) {
                changeLevels(3);
                return;
            }
            myLevel++;
            changeLevels(0);
        }
    }

    private void powerUpStep() {
        for (int i = 0; i < myPowerNumber; i++) {
            myPower[i].setLayoutY(myPower[i].getLayoutY() + POWER_SPEED_Y * GameLoop.SECOND_DELAY);
            if (myPower[i].powerUpIntersect(myPlatform)) {
                root.getChildren().remove(myPower[i]);
                Color powerColor = myPower[i].myColor;
//                myPower[i] = new PowerUp();
                determinePowerUp(powerColor);
            }
        }
    }

    private void generatePowerUp(int x, int y) {
        Random rand = new Random();
        double chance = rand.nextDouble();
        if (chance <= 0.5) {
            int power = rand.nextInt(myLevel);
            myPower[myPowerNumber] = makePowerUp(power);
            myPower[myPowerNumber].relocate(myBrickConfig[x][y].getX(), myBrickConfig[x][y].getY());
            root.getChildren().add(myPower[myPowerNumber]);
            myPowerNumber++;
        }
    }

    private PowerUp makePowerUp(int i) {
         List<PowerUp> POSSIBLE_POWERUPS = List.of(
            new PowerUpLife(POWERUP_RADIUS),
            new PowerUpExtend(POWERUP_RADIUS),
            new PowerUpFast(POWERUP_RADIUS),
            new PowerUpReduce(POWERUP_RADIUS),
            new PowerUpEnd(POWERUP_RADIUS)
        );
         return POSSIBLE_POWERUPS.get(i);
    }

    // FIXME WE WILL NOT NEED THIS, JUST MAKE A METHOD CALL TO POWERUP AND IT WILL KNOW WHAT TO DO
    private void determinePowerUp(Color color) {
        if (POWERUP_COLOR[0] == color) {
            myLives++;
            root.getChildren().remove(myDisplayBox);
            makeDisplayBox();
        }
        if (POWERUP_COLOR[1] == color) {
            extendPlatform(true);
        }
        if (POWERUP_COLOR[2] == color) {
            myBallSpeedY *= 1.25;
        }
        if (POWERUP_COLOR[3] == color) {
            extendPlatform(false);
        }
        if (POWERUP_COLOR[4] == color) {
            changeLevels(2);
        }
    }

    private void brickBounce() {
        for (int x = 0; x < NUM_COLS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                var brickBreak = Shape.intersect(myBall, myBrickConfig[x][y]);
                if (brickBreak.getBoundsInLocal().getWidth() != -1) {
                    myBallSpeedY = -myBallSpeedY;
                    MediaPlayer mediaPlayer1 = new MediaPlayer(sound1);
                    mediaPlayer1.play();
                    myBrickConfig[x][y].reduceHealth();
                    if (myBrickConfig[x][y].isDestroyed()) {
                        myNumBricks--;
                        myScore += SCORE_PER_BRICK;
                        root.getChildren().remove(myBrickConfig[x][y]);
                        generatePowerUp(x, y);
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
}