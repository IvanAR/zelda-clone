package zelda.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    public static final SpriteSheet objectSpriteSheet = new SpriteSheet("/spritesheet/world/objects.png");
    public static final SpriteSheet playerSpriteSheet = new SpriteSheet("/spritesheet/player/link_spritesheet.png");
    public static final SpriteSheet enemySpriteSheet = new SpriteSheet("/spritesheet/world/npc.png");

    private BufferedImage spriteSheet;

    public SpriteSheet(String path) {
        try {
            this.spriteSheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            // TODO fallBack here ?
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spriteSheet.getSubimage(x, y, width, height);
    }
}
