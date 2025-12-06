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

    //Méthode permettant de lire la touche pressée par l'utilisateur
    // Et renvoie le string correspondant à la touche
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
    private int printActiveAlienStats(GameManager gameManager, int x, int y) {
        Alien alien = gameManager.getActiveAlien();

        if (alien != null) {

            //Affiche les stats les uns au-dessus des autres
            drawString(x, y, "🐛 Alien actif ("
                    + gameManager.getActiveTeam().getName() + "):");
            y++;

            drawString(x, y, "Position: " + alien.getPosition());
            y++;

            drawString(x, y, "PV: " + alien.getHealthPoints() + " /100");
            y++;

            drawString(x, y, printHealthBar(alien.getHealthPoints()));
            y++;

        } else {
            //Si l'alien choisi est nul
            drawString(x, y, "\n⚠️  Aucun alien actif");
            y++;
        }

        //Renvoie la colonne d'affichage pour printGameState()
        return y;
    }

    //Méthode permettant la représentation de la barre de vie d'un alien
    private String printHealthBar(int pv) {
        int barres = pv / 10;
        String healthBar = "[";

        for (int i = 0; i < 10; i++) {
            if (i < barres) {
                healthBar += "█";
            } else {
                healthBar += "░";
            }
        }
        healthBar += "]";
        return healthBar;
    }

    //Méthode permettant d'afficher les stats des équipes
    public void printAliensStats(GameManager gameManager) {

        //Coordonnées d'affichage par défaut à modifier selon où on veut placer l'affichage
        int x = 2;
        int y = 2;

        //Affiche l'état des aliens
        //y++ pour les empiler en colonne
        drawString(x, y, "═".repeat(60));
        y++;
        drawString(x, y, "🐛 ÉTAT DE TOUS LES ALIENS VIVANTS 🐛");
        y++;
        drawString(x, y, "═".repeat(60));
        y++;

        Team teamActive = gameManager.getActiveTeam();
        Team teamEnnemie = gameManager.getEnnemyTeam();

        //Affiche le nom de l'équipe alliée
        drawString(x, y, "💚 ÉQUIPE ALLIÉE : " + teamActive.getName());
        y++;
        drawString(x, y, "─".repeat(60));
        y++;

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats des aliens alliés
        y = printAliensTeam(teamActive, gameManager.getActiveAlien(), x, y);

        //Affiche le nom de l'équipe ennemie
        drawString(x, y, "💀 ÉQUIPE ENNEMIE : " + teamEnnemie.getName());
        y++;
        drawString(x, y, "─".repeat(60));
        y++;

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats des aliens ennemis
        y = printAliensTeam(teamEnnemie, null, x, y);

        drawString(x, y, "═".repeat(60));

        //Affiche le rendu
        this.refresh();
    }

    //Méthode permettant d'afficher les stats de chaque alien d'une équipe
    private int printAliensTeam(Team team, Alien alienActif, int x, int y) {

        Alien[] aliens = team.getAliens();
        int count = 1;
        boolean noneAlive = true;

        //Pour passe d'un alien à alien
        for (Alien alien : aliens) {

            //Vérifie si l'alien est vivant et présent
            if (alien != null && alien.isAlive()) {
                noneAlive = false;
                Position pos = alien.getPosition();

                //Vérifie si c'est l'alien actif
                String indicateur = (alien == alienActif) ? "► " : "  ";

                //Affiche stats de l'alien
                drawString(x, y, indicateur +  " :Alien #"
                        + count + "  | Position: "
                        + pos + " | PV: "
                        + alien.getHealthPoints() + "/100 "
                        + printHealthBar(alien.getHealthPoints()));

                //Pour afficher sur une plusieurs lignes les uns au-dessus des autres
                y++;

                //Itération des aliens
                count++;
            }
        }

        //Vérifie si tous les aliens d'une équipe sont morts
        if (noneAlive) {

            //Affiche l'information
            drawString(x, y, "  ⚰️  Tous les aliens sont morts");
            y++;
        }

        //Renvoie la colonne d'écriture pour printAliensStats
        return y;
    }

    //Méthode permettant de les stats d'une équipe + stats de l'alien actif
    public void printGameState(GameManager gameManager) {

        //Coordonnées d'affichage par défaut à modifier selon où on veut placer l'affichage
        int x = 2;
        int y = 1;

        drawString(x, y, "─".repeat(60));
        y++;

        drawString(x, y, "📊 " + gameManager.toString());
        y++;

        drawString(x, y, "─".repeat(60));
        y++;

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats de l'équipe
        y = printTeamStats(gameManager, x, y);

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats de l'alien aciif
        y = printActiveAlienStats(gameManager, x, y);

        drawString(x, y, "─".repeat(60));

        //Affiche le rendu
        this.refresh();
    }

    //Méthode permettant les stats d'une équipe
    private int printTeamStats(GameManager gameManager, int x, int y) {
        drawString(x, y, "🏆 État des équipes:");

        Team[] teams = gameManager.getTeams();

        for (int i = 0; i < teams.length; i++) {

            //affiche les stats de chaque alien d'une équipe
            String indicateur = (i == gameManager.getActiveTeamIndex()) ? "►" : " ";
            drawString(x, y, indicateur + "  " + teams[i].toString());
            y++;
        }

        //Renvoie la colonne d'affichage pour printGameState()
        return y;
    }

    public void printMovementResult(boolean success, String direction, GameManager gameManager) {
        Alien alien = gameManager.getActiveAlien();
        Position pos = alien.getPosition();

        this.clear();
        printAliensStats(gameManager);

        if (success) {
            drawString(pos.getX(), pos.getY(), "✅ Déplacement à " + direction + " réussi");
        } else {
            drawString(pos.getX(), pos.getY(), "❌ Déplacement impossible (collision ou hors limites)");
        }

        this.refresh();
    }

}
