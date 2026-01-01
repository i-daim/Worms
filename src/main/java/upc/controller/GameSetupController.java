package upc.controller;

import upc.model.jeu.GameManager;
import upc.model.jeu.Inventory;
import upc.model.items.Item;
import upc.view.gui.GamePanel;
import upc.view.gui.GameSetupPanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;


public class GameSetupController {
    private final GameSetupPanel view;
    private final MainFrame mainFrame;

    private JButton startButton;
    private JButton backButton;
    private JComboBox<String> mapSelector;
    private JSlider teamSizeSlider;
    private JCheckBox chkBazooka, chkUzi, chkShotgun, chkGrenade, chkMine;

    public GameSetupController(GameSetupPanel view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
    }

    private void startGame() {
        Set<Item.Type> allowed = new HashSet<>();
        if (view.isBazookaEnabled()) allowed.add(Item.Type.BAZOOKA);
        if (view.isUziEnabled()) allowed.add(Item.Type.UZI);
        if (view.isShotgunEnabled()) allowed.add(Item.Type.SHOTGUN);
        if (view.isGrenadeEnabled()) allowed.add(Item.Type.GRENADE);
        if (view.isMineEnabled()) allowed.add(Item.Type.MINE);

    }
}
