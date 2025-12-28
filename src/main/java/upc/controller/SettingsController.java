package upc.controller;

import upc.assets.Sound;
import upc.assets.SoundManager;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;
import upc.view.gui.SettingsPanel;

public class SettingsController {

    private final SettingsPanel settingsPanel;
    private final MainFrame mainFrame;
    private final MenuPanel menuPanel;

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
                currentMusic.setVolume(-20f);
            } else if (sliderValue < 40) {
                currentMusic.setVolume(-12f);
            } else if (sliderValue < 60) {
                currentMusic.setVolume(-5f);
            } else if (sliderValue < 80) {
                currentMusic.setVolume(1f);
            } else {
                currentMusic.setVolume(6f);
            }
        }
    }
}
