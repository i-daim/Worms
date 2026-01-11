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
/**
 * Contrôleur de l'écran de configuration de partie.
 * <p>
 * Cette classe gère l'interface de configuration avant le lancement d'une partie.
 * Elle permet au joueur de personnaliser les paramètres de jeu :
 * <ul>
 *     <li>Sélection de la carte (ou génération aléatoire)</li>
 *     <li>Nombre d'aliens par équipe</li>
 *     <li>Activation/désactivation de chaque arme et outil</li>
 *     <li>Activation/désactivation du vent</li>
 * </ul>
 * <p>
 * Une fois la configuration validée, le contrôleur crée le GameManager
 * avec les paramètres choisis et lance la partie.
 */
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
    /**
     * Initialise les listeners sur les boutons de l'interface.
     * <p>
     * Cette méthode associe les actions suivantes :
     * <ul>
     *     <li>Bouton "Retour" : retourne au menu principal</li>
     *     <li>Bouton "Démarrer" : valide la configuration et lance la partie</li>
     * </ul>
     */
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

    /**
     * Démarre une nouvelle partie avec les paramètres configurés par le joueur.
     * <p>
     * Cette méthode effectue les étapes suivantes :
     * <ol>
     *     <li>Récupère les armes/outils activés par le joueur et configure l'inventaire global</li>
     *     <li>Récupère la carte sélectionnée (ou génère aléatoirement)</li>
     *     <li>Récupère la taille des équipes</li>
     *     <li>Crée le terrain selon la carte choisie</li>
     *     <li>Initialise le GameManager avec ces paramètres</li>
     *     <li>Active/désactive le vent selon le choix du joueur</li>
     *     <li>Crée le GamePanel et son contrôleur</li>
     *     <li>Affiche le jeu dans la fenêtre principale</li>
     * </ol>
     */
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

        // Active ou désactive le vent selon le choix du joueur
        gameManager.setWindEnabled(view.isWindEnabled());

        GamePanel gamePanel = new GamePanel(gameManager);
        new GameController(gamePanel, gameManager);

        mainFrame.setContentPane(gamePanel);
        gamePanel.requestFocusInWindow();
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}