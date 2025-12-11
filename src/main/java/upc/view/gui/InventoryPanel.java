package upc.view.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class InventoryPanel extends JPanel {

    public static final int INVENTORY_SLOTS = 5;
    public static final int SLOT_SIZE = 30; // Réduit à 30 (était 40)
    public static final int PADDING = 4;    // Réduit à 4 (était 6)
    public static final int TOTAL_WIDTH = INVENTORY_SLOTS * SLOT_SIZE + (INVENTORY_SLOTS - 1) * PADDING + 2 * PADDING;

    private static final Color COULEUR_NORMALE = new Color(75, 75, 75, 200);
    private static final Color COULEUR_SELECTIONNEE = new Color(50, 50, 50, 255);
    private static final Color COULEUR_BORDURE = new Color(120, 120, 120);

    private InventoryPanel() {
    }

    public static void drawAlienInventory(Graphics2D g2, int drawX, int drawY, int selectedItemIndex) {

        int currentX = drawX + PADDING;

        for (int i = 0; i < INVENTORY_SLOTS; i++) {
            Color couleurFond = (i == selectedItemIndex) ? COULEUR_SELECTIONNEE : COULEUR_NORMALE;

            g2.setColor(couleurFond);
            g2.fillRect(currentX, drawY + PADDING, SLOT_SIZE, SLOT_SIZE);

            g2.setColor(COULEUR_BORDURE);
            g2.drawRect(currentX, drawY + PADDING, SLOT_SIZE, SLOT_SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14)); // Police légèrement réduite
            String numero = String.valueOf(i + 1);

            int textWidth = g2.getFontMetrics().stringWidth(numero);
            g2.drawString(numero, currentX + (SLOT_SIZE - textWidth) / 2, drawY + PADDING + 22); // Ajustement Y

            currentX += SLOT_SIZE + PADDING;
        }
    }
}