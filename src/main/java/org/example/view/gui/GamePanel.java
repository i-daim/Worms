package org.example.view.gui;

import org.example.model.jeu.Partie;
import org.example.model.jeu.Terrain;
import org.example.model.entites.Worm;

import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

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
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Zoom + ajustement de la caméra
        g2.scale(getZoomX(), getZoomY());
        g2.translate(-cameraX, -cameraY);

        // Affichage du terrain
        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        // Affichage des entites
        drawWorms(g2);


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
                    g2.drawImage(sprite, posX, posY, this);
                } else {
                    // Pour quand on aura des animations séparées pour la 2e équipe
                    BufferedImage sprite = AnimationManager.getGlorb("idle").getFrames().get(0);
                    g2.drawImage(sprite, posX, posY, this);
                }
            }
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