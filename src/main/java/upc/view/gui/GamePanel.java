package upc.view.gui;

import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;
import upc.view.Sound;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    private final AnimationManager animationManager = new AnimationManager();

    private final Sound gameMusic = new Sound("/sounds/game_music.wav");

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.terrain = gameManager.getTerrain();
        this.hudPanel = new HUDPanel(gameManager);
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
        int waterY = DEFAULT_HEIGHT
                - animationManager.getAnimation("water").getFrames().get(0).getHeight() - 15;

        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY, 100);

        // Affichage du terrain
        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        // Affichage des entites
        drawAliens(g2);

        // Affichage de l'eau
        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY + 30, 100);

        // Affichage des barres de vie
        hudPanel.drawHealthBars(g2, gameManager, cameraX, cameraY);

        // Affichage des animations du jeu
        ArrayList<ActiveAnimation> activeAnimations = gameManager.getActiveAnimations();
        for (ActiveAnimation active : activeAnimations) {
            active.update();
            if (active.isDone()) {
                activeAnimations.remove(active);
            } else {
                g2.drawImage(
                        active.getCurrentFrame(), active.getDrawX(), active.getDrawY(), this);
            }
        }

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
        for (Alien w : gameManager.getWorld().getActiveWorms()) {
            if (w != null && w.isAlive() && w.isVisible(cameraX, cameraY)) {
                int posX = w.getPosition().getX();
                int posY = w.getPosition().getY();

                if (w.getTeam() == gameManager.getTeam(0)) {
                    drawAlien(g2, w, 0);
                } else {
                    // Pour quand on aura des animations séparées pour la 2e équipe
                    drawAlien(g2, w, 1);
                }
            }
        }
    }

    private void drawAnimation(Graphics2D g2, Animation animation,
                               int drawX, int drawY, int speedMs) {

        if (animation != null && !animation.getFrames().isEmpty()) {
            List<BufferedImage> frames = animation.getFrames();

            long timeIndex = System.currentTimeMillis() / speedMs;

            int frameIndex = (int) (timeIndex % frames.size());

            BufferedImage currentFrame = frames.get(frameIndex);

            g2.drawImage(currentFrame, drawX, drawY, this);
        }
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

        drawAnimation(g2, animation, drawX, drawY, 100);
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
