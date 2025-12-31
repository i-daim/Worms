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
import upc.model.entites.projectiles.Trap;
import upc.model.entites.projectiles.Projectile;
import upc.model.entites.projectiles.Rocket;
import upc.model.entites.projectiles.Throwable;
import upc.model.entites.projectiles.UziBullet;
import upc.model.entites.projectiles.ShotgunBullet;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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

    private final GameManager gameManager;
    private final Terrain terrain;
    private final HUDPanel hudPanel;
    private final PausePanel pausePanel;

    private double cameraX = 0;
    private double cameraY = 0;

    private boolean inventoryVisible = false;

    private List<ActiveAnimation> activeAnimations;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.terrain = gameManager.getTerrain();
        this.hudPanel = new HUDPanel(gameManager);
        this.pausePanel = new PausePanel();

        this.setLayout(null); // Pour "superposer" les JPnaels
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
        hudPanel.drawAlienInfo(g2, gameManager);

        // Affichage des animations du jeu
        drawActiveAnimations(g2);

        // Affichage de l'inventaire
        if (inventoryVisible) {
            drawInventory(g2);
        }

        // Affichage des Projectiles
        drawProjectiles(g2);

        // Annulation du zoom
        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / ZOOM, 1.0 / ZOOM);

        // Affichage du HUD (après le zoom car non concerné)
        hudPanel.setSize(getWidth(), 100);

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
        for (Alien alien : gameManager.getWorld().getActiveWorms()) {
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
            double angle = 0;
            if (p instanceof Rocket) {
                Animation rocketAnim = AnimationManager.getAnimation("rocket");
                if (rocketAnim != null && !rocketAnim.getFrames().isEmpty()) {
                    int frameIndex = p.getFrameCount() % rocketAnim.getFrames().size();
                    BufferedImage frame = rocketAnim.getFrames().get(frameIndex);
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
            } else if (p instanceof Throwable) { // Grenade
                BufferedImage grenadeImg = ImageManager.get("Grenade");
                if (grenadeImg != null) {
                    AffineTransform oldTransform = g2.getTransform();
                    g2.translate(x, y);
                    g2.rotate(p.getFrameCount() * 0.1);
                    g2.drawImage(grenadeImg, -grenadeImg.getWidth() / 2,
                            -grenadeImg.getHeight() / 2, null);
                    g2.setTransform(oldTransform);
                } else {
                    g2.setColor(Color.GREEN);
                    g2.fillOval(x - 4, y - 4, 8, 8);
                }
            } else if (p instanceof UziBullet) {
                BufferedImage uziImg = ImageManager.get("MiniBullet");
                if (uziImg != null) {
                    AffineTransform oldTransform = g2.getTransform();
                    g2.translate(x, y);
                    g2.drawImage(uziImg, -uziImg.getWidth() / 2, -uziImg.getHeight() / 2, null);
                    g2.setTransform(oldTransform);
                } else {
                    g2.setColor(Color.YELLOW);
                    g2.fillOval(x - 2, y - 2, 4, 4);
                }
            } else if (p instanceof ShotgunBullet) {
                BufferedImage shotgunImg = ImageManager.get("Bullet");

                if (shotgunImg != null) {
                    AffineTransform oldTransform = g2.getTransform();
                    g2.translate(x, y);
                    g2.rotate(angle);
                    g2.drawImage(shotgunImg, -shotgunImg.getWidth() / 2,
                            -shotgunImg.getHeight() / 2, null);
                    g2.setTransform(oldTransform);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                }
            } else if (p instanceof Trap) { // Mine
                BufferedImage mineImg = ImageManager.get("Mine");
                if (mineImg != null) {
                    g2.drawImage(mineImg, x - mineImg.getWidth() / 2,
                            y - mineImg.getHeight() / 2, null);

                } else {
                    g2.setColor(Color.RED);
                    g2.fillRect(x - 5, y - 2, 10, 5);
                }
            }
        }
    }

    private void drawInventory(Graphics2D g2) {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien != null && activeAlien.isAlive()) {
            int alienPosX = activeAlien.getPosition().getX();

            int inventoryY = activeAlien.getPosition().getY() - 80;
            int inventoryX = alienPosX - InventoryPanel.TOTAL_WIDTH / 2;

            InventoryPanel.drawAlienInventory(g2, inventoryX, inventoryY,
                    activeAlien.getSelectedItemIndex());
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

    public Point screenToWorld(Point screenPoint) {
        double worldX = (screenPoint.x / ZOOM) + cameraX;
        double worldY = (screenPoint.y / ZOOM) + cameraY;
        return new Point((int) worldX, (int) worldY);
    }

    public void centralizeCamera(Entity entity) {
        if (entity == null || isCameraCentered(entity)) {
            return;
        }

        int posX = entity.getPosition().getX();
        int posY = entity.getPosition().getY();

        double viewX = posX - (getWidth() / (2 * ZOOM));
        double viewY = posY - (getHeight() / (2 * ZOOM));

        double maxX = terrain.getWidth() - getWidth() / ZOOM;
        double maxY = terrain.getHeight() - getHeight() / ZOOM;

        double targetCamX = Math.max(0, Math.min(viewX, maxX));
        double targetCamY = Math.max(0, Math.min(viewY, maxY));

        cameraX += (targetCamX - cameraX) * 0.1;
        cameraY += (targetCamY - cameraY) * 0.1;

        if (Math.abs(targetCamX - cameraX) < 0.1) {
            cameraX = targetCamX;
        }
        if (Math.abs(targetCamY - cameraY) < 0.1) {
            cameraY = targetCamY;
        }
    }

    public boolean isCameraCentered(Entity entity) {
        if (entity == null) {
            return false;
        }
        int posX = entity.getPosition().getX();
        int posY = entity.getPosition().getY();

        double viewX = posX - (getWidth() / (2 * ZOOM));
        double viewY = posY - (getHeight() / (2 * ZOOM));

        double maxX = terrain.getWidth() - getWidth() / ZOOM;
        double maxY = terrain.getHeight() - getHeight() / ZOOM;

        double camX = Math.max(0, Math.min(viewX, maxX));
        double camY = Math.max(0, Math.min(viewY, maxY));

        return (Math.abs(camX - cameraX) < 8) && (Math.abs(camY - cameraY) < 8);
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

    public boolean isInventoryVisible() {
        return inventoryVisible;
    }

    public void toggleInventory() {
        inventoryVisible = !inventoryVisible;
        repaint();
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
}
