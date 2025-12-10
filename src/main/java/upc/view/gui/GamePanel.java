package upc.view.gui;

import upc.assets.*;
import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class GamePanel extends JPanel {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;
    public static final double ZOOM = 2;

    private final GameManager gameManager;
    private final Terrain terrain;
    private final HUDPanel hudPanel;
    private int cameraX = 0;
    private int cameraY = 0;

    private List<ActiveAnimation> activeAnimations;

    private final Sound gameMusic = new Sound("/sounds/game_music.wav");

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.terrain = gameManager.getTerrain();
        this.hudPanel = new HUDPanel(gameManager);

        activeAnimations = gameManager.getActiveAnimations();
        this.setDoubleBuffered(true);
        gameMusic.loop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        // Zoom + ajustement de la caméra
        g2.scale(ZOOM, ZOOM);
        g2.translate(-cameraX, -cameraY);

        // Affichage du background
        BufferedImage backgroundImage = terrain.getBackground();
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, this);
        }

        // Affichage de l'eau (arrière plan)
        int waterY = terrain.getHeight() - (Terrain.WATER_LEVEL);
        Animation waterAnim = AnimationManager.getAnimation("water");
        drawAnimation(g2, waterAnim, 0, waterY, 0, 100, true);

        // Affichage du terrain
        g2.drawImage(terrain.getImage(), 0, 0, this);

        // Affichage des entites
        drawAliens(g2);

        // Affichage de l'eau (avant-plan)
        drawAnimation(g2, waterAnim, 0, waterY + 30, 0, 100, true);

        // Affichage des barres de vie
        hudPanel.drawHealthBars(g2, gameManager, cameraX, cameraY);

        // Affichage des animations du jeu
        drawActiveAnimations(g2);

        // Annulation du zoom
        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / ZOOM, 1.0 / ZOOM);

        // Affichage du HUD (après le zoom car non concerné)
        hudPanel.setSize(getWidth(), 100);
        hudPanel.paintComponent(g2);

        g2.dispose();
    }

    public Point screenToWorld(Point screenPoint) {
        double worldX = (screenPoint.x / ZOOM) + cameraX;
        double worldY = (screenPoint.y / ZOOM) + cameraY;
        return new Point((int) worldX, (int) worldY);
    }


    public void drawAliens(Graphics2D g2) {
        for (Alien alien : gameManager.getWorld().getActiveWorms()) {
            if (alien != null && alien.isAlive() && alien.isVisible(cameraX, cameraY)) {
                if (alien.getTeam() == gameManager.getTeam(0)) {
                    drawAlien(g2, alien, 0);
                } else {
                    drawAlien(g2, alien, 1);
                }
            }
        }
    }

    public void drawActiveAnimations(Graphics2D g2) {
        for (ActiveAnimation active : activeAnimations) {
            drawAnimation(g2, active.getAnimation(), active.getDrawX(), active.getDrawY(),
                    active.getStartTime(), active.getFrameTime(), false);
        }
    }

    private void drawAnimation(Graphics2D g2, Animation anim, int x, int y,
                               long startTime, int speedMs, boolean loop) {
        if (anim == null || anim.getFrames().isEmpty()) {
            return;
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        int animSize = anim.getFrames().size();

        int frameIndex = (int) (elapsedTime / speedMs);

        frameIndex = loop
                ? frameIndex % animSize
                : Math.min(frameIndex, animSize - 1);

        g2.drawImage(anim.getFrames().get(frameIndex), x, y, this);
    }

    public void drawAlien(Graphics2D g2, Alien alien, int type) {
        Animation animation;
        boolean direction = alien.getDirection();
        switch (alien.getState()) {
            case WALK:
                animation = AnimationManager.getSprite(type, "walk", direction);
                break;
            case JUMP:
                animation = AnimationManager.getSprite(type, "jump", direction);
                break;
            default:
                animation = AnimationManager.getSprite(type, "idle", direction);
                break;
        }

        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();

        int drawX = posX - 10;
        int drawY = posY - 18;

        drawAnimation(g2, animation, drawX, drawY, 0, 100, true);
    }

    public void centralizeCamera(Alien alien) {
        if (alien == null) {
            return;
        }
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();

        int viewX = posX - (int) (getWidth() / (2 * ZOOM));
        int viewY = posY - (int) (getHeight() / (2 * ZOOM));

        int maxX = (int) (terrain.getWidth() - getWidth() / ZOOM);
        int maxY = (int) (terrain.getHeight() - getHeight() / ZOOM);

        int camX = Math.max(0, Math.min(viewX, maxX));
        int camY = Math.max(0, Math.min(viewY, maxY));
        setCamera(camX, camY);
    }


    public void setCamera(int x, int y) {
        cameraX = x;
        cameraY = y;
        repaint();
    }

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void stopMusic() {
        gameMusic.stop();
    }
}
