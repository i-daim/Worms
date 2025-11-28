package org.example.view.gui;

import org.example.controller.MenuController;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Glorb26");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        MenuPanel menuPanel = new MenuPanel();
        new MenuController(this, menuPanel);
        this.setContentPane(menuPanel);
        this.setVisible(true);
    }



}
