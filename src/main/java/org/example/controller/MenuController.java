package org.example.controller;

import org.example.view.gui.MenuPanel;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MenuController {

    private final MenuPanel menuPanel;

    public MenuController(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
        addListeners();
    }

    public void addListeners() {
        menuPanel.addNewGameListener(e -> {
            try {
                String currentPath = new File(MenuController.class
                        .getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

                String consoleMainClass = "org.example.MainConsole";

                new ProcessBuilder(
                        "cmd", "/c", "start", "java", "-cp", currentPath, consoleMainClass
                ).start();

            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        menuPanel.addLoadGameListener(e -> {
            // à implémenter plus tard
        });

        menuPanel.addQuitterListener(e -> System.exit(0));
    }
}
