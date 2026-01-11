package upc.view.gui;

import upc.model.entites.Alien;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.items.Item;
import org.dyn4j.geometry.Vector2;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;


/**
 * Panneau gérant l'affichage tête haute (HUD - Heads-Up Display) du jeu.
 * <p>
 * Ce panneau est superposé au {@link GamePanel} avec fond transparent et est
 * responsable de l'affichage :
 * <ul>
 * <li>Des informations globales (Timer, Vent).</li>
 * <li>Des boutons d'interaction (Passer le tour, Inventaire).</li>
 * <li>Des informations contextuelles sur les aliens (Barres de vie, Viseurs, Armes).</li>
 * <li>De l'interface d'inventaire lorsqu'elle est ouverte.</li>
 * </ul>
 */
public class HUDPanel extends JPanel {

    private final GameManager gameManager;
    private boolean inventoryVisible = false;
    private JButton inventoryButton;

    private final double zoom = GamePanel.ZOOM;

    /**
     * Constructeur du panneau HUD.
     * <p>
     * Configure l'overlay pour être transparent , définit ses dimensions
     * et initialise les boutons d'interface.
     * </p>
     * @param gameManager Le controleur du jeu.
     */
    public HUDPanel(GameManager gameManager) {
        this.gameManager = gameManager;

        this.setOpaque(false);
        this.setLayout(null); // pour positionner le bouton librement
        this.setPreferredSize(new Dimension(800, 100));

        addSkipButton();
        addInventoryButton();
    }

    /**
     * Méthode principale de dessin de l'interface utilisateur.
     * <p>
     * Cette méthode orchestre l'affichage des différents éléments du HUD :
     * <ul>
     * <li>La barre supérieure avec timer.</li>
     * <li>Les informations sur l'arme équipée.</li>
     * <li>L'indicateur de vent.</li>
     * <li>L'inventaire si visible.</li>
     * </ul>
     * Active également l'anti-aliasing pour un rendu plus net du texte et des formes.
     *
     * @param g L'objet Graphics.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawTopBar(g2);
        drawWeaponInfo(g2);
        drawWindIndicator(g2);

        if (inventoryVisible) {
            InventoryPanel.drawInventory(g2, getWidth(), getHeight(), gameManager.getActiveAlien());
        }
    }

    /**
     * Met à jour la position et la taille du panneau, ainsi que de ses composants enfants.
     * <p>
     * Repositionne notamment le bouton d'inventaire pour qu'il reste centré en bas de l'écran.
     * </p>
     * @param x Position X.
     * @param y Position Y.
     * @param width Largeur.
     * @param height Hauteur.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (inventoryButton != null) {
            int posX = (width - inventoryButton.getWidth()) / 2;
            int posY = height - inventoryButton.getHeight() - 10;
            inventoryButton.setLocation(posX, posY);
        }
    }

    /**
     * Crée et ajoute le bouton "Passer le tour".
     */
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
        skipButton.setBounds(20, 5, (int) (60 * zoom), (int) (20 * zoom));
        skipButton.setFont(new Font("Arial", Font.BOLD, (int) (8 * zoom)));
        this.add(skipButton);
    }

    /**
     * Crée et ajoute le bouton d'ouverture de l'inventaire.
     */
    private void addInventoryButton() {
        inventoryButton = ButtonManager.createGameButton("Inventaire");
        inventoryButton.addActionListener(e -> toggleInventory());
        inventoryButton.setFocusable(false);
        inventoryButton.setSize((int) (80 * zoom), (int) (25 * zoom));
        inventoryButton.setFont(new Font("Arial", Font.BOLD, (int) (8 * zoom)));
        this.add(inventoryButton);
    }

    /**
     * Bascule l'état de visibilité de l'inventaire et redessine le panneau.
     */
    public void toggleInventory() {
        inventoryVisible = !inventoryVisible;
        this.repaint();
    }

    /**
     * Vérifie si l'inventaire est actuellement affiché.
     * @return true si visible, false sinon.
     */
    public boolean isInventoryVisible() {
        return inventoryVisible;
    }


