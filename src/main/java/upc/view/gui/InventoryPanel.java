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

}