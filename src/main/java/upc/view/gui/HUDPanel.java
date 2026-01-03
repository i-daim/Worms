package upc.view.gui;

import upc.model.entites.Alien;
import upc.model.items.Item;
import upc.model.items.Weapon;
import upc.model.jeu.GameManager;
import upc.model.jeu.Team;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

public class HUDPanel extends JPanel {

    private final GameManager gameManager;


    private boolean isInventoryVisible = true;
    private JButton inventoryButton;

    public HUDPanel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.setOpaque(false);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 100));
        this.setFocusable(false);

        addSkipButton();
        addInventoryToggleButton();
    }

    private void addInventoryToggleButton() {
        inventoryButton = ButtonManager.createGameButton("SAC");

        inventoryButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        inventoryButton.addActionListener(e -> {
            isInventoryVisible = !isInventoryVisible;
            inventoryButton.setText(isInventoryVisible ? "CACHER" : "SAC");
            this.repaint();
            if (getParent() != null) {
                getParent().requestFocusInWindow();
            }
        });
        inventoryButton.setFocusable(false);
        inventoryButton.setBounds(0, 0, 80, 30);

        this.add(inventoryButton);
    }

    private void addSkipButton() {
        JButton skipButton = ButtonManager.createGameButton("PASSER");
        skipButton.addActionListener(e -> {
            if (!gameManager.isOver()) {
                gameManager.skipTurn();
            }
            this.getRootPane().requestFocusInWindow();
        });
        skipButton.setFocusable(false);
        skipButton.setBounds(20, 60, 100, 35);
        skipButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.add(skipButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        drawTopBar(g2);
        drawCurrentWeapon(g2);
        int invWidth = InventoryPanel.TOTAL_WIDTH;
        int invHeight = InventoryPanel.TOTAL_HEIGHT;
        int invX = 60;
        int invY = height - invHeight - 80;
        if (inventoryButton != null) {
            int btnX = invX + invWidth + 10;
            int btnY = invY + invHeight - 40;
            inventoryButton.setBounds(btnX, btnY, 80, 40);
        }

        if (isInventoryVisible) {
            InventoryPanel.drawAlienInventory(g2, invX, invY, gameManager.getActiveAlien());
        }

        g2.dispose();
    }
    private void drawTopBar(Graphics2D g2) {
        int width = this.getWidth();
        int height = 50;
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);
        drawTurnTimer(g2, width / 2 - 50, height / 2 - 20);
    }

    private void drawTurnTimer(Graphics2D g2, int x, int y) {
        int timeLeft = gameManager.getTimeLeft();
        if (timeLeft <= 5) {
            g2.setColor(new Color(200, 0, 0, 200));
        } else {
            g2.setColor(new Color(255, 255, 255, 50));
        }

        g2.fillRoundRect(x, y, 100, 40, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        String text = String.format("%02d", timeLeft);
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, x + (100 - textWidth) / 2, y + 28);
    }

    private void drawCurrentWeapon(Graphics2D g2) {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null) {
            return;
        }

        Item currentItem = activeAlien.getEquippedItem();
        if (currentItem instanceof Weapon) {
            Weapon weapon = (Weapon) currentItem;
            int width = getWidth();
            int boxW = 200;
            int boxH = 50;
            // Positionné à droite
            int x = width - boxW - 20;
            int y = 10;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x, y, boxW, boxH, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString(weapon.getName(), x + 15, y + 30);

            String ammo = (weapon.getNbOfShots() < 0) ? "∞" : "x" + weapon.getNbOfShots();
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.setColor(Color.YELLOW);
            g2.drawString(ammo, x + 140, y + 30);
        }
    }

    public void drawAlienInfo(Graphics2D g2, GameManager gameManager) {
        for (Team team : gameManager.getTeams()) {
            for (Alien alien : team.getAliens()) {

                if (alien != null && alien.isAlive()) {
                    boolean active = gameManager.getActiveAlien() == alien;

                    drawAlienHealthBar(g2, alien, active);

                    if (active) {
                        drawAlienAim(g2, alien);
                        drawEquippedWeaponText(g2, alien);
                    }
                }
            }
        }
    }

    private void drawAlienHealthBar(Graphics2D g2, Alien alien, boolean active) {
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();
        int barWidth = 40;
        int barHeight = 6;
        int barX = posX - barWidth / 2;
        int barY = posY - 35;

        g2.setColor(Color.BLACK);
        g2.fillRoundRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2, 4, 4);

        int hp = alien.getHealthPoints();
        int maxHp = 100;
        int healthWidth = (int) (((double) Math.max(0, hp) / maxHp) * barWidth);

        Color healthColor = hp > 60 ? new Color(50, 205, 50) : hp > 25 ? Color.ORANGE : Color.RED;
        g2.setColor(healthColor);
        g2.fillRoundRect(barX, barY, healthWidth, barHeight, 4, 4);

        if (active) {
            g2.setColor(new Color(255, 50, 50));
            int[] xPoints = {posX, posX - 5, posX + 5};
            int[] yPoints = {barY - 5, barY - 12, barY - 12};
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawAlienAim(Graphics2D g2, Alien alien) {
        AffineTransform originalTransform = g2.getTransform();
        int centerX = alien.getPosition().getX();
        int centerY = alien.getPosition().getY();

        g2.translate(centerX, centerY);
        if (!alien.getDirection()) {
            g2.scale(-1, 1);
        }
        g2.rotate(Math.toRadians(-alien.getAimAngle()));

        int distance = 25;
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new java.awt.BasicStroke(1f, java.awt.BasicStroke.CAP_BUTT,
                java.awt.BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
        g2.drawLine(distance, 0, distance + 40, 0);

        g2.setStroke(new java.awt.BasicStroke(1.5f));
        g2.setColor(Color.RED);
        int targetX = distance + 40;
        g2.drawLine(targetX - 3, 0, targetX + 3, 0);
        g2.drawLine(targetX, -3, targetX, 3);

        g2.setTransform(originalTransform);
    }

    private void drawEquippedWeaponText(Graphics2D g2, Alien alien) {
        Item item = alien.getEquippedItem();
        if (item == null) {
            return;
        }
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        String text = item.getName();
        int textX = posX - g2.getFontMetrics().stringWidth(text) / 2;
        int textY = posY - 45;
        g2.setColor(Color.BLACK);
        g2.drawString(text, textX + 1, textY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
    }
}