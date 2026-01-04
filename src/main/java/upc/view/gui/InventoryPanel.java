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

public class InventoryPanel extends JPanel {

    public static final int INVENTORY_SLOTS = 6;
    public static final int SLOT_SIZE = 80;
    public static final int GAP = 12;
    public static final int CONTAINER_MARGIN = 15;

    public static final int TOTAL_WIDTH = (INVENTORY_SLOTS * SLOT_SIZE)
            + ((INVENTORY_SLOTS - 1) * GAP)
            + (2 * CONTAINER_MARGIN);

    public static final int TOTAL_HEIGHT = SLOT_SIZE + (2 * CONTAINER_MARGIN);


    private static final Color GLOBAL_BG_COLOR = new Color(20, 20, 20, 230);

    private static final Color GLOBAL_BORDER_OUTER = new Color(220, 220, 220);
    private static final Color GLOBAL_BORDER_INNER = new Color(50, 50, 50);


    private static final Color SLOT_BG_NORMAL = new Color(255, 255, 255, 10);
    private static final Color SLOT_BG_SELECTED = new Color(255, 255, 255, 50);
    private static final Color SLOT_BORDER_SELECTED = new Color(255, 215, 0);

    private InventoryPanel() {

    }

    public static void drawInventory(Graphics2D g2, int screenWidth, int screenHeight,
                                     Alien alien) {
        int x = (screenWidth - TOTAL_WIDTH) / 2;
        int y = screenHeight - TOTAL_HEIGHT - 80;

        drawAlienInventory(g2, x, y, alien);
    }


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

    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

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

    private static String formatEnumName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

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
