package upc.controller;

import upc.model.jeu.GameManager;
import upc.model.jeu.Inventory;
import upc.model.items.Item;
import upc.view.gui.GamePanel;
import upc.view.gui.SetupPanel;
import upc.view.gui.MainFrame;
import upc.view.gui.MenuPanel;

import java.util.HashSet;
import java.util.Set;


public class GameSetupController {
    /** Vue de l'écran de configuration contenant les options et les boutons */
    private final SetupPanel view;

    /** Fenêtre principale de l'application (pour changer de panneau) */
    private final MainFrame mainFrame;

    /**
     * Construit un nouveau contrôleur pour l'écran de configuration.
     * <p>
     * Le constructeur initialise les listeners sur les boutons de la vue
     * (Retour au menu, Démarrer la partie).
     *
     * @param view      Le panneau de configuration contenant les options et boutons.
     * @param mainFrame La fenêtre principale de l'application.
     */
    public GameSetupController(SetupPanel view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
        initListeners();
    }
    private void initListeners() {
        view.getBackButton().addActionListener(e -> {
            MenuPanel menuPanel = new MenuPanel();
            new MenuController(mainFrame, menuPanel);
            mainFrame.setContentPane(menuPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        view.getStartButton().addActionListener(e -> startGame());
    }

    private void startGame() {
        Set<Item.Type> allowed = new HashSet<>();
        if (view.isBazookaEnabled()) {
            allowed.add(Item.Type.BAZOOKA);
        }
        if (view.isUziEnabled()) {
            allowed.add(Item.Type.UZI);
        }
        if (view.isShotgunEnabled()) {
            allowed.add(Item.Type.SHOTGUN);
        }
        if (view.isGrenadeEnabled()) {
            allowed.add(Item.Type.GRENADE);
        }
        if (view.isMineEnabled()) {
            allowed.add(Item.Type.MINE);
        }
        if (view.isJetpackEnabled()) {
            allowed.add(Item.Type.WINGS);
        }

        Inventory.setAllowedWeapons(allowed);

        String mapName = view.getSelectedMap();
        int teamSize = view.getTeamSize();

        upc.model.jeu.Terrain terrain;

        if (mapName.equalsIgnoreCase("Random")) {
            terrain = upc.model.jeu.TerrainGenerator.chooseRandomMap();
        } else {
            terrain = upc.model.jeu.TerrainGenerator.createTerrain(mapName.toLowerCase());
        }

        GameManager gameManager = new GameManager(terrain, teamSize);

        // Active le vent dans le manager si le joueur veut
        gameManager.setWindEnabled(view.isWindEnabled());

        GamePanel gamePanel = new GamePanel(gameManager);
        new GameController(gamePanel, gameManager);

        mainFrame.setContentPane(gamePanel);
        gamePanel.requestFocusInWindow();
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}