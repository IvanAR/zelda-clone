package zelda.graphics;

import zelda.core.Renderable;
import zelda.entities.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UI implements Renderable {
    final Player player;
    private final int X = 20;
    private final int LIFE_Y = 10;
    private final int STAMINA_Y = 30;

    private static final int BAR_W_SIZE = 50;
    private static final int BAR_H_SIZE = 5;

    public UI(Player player) {
        this.player = player;
    }

    @Override
    public void render(Graphics g) {
        renderLifeBar(g);
        renderStaminaBar(g);
    }

    private void renderLifeBar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(X, LIFE_Y, getLifeBarWidth() + 1, BAR_H_SIZE + 1);
        g.setColor(Color.RED);
        g.fillRect(X, LIFE_Y, BAR_W_SIZE, BAR_H_SIZE);
        g.setColor(Color.GREEN);
        g.fillRect(X, LIFE_Y, getLifeBarWidth(), BAR_H_SIZE);

        g.setColor(Color.WHITE);
        final String life = player.getLife() + "/" + player.getMaxLife();
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString(life, X, LIFE_Y + BAR_H_SIZE + 10);

        final BufferedImage heart = SpriteSheet.objectSpriteSheet.getSprite(64, 2, 14, 13);
        g.drawImage(heart, X - 15, LIFE_Y - 5, null);
    }

    private void renderStaminaBar(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(X, STAMINA_Y, getStaminaBarWidth() + 1, BAR_H_SIZE + 1);
        g.setColor(Color.RED);
        g.fillRect(X, STAMINA_Y, BAR_W_SIZE, BAR_H_SIZE);
        g.setColor(Color.BLUE);
        g.fillRect(X, STAMINA_Y, getStaminaBarWidth(), BAR_H_SIZE);

        g.setColor(Color.WHITE);
        final String stamina = player.getStamina() + "/" + player.getMaxStamina();
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString(stamina, X, STAMINA_Y + BAR_H_SIZE + 10);
    }

    private int getLifeBarWidth() {
        return (int)(((double)player.getLife() / (double)player.getMaxLife()) * BAR_W_SIZE);
    }

    private int getStaminaBarWidth() {
        return (int)(((double)player.getStamina() / (double)player.getMaxStamina()) * BAR_W_SIZE);
    }
}
