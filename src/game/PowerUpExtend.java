package game;

import javafx.scene.paint.Color;

public class PowerUpExtend extends PowerUp {
    public Color myColor;

    public PowerUpExtend(int radius) {
        super(radius);
        myColor = Color.RED;
    }
}
