package edu.hm.hafner.sokoban;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import static edu.hm.hafner.sokoban.Field.*;
import static edu.hm.hafner.sokoban.Orientation.*;

/**
 * Entry point for Sokoban.
 *
 * @author Ullrich Hafner
 */
public class Sokoban extends JPanel {
    /**
     * Starts Sokoban.
     *
     * @param args
     *         optional level file name
     */
    public static void main(final String... args) {
        AbstractSokobanModel level;
        if (args.length == 1) { // use level name of the command line argument #1
            level = new SokobanReader().read(args[0].replace(".sok", ""), read(args[0]));
        }
        else if (args.length == 0) { // use built in level
            level = createLevel();
        }
        else {
            throw new IllegalArgumentException("Usage: java GameLoop [level-file-name]");
        }

        javax.swing.SwingUtilities.invokeLater(() -> showLevel(level));
    }

    private static List<String> read(final String fileName) {
        try {
            return Files.readAllLines(Paths.get("target/classes/" + fileName));
        }
        catch (IOException exception) {
            throw new IllegalArgumentException("Level file not found: " + fileName);
        }
    }

    private static SokobanGameModel createLevel() {
        Field[][] fields = {
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, TARGET, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
        };
        SokobanGameModel sokoban = new SokobanGameModel("Demo Level");
        sokoban.setLevel(fields);
        sokoban.setPlayer(new Point(3, 4));
        sokoban.addTreasure(new Point(2, 4));
        sokoban.addTreasure(new Point(4, 5));
        sokoban.validate();
        return sokoban;
    }

    @SuppressWarnings("checkstyle:AnonInnerLength")
    private static void showLevel(final AbstractSokobanModel sokoban) {
        String name = sokoban.getName();
        SokobanGameRenderer painter = new SokobanGameRenderer();
        BufferedImage bitmap = painter.toImage(sokoban, DOWN);
        Sokoban game = new Sokoban(bitmap);
        game.setFocusable(true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setSize(bitmap.getWidth(), bitmap.getHeight() + 32);
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setTitle(createTitleMessage(sokoban, name));
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                    sokoban.moveLeft();
                    game.setOrientation(LEFT);
                }
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    sokoban.moveRight();
                    game.setOrientation(RIGHT);
                }
                else if (event.getKeyCode() == KeyEvent.VK_UP) {
                    sokoban.moveUp();
                    game.setOrientation(UP);
                }
                else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    sokoban.moveDown();
                    game.setOrientation(DOWN);
                }
                else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                game.setImage(painter.toImage(sokoban, game.getOrientation()));
                frame.setTitle(createTitleMessage(sokoban, name));
                frame.repaint();
            }
        });
    }

    private static String createTitleMessage(final AbstractSokobanModel sokoban, final String name) {
        return name + " - " + sokoban.toString();
    }

    /**
     * Sets the image of this frame.
     *
     * @param image
     *         the new image
     */
    protected void setImage(final BufferedImage image) {
        this.image = image;
        repaint();
    }

    private transient BufferedImage image;
    private Orientation orientation = DOWN;

    /**
     * Creates a new instance of {@link Sokoban}.
     *
     * @param image
     *         the image to show
     */
    Sokoban(final BufferedImage image) {
        super();

        this.image = image;
    }

    /**
     * Drawing an image can allow for more flexibility in processing/editing.
     */
    @Override
    protected void paintComponent(final Graphics graphics) {
        graphics.drawImage(image, 0, 0, this);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Draws the Sokoban game state into an image.
     *
     * @author Ullrich Hafner
     */
    private static class SokobanGameRenderer {
        private static final int BLOCK_SIZE = 64;

        /**
         * Returns the Sokoban level as an image.
         *
         * @return an image of this board
         */
        public BufferedImage toImage(final AbstractSokobanModel sokoban, final Orientation orientation) {
            BufferedImage boardImage = new BufferedImage(sokoban.getWidth() * BLOCK_SIZE,
                    sokoban.getHeight() * BLOCK_SIZE, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < sokoban.getHeight(); y++) {
                for (int x = 0; x < sokoban.getWidth(); x++) {
                    drawImage(boardImage, asImageName(sokoban.getField(new Point(x, y))), x, y);
                }
            }
            drawImage(boardImage, sokoban.getPlayer(), asImageName(orientation));
            PointSet treasures = sokoban.getTreasures();
            for (int i = 0; i < treasures.size(); i++) {
                drawImage(boardImage, treasures.get(i), "treasure");
            }

            return boardImage;
        }

        private String asImageName(final Enum<?> type) {
            return type.name().toLowerCase();
        }

        private void drawImage(final BufferedImage boardImage, final Point position, final String imageName) {
            drawImage(boardImage, imageName, position.getX(), position.getY());
        }

        private void drawImage(final BufferedImage boardImage, final String imageName, final int x, final int y) {
            boardImage.createGraphics().drawImage(loadImage(imageName + ".png"),
                    x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
        }

        private static BufferedImage loadImage(final String imageName) {
            try {
                return readImageFromStream(imageName);
            }
            catch (IOException exception) {
                throw new IllegalArgumentException("Can't read image " + imageName, exception);
            }
        }

        private static BufferedImage readImageFromStream(final String fileName) throws IOException {
            InputStream stream = SokobanGameRenderer.class.getResourceAsStream("/" + fileName);
            if (stream == null) {
                throw new IllegalArgumentException("Can't find image " + fileName);
            }
            return ImageIO.read(stream);
        }
    }
}
