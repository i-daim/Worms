package upc.controller;

import upc.model.entites.Alien;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import static upc.controller.GameController.CAMERA_SPEED;

public class MouseHandler extends MouseAdapter {
    /** Position actuelle de la souris à l'écran */
    private Point position = new Point(0, 0);

    /** Ensemble des boutons de souris actuellement enfoncés */
    private final HashSet<Integer> pressedButtons = new HashSet<>();

    /** Ensemble des boutons qui viennent d'être pressés cette frame */
    private final HashSet<Integer> justPressedButtons = new HashSet<>();

    /** Ensemble des boutons qui viennent d'être relâchés cette frame */
    private final HashSet<Integer> justReleasedButtons = new HashSet<>();


    /**
     * Appelée automatiquement lorsque la souris se déplace (sans clic).
     *
     * @param e L'événement souris contenant la nouvelle position.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = e.getPoint();
    }

    /**
     * Appelée automatiquement lorsque la souris se déplace avec un bouton enfoncé (drag).
     *
     * @param e L'événement souris contenant la nouvelle position.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        this.position = e.getPoint();
    }

    /**
     * Appelée automatiquement lorsqu'un bouton de souris est enfoncé.
     * <p>
     * Détecte si c'est la première pression (pas une répétition) et l'ajoute
     * aux ensembles des boutons pressés et venant d'être pressés.
     *
     * @param e L'événement souris contenant le bouton pressé.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!pressedButtons.contains(e.getButton())) {
            justPressedButtons.add(e.getButton());
        }
        pressedButtons.add(e.getButton());
    }

    /**
     * Appelée automatiquement lorsqu'un bouton de souris est relâché.
     *
     * @param e L'événement souris contenant le bouton relâché.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        pressedButtons.remove(e.getButton());
        justReleasedButtons.add(e.getButton());
    }


    /**
     * Nettoie l'ensemble des boutons "viennent d'être pressés".
     * <p>
     * Doit être appelée à la fin de chaque frame de jeu.
     */
    public void clearJustPressed() {
        justPressedButtons.clear();
    }

    /**
     * Nettoie l'ensemble des boutons "viennent d'être relâchés".
     * <p>
     * Doit être appelée à la fin de chaque frame de jeu.
     */
    public void clearJustReleased() {
        justReleasedButtons.clear();
    }

    /**
     * Vérifie si la souris est proche d'un bord de l'écran.
     * <p>
     * Cette méthode est utilisée pour déclencher le scroll manuel de la caméra.
     * La zone sensible s'adapte au zoom actuel. Retourne false si le jeu est en pause
     * ou si la souris est trop en haut (zone du bouton Skip).
     *
     * @param gamePanel Le panneau de jeu pour obtenir les dimensions et l'état.
     * @return true si la souris est près d'un bord, false sinon.
     */
    public boolean isMouseNearEdge(GamePanel gamePanel) {
        if (gamePanel.getGameManager().isPaused()) {
            return false;
        }

        if (position == null) {
            return false;
        }
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * gamePanel.getCurrentZoom()) + 50;

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
        if (gamePanel.getGameManager().isPaused()) {
            return;
        }

        if (position == null) {
            return;
        }

        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * gamePanel.getCurrentZoom());

        double camX = gamePanel.getCameraX();
        double camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        int viewportWidth = (int) (width / gamePanel.getCurrentZoom());
        int viewportHeight = (int) (height / gamePanel.getCurrentZoom());

        if (position.x > width - limit && camX < terrainWidth - viewportWidth) {
            camX += CAMERA_SPEED;
        } else if (position.x < limit && camX > 0) {
            camX -= CAMERA_SPEED;
        }

        if (position.y > height - limit && camY < terrainHeight - viewportHeight) {
            camY += CAMERA_SPEED;
        } else if (position.y < limit && camY > 0) {
            camY -= CAMERA_SPEED;
        }

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