////    AFFICHAGE DU HUD EN HAUT DE L'ECRAN    ////
    /**
     * Dessine la barre d'information supérieure (fond sombre).
     * @param g2 Le contexte graphique.
     */
    private void drawTopBar(Graphics2D g2) {
        int width = this.getWidth();
        int height = (int) (25 * zoom);

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, width, height);

        drawTurnTimer(g2, width / 2 - 50, height / 2 - 20);
    }

    /**
     * Dessine le compte à rebours du tour.
     * @param g2 Le contexte graphique.
     * @param x Position X.
     * @param y Position Y.
     */
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

    /**
     * Affiche les informations de l'arme actuellement équipée par l'alien actif.
     * <p>
     * Affiche le nom de l'arme et le nombre de munitions restantes.
     * </p>
     * @param g2 Le contexte graphique.
     */
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

    /**
     * Dessine l'indicateur de vent en haut à droite.
     * <p>
     * Affiche une barre blanche dont la taille et la direction dépendent du vecteur vent.
     * </p>
     * @param g2 Le contexte graphique.
     */
    private void drawWindIndicator(Graphics2D g2) {
        if (gameManager.getWorld().getPhysicsEngine() == null
                || !gameManager.isWindEnabled()) {
            return;
        }

        Vector2 wind = gameManager.getWorld().getPhysicsEngine().getWind();
        if (wind == null) {
            return;
        }
        int x = getWidth() - 100;
        int y = (int) (40 * zoom);
        int width = 80;
        int height = 20;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, width, height, 10, 10);

        int centerX = x + width / 2;

        // Barre de vent
        double maxWind = 15.0;
        double windRatio = wind.x / maxWind;
        int barLength = (int) (windRatio * (width / 2 - 5));

        g2.setColor(Color.WHITE);
        if (barLength > 0) {
            g2.fillRect(centerX, y + 5, barLength, height - 10);
        } else {
            g2.fillRect(centerX + barLength, y + 5, -barLength, height - 10);
        }

        // Ligne centrale
        g2.setColor(Color.GRAY);
        g2.drawLine(centerX, y, centerX, y + height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString("WIND", x + 25, y - 5);
    }


////    AFFICHAGE POUR LES ALIENS    ////
    /**
     * Dessine les informations contextuelles au-dessus de tous les aliens vivants.
     * <p>
     * Appelle les méthodes spécifiques pour dessiner :
     * <ul>
     * <li>La barre de vie pour tous.</li>
     * <li>Le viseur pour l'alien actif.</li>
     * <li>Le nom de l'arme équipée pour l'alien actif.</li>
     * </ul>
     * Note: Cette méthode est appelée par {@link GamePanel#paintComponent} pour se caler
     * sur les coordonnées du monde avec le zoom camera.
     *
     * @param g2 Le contexte graphique.
     * @param gameManager Le gestionnaire de jeu.
     */
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


    /**
     * Dessine le système de visée pour l'alien actif.
     * <p>
     * Affiche un réticule (cercle + croix) et un cône coloré representant la puissance
     * de tir vert = faible, orange = moyen, rouge = fort.
     * </p>
     * @param g2 Le contexte graphique.
     * @param alien L'alien actif.
     */
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
        int power = alien.getShootPower();

        // Dimensions du cône
        int coneLength = power / 2;
        int coneWidth = 4 + (power / 8);

        if (power < 30) {
            g2.setColor(Color.GREEN);
        } else if (power < 70) {
            g2.setColor(Color.ORANGE);
        } else {
            g2.setColor(Color.RED);
        }

        if (power > 0) {
            int startX = 15;
            // Points du triangle (cône)
            int[] xPoints = {startX, startX + coneLength, startX + coneLength};
            int[] yPoints = {0, -coneWidth / 2, coneWidth / 2};

            g2.fillPolygon(xPoints, yPoints, 3);

            g2.setColor(Color.BLACK);
            g2.drawPolygon(xPoints, yPoints, 3);
        }

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


    /**
     * Affiche le nom de l'arme équipée au-dessus de l'alien.
     * @param g2 Le contexte graphique.
     * @param alien L'alien concerné.
     */
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


    /**
     * Dessine la barre de vie et l'indicateur d'activité au-dessus d'un alien.
     * <p>
     * La couleur de la barre change selon les PV restants :
     * <ul>
     * <li>Vert : > 60%</li>
     * <li>Orange : > 25%</li>
     * <li>Rouge : < 25%</li>
     * </ul>
     * Si l'alien est actif, un triangle rouge pointe vers lui.
     *
     * @param g2 Le contexte graphique.
     * @param alien L'alien.
     * @param active Indique si c'est l'alien actif du tour courant.
     */
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