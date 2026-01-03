package upc.view.gui;

import upc.model.entites.Alien;
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
        drawWeaponInfo(g2);
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


////    AFFICHAGE DU HUD EN HAUT DE L'ECRAN    ////
    private void drawTopBar(Graphics2D g2) {
        int width = this.getWidth();
        int height = 50;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);

        drawTurnTimer(g2, width / 2 - 50, height / 2 - 20);
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

    private void drawWeaponInfo(Graphics2D g2) {
        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null) {
            return;
        }
        Item currentItem = activeAlien.getEquippedItem();
        if (currentItem instanceof Item item) {
            int boxW = 200;
            int boxH = 50;
            int x = getWidth() - boxW - 20;
            int y = 5;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x, y, boxW, boxH, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(item.getName(), x + 15, y + 30);

            int ammo = activeAlien.getTeam().getInventory().getAmmo(item.getType());
            String info = (ammo < 0) ? "Infini" : "x" + ammo;
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.YELLOW);
            g2.drawString(info, x + 140, y + 30);
        }
    }


////    AFFICHAGE POUR LES ALIENS    ////
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

        // Indice de puissance de tir
        g2.setColor(Color.RED);
        int power = alien.getShootPower();
        g2.drawRect(10, 0, power, 10);
        g2.fillRect(10, 0, power, 10);

        // Viseur représenté par un cercle
        int distance = 50;
        int radius = 6;

        g2.setColor(Color.RED);
        int circleX = distance + radius;
        int circleY = -7;
        g2.drawOval(circleX, circleY, radius, radius);

        // Croix au milieu
        g2.drawLine(circleX, circleY + 3, circleX + 6, circleY + 3);
        g2.drawLine(circleX + 3, circleY, circleX + 3, circleY + 6);

        // On revient à l'état original
        g2.setTransform(originalTransform);
    }


    private void drawEquippedWeapon(Graphics2D g2, Alien alien) {
        Item item = alien.getEquippedItem();
        if (item == null) {
            return;
        }
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
}