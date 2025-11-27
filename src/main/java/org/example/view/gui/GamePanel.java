package org.example.view.gui;

import org.example.model.jeu.Partie;
import org.example.model.jeu.Terrain;
import org.example.model.entites.Worm;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class GamePanel extends JPanel {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;

    private final Partie partie;
    private final Terrain terrain;
    private int cameraX = 0;
    private int cameraY = 0;

    private final AnimationManager animationManager = new AnimationManager();

    public GamePanel(Partie partie) {
        this.partie = partie;
        this.terrain = partie.getTerrain();
        this.setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        // Zoom + ajustement de la caméra
        g2.scale(getZoomX(), getZoomY());
        g2.translate(-cameraX, -cameraY);

        // Affichage du background
        BufferedImage backgroundImage = terrain.getBackground();
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, this);
        }

        // Affichage de l'eau (arrière plan)
        int waterY = DEFAULT_HEIGHT -
                animationManager.getAnimation("water").getFrames().get(0).getHeight() - 15;

        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY, 100);

        // Affichage du terrain
        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        // Affichage des entites
        drawWorms(g2);

        // Affichage de l'eau
        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY + 30, 100);



        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / getZoomX(), 1.0 / getZoomY());

    }

    public void drawWorms(Graphics2D g2) {
        for (Worm w : partie.getMonde().getActiveWorms()) {
            if (w != null && w.isAlive() && w.isVisible(cameraX, cameraY)) {
                int posX = w.getPosition().getX();
                int posY = w.getPosition().getY();

                if (w.getTeam() == partie.getTeam(0)) {
                    BufferedImage sprite = AnimationManager.getZlerp("idle").getFrames().get(0);
                    int drawX = posX - sprite.getWidth() / 2;
                    int drawY = posY - sprite.getHeight() / 2;
                    g2.drawImage(sprite, drawX, drawY, this);
                } else {
                    // Pour quand on aura des animations séparées pour la 2e équipe
                    BufferedImage sprite = AnimationManager.getGlorb("idle").getFrames().get(0);
                    int drawX = posX - sprite.getWidth() / 2;
                    int drawY = posY - sprite.getHeight() / 2;
                    g2.drawImage(sprite, drawX, drawY, this);
                }
            }
        }
    }

    private void drawAnimation(Graphics2D g2, Animation animation, int drawX, int drawY, int speedMs) {

        if (animation != null && !animation.getFrames().isEmpty()) {
            List<BufferedImage> frames = animation.getFrames();

            long timeIndex = System.currentTimeMillis() / speedMs;

            int frameIndex = (int) (timeIndex % frames.size());

            BufferedImage currentFrame = frames.get(frameIndex);

            g2.drawImage(currentFrame, drawX, drawY, this);
        }
    }


    public double getZoomX() {
        return Math.max(1.0, (double) this.getWidth() / DEFAULT_WIDTH);
    }

    public double getZoomY() {
        return Math.max(1.0, (double) this.getHeight() / DEFAULT_HEIGHT);
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
}
