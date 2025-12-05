package upc.controller;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {

    private final GamePanel gamePanel;
    private final GameManager gameManager;
    private static final int CAMERA_SPEED = 10;
    private Point mousePosition = new Point(0, 0);

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private volatile boolean running = true;
    private Thread gameThread;

    public GameController(GamePanel gamePanel, GameManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;

        addListeners();
        startGameLoop();
    }

    private void addListeners() {
        gamePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
            }
        });

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyPress(e.getKeyCode(), false);
            }
        });

        gamePanel.setFocusable(true);
    }

    private void handleKeyPress(int keyCode, boolean pressed) {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null || !activeAlien.isAlive()) {
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_Q:
                leftPressed = pressed;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightPressed = pressed;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                if (pressed) {
                    gameManager.getWorld().getPhysicsEngine().jumpAlien(activeAlien);
                }
                break;
            case KeyEvent.VK_E:
                Point worldPos = gamePanel.screenToWorld(mousePosition);
                gameManager.getTerrain().makeHole(worldPos.x, worldPos.y, 25);
            default:
                break;
        }
    }

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

    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        int camX = gamePanel.getCameraX();
        int camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        double zoomX = GamePanel.ZOOM;
        double zoomY = GamePanel.ZOOM;

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

        int viewportWidth = (int) (width / zoomX);
        int viewportHeight = (int) (height / zoomY);

        camX = Math.max(0, Math.min(camX, terrainWidth - viewportWidth));
        camY = Math.max(0, Math.min(camY, terrainHeight - viewportHeight));

        gamePanel.setCamera(camX, camY);
    }

    private void update() {
        if (mousePosition != null) {
            handleCameraScroll(mousePosition);
        }

        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien != null && activeAlien.isAlive()) {
            if (leftPressed && !rightPressed) {
                gameManager.getWorld().getPhysicsEngine().moveAlien(activeAlien, -1.0);
            } else if (rightPressed && !leftPressed) {
                gameManager.getWorld().getPhysicsEngine().moveAlien(activeAlien, 1.0);
            } else {
                gameManager.getWorld().getPhysicsEngine().moveAlien(activeAlien, 0);
            }
        }

        gameManager.update(1.0 / 60.0);

        gamePanel.repaint();
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
