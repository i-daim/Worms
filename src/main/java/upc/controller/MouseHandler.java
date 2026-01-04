package upc.controller;

import upc.model.entites.Alien;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import static upc.controller.GameController.CAMERA_SPEED;

public class MouseHandler extends MouseAdapter {

    private Point position = new Point(0, 0);
    private final HashSet<Integer> pressedButtons = new HashSet<>();
    private final HashSet<Integer> justPressedButtons = new HashSet<>();
    private final HashSet<Integer> justReleasedButtons = new HashSet<>();


////    EVENTS    ////
    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.position = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!pressedButtons.contains(e.getButton())) {
            justPressedButtons.add(e.getButton());
        }
        pressedButtons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedButtons.remove(e.getButton());
        justReleasedButtons.add(e.getButton());
    }


////    FONCTIONS    ////
    public void clearJustPressed() {
        justPressedButtons.clear();
    }

    public void clearJustReleased() {
        justReleasedButtons.clear();
    }

    public boolean isMouseNearEdge(GamePanel gamePanel) {
        if (position == null) {
            return false;
        }
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM) + 50;

        // empêche la cam de se déplacer quand on appuie sur le bouton "Skip"
        if (position.y < 50) {
            return false;
        }

        return position.x > width - limit || position.x < limit
                || position.y > height - limit || position.y < limit;
    }

    public void handleAim(Alien activeAlien, double cameraX, double cameraY, double zoom) {
        if (position != null) {
            // Convertit la position de la souris en coordonnées utilisables
            double worldX = cameraX + position.x / zoom;
            double worldY = cameraY + position.y / zoom;

            // Calcule l'angle entre l'alien et la souris
            double deltaX = worldX - activeAlien.getPosition().getX();
            double deltaY = worldY - activeAlien.getPosition().getY();

            // L'axe pour Y est inversé donc on inverse Y
            double angleRad = Math.atan2(-deltaY, deltaX);
            int angleDeg = (int) Math.toDegrees(angleRad);

            // Ajuste l'angle et la direction de l'alien
            if (deltaX < 0) {
                activeAlien.setDirection(false);
                angleDeg = 180 - Math.abs(angleDeg);
                if (deltaY > 0) {
                    angleDeg = -angleDeg;
                }
            } else {
                activeAlien.setDirection(true);
            }

            // Pour limiter l'angle
            if (angleDeg > 80) {
                angleDeg = 80;
            }
            if (angleDeg < -80) {
                angleDeg = -80;
            }

            activeAlien.setAimAngle(angleDeg);
        }
    }

    public void handleCameraScroll(GamePanel gamePanel) {
        if (position == null) {
            return;
        }

        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        double camX = gamePanel.getCameraX();
        double camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        if (position.x > width - limit) {
            camX += CAMERA_SPEED;
        } else if (position.x < limit) {
            camX -= CAMERA_SPEED;
        }

        if (position.y > height - limit) {
            camY += CAMERA_SPEED;
        } else if (position.y < limit) {
            camY -= CAMERA_SPEED;
        }

        int viewportWidth = (int) (width / GamePanel.ZOOM);
        int viewportHeight = (int) (height / GamePanel.ZOOM);

        camX = Math.max(0, Math.min(camX, terrainWidth - viewportWidth));
        camY = Math.max(0, Math.min(camY, terrainHeight - viewportHeight));

        gamePanel.setCamera(camX, camY);
    }


////    GETTERS    ////
    public Point getPosition() {
        return position;
    }

    public boolean isPressed(int buttonCode) {
        return pressedButtons.contains(buttonCode);
    }

    public boolean isJustPressed(int buttonCode) {
        return justPressedButtons.contains(buttonCode);
    }

    public boolean isJustReleased(int buttonCode) {
        return justReleasedButtons.contains(buttonCode);
    }

}
