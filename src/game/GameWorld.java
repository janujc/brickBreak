package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This application demonstrates a JavaFX 2.x Game Loop.
 * Shown below are the methods which comprise of the fundamentals to a
 * simple game loop in JavaFX:
 *
 *  <strong>initialize()</strong> - Initialize the game world.
 *  <strong>beginGameLoop()</strong> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <strong>updateSprites()</strong> - Updates the sprite objects each period (per frame)
 *  <strong>checkCollisions()</strong> - Method will determine objects that collide with each other.
 *  <strong>cleanupSprites()</strong> - Any sprite objects needing to be removed from play.
 *
 * @author cdea
 */
public abstract class GameWorld {

    /**
     * The JavaFX Scene as the game surface
     */
    private Scene gameSurface;
    /**
     * All nodes to be displayed in the game window.
     */
    private Group sceneNodes;
    /**
     * The game loop using JavaFX's <code>Timeline</code> API.
     */
    private static Timeline gameLoop;
    private final int framesPerSecond;
    private final String windowTitle;

    /**
     * Constructor that is called by the derived class. This will
     * set the frames per second, title, and setup the game loop.
     *
     * @param fps   - Frames per second.
     * @param title - Title of the application window.
     */
    public GameWorld(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        final Duration oneFrameAmt = Duration.millis(1000 / getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
            new EventHandler() {

                @Override
                public void handle(javafx.event.ActionEvent event) {

                    // update actors
                    updateSprites();

                    // check for collision
                    checkCollisions();

                    // removed dead things
                    cleanupSprites();

                }
            }); // oneFrame

        // sets the game world's game loop (Timeline)
        setGameLoop(TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build());
    }

    /**
     * Initialize the game world by update the JavaFX Stage.
     *
     * @param primaryStage
     */
    public abstract void initialize(final Stage primaryStage);

    /**
     * Kicks off (plays) the Timeline objects containing one key frame
     * that simply runs indefinitely with each frame invoking a method
     * to update sprite objects, check for collisions, and cleanup sprite
     * objects.
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }

    /**
     * Updates each game sprite in the game world. This method will
     * loop through each sprite and passing it to the handleUpdate()
     * method. The derived class should override handleUpdate() method.
     */
    protected void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleUpdate(sprite);
        }
    }

    /**
     * Updates the sprite object's information to position on the game surface.
     *
     * @param sprite - The sprite to update.
     */
    protected void handleUpdate(Sprite sprite) {

    }

    /**
     * Checks each game sprite in the game world to determine a collision
     * occurred. The method will loop through each sprite and
     * passing it to the handleCollision()
     * method. The derived class should override handleCollision() method.
     */
    protected void checkCollisions() {
        // check other sprite's collisions
        spriteManager.resetCollisionsToCheck();
        // check each sprite against other sprite objects.
        for (Sprite spriteA : spriteManager.getCollisionsToCheck()) {
            for (Sprite spriteB : spriteManager.getAllSprites()) {
                if (handleCollision(spriteA, spriteB)) {
                    // The break helps optimize the collisions
                    //  The break statement means one object only hits another
                    // object as opposed to one hitting many objects.
                    // To be more accurate comment out the break statement.
                    break;
                }
            }
        }
    }

    /**
     * When two objects collide this method can handle the passed in sprite
     * objects. By default it returns false, meaning the objects do not
     * collide.
     *
     * @param spriteA - called from checkCollision() method to be compared.
     * @param spriteB - called from checkCollision() method to be compared.
     * @return boolean True if the objects collided, otherwise false.
     */
    protected boolean handleCollision(Circle circle, Brick brick) { //FIXME
        return false;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    protected static void setGameLoop(Timeline gameLoop) {
        GameWorld.gameLoop = gameLoop;
    }

    public Scene getGameSurface() {
        return gameSurface;
    }

    /**
     * Sets the JavaFX Scene. This is called the game surface to
     * allow the developer to add JavaFX Node objects onto the Scene.
     *
     * @param gameSurface The main game surface (JavaFX Scene).
     */
    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    protected void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }
}
