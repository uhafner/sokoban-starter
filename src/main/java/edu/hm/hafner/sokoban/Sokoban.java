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

import edu.hm.hafner.sokoban.HighScoreService.SystemOutPrinter;
import edu.hm.hafner.sokoban.LevelInformationBoard.AttemptResult;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static edu.hm.hafner.sokoban.Field.*;
import static edu.hm.hafner.sokoban.Orientation.*;

/**
 * Entry point for Sokoban.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class Sokoban extends JPanel {
    private static final String START_MESSAGE = "Press UP, DOWN, LEFT or RIGHT to start";

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

        SwingUtilities.invokeLater(() -> showLevel(level));
    }

    private static List<String> read(final String fileName) {
        try {
            return Files.readAllLines(Paths.get("target/classes/" + fileName));
        }
        catch (IOException exception) {
            throw new IllegalArgumentException("Level file not found: " + fileName, exception);
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

    private static void showLevel(final AbstractSokobanModel sokoban) {
        String name = sokoban.getName();

        LevelInformationBoard levelScore = new LocalLevelInformationBoard(name);
        LocalHighScoreService uploadHighScore = new LocalHighScoreService();

        SokobanGameRenderer painter = new SokobanGameRenderer();
        BufferedImage bitmap = painter.toImage(sokoban, DOWN);
        Sokoban game = new Sokoban(bitmap);
        game.setFocusable(true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);

        JLabel statusBar = new JLabel(START_MESSAGE);
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");

        addRestartMenu(sokoban, name, levelScore, painter, game, frame, statusBar, menu);
        addPrintMenu(uploadHighScore, menu);
        menu.addSeparator();
        addExitMenu(menu);
        menubar.add(menu);

        frame.setJMenuBar(menubar);
        frame.setSize(bitmap.getWidth(), bitmap.getHeight() + 32);
        frame.setFocusable(true);
        frame.setVisible(true);
        frame.setTitle(name);

        frame.addKeyListener(new SokobanGameLoop(sokoban, levelScore, game, painter, frame, name, uploadHighScore, statusBar));
    }

    private static void addPrintMenu(final LocalHighScoreService uploadHighScore, final JMenu menu) {
        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(e -> uploadHighScore.printBoards(new SystemOutPrinter()));
        menu.add(print);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private static void addRestartMenu(final AbstractSokobanModel sokoban, final String name,
            final LevelInformationBoard levelScore, final SokobanGameRenderer painter, final Sokoban game,
            final JFrame frame, final JLabel statusBar, final JMenu menu) {
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> {
            sokoban.reset();
            levelScore.startNewAttempt();

            game.setOrientation(DOWN);
            game.setImage(painter.toImage(sokoban, game.getOrientation()));

            statusBar.setText(START_MESSAGE);
            frame.setTitle(name);
            frame.repaint();
        });
        menu.add(restart);
    }

    @SuppressFBWarnings("DM_EXIT")
    private static void addExitMenu(final JMenu menu) {
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menu.add(exit);
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
    static class SokobanGameRenderer {
        private static final int BLOCK_SIZE = 64;

        /**
         * Returns the Sokoban level as an image.
         *
         * @param sokoban
         *         the model
         * @param orientation
         *         current orientation
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
            List<Point> treasures = sokoban.getTreasures();
            for (Point treasure : treasures) {
                drawImage(boardImage, treasure, "treasure");
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

        static BufferedImage loadImage(final String fileName) {
            try (InputStream stream = SokobanGameRenderer.class.getResourceAsStream("/" + fileName)) {
                if (stream == null) {
                    throw new IllegalArgumentException("Can't find image " + fileName);
                }
                return ImageIO.read(stream);
            }
            catch (IOException exception) {
                throw new IllegalArgumentException("Can't read image " + fileName, exception);
            }
        }
    }

    private static class SokobanGameLoop extends KeyAdapter {
        private final AbstractSokobanModel sokoban;
        private final LevelInformationBoard levelScore;
        private final Sokoban game;
        private final SokobanGameRenderer painter;
        private final JFrame frame;
        private final String name;
        private final LocalHighScoreService uploadHighScore;
        private final JLabel statusBar;

        @SuppressWarnings("checkstyle:ParameterNumber")
        SokobanGameLoop(final AbstractSokobanModel sokoban, final LevelInformationBoard levelScore,
                final Sokoban game,
                final SokobanGameRenderer painter, final JFrame frame, final String name,
                final LocalHighScoreService uploadHighScore, final JLabel statusBar) {
            this.sokoban = sokoban;
            this.levelScore = levelScore;
            this.game = game;
            this.painter = painter;
            this.frame = frame;
            this.name = name;
            this.uploadHighScore = uploadHighScore;
            this.statusBar = statusBar;
        }

        @Override
        public void keyPressed(final KeyEvent event) {
            if (!sokoban.isSolved()) {
                if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                    sokoban.moveLeft();
                    levelScore.recordMove(LEFT);
                    game.setOrientation(LEFT);
                    checkIfSolved();
                }
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    sokoban.moveRight();
                    levelScore.recordMove(RIGHT);
                    game.setOrientation(RIGHT);
                    checkIfSolved();
                }
                else if (event.getKeyCode() == KeyEvent.VK_UP) {
                    sokoban.moveUp();
                    levelScore.recordMove(UP);
                    game.setOrientation(UP);
                    checkIfSolved();
                }
                else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    sokoban.moveDown();
                    levelScore.recordMove(DOWN);
                    game.setOrientation(DOWN);
                    checkIfSolved();
                }
                game.setImage(painter.toImage(sokoban, game.getOrientation()));
                frame.setTitle(name);
                frame.repaint();
            }
        }

        private void checkIfSolved() {
            if (sokoban.isSolved()) {
                game.setOrientation(SOLVED);
                AttemptResult result = levelScore.finishLevel();
                uploadHighScore.registerSolution("Ulli Hafner",
                        levelScore.getName(), levelScore.getHighScore(), levelScore.getAttempts(),
                        levelScore.printMoves());
                statusBar.setText(createMessage(result));

                SwingUtilities.invokeLater(() -> {
                    ImageIcon icon = new ImageIcon(SokobanGameRenderer.loadImage("solved.png"));
                    JOptionPane.showMessageDialog(frame,
                            createMessage(result),
                            "Level solved",
                            JOptionPane.INFORMATION_MESSAGE,
                            icon);

                });
            }
            else {
                statusBar.setText(String.format("Number of attempts: %d,  number of moves: %d",
                        levelScore.getAttempts(), levelScore.getMoves()));
            }
        }

        private String createMessage(final AttemptResult result) {
            String message = String.format(
                    "Congratulations!!!%nLevel «%s» solved%nin %d moves",
                    sokoban.getName(), levelScore.getMoves());
            if (result == AttemptResult.NEW_HIGH_SCORE) {
                return message + "\nThis is a new High Score!!!";
            }
            if (result == AttemptResult.HIGH_SCORE_SET) {
                return message + "\nSame moves as High Score.";
            }
            return message + "\nPrevious High Score: " + levelScore.getHighScore() + " moves.";
        }
    }
}
