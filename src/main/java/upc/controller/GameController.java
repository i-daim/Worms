package upc.controller;

import upc.model.entites.projectiles.Projectile;
import upc.model.items.Item;
import upc.model.jeu.Team;
import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.save.GameSave;
import upc.model.items.Jetpack;
import upc.view.gui.GameOverPanel;
import upc.view.gui.GamePanel;
import upc.view.gui.PausePanel;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class GameController {

    private final KeyboardHandler keyHandler = new KeyboardHandler();
    private final MouseHandler mouseHandler = new MouseHandler();

    private final GamePanel gamePanel;
    private final GameManager gameManager;
    private final PauseActions pauseActions;

    private static final int CAMERA_SPEED = 10;

    private volatile boolean running = true;
    private boolean paused = false;
    private Thread gameThread;
    private int frameCount;

    // Constructeur //
    public GameController(GamePanel gamePanel, GameManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;
        this.pauseActions = new PauseActions(this, gamePanel);

        addListeners();
        startGameLoop();
    }

    private void addListeners() {
        gamePanel.addKeyListener(keyHandler);
        gamePanel.addMouseMotionListener(mouseHandler);
        gamePanel.setFocusable(true);

        PausePanel pausePanel = gamePanel.getPausePanel();
        pausePanel.getResumeButton().addActionListener(pauseActions.getResumeAction());
        pausePanel.getMainMenuButton().addActionListener(pauseActions.getMainMenuAction());
        pausePanel.getSettingsButton().addActionListener(pauseActions.getSettingsAction());
        pausePanel.getQuitButton().addActionListener(pauseActions.getQuitAction());
    }

////    GESTION DE LA BOUCLE DU JEU    ////
    private void startGameLoop() {
        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double targetFps = 60.0;
            final double nsPerUpdate = 1_000_000_000.0 / targetFps;

            while (running) {
                long now = System.nanoTime();
                double delta = (now - lastTime) / nsPerUpdate;
                lastTime = now;

                if (delta > 0) {
                    update();
                }

                try {
                    long sleepNs = (long) (nsPerUpdate - (System.nanoTime() - now));
                    long sleepTime = sleepNs / 1_000_000;
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void update() {
        handlePause();
        if (paused) {
            return;
        }

        handleCameraScroll(mouseHandler.getPosition());
        handleControls();

        // Ne centre pas la caméra si la souris est sur le bord
        if (!mouseHandler.isMouseNearEdge(gamePanel)) {
            Alien activeAlien = gameManager.getActiveAlien();
            if (gameManager.isPaused()) {
                gamePanel.unzoomCamera();
            } else if (activeAlien != null && activeAlien.isAlive()) {
                gamePanel.centerCamera(activeAlien);
            }
        }
        if (gameManager.getActiveProjectile() != null) {
            gamePanel.centerCamera(gameManager.getActiveProjectile());
        }

        gameManager.update(1.0 / 60.0);

        if (gameManager.isOver()) {
            showGameOver();
            return;
        }

        keyHandler.clearJustPressed();
        gamePanel.repaint();
    }

////    GESTION DES ENTREES SOURIS/CLAVIER    ////
    private void handleControls() {
        Alien activeAlien = gameManager.getActiveAlien();
        Item equipped = activeAlien.getEquippedItem();
        boolean canShoot = !gameManager.isActionDone();

        if (!activeAlien.isAlive()) {
            return;
        }

        // Gestion de l'inventaire
        if (gamePanel.isInventoryVisible()) {
            for (int i = 0; i < 6; i++) {
                int keyCode = KeyEvent.VK_1 + i;

                if (keyHandler.isJustKeyPressed(keyCode)) {
                    activeAlien.setSelectedItemIndex(i);
                }
            }
        }

        // Gestion des mouvements
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_SPACE)) {
            activeAlien.jump();
        }
        if (keyHandler.isKeyPressed(KeyEvent.VK_LEFT)) {
            activeAlien.moveLeft();
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_RIGHT)) {
            activeAlien.moveRight();
        } else {
            activeAlien.idle();
        }

        // Gestion de la visée
        if (keyHandler.isKeyPressed(KeyEvent.VK_UP)) {
            int angle = activeAlien.getAimAngle() + 2;
            if (angle > 80) {
                angle = 80;
            }
            activeAlien.setAimAngle(angle);
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_DOWN)) {
            int angle = activeAlien.getAimAngle() - 2;
            if (angle < -45) {
                angle = -45;
            }
            activeAlien.setAimAngle(angle);
        }

        // Utilisation de l'item
        if (equipped != null && canShoot) {
            if (equipped instanceof Jetpack || equipped instanceof upc.model.items.Uzi) {
                if (keyHandler.isKeyPressed(KeyEvent.VK_A)) {
                    if (frameCount % 5 == 0) {
                        activeAlien.useEquippedItem();
                        frameCount = 0;
                    }
                    frameCount++;
                }
            } else {
                if (keyHandler.isJustKeyPressed(KeyEvent.VK_A)) {
                    Projectile p = activeAlien.useEquippedItem();
                    gameManager.setActiveProjectile(p);
                }
            }
        }

        // Touche pour la sauvegarde
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_F5)) {
            try {
                saveGame();
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde du jeu : " + e.getMessage());
            }
        }
    }

    private void handleCameraScroll(Point mousePos) {
        if (mousePos == null) {
            return;
        }

        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        double camX = gamePanel.getCameraX();
        double camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        if (mousePos.x > width - limit) {
            camX += CAMERA_SPEED;
        } else if (mousePos.x < limit) {
            camX -= CAMERA_SPEED;
        }

        if (mousePos.y > height - limit) {
            camY += CAMERA_SPEED;
        } else if (mousePos.y < limit) {
            camY -= CAMERA_SPEED;
        }

        int viewportWidth = (int) (width / GamePanel.ZOOM);
        int viewportHeight = (int) (height / GamePanel.ZOOM);

        camX = Math.max(0, Math.min(camX, terrainWidth - viewportWidth));
        camY = Math.max(0, Math.min(camY, terrainHeight - viewportHeight));

        gamePanel.setCamera(camX, camY);
    }

////    ARRET DU JEU    ////
    public void handlePause() {
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_ESCAPE)) {
            togglePause();
        }
        gamePanel.togglePauseMenu(paused);
        if (paused) {
            keyHandler.clearJustPressed();
        }
    }

    public void togglePause() {
        paused = !paused;
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void showGameOver() {
        stop();
        gamePanel.stopMusic();
        Team winner = gameManager.getWinningTeam();

        SwingUtilities.invokeLater(() -> {
            GameOverPanel gameOverPanel = new GameOverPanel(winner);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
            if (frame != null) {
                frame.setContentPane(gameOverPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    /**
     * Méthode permettant la sauvegarde du jeu
     */
    public void saveGame() throws IOException {
        GameSave gameSave = new GameSave(gameManager);
        gameSave.save();
    }
}