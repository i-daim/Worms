package org.example.controller;

import org.example.view.gui.MenuPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController {

    private final MenuPanel menuPanel;

    public MenuController(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public void addListeners() {
        menuPanel.addNewGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO : Faire en sorte de lancer la partie.
            }
        });

        menuPanel.addLoadGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO : Faire en sorte de charger une partie.
            }
        });

        menuPanel.addQuitterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


}
