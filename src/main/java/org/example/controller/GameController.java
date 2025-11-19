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
                handleCameraScroll(e.getPoint());
            }
        });

    }

    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int limit = 50;
        int camX = gamePanel.getCameraX();
        int terrainWidth = gamePanel.getTerrain().getWidth();


        if (mousePos.x > width - limit) {
            camX += CAMERA_SPEED;
        } else if (mousePos.x < limit) {
            camX -= CAMERA_SPEED;
        }

        camX = Math.max(0, Math.min(camX, terrainWidth - width));

        gamePanel.setCamera(camX);
    }

    public void update() {

    }

}
