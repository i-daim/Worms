package org.example.controller;

import org.example.model.entites.Worm;
import org.example.model.jeu.Partie;
import org.example.view.gui.GamePanel;

import java.awt.Point;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {

    private final GamePanel gamePanel;
    private final Partie partie;
    private final Timer gameLoop;
    private static final int CAMERA_SPEED = 15;
    private Point mousePosition = new Point(0, 0);

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GameController(GamePanel gamePanel, Partie partie) {
        this.gamePanel = gamePanel;
        this.partie = partie;

        addListeners();

        this.gameLoop = new Timer(16, e -> update());
        this.gameLoop.start();
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
        Worm activeWorm = partie.getActiveWorm();
        if (activeWorm == null || !activeWorm.isAlive()) {
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
                    partie.getMonde().getPhysicsEngine().jumpWorm(activeWorm);
                }
                break;
            case KeyEvent.VK_E:
                Point worldPos = gamePanel.screenToWorld(mousePosition);
                partie.getTerrain().makeHole(worldPos.x, worldPos.y, 25);
            default:
                // Ne rien faire pour les autres touches
                break;
        }
    }


    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = 50;

        int camX = gamePanel.getCameraX();
        int camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        double zoomX = gamePanel.getZoomX();
        double zoomY = gamePanel.getZoomY();

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

    public void update() {
        if (mousePosition != null) {
            handleCameraScroll(mousePosition);
        }

        Worm activeWorm = partie.getActiveWorm();
        if (activeWorm != null && activeWorm.isAlive()) {
            if (leftPressed && !rightPressed) {
                partie.getMonde().getPhysicsEngine().moveWorm(activeWorm, -1.0);
            } else if (rightPressed && !leftPressed) {
                partie.getMonde().getPhysicsEngine().moveWorm(activeWorm, 1.0);
            } else {
                partie.getMonde().getPhysicsEngine().moveWorm(activeWorm, 0);
            }
        }

        partie.update(1.0 / 60.0);

        gamePanel.repaint();
    }
}
