package game;

import javafx.scene.paint.Color;

public class PowerUpReduce extends PowerUp {
    public Color myColor;

    public PowerUpReduce(int radius) {
        super(radius);
        myColor = Color.ORANGE;
    }
}
