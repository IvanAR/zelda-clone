package zelda.entities;

import zelda.graphics.SpriteSheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LifeHeart extends Entity {
    public static final BufferedImage LIFE_HEART_SPRITE = SpriteSheet.objectSpriteSheet.getSprite(2, 51, 12, 10);
    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_HEIGHT = 10;

    private int frames = 0;
    private final int maxFps = 7;
    private int heartAnimationIndex = 0;
    private final int heartAnimationMaxIndex = 4;
    private final BufferedImage[] heartAnimation  = new BufferedImage[4];
    protected double x, y, width, height;
    private final BufferedImage sprite;

    public LifeHeart(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        for (int i = 0; i < heartAnimation.length; i++) {
            heartAnimation[i] = SpriteSheet.objectSpriteSheet.getSprite(i * 16, 51, 16, 10);
        }
    }

    @Override
    public void render(final Graphics g) {
        frames++;
        if (frames == maxFps) {
            frames = 0;
            heartAnimationIndex++;
            if (heartAnimationIndex == heartAnimationMaxIndex) {
                heartAnimationIndex = 0;
            }
        }
//        fillRect for debugging purposes
//        g.setColor(Color.BLACK);
//        g.fillRect(getXCamera(), getYCamera(), getMaskW(), getMaskH());
        g.drawImage(heartAnimation[heartAnimationIndex], getXCamera(), getYCamera(), null);
    }
}
