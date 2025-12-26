package upc.view.gui;

import upc.assets.ImageManager;
import upc.assets.SoundManager;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Dimension;

public class SettingsPanel extends JPanel {

    private final Image background = ImageManager.get("settings_background");

    private final JButton backButton;
    private JComboBox<String> musicSelection;
    private JSlider volumeSlider;

    public SettingsPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setAlignmentY(Component.TOP_ALIGNMENT);
        title.setForeground(Color.WHITE);

        backButton = ButtonManager.createGameButton("Back");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setAlignmentY(Component.TOP_ALIGNMENT);

        this.add(Box.createHorizontalStrut(30));
        this.add(title);
        this.add(Box.createVerticalStrut(30));

        initControls();

        this.add(Box.createVerticalStrut(30));
        this.add(backButton);
        this.add(Box.createVerticalGlue());
        this.add(Box.createVerticalGlue());
    }

    private void initControls() {
        JLabel musicLabel = new JLabel("Music:");
        musicLabel.setFont(new Font("Arial", Font.BOLD, 18));
        musicLabel.setForeground(Color.WHITE);
        musicLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] musics = SoundManager.getMusicsSelection();
        musicSelection = new JComboBox<>(musics);
        musicSelection.setSelectedIndex(0);
        musicSelection.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicSelection.setFont(new Font("Arial", Font.BOLD, 16));
        musicSelection.setForeground(new Color(126, 217, 87));
        musicSelection.setBackground(new Color(55, 65, 75));
        musicSelection.setSelectedIndex(1);

        Dimension d = new Dimension(300, 40);
        musicSelection.setMaximumSize(d);
        musicSelection.setPreferredSize(d);

        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        volumeSlider = new JSlider(0, 100, 50); 
        volumeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeSlider.setOpaque(false);
        volumeSlider.setMaximumSize(new Dimension(300, 50));
        volumeSlider.setPreferredSize(new Dimension(300, 50));

        this.add(musicLabel);
        this.add(Box.createVerticalStrut(5));
        this.add(musicSelection);
        this.add(Box.createVerticalStrut(20));
        this.add(volumeLabel);
        this.add(Box.createVerticalStrut(5));
        this.add(volumeSlider);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JComboBox<String> getMusicSelection() {
        return musicSelection;
    }

    public JSlider getVolumeSlider() {
        return volumeSlider;
    }
}
