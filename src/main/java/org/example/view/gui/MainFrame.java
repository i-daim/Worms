package org.example.view.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Worms 0.5");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setVisible(true);

        MainPanel mainPanel = new MainPanel();
        this.setContentPane(mainPanel);
        // TODO : Ajouter un controleur à relier à la vue.
    }



}
