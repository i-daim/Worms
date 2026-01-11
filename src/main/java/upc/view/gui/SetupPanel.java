package upc.view.gui;

import upc.assets.ImageManager;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.DefaultListCellRenderer;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.awt.RenderingHints;
import java.awt.Cursor;

/**
 * Setup de la partie.
 * <p>
 * Ce panneau permet au joueur de définir les paramètres de la partie avant de la lancer.
 * Il propose une interface stylisée permettant de configurer :
 * <ul>
 * <li><b>La carte</b> : Choix du thème (Glace, Enfer, Poison, etc.).</li>
 * <li><b>Les équipes</b> : Taille des effectifs via un slider.</li>
 * <li><b>L'environnement</b> : Activation ou désactivation du vent.</li>
 * <li><b>L'arsenal</b> : Sélection des armes autorisées via des cases à cocher illustrées.</li>
 * </ul>
 */
public class SetupPanel extends JPanel {
    private final Image background = ImageManager.get("menu_background");

    private final JComboBox<String> mapSelector;
    private final JSlider teamSizeSlider;
    private final JButton startButton;
    private final JButton backButton;

    private final JCheckBox chkBazooka;
    private final JCheckBox chkUzi;
    private final JCheckBox chkShotgun;
    private final JCheckBox chkGrenade;
    private final JCheckBox chkMine;
    private final JCheckBox chkWings;
    private final JCheckBox chkWind;

