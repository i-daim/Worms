package upc.controller;

import upc.model.entites.Alien;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import static upc.controller.GameController.CAMERA_SPEED;
/**
 * Gestionnaire des entrées souris pour le jeu.
 * <p>
 * Cette classe hérite de {@link MouseAdapter} et gère :
 * <ul>
 *     <li>La position de la souris à l'écran</li>
 *     <li>Les clics (pressés, maintenus, relâchés)</li>
 *     <li>La visée automatique vers la souris</li>
 *     <li>Le scroll de caméra quand la souris est près des bords</li>
 * </ul>
 * <p>
 * Comme pour le clavier, les états "vient d'être pressé/relâché" doivent être
 * nettoyés manuellement à chaque frame via {@link #clearJustPressed()} et
 * {@link #clearJustReleased()}.
 */

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

    /**
     * Calcule et applique l'angle de visée de l'alien actif vers la position de la souris.
     * <p>
     * Cette méthode effectue les conversions suivantes :
     * <ol>
     *     <li>Convertit la position souris écran → coordonnées monde</li>
     *     <li>Calcule l'angle entre l'alien et la souris</li>
     *     <li>Ajuste la direction de l'alien (gauche/droite)</li>
     *     <li>Limite l'angle entre -80° et +80° pour réalisme</li>
     * </ol>
     *
     * @param activeAlien L'alien dont on veut définir la visée.
     * @param cameraX     La position X de la caméra (pour conversion écran → monde).
     * @param cameraY     La position Y de la caméra (pour conversion écran → monde).
     * @param zoom        Le niveau de zoom actuel (pour conversion écran → monde).
     */
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

    /**
     * Déplace la caméra manuellement lorsque la souris est près des bords.
     * <p>
     * La caméra se déplace à vitesse constante ({@link GameController#CAMERA_SPEED})
     * vers le bord survolé. Les déplacements sont limités pour ne pas sortir du terrain.
     *
     * @param gamePanel Le panneau de jeu contenant la caméra et le terrain.
     */
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



    /**
     * Retourne la position actuelle de la souris à l'écran.
     *
     * @return La position de la souris en pixels.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Vérifie si un bouton de souris est actuellement enfoncé (maintenu).
     *
     * @param buttonCode Le code du bouton (ex: {@link MouseEvent#BUTTON1}).
     * @return true si le bouton est enfoncé, false sinon.
     */
    public boolean isPressed(int buttonCode) {
        return pressedButtons.contains(buttonCode);
    }

    /**
     * Vérifie si un bouton de souris vient d'être pressé cette frame.
     *
     * @param buttonCode Le code du bouton (ex: {@link MouseEvent#BUTTON1}).
     * @return true si le bouton vient d'être pressé, false sinon.
     */
    public boolean isJustPressed(int buttonCode) {
        return justPressedButtons.contains(buttonCode);
    }


    /**
     * Vérifie si un bouton de souris vient d'être relâché cette frame.
     *
     * @param buttonCode Le code du bouton (ex: {@link MouseEvent#BUTTON1}).
     * @return true si le bouton vient d'être relâché, false sinon.
     */
    public boolean isJustReleased(int buttonCode) {
        return justReleasedButtons.contains(buttonCode);
    }

}
