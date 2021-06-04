package zelda.world.tile.colliding;

import zelda.world.tile.Tile;

import java.awt.image.BufferedImage;

public class Wall extends Tile implements CollidingTile {
    public Wall(BufferedImage sprite, int x, int y) {
        super(sprite, x, y);
    }
}
