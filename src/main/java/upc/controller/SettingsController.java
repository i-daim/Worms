package upc.controller;

import upc.assets.Sound;
import upc.assets.SoundManager;
import upc.view.gui.SettingsPanel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsController {

    private final SettingsPanel settingsPanel;

    public SettingsController(SettingsPanel settingsPanel) {
        this.settingsPanel = settingsPanel;
        initListeners();
    }

    private void initListeners() {
        settingsPanel.getMusicSelection().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMusicName = (String)
                        settingsPanel.getMusicSelection().getSelectedItem();

                if (selectedMusicName != null) {
                    SoundManager.playMusic(selectedMusicName);
                    updateVolume();
                }
            }
        });

        settingsPanel.getVolumeSlider().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateVolume();
            }
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
