package org.example.view.gui;

import org.example.controller.MenuController;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Worms 0.5");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setVisible(true);
        this.setResizable(false);

        MenuPanel menuPanel = new MenuPanel();
        new MenuController(menuPanel);
        this.setContentPane(menuPanel);
    }



}
