package zelda.graphics;

import zelda.core.Renderable;
import zelda.entities.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UI implements Renderable {
    final Player player;
    final int x = 20, y = 10;

    private static final int BAR_W_SIZE = 50;
    private static final int BAR_H_SIZE = 5;

    public UI(Player player) {
        this.player = player;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, getBarWidth() + 1, BAR_H_SIZE + 1);
        g.setColor(Color.RED);
        g.fillRect(x, y, BAR_W_SIZE, BAR_H_SIZE);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, getBarWidth(), BAR_H_SIZE);

        g.setColor(Color.WHITE);
        final String life = player.getLife() + "/" + player.getMaxLife();
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString(life, x, y + BAR_H_SIZE + 10);

        final BufferedImage heart = SpriteSheet.objectSpriteSheet.getSprite(64, 2, 14, 13);
        g.drawImage(heart, x - 15, y - 5, null);
    }

    private int getBarWidth() {
        return (int)(((double)player.getLife() / (double)player.getMaxLife()) * BAR_W_SIZE);
    }

}
