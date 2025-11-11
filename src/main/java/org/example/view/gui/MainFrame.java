package org.example.view.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Worms 0.5");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setVisible(true);

        EcranAccueil ecranAccueil = new EcranAccueil();
        this.setContentPane(ecranAccueil);
        // TODO : Ajouter un controleur à relier à la vue.
    }



}
