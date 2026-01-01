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

        JPanel mapPanel = createRow("Carte : ");
        String[] maps = {"Aléatoire", "Liberty", "Spaceship"}; // Ajoutez vos noms de maps ici
        mapSelector = new JComboBox<>(maps);
        mapPanel.add(mapSelector);

        JPanel wormPanel = createRow("Vers par équipe : ");
        teamSizeSlider = new JSlider(1, 5, 2); // Min 1, Max 5, Défaut 2
        teamSizeSlider.setMajorTickSpacing(1);
        teamSizeSlider.setPaintTicks(true);
        teamSizeSlider.setPaintLabels(true);
        teamSizeSlider.setOpaque(false);
        teamSizeSlider.setForeground(Color.WHITE);
        wormPanel.add(teamSizeSlider);

        JPanel weaponPanel = new JPanel();
        weaponPanel.setOpaque(false);
        weaponPanel.setBorder(BorderFactory.createTitledBorder(null, "Armes autorisées", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        chkBazooka = new JCheckBox("Bazooka", true);
        chkUzi = new JCheckBox("Uzi", true);
        chkShotgun = new JCheckBox("Shotgun", true);
        chkGrenade = new JCheckBox("Grenade", true);
        chkMine = new JCheckBox("Mine", true);

        styleCheckBox(chkBazooka); styleCheckBox(chkUzi); styleCheckBox(chkShotgun);
        styleCheckBox(chkGrenade); styleCheckBox(chkMine);

        weaponPanel.add(chkBazooka);
        weaponPanel.add(chkUzi);
        weaponPanel.add(chkShotgun);
        weaponPanel.add(chkGrenade);
        weaponPanel.add(chkMine);



    }
    private void styleCheckBox(JCheckBox box) {
        box.setOpaque(false);
        box.setForeground(Color.WHITE);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
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
