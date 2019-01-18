package game;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.scene.Node;

public abstract class Sprite {

    public List animations = new ArrayList<>();
    public Node node;
    public double dX = 0;
    public double dY = 0;
    public boolean hasLives = true;

    public abstract void update();

    public boolean collide(Sprite other) {
        return false;
    }
}