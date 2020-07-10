package zelda.world;

import zelda.entities.*;
import zelda.entities.weapon.Bow;
import zelda.graphics.SpriteSheet;
import zelda.window.Window;
import zelda.world.tile.Floor;
import zelda.world.tile.Tile;
import zelda.world.tile.Wall;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static zelda.world.tile.Tile.*;
import static zelda.world.tile.Tile.TILE_SIZE;

public class World {
    private static Tile[] tiles;
    public static int HEIGHT, WIDTH;

    public World(final String path, final Player player, final List<Entity> entities, final List<Enemy> enemies) {
        try {
            final BufferedImage map = ImageIO.read(getClass().getResource(path));
            tiles = new Tile[map.getWidth() * map.getHeight()];
            HEIGHT = map.getHeight();
            WIDTH = map.getWidth();
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    final int pixelColor = map.getRGB(x, y);
                    tiles[x + (y * WIDTH)] = new Floor(TILE_FLOOR,x * TILE_SIZE, y * TILE_SIZE);
                    // TODO apply a switch case
                    if (pixelColor == 0xFFFFFFFF){
                        tiles[x + (y * WIDTH)] = new Wall(TILE_WALL,x * TILE_SIZE, y * TILE_SIZE);
                    } else if (pixelColor == 0xFF0026FF) {
                        player.setX(x * TILE_SIZE);
                        player.setY(y * TILE_SIZE);
                    } else if (pixelColor == 0xFFff0000) {
                        final Enemy enemy = new Enemy(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, 20, SpriteSheet.enemySpriteSheet, player);
                        entities.add(enemy);
                        enemies.add(enemy);
                    } else if (pixelColor == 0xFF1fff00) {
                        entities.add(new LifeHeart(x * TILE_SIZE, y * TILE_SIZE, LifeHeart.DEFAULT_WIDTH, LifeHeart.DEFAULT_HEIGHT, LifeHeart.LIFE_HEART_SPRITE));
                    } else if (pixelColor == 0xFF6000ff) {
                        entities.add(new Bow(x * TILE_SIZE, y * TILE_SIZE, Bow.WIDTH, Bow.HEIGHT, Bow.BOW_SPRITES[Bow.LEFT_BOW]));
                    } else if (pixelColor == 0xFFfff200) {
                        entities.add(new Book(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, 20, Book.BOOK_SPRITE));
                        tiles[x + (y * WIDTH)] = new Floor(TILE_FLOOR,x * TILE_SIZE, y * TILE_SIZE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFree(int nextX, int nextY) {
        // FIXME left colision is shit.
        int x1 = (nextX + 1) / TILE_SIZE;
        int y1 = (nextY + 14) / TILE_SIZE;

        int x2 = (nextX + TILE_SIZE) / TILE_SIZE;
        int y2 = (nextY + 14) / TILE_SIZE;

        int x3 = (nextX + 4) / TILE_SIZE;
        int y3 = (nextY + 7 + TILE_SIZE ) / TILE_SIZE;

        int x4 = (nextX + 4 + TILE_SIZE - 1)  / TILE_SIZE;
        int y4 = (nextY + 7 + TILE_SIZE ) / TILE_SIZE;

        return !((tiles[x1 + (y1 * WIDTH)] instanceof Wall) ||
                 (tiles[x2 + (y2 * WIDTH)] instanceof Wall) ||
                 (tiles[x3 + (y3 * WIDTH)] instanceof Wall) ||
                 (tiles[x4 + (y4 * WIDTH)] instanceof Wall));
    }

    public void render(final Graphics g) {
        int xStart = Camera.x / TILE_SIZE;
        int xFinal = xStart + (Window.WIDTH / TILE_SIZE);
        int yStart = Camera.y / TILE_SIZE;
        int yFinal = yStart + (Window.HEIGHT / TILE_SIZE);

        for (int x = xStart; x <= xFinal; x++) {
            for (int y = yStart; y <= yFinal; y++) {
                if (y < 0 || x < 0 || y >= HEIGHT || x >= WIDTH)
                    continue;
                final Tile tile = tiles[x + ( y * WIDTH)];
                if (tile != null)
                    tile.render(g);
            }
        }
    }
}
