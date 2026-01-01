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
    private JPanel createRow(String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(false);
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 16));
        l.setForeground(Color.WHITE);
        p.add(l);
        return p;
    }
    public JButton getStartButton() {
        return startButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public String getSelectedMap() {
        return (String) mapSelector.getSelectedItem();
    }

    public int getTeamSize() {
        return teamSizeSlider.getValue();
    }
    public boolean isBazookaEnabled() { return chkBazooka.isSelected(); }
    public boolean isUziEnabled() { return chkUzi.isSelected(); }
    public boolean isShotgunEnabled() { return chkShotgun.isSelected(); }
    public boolean isGrenadeEnabled() { return chkGrenade.isSelected(); }
    public boolean isMineEnabled() { return chkMine.isSelected(); }
}
