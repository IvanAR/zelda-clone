package zelda.world.tile;

import zelda.graphics.SpriteSheet;
import zelda.world.Camera;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
    public static final SpriteSheet spriteSheet = new SpriteSheet("/spritesheet/world/overworld.png");// FIXME load tiles here
    public static final BufferedImage TILE_FLOOR = spriteSheet.getSprite(0, 0, 16, 16); // FIXME positions
    public static final BufferedImage TILE_WALL = spriteSheet.getSprite(370, 60, 16, 16); // FIXME positions

    public static final int TILE_SIZE = 16;
    private final BufferedImage sprite;
    protected int x, y;

    public Tile(BufferedImage sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}
