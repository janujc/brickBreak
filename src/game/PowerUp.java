package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;

// GREEN MORE LIVES
// YELLOW EXTENDS PLATFORM
// BLUE FAST BALL
// ORANGE REDUCES PLATFORM
// RED LOSE GAME

public class PowerUp extends Circle {
    Color myColor;

    public PowerUp() {
        super(0);
        myColor = Color.BLACK;
    }

    public PowerUp(int radius, int level) {
        super(radius);
        Random rand = new Random();
        int generatedPowerUp = rand.nextInt(level);
        Color[] color = {Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.RED};
        myColor = color[generatedPowerUp];
        super.setFill(myColor);
    }
}
