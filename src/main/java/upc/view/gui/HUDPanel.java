package upc.view.gui;

import upc.model.entites.Alien;
import upc.model.items.Weapon;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.items.Item;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HUDPanel extends JPanel {

    private final GameManager gameManager;

    public HUDPanel(GameManager gameManager) {
        this.gameManager = gameManager;

        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(800, 100));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawTopBar(g2);
    }
    private void drawCurrentWeapon(Graphics2D g2) {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null) return;
        Item currentItem = activeAlien.getEquippedItem(); // Assurez-vous que cette méthode existe dans Alien
        if (currentItem instanceof Weapon) {
            Weapon weapon = (Weapon) currentItem;
            int width = getWidth();
            int boxW = 200;
            int boxH = 50;
            int x = width - boxW - 20;
            int y = 10;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x, y, boxW, boxH, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(weapon.getName(), x + 15, y + 30);

            String ammo = (weapon.getNbOfShots() < 0) ? "Infini" : "x" + weapon.getNbOfShots();
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.YELLOW);
            g2.drawString(ammo, x + 140, y + 30);
        }
    }

    private void drawTopBar(Graphics2D g2) {
        int width = this.getWidth();
        int height = 50;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);

        drawTurnTimer(g2, width / 2 - 50, height / 2 - 20);
    }

    public void drawHealthBars(Graphics2D g2, GameManager gameManager, int cameraX, int cameraY) {
        for (Team team : gameManager.getTeams()) {
            for (Alien alien : team.getAliens()) {
                if (alien != null && alien.isAlive()) {
                    drawWormHealthBar(g2, alien, gameManager.getActiveAlien() == alien);
                }
            }
        }
    }

    private void drawWormHealthBar(Graphics2D g2, Alien alien, boolean active) {
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();

        int barWidth = 30;
        int barHeight = 5;
        int barX = posX - barWidth / 2;
        int barY = posY - 25;

        g2.setColor(Color.BLACK);
        g2.fillRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);

        // Barre de vie
        int hp = alien.getHealthPoints();
        int healthWidth = (int) ((hp / 100.0) * barWidth);

        Color healthColor = hp > 60 ? Color.GREEN : hp > 25 ? Color.ORANGE : Color.RED;
        g2.setColor(healthColor);
        g2.fillRect(barX, barY, healthWidth, barHeight);

        // Inidicateur en triangle si le worm est actif
        if (active) {
            g2.setColor(Color.RED);
            int[] xPoints = {posX, posX - 6, posX + 6};
            int[] yPoints = {barY - 5, barY - 12, barY - 12};
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawTurnTimer(Graphics2D g2, int x, int y) {
        int timeLeft = gameManager.getTimeLeft();

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, 100, 40, 10, 10);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String text = String.format("%02d", timeLeft);
        int textWidth = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, x + (100 - textWidth) / 2, y + 35);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("TIME", x + 35, y + 15);
    }



}
