package game;

import java.util.*;

public class SpriteManager {
    private static final ArrayList<Sprite> SPRITES = new ArrayList<>();
    private static final List CHECK_COLLISION_LIST = new ArrayList<>();
    private static final Set CLEAN_UP_SPRITES = new HashSet<>();

    public List getSprites() {
        return SPRITES;
    }

    /**
     * VarArgs of sprite objects to be added to the game.
     * @param sprites
     */
    public void addSprites(Sprite... sprites) {
        SPRITES.addAll(Arrays.asList(sprites));
    }

    /**
     * VarArgs of sprite objects to be removed from the game.
     * @param sprites
     */
    public void removeSprites(Sprite... sprites) {
        SPRITES.removeAll(Arrays.asList(sprites));
    }

    /** Returns a set of sprite objects to be removed from the SPRITES.
     * @return CLEAN_UP_SPRITES
     */
    public Set getSpritesToBeRemoved() {
        return CLEAN_UP_SPRITES;
    }

    public void addSpritesToBeRemoved(Sprite... sprites) {
        if (sprites.length > 1) {
            CLEAN_UP_SPRITES.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            CLEAN_UP_SPRITES.add(sprites[0]);
        }
    }

    public List getCollisionsToCheck() {
        return CHECK_COLLISION_LIST;
    }

    public void resetCollisionsToCheck() {
        CHECK_COLLISION_LIST.clear();
        CHECK_COLLISION_LIST.addAll(SPRITES);
    }

    public void cleanupSprites() {
        SPRITES.removeAll(CLEAN_UP_SPRITES);
        CLEAN_UP_SPRITES.clear();
    }
}