package upc.view.gui;

import upc.assets.ImageManager;
import upc.model.entites.Alien;
import upc.model.items.Item;
import upc.model.jeu.Inventory;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Composant graphique responsable de l'affichage de l'inventaire des armes.
 * <p>
 * Cette classe agit principalement comme un moteur de rendu statique pour l'interface d'inventaire.
 * Elle gère :
 * <ul>
 * <li>Le dessin du conteneur et des slots d'armes.</li>
 * <li>L'affichage des icônes et des munitions restantes.</li>
 * <li>La mise en évidence de l'arme sélectionnée.</li>
 * <li>La détection des clics souris sur les slots.</li>
 * </ul>
 */
public class InventoryPanel extends JPanel {

    public static final int INVENTORY_SLOTS = 6;

    private static final int SLOT_SIZE = (int) (50 * GamePanel.ZOOM);
    private static final int GAP = (int) (8 * GamePanel.ZOOM);
    private static final int CONTAINER_MARGIN = (int) (10 * GamePanel.ZOOM);

    private static final int TOTAL_WIDTH = (INVENTORY_SLOTS * SLOT_SIZE)
            + ((INVENTORY_SLOTS - 1) * GAP)
            + (2 * CONTAINER_MARGIN);

    private static final int TOTAL_HEIGHT = SLOT_SIZE + (2 * CONTAINER_MARGIN);

    private static final Color GLOBAL_BG_COLOR = new Color(20, 20, 20, 230);

    private static final Color GLOBAL_BORDER_OUTER = new Color(220, 220, 220);
    private static final Color GLOBAL_BORDER_INNER = new Color(50, 50, 50);


    private static final Color SLOT_BG_NORMAL = new Color(255, 255, 255, 10);
    private static final Color SLOT_BG_SELECTED = new Color(255, 255, 255, 50);
    private static final Color SLOT_BORDER_SELECTED = new Color(255, 215, 0);


    /**
     * Point d'entrée principal pour dessiner l'inventaire à l'écran.
     * <p>
     * Calcule automatiquement la position centrée en bas de l'écran avant de déléguer
     * le dessin à {@link #drawAlienInventory}.
     * </p>
     * @param g2 La graphique 2D.
     * @param screenWidth La largeur de l'écran.
     * @param screenHeight La hauteur de l'écran.
     * @param alien L'alien dont l'inventaire doit être affiché.
     */
    public static void drawInventory(Graphics2D g2, int screenWidth, int screenHeight,
                                     Alien alien) {
        int x = (screenWidth - TOTAL_WIDTH) / 2;
        int y = screenHeight - TOTAL_HEIGHT - 80;

        drawAlienInventory(g2, x, y, alien);
    }


