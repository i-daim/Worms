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
    private int selectedItemIndex = 0;

    private static final int INV_OFFSET_Y = 80;
    private boolean inventoryVisible = true;

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

        g2.scale(ZOOM, ZOOM);
        g2.translate(-cameraX, -cameraY);

        BufferedImage backgroundImage = terrain.getBackground();
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, this);
        }

        int waterY = DEFAULT_HEIGHT
                - animationManager.getAnimation("water").getFrames().get(0).getHeight() - 15;

        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY, 100);

        BufferedImage terrainImage = terrain.getImage();
        g2.drawImage(terrainImage, 0, 0, this);

        drawAliens(g2);

        drawAnimation(g2, animationManager.getAnimation("water"), 0, waterY + 30, 100);

        hudPanel.drawHealthBars(g2, gameManager, cameraX, cameraY);

        if (inventoryVisible) {
            drawActiveAlienInventory(g2);
        }

        g2.translate(cameraX, cameraY);
        g2.scale(1.0 / ZOOM, 1.0 / ZOOM);

        hudPanel.setSize(getWidth(), 100);
        hudPanel.paintComponent(g2);

        g2.dispose();
    }

    private void drawActiveAlienInventory(Graphics2D g2) {
        Alien alienActif = gameManager.getActiveAlien();
        if (alienActif != null && alienActif.isAlive()) {
            int alienPosX = alienActif.getPosition().getX();

            int inventoryY = alienActif.getPosition().getY() - INV_OFFSET_Y;
            int inventoryX = alienPosX - InventoryPanel.TOTAL_WIDTH / 2;

            InventoryPanel.drawAlienInventory(g2, inventoryX, inventoryY, selectedItemIndex);
        }
    }

    public void setSelectedItemIndex(int index) {
        if (index >= 0 && index < InventoryPanel.INVENTORY_SLOTS) {
            this.selectedItemIndex = index;
            repaint();
        }
    }

    public void setInventoryVisible(boolean visible) {
        this.inventoryVisible = visible;
        repaint();
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