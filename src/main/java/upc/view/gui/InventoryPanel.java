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
    public static void drawCenteredBottom(Graphics2D g2, int screenWidth, int screenHeight, Alien alien) {
        int x = (screenWidth - TOTAL_WIDTH) / 2;
        int y = screenHeight - TOTAL_HEIGHT - 80;

        drawAlienInventory(g2, x, y, alien);
    }

    /**
     * Dessine l'inventaire complet avec un conteneur global stylisé.
     */
    public static void drawAlienInventory(Graphics2D g2, int drawX, int drawY, Alien alien) {
        if (alien == null) return;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(drawX + 8, drawY + 8, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);


        g2.setColor(GLOBAL_BG_COLOR);
        g2.fillRoundRect(drawX, drawY, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);


        g2.setStroke(new BasicStroke(4f));
        g2.setColor(GLOBAL_BORDER_OUTER);
        g2.drawRoundRect(drawX, drawY, TOTAL_WIDTH, TOTAL_HEIGHT, 30, 30);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(GLOBAL_BORDER_INNER);
        g2.drawRoundRect(drawX + 5, drawY + 5, TOTAL_WIDTH - 10, TOTAL_HEIGHT - 10, 20, 20);


        Inventory inventory = alien.getTeam().getInventory();
        int selectedIndex = alien.getSelectedItemIndex();


        int currentX = drawX + CONTAINER_MARGIN;
        int currentY = drawY + CONTAINER_MARGIN;

        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            Item.Type itemType = Inventory.getItemTypeAt(i);
            int ammoCount = (itemType != null) ? inventory.getAmmo(itemType) : 0;
            boolean isSelected = (i == selectedIndex);


            g2.setColor(isSelected ? SLOT_BG_SELECTED : SLOT_BG_NORMAL);
            g2.fillRoundRect(currentX, currentY, SLOT_SIZE, SLOT_SIZE, 15, 15);


            if (isSelected) {
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(SLOT_BORDER_SELECTED);
                g2.drawRoundRect(currentX, currentY, SLOT_SIZE, SLOT_SIZE, 15, 15);
            } else {

                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(100, 100, 100, 100));
                g2.drawRoundRect(currentX, currentY, SLOT_SIZE, SLOT_SIZE, 15, 15);
            }


            if (itemType != null) {
                String keyName = formatEnumName(itemType.name()) + "_Inv";
                BufferedImage img = ImageManager.get(keyName);

                if (img != null) {

                    int imgSize = SLOT_SIZE - 20;
                    g2.drawImage(img, currentX + 10, currentY + 10, imgSize, imgSize, null);
                } else {

                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2.drawString(itemType.name().substring(0, 3), currentX + 10, currentY + SLOT_SIZE / 2);
                }
            }


            g2.setColor(isSelected ? Color.WHITE : Color.LIGHT_GRAY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(String.valueOf(i + 1), currentX + 8, currentY + 20);


            if (itemType != null) {
                String ammoText = (ammoCount == -1) ? "∞" : String.valueOf(ammoCount);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2.setColor(isSelected ? new Color(255, 255, 0) : Color.WHITE);

                int textWidth = g2.getFontMetrics().stringWidth(ammoText);
                g2.drawString(ammoText, currentX + SLOT_SIZE - textWidth - 8, currentY + SLOT_SIZE - 8);
            }


            currentX += SLOT_SIZE + GAP;
        }
    }

    private static String formatEnumName(String name) {
        if (name == null || name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}