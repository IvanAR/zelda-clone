package zelda.entities;

import zelda.graphics.SpriteSheet;

import java.awt.image.BufferedImage;

public class Book extends Entity{
    public static final BufferedImage BOOK_SPRITE = SpriteSheet.objectSpriteSheet.getSprite(244, 4, 16, 18);

    public Book(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
}
