package upc.view.gui;

import upc.model.entites.Alien;
import upc.model.items.Weapon;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.items.Item;

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

    public HUDPanel(GameManager gameManager) {
        this.gameManager = gameManager;

        this.setOpaque(false);
        this.setLayout(null); // pour positionner le bouton librement
        this.setPreferredSize(new Dimension(800, 100));

        addSkipButton();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawTopBar(g2);
        drawCurrentWeapon(g2);
    }


    private void addSkipButton() {
        JButton skipButton = ButtonManager.createGameButton("Passer");
        // passer le tour quand on clique
        skipButton.addActionListener(e -> {
            if (!gameManager.isOver()) {
                gameManager.skipTurn();
            }
        });
        // Empêche le bouton de prendre le focus clavier.
        // Sinon après un clic, les flèches contrôleraient plus l'alien
        skipButton.setFocusable(false);
        // Positionnement et taille
        skipButton.setBounds(20, 5, 120, 40);
        skipButton.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(skipButton);
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


    public void drawAlienInfo(Graphics2D g2, GameManager gameManager) {
        for (Team team : gameManager.getTeams()) {
            for (Alien alien : team.getAliens()) {

                if (alien != null && alien.isAlive()) {
                    boolean active = gameManager.getActiveAlien() == alien;

                    drawAlienHealthBar(g2, alien, active);

                    if (active) {
                        drawAlienAim(g2, alien);
                        drawEquippedWeapon(g2, alien);
                    }
                }
            }
        }
    }


    private void drawAlienAim(Graphics2D g2, Alien alien) {
        // "Sauvegarde" de l'état des graphiques pour ne transformer que le viseur
        AffineTransform originalTransform = g2.getTransform();

        int centerX = alien.getPosition().getX();
        int centerY = alien.getPosition().getY();
        g2.translate(centerX, centerY);

        // Inverse le viseur si l'alien est tourné à gauche
        if (!alien.getDirection()) {
            g2.scale(-1, 1);
        }

        // Rotation du viseur en fonction de l'angle
        g2.rotate(Math.toRadians(-alien.getAimAngle()));

        int distance = 20;
        int width = 25;
        int height = 6;

        // Bordures noires pour le viseur
        g2.setColor(Color.BLACK);
        g2.drawRect(distance, -height / 2, width, height);

        // Rectangle transparent blanc
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect(distance + 1, -height / 2 + 1, width - 1, height - 1);

        // Flèche au bout du viseur
        g2.setColor(Color.BLACK);
        int arrowX = distance + width + 1;
        int[] xPoints = {arrowX, arrowX + 5, arrowX};
        int[] yPoints = {-4, 0, 4};
        g2.fillPolygon(xPoints, yPoints, 3);

        g2.setTransform(originalTransform);
    }


    private void drawEquippedWeapon(Graphics2D g2, Alien alien) {
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String text = alien.getEquippedItem().getName();
        int textWidth = g2.getFontMetrics().stringWidth(text);

        g2.drawString(text, posX - textWidth / 2, posY - 42);
    }


    private void drawAlienHealthBar(Graphics2D g2, Alien alien, boolean active) {
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

        // Indicateur en triangle si le worm est actif
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