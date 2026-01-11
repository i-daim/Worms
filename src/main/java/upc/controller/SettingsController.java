package upc.controller;

import upc.assets.Sound;
import upc.assets.SoundManager;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

public class SettingsController {

    /** Panneau de paramètres contenant les contrôles (slider, sélecteur, boutons) */
    private final SettingsPanel settingsPanel;

    /** Fenêtre principale de l'application */
    private final MainFrame mainFrame;

    /** Panneau de menu pour le retour (peut être null si vient du jeu) */
    private final MenuPanel menuPanel;


    /**
     * Construit le contrôleur des paramètres et initialise les listeners.
     *
     * @param settingsPanel Le panneau de paramètres à contrôler.
     * @param mainFrame     La fenêtre principale.
     * @param menuPanel     Le panneau de menu (null si appelé depuis le jeu en pause).
     */
    public SettingsController(SettingsPanel settingsPanel,
                              MainFrame mainFrame, MenuPanel menuPanel) {
        this.settingsPanel = settingsPanel;
        this.mainFrame = mainFrame;
        this.menuPanel = menuPanel;
        initListeners();
    }

    private void initListeners() {
        settingsPanel.getMusicSelection().addActionListener(e -> {
            String selectedMusicName = (String)
                    settingsPanel.getMusicSelection().getSelectedItem();

            if (selectedMusicName != null) {
                SoundManager.playMusic(selectedMusicName);
                updateVolume();
            }
        });

        settingsPanel.getVolumeSlider().addChangeListener(e -> updateVolume());

        settingsPanel.getBackButton().addActionListener(e -> {
            mainFrame.setContentPane(menuPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    private void updateVolume() {
        Sound currentMusic = SoundManager.getCurrentMusic();
        if (currentMusic != null) {
            int sliderValue = settingsPanel.getVolumeSlider().getValue();

            if (sliderValue == 0) {
                currentMusic.setVolume(-80f);
            } else if (sliderValue < 20) {
                currentMusic.setVolume(-26f);
            } else if (sliderValue < 40) {
                currentMusic.setVolume(-18f);
            } else if (sliderValue < 60) {
                currentMusic.setVolume(-10f);
            } else if (sliderValue < 80) {
                currentMusic.setVolume(0f);
            } else {
                currentMusic.setVolume(4f);
            }
        }
    }
}
