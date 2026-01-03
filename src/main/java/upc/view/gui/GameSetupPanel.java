package upc.view.gui;

import upc.assets.ImageManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.BorderLayout;
import upc.controller.GameSetupController;

public class GameSetupPanel extends JPanel {
    private final Image background = ImageManager.get("menu_background");
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
    private JCheckBox chkJetpack;

    private static final Color TITLE_COLOR = new Color(255, 255, 255);
    private static final Color PANEL_BG_COLOR = new Color(30, 30, 40, 220); 
    private static final Color ACCENT_COLOR = new Color(100, 255, 201);
    private static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 22);

    public GameSetupPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setDoubleBuffered(true);
        this.setLayout(new BorderLayout()); // Changement pour BorderLayout global pour centrer facilement

        JLabel title = new JLabel("PARAMÈTRES DE LA PARTIE :", SwingConstants.CENTER) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                g2.setColor(new Color(0, 0, 0, 150));
                g2.drawString(getText(), 4, getHeight() - 4);
                g2.setColor(getForeground());
                g2.drawString(getText(), 0, getHeight() - 6);
            }
        };
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(TITLE_COLOR);
        title.setBorder(new EmptyBorder(350, 50, 0, 50));


        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(new EmptyBorder(50, 50, 0, 50)); // Marges latérales

        JPanel configPanel = createRoundedPanel();
        configPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        configPanel.setMaximumSize(new Dimension(1000, 140));

        JPanel mapBox = createSection("CARTE", 240);
        String[] maps = {"Aléatoire", "Liberty", "Spaceship"};
        mapSelector = new JComboBox<>(maps);
        styleComboBox(mapSelector);
        mapBox.add(mapSelector);
        configPanel.add(mapBox);

        JPanel teamBox = createSection("VERS PAR ÉQUIPE", 320);
        teamSizeSlider = new JSlider(1, 5, 2);
        teamSizeSlider.setOpaque(false);
        teamSizeSlider.setForeground(Color.WHITE);
        teamSizeSlider.setFont(new Font("Arial", Font.BOLD, 20));

        teamSizeSlider.setMajorTickSpacing(1);
        teamSizeSlider.setPaintTicks(true);
        teamSizeSlider.setPaintLabels(true);
        teamSizeSlider.setPreferredSize(new Dimension(300, 60));
        wormPanel.add(teamSizeSlider);

        topSettingsPanel.add(mapPanel);
        topSettingsPanel.add(wormPanel);


        JPanel weaponPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(20, 20, 20, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        weaponPanel.setOpaque(false);
        weaponPanel.setLayout(new GridLayout(3, 2, 20, 10));

        weaponPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2), // Cadre gris
                " Items Autorisées ",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 22),
                new Color(100, 255, 201) // Vert bleue
        ));
        weaponPanel.setMaximumSize(new Dimension(900, 300));
        weaponPanel.setPreferredSize(new Dimension(900, 300));
        chkBazooka = new JCheckBox("Bazooka", false);
        styleCheckBoxWithIcon(chkBazooka, "Bazooka");
        chkUzi = new JCheckBox("Uzi", false);
        styleCheckBoxWithIcon(chkUzi, "Uzi");
        chkShotgun = new JCheckBox("Shotgun", false);
        styleCheckBoxWithIcon(chkShotgun, "Shotgun");
        chkGrenade = new JCheckBox("Grenade", false);
        styleCheckBoxWithIcon(chkGrenade, "Grenade");
        chkMine = new JCheckBox("Mine", false);
        styleCheckBoxWithIcon(chkMine, "Mine");


        weaponPanel.add(chkBazooka);
        weaponPanel.add(chkUzi);
        weaponPanel.add(chkShotgun);
        weaponPanel.add(chkGrenade);
        weaponPanel.add(chkMine);

        weaponPanel.add(new JLabel(""));


        startButton = ButtonManager.createGameButton("LANCER LA PARTIE !");
        backButton = ButtonManager.createGameButton("Retour");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        this.add(Box.createVerticalStrut(30));
        this.add(title);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(topSettingsPanel);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(weaponPanel);
        centerPanel.add(Box.createVerticalStrut(80));
        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(backButton);

        centerPanel.add(Box.createVerticalGlue());

        this.add(centerPanel, BorderLayout.CENTER);

        new GameSetupController(this, mainFrame);




    }
    private JPanel createVerticalBox(String text) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 26));
        l.setForeground(new Color(255, 220, 100));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        return p;
    }
    private void styleCheckBoxWithIcon(JCheckBox box, String imageName) {
        box.setOpaque(false);
        box.setFocusPainted(false);
        box.setContentAreaFilled(false);
        box.setBorderPainted(false);

        box.setForeground(Color.WHITE);
        box.setFont(new Font("Arial", Font.BOLD, 20));
        box.setIconTextGap(15);

        Image originalImg = ImageManager.get(imageName);
        if (originalImg == null) {
            return;
        }

        int size = 60;

        BufferedImage unselectedImg = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = unselectedImg.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.drawImage(originalImg, 0, 0, size, size, null);
        g2.dispose();

        BufferedImage selectedImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g3 = selectedImg.createGraphics();

        g3.drawImage(originalImg, 0, 0, size, size, null);

        // On dessine un cadre autour
        g3.setColor(new Color(224, 185, 145));
        g3.setStroke(new BasicStroke(3));
        g3.drawRoundRect(1, 1, size - 2, size - 2, 10, 10); // Cadre arrondi
        g3.dispose();

        box.setIcon(new ImageIcon(unselectedImg));
        box.setSelectedIcon(new ImageIcon(selectedImg));
        box.setPressedIcon(new ImageIcon(selectedImg));
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
    public boolean isBazookaEnabled() {

        return chkBazooka.isSelected();
    }
    public boolean isUziEnabled() {

        return chkUzi.isSelected();
    }
    public boolean isShotgunEnabled() {

        return chkShotgun.isSelected();
    }
    public boolean isGrenadeEnabled() {

        return chkGrenade.isSelected();
    }
    public boolean isMineEnabled() {

        return chkMine.isSelected();
    }
}
