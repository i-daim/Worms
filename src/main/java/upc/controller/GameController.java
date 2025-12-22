package upc.controller;
import upc.model.jeu.Inventory;
import upc.model.jeu.Team;


import upc.model.entites.Alien;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;
import upc.model.save.SaveInventory;
import upc.model.save.SaveMap;
import upc.model.save.SaveTeam;
import upc.view.gui.GameOverPanel;
import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class GameController {

    private final KeyboardHandler keyHandler = new KeyboardHandler();
    private final MouseHandler mouseHandler = new MouseHandler();

    private final GamePanel gamePanel;
    private final GameManager gameManager;

    private static final int CAMERA_SPEED = 10;

    private boolean inventoryVisible = true;

    // Variables pour la gestion manuelle du TOGGLE (pour éviter la répétition)
    private boolean iKeyWasPressed = false;
    private boolean[] selectionKeysWasPressed = new boolean[5];

    private volatile boolean running = true;
    private Thread gameThread;
    private boolean lastAWasPressed = false;

    // Constructeur //
    public GameController(GamePanel gamePanel, GameManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;

        gamePanel.setInventoryVisible(inventoryVisible);

        addListeners();
        startGameLoop();
    }

    private void addListeners() {
        gamePanel.addKeyListener(keyHandler);
        gamePanel.addMouseMotionListener(mouseHandler);
        gamePanel.setFocusable(true);
    }


    ////    GESTION DE LA BOUCLE DU JEU    ////
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

    private void update() {
        // Gestion de la caméra à travers mouseHandler
        handleCameraScroll(mouseHandler.getPosition());
        // Gestion des touches clavier et de l'inventaire
        handleControls();

        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien != null && activeAlien.isAlive()) {
            gamePanel.centralizeCamera(activeAlien);
        }

        gameManager.update(1.0 / 60.0);

        if (gameManager.isOver()) {
            showGameOver();
            return;
        }
        boolean isAPressed = keyHandler.isKeyPressed(KeyEvent.VK_A);
        if (isAPressed && !lastAWasPressed) {  // ← EDGE DETECTION
            if (!gameManager.isActionDone() && activeAlien != null) {
                activeAlien.useEquippedItem();  // ← Appelé UNE SEULE FOIS
                System.out.println("🎯 TIR DÉCLENCHÉ UNE FOIS !");
            }
        }
        lastAWasPressed = isAPressed;
        gamePanel.repaint();
    }


    ////    GESTION DES ENTREES SOURIS/CLAVIER    ////
    private void handleControls() {

        boolean iIsPressed = keyHandler.isKeyPressed(KeyEvent.VK_I);

        if (iIsPressed && !iKeyWasPressed) {
            inventoryVisible = !inventoryVisible;
            gamePanel.setInventoryVisible(inventoryVisible);
        }
        iKeyWasPressed = iIsPressed;

        if (inventoryVisible) {
            for (int i = 0; i < 5; i++) {
                int keyCode = KeyEvent.VK_1 + i;
                boolean keyIsPressed = keyHandler.isKeyPressed(keyCode);

                if (keyIsPressed && !selectionKeysWasPressed[i]) {
                    gamePanel.setSelectedItemIndex(i);

                }
                selectionKeysWasPressed[i] = keyIsPressed;
            }
        } else {
            for (int i = 0; i < 5; i++) {
                selectionKeysWasPressed[i] = false;
            }
        }

        Alien activeAlien = gameManager.getActiveAlien();
        if (activeAlien == null || !activeAlien.isAlive()) {
            return;
        }

        if (keyHandler.isKeyPressed(KeyEvent.VK_SPACE)) {
            activeAlien.jump();
        }

        if (keyHandler.isKeyPressed(KeyEvent.VK_LEFT)) {
            activeAlien.moveLeft();
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_RIGHT)) {
            activeAlien.moveRight();
        } else {
            activeAlien.idle();
        }

        if (keyHandler.isKeyPressed(KeyEvent.VK_UP)) {
            int angle = activeAlien.getAimAngle() + 2;
            if (angle > 80) {
                angle = 80;
            }
            activeAlien.setAimAngle(angle);
        } else if (keyHandler.isKeyPressed(KeyEvent.VK_DOWN)) {
            int angle = activeAlien.getAimAngle() - 2;
            if (angle < -45) {
                angle = -45;
            }
            activeAlien.setAimAngle(angle);
        }


        if (keyHandler.isKeyPressed(KeyEvent.VK_E)) {
            Point worldPos = gamePanel.screenToWorld(mouseHandler.getPosition());
            gameManager.getWorld().explode(worldPos.x, worldPos.y, 30, 30);
        }
    }

    private void handleCameraScroll(Point mousePos) {
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        int camX = gamePanel.getCameraX();
        int camY = gamePanel.getCameraY();
        int terrainWidth = gamePanel.getTerrain().getWidth();
        int terrainHeight = gamePanel.getTerrain().getHeight();

        if (mousePos.x > width - limit) {
            camX += CAMERA_SPEED;
        } else if (mousePos.x < limit) {
            camX -= CAMERA_SPEED;
        }

        if (mousePos.y > height - limit) {
            camY += CAMERA_SPEED;
        } else if (mousePos.y < limit) {
            camY -= CAMERA_SPEED;
        }

        int viewportWidth = (int) (width / GamePanel.ZOOM);
        int viewportHeight = (int) (height / GamePanel.ZOOM);

        camX = Math.max(0, Math.min(camX, terrainWidth - viewportWidth));
        camY = Math.max(0, Math.min(camY, terrainHeight - viewportHeight));

        gamePanel.setCamera(camX, camY);
    }

    ////    ARRET DU JEU    ////
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

    /*Méthode permettant la sauvegarde du jeu*/
    public void saveGame() throws IOException {

        //Crée un objet saveMap
        //Sauvegarde de la map dans un fichier dédié
        SaveMap saveMap = new SaveMap();
        saveMap.saveMap(gameManager.getTerrain());

        //Crée un objet saveTeam
        //Sauvegarde des équipes dans un fichier dédiés
        SaveTeam saveTeam = new SaveTeam();
        saveTeam.saveTeam(gameManager.getTeams()[0], gameManager.getTeams()[1]);

        //Crée un objet saveInventory
        //Sauvegarde des inventaires des équipes dans 2 fichier dédiés
        SaveInventory saveInventory = new SaveInventory();
        saveInventory.saveInventory(gameManager.getTeams()[0].getInventory(),
                gameManager.getTeams()[1].getInventory());
    }

    /*Méthode permettant de charger la sauvegarde du jeu*/
    public void loadGame() throws IOException {

        //Récupère la map sauvegardée
        SaveMap saveMap = new SaveMap();
        Terrain terrain = saveMap.loadMap();

        //Récupère les équipes sauvegardées
        SaveTeam saveTeam = new SaveTeam();
        Team[] teams = saveTeam.loadTeam();

        //Récupère les inventaire sauvegardés
        SaveInventory saveInventory = new SaveInventory();
        Inventory[] inventory = saveInventory.loadInventory();

        //Charge les équipes sauvegardées
        gameManager.setTeams(teams);

        //Charge les inventaires de chaque équipes
        gameManager.getTeams()[0].setInventory(inventory[0]);
        gameManager.getTeams()[1].setInventory(inventory[1]);

        //Charge la map
        gameManager.setMap(terrain);
    }

}