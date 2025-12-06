package upc.view.console;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.input.KeyStroke;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.jeu.Team;
import upc.model.jeu.GameManager;
import upc.model.jeu.Terrain;

import javax.swing.*;

public class LanternaView {
    //attribut permettant la réprésentation du terminal et de le styliser
    private Screen screen;

    //attribut permettant l'écriture du texte, des caractères avec coordonnées et couleur
    private TextGraphics graphics;

    //Constructeur
    public LanternaView() {
        //Gestion des exceptions
        try {
            //Crée le terminal
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

            //Crée le screen en lien avec le terminal terminalFactory
            this.screen = terminalFactory.createScreen();

            //Ouvre l'écran et passe en mode Lanterna
            screen.startScreen();

            //Crée le graphics en lien avec le terminal(screen)
            // pour dessiner des chaînes ou caractères
            this.graphics = this.screen.newTextGraphics();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //Méthode permettant de vider l'écran (avant de redessiner)
    public void clear() {

        this.screen.clear();

    }

    //Méthode permettant d'afficher tout ce qui a été dessiné
    public void refresh() {

        //Gestion des exceptions
        // car Java oblige à gérer toutes les exceptions checked en lien avec refresh()
        try {

            this.screen.refresh();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    //Méthode permettant d'écrire un texte à une position donnée
    public void drawString(int x, int y, String string) {

        //Gestion des exceptions
        try {
            //Permet de dessiner à une position donnée
            graphics.putString(x, y, string);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    //Méthode permettant de lire la touche pressée par l'utilisateur et renvoie le string correspondt à la touche
    public String readKey() {

        //Gestion des exceptions
        try {

            //Permet de récupérer la touche pressée
            KeyStroke key = screen.readInput();

            //Conditions qui renvoient le type de touche pressé
            //Si la touche pressée est <- alors renvoie LEFT
            if (key.getKeyType() == KeyType.ArrowLeft) {
                return "LEFT";
            }

            //Si la touche pressée est -> alors renvoie RIGHT
            if (key.getKeyType() == KeyType.ArrowRight) {
                return "RIGHT";
            }

            //Si la touche pressée est flèche du haut alors renvoie UP
            if (key.getKeyType() == KeyType.ArrowUp) {
                return "UP";
            }

            //Si la touche pressée est flèche du bas alors renvoie DOWN
            if (key.getKeyType() == KeyType.ArrowDown) {
                return "DOWN";
            } else {
                //Sinon renvoie rien
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Méthode permettant l'affichage du terrain de dans le terminal
    public void printTerrain(GameManager gameManager) {
        Terrain terrain = gameManager.getTerrain();

        //Permet d'avoir les dimensions du terrain
        int height = Math.min(terrain.getCollisionsHeight(), 25);
        int width = Math.min(terrain.getCollisionsWidth(), 60);

        //Vide l'écran
        this.clear();

        //Parcourt chaque case du terrain
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                //Dessine chaque case avec le symbole correspondant
                drawString(x, y, symbolForTile(x, y, gameManager));
            }
        }

        //Affiche le rendu
        this.refresh();
    }

    //Méthode permettant d'envoyer le bon caractère à la position donnée
    private String symbolForTile(int x, int y, GameManager gameManager) {
        Alien w = gameManager.getAlienAtPosition(x, y);
        Terrain terrain = gameManager.getTerrain();

        //Renvoie w pour un alien vivant à cette position
        if (w != null && w.isAlive()) {
            return "W";
        }

        //Renvoie ~ pour de l'eau avec collision
        if (terrain.isWater(y) && terrain.isCollision(x, y)) {
            return "~";
        }

        //Renvoie X pour une collision sans eau
        if (terrain.isCollision(x, y)) {
            return "X";
        }

        //Renvoie un espace pour une case vide
        return " ";
    }

    //Methode permettant d'afficher les stats de l'alien active
    private void printActiveAlienStats(GameManager gameManager) {
        Alien alien = gameManager.getActiveAlien();

        if (alien != null) {

            Position pos = alien.getPosition();

            //Affiche les stats les uns au-dessus des autres
            drawString(pos.getX(), pos.getY(),"🐛 Alien actif (" + gameManager.getActiveTeam().getName() + "):");
            drawString(pos.getX(), pos.getY() + 1 , "Position: " + alien.getPosition());
            drawString(pos.getX(), pos.getY() + 2, "PV: " + alien.getHealthPoints() + " /100");
            drawString(pos.getX(), pos.getY() + 3, printHealthBar(alien.getHealthPoints()));

        } else {
            //Si l'alien choisi est nul
            drawString(0, 0,"\n⚠️  Aucun alien actif");
        }

        this.refresh();
    }

    //Méthode permettant la représentation de la barre de vie d'un alien
    private String printHealthBar(int pv) {
        int barres = pv / 10;
        String healthBar = "[";

        for (int i = 0; i < 10; i++) {
            if (i < barres) {
                healthBar += ("█");
            } else {
                healthBar += "░";
            }
        }
        healthBar += "]";
        return healthBar;
    }

    public void printAliensStats(GameManager gameManager) {
        drawString(,, "═".repeat(60));
        drawString(,,"🐛 ÉTAT DE TOUS LES ALIENS VIVANTS 🐛");
        drawString(,,"═".repeat(60));

        Team teamActive = gameManager.getActiveTeam();
        Team teamEnnemie = gameManager.getEnnemyTeam();

        drawString(,,"💚 ÉQUIPE ALLIÉE : " + teamActive.getName());
        drawString(,,"─".repeat(60));
        printAliensTeam(teamActive, gameManager.getActiveAlien());

        drawString(,,"💀 ÉQUIPE ENNEMIE : " + teamEnnemie.getName());
        drawString(,,"─".repeat(60));
        printAliensTeam(teamEnnemie, null);

        drawString(,,"═".repeat(60));
    }

}
