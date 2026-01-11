package upc.controller;

import upc.model.entites.projectiles.Projectile;
import upc.model.items.Item;
import upc.model.jeu.Team;
import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.save.GameSave;
import upc.view.gui.GameOverPanel;
import upc.view.gui.GamePanel;
import upc.view.gui.InventoryPanel;
import upc.view.gui.PausePanel;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class GameController {

    /** Gestionnaire des entrées clavier */
    private final KeyboardHandler keyHandler = new KeyboardHandler();

    /** Gestionnaire des entrées souris (position, clics, survol) */
    private final MouseHandler mouseHandler = new MouseHandler();

    /** Panneau graphique principal du jeu (vue) */
    private final GamePanel gamePanel;

    /** Gestionnaire de la logique métier et de l'état du jeu (modèle) */
    private final GameManager gameManager;

    /** Gestionnaire des actions du menu pause */
    private final PauseActions pauseActions;

    /** Vitesse de déplacement de la caméra lors du scroll manuel (en pixels par frame) */
    public static final int CAMERA_SPEED = 10;

    /** Indicateur de continuation de la boucle de jeu */
    private volatile boolean running = true;

    /** Indicateur d'état de pause du jeu */
    private static boolean paused = false;

    /** Thread dédié à la boucle de jeu (60 FPS) */
    private Thread gameThread;

    /** Compteur de frames pour gérer la cadence de tir de certaines armes (Uzi) */
    private int frameCount;

    /**
     * Construit un nouveau contrôleur de jeu et démarre la boucle de jeu.
     * <p>
     * Le constructeur initialise les gestionnaires d'entrées, configure les listeners
     * pour les boutons du menu pause, et lance immédiatement la boucle de jeu à 60 FPS.
     *
     * @param gamePanel   Le panneau graphique du jeu (vue).
     * @param gameManager Le gestionnaire de la logique de jeu (modèle).
     */
    public GameController(GamePanel gamePanel, GameManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;
        this.pauseActions = new PauseActions(this, gamePanel);

        addListeners();
        startGameLoop();
    }
    /**
     * Configure tous les listeners nécessaires au jeu.
     * <p>
     * Cette méthode enregistre les gestionnaires d'entrées (clavier, souris) sur le GamePanel
     * et associe les actions aux boutons du menu pause (reprendre, sauvegarder, quitter, etc.).
     */
    private void addListeners() {
        gamePanel.addKeyListener(keyHandler);
        gamePanel.addMouseMotionListener(mouseHandler);
        gamePanel.addMouseListener(mouseHandler);
        gamePanel.setFocusable(true);

        PausePanel pausePanel = gamePanel.getPausePanel();
        pausePanel.getResumeButton().addActionListener(pauseActions.getResumeAction());
        pausePanel.getSaveButton().addActionListener(pauseActions.getSaveGameAction());
        pausePanel.getMainMenuButton().addActionListener(pauseActions.getMainMenuAction());
        pausePanel.getSettingsButton().addActionListener(pauseActions.getSettingsAction());
        pausePanel.getQuitButton().addActionListener(pauseActions.getQuitAction());
    }

    /**
     * Démarre la boucle principale du jeu dans un thread dédié.
     * <p>
     * La boucle tourne à 60 FPS (frames par seconde) et appelle la méthode {@link #update()}
     * à chaque frame. Le thread dort entre chaque frame pour maintenir une cadence stable.
     * <p>
     * Le thread est marqué comme daemon pour qu'il se termine automatiquement
     * lorsque l'application se ferme.
     */
    private void startGameLoop() {
        gameThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double targetFps = 60.0;
            final double nsPerUpdate = 1_000_000_000.0 / targetFps;

            while (running) {
                long now = System.nanoTime();
                double delta = (now - lastTime) / nsPerUpdate;
                lastTime = now;

                if (delta > 0) {
                    update();
                }

                try {
                    long sleepNs = (long) (nsPerUpdate - (System.nanoTime() - now));
                    long sleepTime = sleepNs / 1_000_000;
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }
    /**
     * Met à jour l'état du jeu pour une frame (1/60ème de seconde).
     * <p>
     * Cette méthode coordonne toutes les mises à jour nécessaires :
     * <ul>
     *     <li>Gestion de la pause</li>
     *     <li>Mise à jour du modèle (physique, projectiles, aliens)</li>
     *     <li>Gestion de la caméra (suivi automatique ou manuel)</li>
     *     <li>Traitement des entrées utilisateur</li>
     *     <li>Détection de fin de partie</li>
     * </ul>
     */
    private void update() {
        // Si le jeu est pausé on ne met pas à jour le reste
        handlePause(); // gestion des entrées necéssaires seulement
        if (paused) {
            return;
        }
        // Mise à jour du modèle
        gameManager.update(1.0 / 60.0);

        // Gestion de la fin de jeu
        if (gameManager.isOver()) {
            showGameOver();
            return;
        }

        // Gestion de la caméra
        Alien activeAlien = gameManager.getActiveAlien();

        if (gameManager.getActiveProjectile() != null) {
            // Cas 1 : Projectile en vol
            gamePanel.centerCamera(gameManager.getActiveProjectile());

        } else if (mouseHandler.isMouseNearEdge(gamePanel)) {
            // Cas 2 : Mouvement manuel à la souris
            handleCameraScroll();

        } else if (activeAlien != null && activeAlien.isAlive()) {
            // Cas 3 : Suivi du joueur ou dézoom
            if (gameManager.isPaused()) {
                gamePanel.unzoomCamera();
            } else {
                gamePanel.centerCamera(activeAlien);
            }
        }

        handleControls();

        keyHandler.clearJustPressed();
        mouseHandler.clearJustPressed();
        mouseHandler.clearJustReleased();
        gamePanel.repaint();
    }

    /**
     * Traite toutes les entrées utilisateur (clavier et souris) pour contrôler l'alien actif.
     * <p>
     * Cette méthode gère :
     * <ul>
     *     <li>La sélection d'items dans l'inventaire (clic souris)</li>
     *     <li>Les déplacements (Q/D) et le saut (Z)</li>
     *     <li>La visée automatique vers la souris</li>
     *     <li>L'utilisation des items avec des comportements spécifiques par arme</li>
     *     <li>La sauvegarde rapide (F5)</li>
     * </ul>
     */
    private void handleControls() {
        Alien activeAlien = gameManager.getActiveAlien();

        if (!activeAlien.isAlive()) {
            return;
        }

        // Gestion de l'inventaire (clic souris)
        if (isMouseOnInv()) {
            if (mouseHandler.isJustPressed(MouseEvent.BUTTON1)) {
                Point clickPos = mouseHandler.getPosition();

                int slotIndex = InventoryPanel.getClickedSlotIndex(
                        clickPos, gamePanel.getWidth(), gamePanel.getHeight());

                if (slotIndex != -1) {
                    activeAlien.setSelectedItemIndex(slotIndex);
                }
            }
        }

        // Gestion des mouvements
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_Z)) {
            activeAlien.jump();
        }
        if (keyHandler.isKeyPressed(KeyEvent.VK_Q)) {
            activeAlien.moveLeft();
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_D)) {
            activeAlien.moveRight();
        } else {
            activeAlien.idle();
        }

        // Gestion de la visée avec la souris
        mouseHandler.handleAim(activeAlien,
                gamePanel.getCameraX(), gamePanel.getCameraY(), GamePanel.ZOOM);


        // Utilisation de l'item
        Item equipped = activeAlien.getEquippedItem();
        boolean canShoot = !gameManager.isActionDone() && activeAlien.canShoot();

        if (equipped != null && canShoot && !isMouseOnInv()) {
            // Peut être utilisé plusieurs fois si c'est un Uzi
            if (equipped instanceof upc.model.items.Uzi) {
                if (mouseHandler.isPressed(MouseEvent.BUTTON1)) {
                    if (frameCount % 5 == 0) {
                        activeAlien.setShootPower(100);
                        activeAlien.useEquippedItem();
                        activeAlien.setShootPower(0);
                        frameCount = 0;
                    }
                    frameCount++;
                }
            // Le shotgun tire directement sans jauge de puissance
            } else if (equipped instanceof upc.model.items.Shotgun
                || equipped instanceof upc.model.items.Wings) {
                if (mouseHandler.isJustPressed(MouseEvent.BUTTON1)) {
                    activeAlien.setShootPower(100);
                    activeAlien.useEquippedItem();
                    activeAlien.setShootPower(0);
                }
            // Pour le reste, on peut maintenir le clic pour plus de puissance
            } else {
                if (mouseHandler.isPressed(MouseEvent.BUTTON1)) {
                    activeAlien.incrementPower();
                }
                if (mouseHandler.isJustReleased(MouseEvent.BUTTON1)) {
                    Projectile p = activeAlien.useEquippedItem();
                    gameManager.setActiveProjectile(p);
                    activeAlien.setShootPower(0);
                }
            }
        }
        // Touche pour la sauvegarde
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_F5)) {
            try {
                saveGame();
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde du jeu : " + e.getMessage());
            }
        }
    }
    /**
     * Vérifie si la souris survole actuellement la zone de l'inventaire.
     * <p>
     * Cette vérification permet d'éviter de déclencher des tirs lorsque l'utilisateur
     * clique sur l'inventaire pour changer d'arme.
     *
     * @return true si la souris est sur l'inventaire, false sinon.
     */
    private boolean isMouseOnInv() {
        if (!gamePanel.getHudPanel().isInventoryVisible()) {
            return false;
        }
        Point mousePos = mouseHandler.getPosition();

        return InventoryPanel.isInsideInventory(
                mousePos, gamePanel.getWidth(), gamePanel.getHeight());
    }

    /**
     * Déplace la caméra manuellement lorsque la souris est proche des bords de l'écran.
     * <p>
     * Cette méthode délègue le calcul et l'application du scroll au MouseHandler.
     */
    private void handleCameraScroll() {
        mouseHandler.handleCameraScroll(gamePanel);
    }


    /**
     * Gère la mise en pause du jeu via la touche Échap.
     * <p>
     * Cette méthode détecte l'appui sur Échap, bascule l'état de pause,
     * affiche ou cache le menu pause, et nettoie les entrées clavier pour éviter
     * des actions involontaires lors de la reprise.
     */
    public void handlePause() {
        if (keyHandler.isJustKeyPressed(KeyEvent.VK_ESCAPE)) {
            togglePause();
        }
        gamePanel.togglePauseMenu(paused);
        if (paused) {
            keyHandler.clearJustPressed();
        }
    }

    public static void togglePause() {
        paused = !paused;
    }

    public void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void showGameOver() {
        stop();
        gamePanel.stopMusic();
        Team winner = gameManager.getWinningTeam();

        SwingUtilities.invokeLater(() -> {
            GameOverPanel gameOverPanel = new GameOverPanel(winner);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
            if (frame != null) {
                frame.setContentPane(gameOverPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    /**
     * Méthode permettant la sauvegarde du jeu
     */
    public void saveGame() throws IOException {
        GameSave gameSave = new GameSave(gameManager);
        gameSave.save();
    }
}
