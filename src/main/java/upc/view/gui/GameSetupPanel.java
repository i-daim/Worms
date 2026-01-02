package upc.view.gui;

import upc.assets.ImageManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import upc.controller.GameSetupController;

public class GameSetupPanel extends JPanel {
    private final Image background = ImageManager.get("menu_background"); // Ou autre image
    private final MainFrame mainFrame;

    private JComboBox<String> mapSelector;
    private JSlider teamSizeSlider;
    private JButton startButton;
    private JButton backButton;

    private JCheckBox chkBazooka;
    private JCheckBox chkUzi;
    private JCheckBox chkShotgun;
    private JCheckBox chkGrenade;
    private JCheckBox chkMine;

    public GameSetupPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Paramètres de la Partie");
        title.setFont(new Font("Arial", Font.BOLD, 45));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel mapPanel = createRow("Carte : ");
        String[] maps = {"Aléatoire", "Liberty", "Spaceship"};
        mapSelector = new JComboBox<>(maps);
        mapPanel.add(mapSelector);
        mapSelector.setFont(new Font("Arial", Font.BOLD, 22));

        mapSelector.setPreferredSize(new Dimension(200, 40));
        mapSelector.setMaximumSize(new Dimension(200, 40));
        mapPanel.add(mapSelector);

        JPanel wormPanel = createRow("Vers par équipe : ");
        teamSizeSlider = new JSlider(1, 5, 2);
        teamSizeSlider.setOpaque(false);
        teamSizeSlider.setForeground(Color.WHITE);
        teamSizeSlider.setFont(new Font("Arial", Font.BOLD, 20));

        teamSizeSlider.setMajorTickSpacing(1);
        teamSizeSlider.setPaintTicks(true);
        teamSizeSlider.setPaintLabels(true);
        teamSizeSlider.setPreferredSize(new Dimension(300, 60));
        wormPanel.add(teamSizeSlider);


        JPanel weaponPanel = new JPanel();
        weaponPanel.setBackground(new Color(20, 20, 20, 180));
        weaponPanel.setOpaque(true);

        weaponPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2), // Cadre gris
                " Armes Autorisées ",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 22),
                new Color(100, 255, 100) // Vert fluo pour le titre
        ));
        weaponPanel.setMaximumSize(new Dimension(400, 300));
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

        startButton = ButtonManager.createGameButton("LANCER LA PARTIE !");
        backButton = ButtonManager.createGameButton("Retour");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createVerticalStrut(40));
        this.add(title);
        this.add(Box.createVerticalStrut(50));
        this.add(mapPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(wormPanel);
        this.add(Box.createVerticalStrut(30));
        this.add(weaponPanel);
        this.add(Box.createVerticalStrut(50));
        this.add(startButton);
        this.add(Box.createVerticalStrut(20));
        this.add(backButton);

        new GameSetupController(this, mainFrame);




    }
    private void styleCheckBox(JCheckBox box) {
        box.setOpaque(false);
        box.setForeground(Color.WHITE);
        box.setFont(new Font("Arial", Font.BOLD, 24));
        box.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    private JPanel createRow(String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(false);
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 26));
        l.setForeground(new Color(255, 220, 100));
        p.add(l);
        return p;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
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
