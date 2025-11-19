package org.example.controller;

import org.example.view.gui.GamePanel;

import java.awt.Point;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {

    private final GamePanel gamePanel;
    private final Timer gameLoop;
    private static final int CAMERA_SPEED = 10;
    private Point mousePosition = new Point(0, 0);

    public GameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

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

    }

    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = 50;
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

        camX = Math.max(0, Math.min(camX, terrainWidth - width));
        camY = Math.max(0, Math.min(camY, terrainHeight - height));

        gamePanel.setCamera(camX, camY);
    }

    public void update() {
        if (mousePosition != null) {
            handleCameraScroll(mousePosition);
        }
    }

}