    /**
     * Dessine l'inventaire complet à une position donnée.
     * <p>
     * Cette méthode orchestre le rendu :
     * <ol>
     * <li>Configuration de la qualité graphique.</li>
     * <li>Dessin du conteneur.</li>
     * <li>Itération sur les slots pour afficher les items et munitions.</li>
     * </ol>
     *
     * @param g2 La graphique 2d.
     * @param drawX La position X du coin haut-gauche de l'inventaire.
     * @param drawY La position Y du coin haut-gauche de l'inventaire.
     * @param alien L'alien actif pour récupérer l'inventaire d'équipe et la sélection.
     */
    public static void drawAlienInventory(Graphics2D g2, int drawX, int drawY, Alien alien) {
        if (alien == null) {
            return;
        }

        setupGraphics(g2);
        drawContainer(g2, drawX, drawY);

        Inventory inventory = alien.getTeam().getInventory();
        int selectedIndex = alien.getSelectedItemIndex();

        int currentX = drawX + CONTAINER_MARGIN;
        int currentY = drawY + CONTAINER_MARGIN;

        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            Item.Type itemType = Inventory.getItemTypeAt(i);
            int ammoCount = (itemType != null) ? inventory.getAmmo(itemType) : 0;
            boolean isSelected = (i == selectedIndex);

            drawSlot(g2, currentX, currentY, isSelected);
            drawItemIcon(g2, currentX, currentY, itemType);
            drawAmmoCount(g2, currentX, currentY, itemType, ammoCount, isSelected);

            currentX += SLOT_SIZE + GAP;
        }
    }

    /**
     * Active l'anti-aliasing pour le texte et les formes géométriques.
     * @param g2 La graphique 2d.
     */
    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /**
     * Dessine l'arrière-plan du panneau d'inventaire.
     * <p>
     * Inclut une ombre portée, un fond semi-transparent et une double bordure.
     * </p>
     * @param g2 Le contexte graphique.
     * @param x Position X.
     * @param y Position Y.
     */
    private static void drawContainer(Graphics2D g2, int x, int y) {
        // Ombre
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x + 8, y + 8, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);

        // Fond
        g2.setColor(GLOBAL_BG_COLOR);
        g2.fillRoundRect(x, y, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);

        // Bordure extérieure
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(GLOBAL_BORDER_OUTER);
        g2.drawRoundRect(x, y, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);

        // Bordure intérieure
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(GLOBAL_BORDER_INNER);
        g2.drawRoundRect(x + 5, y + 5, TOTAL_WIDTH - 10, TOTAL_HEIGHT - 10, 20, 20);
    }

    /**
     * Dessine un slot individuel.
     * <p>
     * Si le slot est sélectionné, il est mis en évidence avec une bordure dorée
     * et un fond plus clair.
     * </p>
     * @param g2 La graphique 2d.
     * @param x Position X.
     * @param y Position Y.
     * @param isSelected Vrai si c'est l'arme actuellement choisie par l'alien.
     */
    private static void drawSlot(Graphics2D g2, int x, int y, boolean isSelected) {
        g2.setColor(isSelected ? SLOT_BG_SELECTED : SLOT_BG_NORMAL);
        g2.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 15, 15);

        if (isSelected) {
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(SLOT_BORDER_SELECTED);
            g2.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 15, 15);
        } else {
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(100, 100, 100, 100));
            g2.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 15, 15);
        }
    }

    /**
     * Dessine l'icône de l'arme à l'intérieur d'un slot.
     * <p>
     * Récupère l'image via {@link ImageManager}. Si l'image n'est pas trouvée,
     * affiche les 3 premières lettres du nom de l'item en secours.
     * </p>
     * @param g2 La graphique 2d.
     * @param x Position X du slot.
     * @param y Position Y du slot.
     * @param itemType Le type d'item à dessiner.
     */
    private static void drawItemIcon(Graphics2D g2, int x, int y, Item.Type itemType) {
        if (itemType == null) {
            return;
        }
        String keyName = formatEnumName(itemType.name()) + "_Inv";
        BufferedImage img = ImageManager.get(keyName);

        if (img != null) {
            int imgSize = SLOT_SIZE - 20;
            g2.drawImage(img, x + 10, y + 10, imgSize, imgSize, null);
        } else {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(itemType.name().substring(0, 3), x + 10, y + SLOT_SIZE / 2);
        }
    }

    /**
     * Affiche le nombre de munitions restantes dans le coin du slot.
     * <p>
     * Affiche "∞" si le nombre de munitions est -1.
     * </p>
     * @param g2 Le contexte graphique.
     * @param x Position X du slot.
     * @param y Position Y du slot.
     * @param itemType Le type d'item.
     * @param ammoCount Le nombre de munitions.
     * @param isSelected Si le slot est sélectionné.
     */
    private static void drawAmmoCount(Graphics2D g2, int x, int y, Item.Type itemType,
                                      int ammoCount, boolean isSelected) {
        if (itemType == null) {
            return;
        }
        String ammoText = (ammoCount == -1) ? "∞" : String.valueOf(ammoCount);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(isSelected ? new Color(255, 255, 0) : Color.WHITE);

        int textWidth = g2.getFontMetrics().stringWidth(ammoText);
        g2.drawString(ammoText, x + SLOT_SIZE - textWidth - 8, y + SLOT_SIZE - 8);
    }

    /**
     * Formate le nom de l'énum pour correspondre aux clés des ressources.
     * <p>
     * Exemple : "BAZOOKA" devient "Bazooka".
     * </p>
     * @param name Le nom de l'énum.
     * @return Le nom formaté.
     */
    private static String formatEnumName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * Détermine si un clic de souris correspond à un slot d'inventaire.
     *
     * @param clickPoint Les coordonnées du clic.
     * @param screenWidth Largeur de l'écran.
     * @param screenHeight Hauteur de l'écran.
     * @return L'index du slot cliqué (0 à INVENTORY_SLOTS-1), ou -1 si aucun slot n'est touché.
     */
    /**
     * Vérifie si un clic est dans un slot de l'inventaire et retourne l'index du slot.
     * Retourne -1 si aucun slot est cliqué.
     */
    public static int getClickedSlotIndex(Point clickPoint, int screenWidth, int screenHeight) {
        int drawX = (screenWidth - TOTAL_WIDTH) / 2;
        int drawY = screenHeight - TOTAL_HEIGHT - 80;

        int currentX = drawX + CONTAINER_MARGIN;
        int currentY = drawY + CONTAINER_MARGIN;

        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            if (clickPoint.x >= currentX && clickPoint.x <= currentX + SLOT_SIZE
                    && clickPoint.y >= currentY && clickPoint.y <= currentY + SLOT_SIZE) {
                return i;
            }
            currentX += SLOT_SIZE + GAP;
        }
        return -1;
    }

    /**
     * Vérifie si un point donné se trouve à l'intérieur de la zone globale de l'inventaire.
     * <p>
     * Utile pour empêcher les clics de traverser l'interface et d'interagir avec
     * le jeu en arrière-plan.
     * </p>
     * @param p Le point à tester.
     * @param screenWidth Largeur de l'écran.
     * @param screenHeight Hauteur de l'écran.
     * @return Vrai si le point est dans l'inventaire, Faux sinon.
     */
    public static boolean isInsideInventory(Point p, int screenWidth, int screenHeight) {
        if (p == null) {
            return false;
        }
        int drawX = (screenWidth - TOTAL_WIDTH) / 2;
        int drawY = screenHeight - TOTAL_HEIGHT - 80;

        return p.x >= drawX && p.x <= drawX + TOTAL_WIDTH
                && p.y >= drawY && p.y <= drawY + TOTAL_HEIGHT;
    }
}