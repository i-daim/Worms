package upc.view.gui;

import upc.assets.Animation;
import upc.assets.AnimationManager;
import upc.assets.ActiveAnimation;
import upc.assets.SoundManager;
import upc.model.entites.Alien;
import upc.model.entites.Entity;
import upc.model.items.Item;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;
import upc.assets.ImageManager;
import upc.model.entites.projectiles.Projectile;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.util.List;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;
    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final double ZOOM = 1.5 * SCREEN_WIDTH / DEFAULT_WIDTH;

    private double currentZoom = ZOOM;

    private final GameManager gameManager;
    private final Terrain terrain;
    private final HUDPanel hudPanel;
    private final PausePanel pausePanel;

    private double cameraX = 0;
    private double cameraY = 0;

    private final List<ActiveAnimation> activeAnimations;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.terrain = gameManager.getTerrain();
        this.hudPanel = new HUDPanel(gameManager);
        this.pausePanel = new PausePanel();

        this.setLayout(null);
        this.add(pausePanel);
        this.add(hudPanel);

        activeAnimations = gameManager.getActiveAnimations();
        this.setDoubleBuffered(true);

        SoundManager.playMusic("Game Theme");
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
        g2.scale(currentZoom, currentZoom);
        g2.translate(-cameraX, -cameraY);

        // Affichage du background
        BufferedImage backgroundImage = terrain.getBackground();
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, this);
        }

        // Affichage de l'eau (arrière plan)
        int waterY = terrain.getHeight() - (Terrain.WATER_LEVEL);

        String waterKey = terrain.getWaterAnimationKey();
        Animation waterAnim;

        if (waterKey != null && waterKey.contains("poison")) {
            waterAnim = AnimationManager.getAnimation("water_poison");
        } else if (waterKey != null && waterKey.contains("hell")) {
            waterAnim = AnimationManager.getAnimation("water_hell");
        } else {
            waterAnim = AnimationManager.getAnimation("water");
        }

        drawAnimation(g2, waterAnim, 0, waterY, 0, 100, true);

        // Affichage du terrain
        g2.drawImage(terrain.getImage(), 0, 0, this);

        // Affichage des entites
        drawAliens(g2);

        // Affichage des Projectiles
        drawProjectiles(g2);

        // Affichage des barres de vie
        hudPanel.drawAlienInfo(g2, gameManager);

        // Affichage des animations du jeu
        drawActiveAnimations(g2);

        // Affichage de l'eau (avant-plan)
        drawAnimation(g2, waterAnim, 0, waterY + 30, 0, 100, true);

        // Annulation du zoom
        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / currentZoom, 1.0 / currentZoom);

        // Affichage du HUD (après le zoom car non concerné)
        hudPanel.setBounds(0, 0, getWidth(), getHeight());

        g2.dispose();
    }


    ////    METHODES D'AFFICHAGE    ////
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

    public void drawAliens(Graphics2D g2) {
        for (Alien alien : gameManager.getWorld().getActiveAliens()) {
            if (alien != null && alien.isAlive()) {
                if (alien.getTeam() == gameManager.getTeam(0)) {
                    drawAlien(g2, alien, 0);
                } else {
                    drawAlien(g2, alien, 1);
                }
            }
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

        int drawX = posX - animation.getFrames().get(0).getWidth() / 2;
        int drawY = posY - animation.getFrames().get(0).getHeight() / 2;

        drawAnimation(g2, animation, drawX, drawY, 0, 100, true);
        drawAlienWeapon(g2, alien, direction, posX, posY);
    }

    public void drawAlienWeapon(Graphics2D g2, Alien alien, boolean direction,
                                int posX, int posY) {
        Item currentItem = alien.getEquippedItem();
        if (currentItem == null) {
            return;
        }

        BufferedImage weaponImage = ImageManager.get(currentItem.getName());

        if (weaponImage != null) {
            AffineTransform oldTransform = g2.getTransform();

            double angleRad = Math.toRadians(alien.getAimAngle());

            if (!direction) {
                angleRad = Math.PI - angleRad;
            }
            int pivotX = posX;
            int pivotY = posY - 5;
            g2.translate(pivotX, pivotY);
            g2.rotate(-angleRad);
            int weaponW = weaponImage.getWidth();
            int weaponH = weaponImage.getHeight();
            if (direction) {
                g2.drawImage(weaponImage, 0, -weaponH / 2, null);
            } else {
                g2.scale(1, -1);
                g2.drawImage(weaponImage, 0, -weaponH / 2, null);
            }

            // Restauration de la transformation
            g2.setTransform(oldTransform);
        }
    }

    private void drawProjectiles(Graphics2D g2) {
        var projectileList = gameManager.getWorld().getActiveProjectiles();
        var projectiles = new ArrayList<>(projectileList);

        for (Projectile p : projectiles) {
            if (p == null) {
                continue;
            }
            int x = p.getPosition().getX();
            int y = p.getPosition().getY();
            double angle;
            Animation anim = AnimationManager.getProjectile(p);
            if (anim != null && !anim.getFrames().isEmpty()) {
                int frameIndex = p.getFrameCount() % anim.getFrames().size();
                BufferedImage frame = anim.getFrames().get(frameIndex);
                AffineTransform oldTransform = g2.getTransform();
                if (p.getBody() != null) {
                    org.dyn4j.geometry.Vector2 velocity = p.getBody().getLinearVelocity();
                    angle = Math.atan2(velocity.y, velocity.x);
                } else {
                    angle = Math.toRadians(p.getShooter().getAimAngle());
                }
                g2.translate(x, y);
                g2.rotate(angle);
                g2.drawImage(frame, -frame.getWidth() / 2, -frame.getHeight() / 2, null);
                g2.setTransform(oldTransform);
            } else {
                g2.setColor(Color.ORANGE);
                g2.fillOval(x - 5, y - 5, 10, 10);
            }
        }
    }

    ////    AUTRES METHODES    ////
    public void togglePauseMenu(boolean paused) {
        pausePanel.setVisible(paused);
        hudPanel.setVisible(!paused);

        if (paused) {
            // Pour mettre le pausePanel au premier plan (et en plein écran)
            pausePanel.setBounds(0, 0, getWidth(), getHeight());
            setComponentZOrder(pausePanel, 0);

            pausePanel.repaint();
        }
    }

    public boolean centerCamera(Entity entity) {
        if (entity == null) {
            return false;
        }

        // 1. Zoom progressif
        double oldZoom = currentZoom;
        currentZoom += (ZOOM - currentZoom) * 0.05;

        // 2. Calculs de la vue
        int posX = entity.getPosition().getX();
        int posY = entity.getPosition().getY();

        double visibleWidth = getWidth() / currentZoom;
        double visibleHeight = getHeight() / currentZoom;

        double viewX = posX - (visibleWidth / 2);
        double viewY = posY - (visibleHeight / 2);

        // Limites du terrain
        double maxX = Math.max(0, terrain.getWidth() - visibleWidth);
        double maxY = Math.max(0, terrain.getHeight() - visibleHeight);

        double targetCamX = Math.max(0, Math.min(viewX, maxX));
        double targetCamY = Math.max(0, Math.min(viewY, maxY));

        // 3. Application du mouvement fluide
        double oldCamX = cameraX;
        double oldCamY = cameraY;

        cameraX += (targetCamX - cameraX) * 0.1;
        cameraY += (targetCamY - cameraY) * 0.1;

        if (cameraX < 0) cameraX = 0;
        if (cameraX > maxX) cameraX = maxX;

        if (cameraY < 0) cameraY = 0;
        if (cameraY > maxY) cameraY = maxY;

        // 4. Détection : Est-ce qu'on est encore en train de bouger ?
        boolean zoomChanged = Math.abs(oldZoom - currentZoom) > 0.001;
        boolean posChanged = Math.abs(oldCamX - cameraX) > 0.5 || Math.abs(oldCamY - cameraY) > 0.5;

        return zoomChanged || posChanged;
    }

    public boolean unzoomCamera() {
        double targetZoom = Math.max(
                (double) getWidth() / terrain.getWidth(),
                (double) getHeight() / terrain.getHeight()
        );

        double oldZoom = currentZoom;
        currentZoom += (targetZoom - currentZoom) * 0.05;

        // Calcul des dimensions visibles
        double visibleWidth = getWidth() / currentZoom;
        double visibleHeight = getHeight() / currentZoom;

        double maxCamX = Math.max(0, terrain.getWidth() - visibleWidth);
        double maxY = Math.max(0, terrain.getHeight() - visibleHeight);

        // Calcul du délai
        double delayFactor = Math.min(1.0, Math.max(0.0, (gameManager.getDelay() - 50) / 200.0));

        double targetCamX = delayFactor * maxCamX;
        double targetCamY = (terrain.getHeight() - getHeight() / currentZoom) / 2;

        double oldCamX = cameraX;
        double oldCamY = cameraY;

        cameraX += (targetCamX - cameraX) * 0.1;
        cameraY += (targetCamY - cameraY) * 0.1;

        if (cameraX < 0) cameraX = 0;
        if (cameraX > maxCamX) cameraX = maxCamX;

        if (cameraY < 0) cameraY = 0;
        if (cameraY > maxY) cameraY = maxY;

        boolean zoomChanged = Math.abs(oldZoom - currentZoom) > 0.001;
        boolean posChanged = Math.abs(oldCamX - cameraX) > 0.5 || Math.abs(oldCamY - cameraY) > 0.5;

        return zoomChanged || posChanged;
    }


    ////    SETTERS & GETTERS    ////
    public void setCamera(double x, double y) {
        cameraX = x;
        cameraY = y;
        repaint();
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void stopMusic() {
        SoundManager.stopMusic();
    }

    public PausePanel getPausePanel() {
        return pausePanel;
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }
}