package upc.view.gui;

import upc.assets.ImageManager;
import javax.swing.*;
import java.awt.*;
import upc.model.items.Item;

public class GameSetupPanel extends JPanel{
    private final Image background = ImageManager.get("menu_background"); // Ou autre image
    private final MainFrame mainFrame;

    public GameSetupPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Titre
        JLabel title = new JLabel("Paramètres de la Partie");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

    }
}
