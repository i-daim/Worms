package upc.controller;
import upc.model.jeu.Team;


import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.view.gui.GameOverPanel;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class GameController {

    private final KeyboardHandler keyHandler = new KeyboardHandler();
    private final MouseHandler mouseHandler = new MouseHandler();

    private final GamePanel gamePanel;
    private final GameManager gameManager;

    private static final int CAMERA_SPEED = 10;

    private volatile boolean running = true;
    private Thread gameThread;

    // Constructeur //
    public GameController(GamePanel gamePanel, GameManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;

        addListeners();
        startGameLoop();
    }

    private void addListeners() {
        gamePanel.addKeyListener(keyHandler);
        gamePanel.addMouseMotionListener(mouseHandler);
        gamePanel.setFocusable(true);
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
        // Gestion de la caméra à travers mouseHandler
        handleCameraScroll(mouseHandler.getPosition());
        // Gestion des touches clavier et de l'inventaire
        handleControls();

        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien != null && activeAlien.isAlive()) {
            gamePanel.centralizeCamera(activeAlien);
        }

        gameManager.update(1.0 / 60.0);

        if (gameManager.isOver()) {
            showGameOver();
            return;
        }
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_A)) {
            if (!gameManager.isActionDone() && activeAlien != null) {
                activeAlien.useEquippedItem();
                System.out.println("🎯 TIR DÉCLENCHÉ UNE FOIS !");
            }
        }
        keyHandler.clearJustPressed();
        gamePanel.repaint();
    }


    ////    GESTION DES ENTREES SOURIS/CLAVIER    ////
    private void handleControls() {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null || !activeAlien.isAlive()) {
            return;
        }

        if (keyHandler.isJustKeyPressed(KeyEvent.VK_I)) {
            gamePanel.toggleInventory();
        }

        if (gamePanel.isInventoryVisible()) {
            for (int i = 0; i < 5; i++) {
                int keyCode = KeyEvent.VK_1 + i;

                if (keyHandler.isJustKeyPressed(keyCode)) {
                    activeAlien.setSelectedItemIndex(i);
                }
            }
        }

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

        if (keyHandler.isJustKeyPressed(KeyEvent.VK_E)) {
            Point worldPos = gamePanel.screenToWorld(mouseHandler.getPosition());
            gameManager.getWorld().explode(worldPos.x, worldPos.y, 30, 30);
        }
    }

    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        int camX = gamePanel.getCameraX();
        int camY = gamePanel.getCameraY();
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

}