    private static final Color PANEL_BG_COLOR = new Color(30, 30, 40, 220);
    private static final Color ACCENT_COLOR = new Color(100, 255, 201);
    private static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 22);

    private final double zoom = GamePanel.ZOOM;
    private final int iconSize = (int) (40 * zoom);

    /**
     * Constructeur du panneau de configuration.
     * <p>
     * Initialise l'ensemble de l'interface graphique :
     * <ul>
     * <li>Crée les panneaux arrondis semi-transparents pour grouper les options.</li>
     * <li>Initialise les composants interactifs.</li>
     * <li>Configure la grille de sélection des armes avec icônes.</li>
     * <li>Instancie le {@link GameSetupController} pour gérer la logique.</li>
     * </ul>
     *
     * @param frame La fenêtre principale de l'application.
     */
    public SetupPanel(MainFrame frame) {
        this.setDoubleBuffered(true);
        this.setLayout(new BorderLayout());

        int topMargin = (int) (90 * zoom);
        int weaponHeight = (int) (170 * zoom);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(new EmptyBorder(topMargin, 50, 20, 50)); // Marges

        mainContentPanel.add(Box.createVerticalGlue());

        JPanel configPanel = createRoundedPanel();
        configPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        configPanel.setMaximumSize(new Dimension(1000, (int) (62 * zoom)));

        JPanel mapBox = createSection("Map", 240);
        String[] maps = {"Random", "Liberty", "Spaceship", "Ice", "Poison", "Hell"};
        mapSelector = new JComboBox<>(maps);
        styleComboBox(mapSelector);
        mapBox.add(mapSelector);
        configPanel.add(mapBox);

        JPanel teamBox = createSection("Team Size", 250);
        teamSizeSlider = new JSlider(1, 4, 2);
        styleSlider(teamSizeSlider);
        teamBox.add(teamSizeSlider);
        configPanel.add(teamBox);

        JPanel windBox = createSection("Wind", 200);
        chkWind = createWeaponCheckbox("Activated");
        chkWind.setSelected(false); // Désactivé par défaut
        chkWind.setAlignmentX(Component.CENTER_ALIGNMENT);
        windBox.add(chkWind);
        configPanel.add(windBox);

        mainContentPanel.add(configPanel);
        mainContentPanel.add(Box.createVerticalStrut(30));

        JPanel weaponPanel = createRoundedPanel();
        weaponPanel.setLayout(new BorderLayout());
        weaponPanel.setMaximumSize(new Dimension(900, weaponHeight));
        weaponPanel.setPreferredSize(new Dimension(900, weaponHeight));

        JLabel weaponTitle = new JLabel("Allowed Items", SwingConstants.CENTER);
        weaponTitle.setFont(new Font("Arial", Font.BOLD, (int) (12 * zoom)));
        weaponTitle.setForeground(ACCENT_COLOR);
        weaponTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        weaponPanel.add(weaponTitle, BorderLayout.NORTH);

        JPanel itemsGrid = new JPanel(new GridLayout(3, 3, 20, 20));
        itemsGrid.setOpaque(false);
        itemsGrid.setBorder(new EmptyBorder(10, 30, 30, 30));

        chkBazooka = createWeaponCheckbox("Bazooka");
        chkUzi = createWeaponCheckbox("Uzi");
        chkShotgun = createWeaponCheckbox("Shotgun");
        chkGrenade = createWeaponCheckbox("Grenade");
        chkMine = createWeaponCheckbox("Mine");
        chkWings = createWeaponCheckbox("Wings");

        itemsGrid.add(chkBazooka);
        itemsGrid.add(chkUzi);
        itemsGrid.add(chkShotgun);
        itemsGrid.add(chkGrenade);
        itemsGrid.add(chkMine);
        itemsGrid.add(chkWings);

        weaponPanel.add(itemsGrid, BorderLayout.CENTER);

        mainContentPanel.add(weaponPanel);
        mainContentPanel.add(Box.createVerticalStrut(40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);

        backButton = ButtonManager.createGameButton("Back");
        startButton = ButtonManager.createGameButton("Start Game");

        buttonPanel.add(backButton);
        buttonPanel.add(startButton);

        mainContentPanel.add(buttonPanel);
        mainContentPanel.add(Box.createVerticalGlue());

        this.add(mainContentPanel, BorderLayout.CENTER);

        new GameSetupController(this, frame);
    }



    /**
     * Crée un panneau personnalisé avec des coins arrondis et un fond semi-transparent.
     * <p>
     * Surcharge la méthode {@code paintComponent} pour dessiner un rectangle arrondi
     * avec une bordure fine.
     * </p>
     * @return Le JPanel stylisé.
     */
    private JPanel createRoundedPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PANEL_BG_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Coins arrondis

                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    /**
     * Crée une section verticale contenant un titre et de l'espace pour un composant.
     *
     * @param labelTitle Le titre de la section.
     * @param width La largeur préférée de la section.
     * @return Le JPanel conteneur.
     */
    private JPanel createSection(String labelTitle, int width) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(width, 80));

        JLabel l = new JLabel(labelTitle);
        l.setFont(new Font("Arial", Font.BOLD, (int) (8 * GamePanel.ZOOM)));
        l.setForeground(ACCENT_COLOR);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(l);
        p.add(Box.createVerticalStrut(10));
        return p;
    }

    /**
     * Applique le style graphique à une ComboBox.
     * @param combo La ComboBox à styliser.
     */
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(MAIN_FONT);
        combo.setPreferredSize(new Dimension(200, 40));
        combo.setMaximumSize(new Dimension(200, 40));
        DefaultListCellRenderer centerRenderer = new DefaultListCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        combo.setRenderer(centerRenderer);
    }

    /**
     * Applique le style graphique au slider de taille d'équipe.
     * @param slider Le JSlider à styliser.
     */
    private void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setForeground(Color.WHITE);
        slider.setFont(new Font("Arial", Font.BOLD, 16));
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    /**
     * Crée une case à cocher personnalisée pour une arme ou une option.
     * <p>
     * Ajoute un écouteur de souris pour changer la couleur et le curseur au survol.
     * Délègue le chargement de l'icône à {@link #styleCheckBoxWithIcon}.
     * </p>
     * @param name Le nom de l'arme/option.
     * @return La JCheckBox configurée.
     */
    private JCheckBox createWeaponCheckbox(String name) {
        JCheckBox box = new JCheckBox(name);
        box.setSelected(true);
        styleCheckBoxWithIcon(box, name);

        box.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                box.setForeground(ACCENT_COLOR);
                box.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                box.setForeground(Color.WHITE);
            }
        });
        return box;
    }

    /**
     * Configure l'aspect visuel de la CheckBox et charge ses icônes.
     * @param box La CheckBox à configurer.
     * @param imageName Le nom de l'image à charger via ImageManager.
     */
    private void styleCheckBoxWithIcon(JCheckBox box, String imageName) {
        box.setOpaque(false);
        box.setFocusPainted(false);
        box.setContentAreaFilled(false);
        box.setBorderPainted(false);
        box.setForeground(Color.WHITE);
        box.setFont(new Font("Arial", Font.BOLD, (int) (8 * GamePanel.ZOOM)));
        box.setHorizontalAlignment(SwingConstants.LEFT);
        box.setIconTextGap(20);

        Image originalImg = ImageManager.get(imageName + "_Inv");
        //  Protection si l'image n'existe pas encore
        if (originalImg == null) {
            return;
        }

        int iconW = this.iconSize;
        int iconH = this.iconSize;
        box.setIcon(new ImageIcon(createIcon(originalImg, iconW, iconH, false)));
        box.setSelectedIcon(new ImageIcon(createIcon(originalImg, iconW, iconH, true)));
        box.setPressedIcon(box.getSelectedIcon());
    }

    /**
     * Génère une icône dynamique pour la CheckBox en fonction de l'image de
     * l'arme et de l'état de sélection.
     * <p>
     * <ul>
     * <li><b>Sélectionné</b> : Fond vert clair, bordure dorée.</li>
     * <li><b>Non sélectionné</b> : Fond sombre, image semi-transparente, bordure blanche.</li>
     * </ul>
     *
     * @param img L'image source de l'arme.
     * @param w La largeur de l'icône.
     * @param h La hauteur de l'icône.
     * @param selected Indique si l'état à générer est "sélectionné".
     * @return Une BufferedImage représentant l'icône.
     */
    private BufferedImage createIcon(Image img, int w, int h, boolean selected) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(selected ? new Color(100, 255, 201, 30) : new Color(0, 0, 0, 100));
        g2.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);

        if (!selected) {
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }
        g2.drawImage(img, 10, 10, w - 20, h - 20, null);

        if (selected) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);
        }
        g2.dispose();
        return image;
    }

    /**
     * Dessine l'arrière-plan du panneau et applique un filtre sombre.
     * @param g Le contexte graphique.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * @return Le bouton pour démarrer la partie.
     */
    public JButton getStartButton() {
        return startButton;
    }
    /**
     * @return Le bouton pour revenir au menu principal.
     */
    public JButton getBackButton() {
        return backButton;
    }
    /**
     * @return Le nom de la carte sélectionnée.
     */
    public String getSelectedMap() {
        return (String) mapSelector.getSelectedItem();
    }
    /**
     * @return La taille de l'équipe sélectionnée (1-4).
     */
    public int getTeamSize() {
        return teamSizeSlider.getValue();
    }
    /**
     * @return Vrai si le Bazooka est activé, Faux sinon.
     */
    public boolean isBazookaEnabled() {
        return chkBazooka.isSelected();
    }
    /**
     * @return Vrai si l'Uzi est activé, Faux sinon.
     */
    public boolean isUziEnabled() {
        return chkUzi.isSelected();
    }
    /**
     * @return Vrai si le Shotgun est activé, Faux sinon.
     */
    public boolean isShotgunEnabled() {
        return chkShotgun.isSelected();
    }
    /**
     * @return Vrai si la Grenade est activée, Faux sinon.
     */
    public boolean isGrenadeEnabled() {
        return chkGrenade.isSelected();
    }
    /**
     * @return Vrai si la Mine est activée, Faux sinon.
     */
    public boolean isMineEnabled() {
        return chkMine.isSelected();
    }
    /**
     * @return Vrai si le Jetpack est activé, Faux sinon.
     */
    public boolean isJetpackEnabled() {
        return chkWings.isSelected();
    }
    /**
     * @return Vrai si le Vent est activé, Faux sinon.
     */
    public boolean isWindEnabled() {
        return chkWind.isSelected();
    }
}