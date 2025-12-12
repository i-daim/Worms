package upc.view.console;

import com.googlecode.lanterna.TextColor;
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

import java.io.IOException;

public class LanternaView {
    //attribut permettant la réprésentation du terminal et de le styliser
    private Screen screen;

    //attribut permettant l'écriture du texte, des caractères avec coordonnées et couleur
    private TextGraphics graphics;

    /*Constructeur*/
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
    /*Méthode permettant de vider l'écran (avant de redessiner)*/
    public void clear() {

        this.screen.clear();

    }

    /*Méthode permettant d'afficher tout ce qui a été dessiné*/
    public void refresh() {

        //Gestion des exceptions
        // car Java oblige à gérer toutes les exceptions checked en lien avec refresh()
        try {

            this.screen.refresh();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    /*Méthode permettant d'écrire un texte à une position donnée*/
    public void drawString(int x, int y, String string) {

        //Gestion des exceptions
        try {
            //Permet de dessiner à une position donnée
            graphics.putString(x, y, string);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    /*Méthode permettant d'écrire un texte à une position donnée dans une couleur prédéfini*/
    public void drawColored(int x, int y, String string, TextColor color) {

        //Gestion des exceptions
        try {
            //Permet de modifier la couleur de l'affichage
            graphics.setForegroundColor(color);

            //Permet de dessiner à une position donnée
            graphics.putString(x, y, string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printWelcome() {
        this.clear();
        int x = 0;
        int y = 0;
        drawString(x, y, "================================="); y++;
        drawString(x, y, "  Bienvenue dans Worms Game!    "); y++;
        drawString(x, y, "================================="); y++;
        drawString(x, y, "Commandes disponibles:"); y++;
        drawString(x, y, "Avec flèches directionnelles :"); y++;
        drawString(x, y, "  gauche  - Déplacer l'alien à gauche avec"); y++;
        drawString(x, y, "  droite  - Déplacer l'alien à droite"); y++;
        drawString(x, y, "  sauter  - Faire sauter l'alien"); y++;
        drawString(x, y, "  dégâts  - Infliger des dégâts à l'équipe ennemie avec "
                + "la touche d"); y++;
        drawString(x, y, "  passer  - Passer son tour avec la touche espace"); y++;
        drawString(x, y, "  aliens  - Afficher tous les aliens vivants avec la touche a"); y++;
        drawString(x, y, "  map     - Afficher la carte du terrain avec la touche m"); y++;
        drawString(x, y, "  exit    - Quitter la partie avec la touche echap"); y++;
        drawString(x, y, "=================================");
        this.refresh();
    }

    /*Méthode permettant de lire la touche pressée par l'utilisateur
     Et renvoie le string correspondant à la touche*/
    public String readKey() {

        //Gestion des exceptions
        try {

            //Permet de récupérer la touche pressée
            KeyStroke key = screen.readInput();

            //Conditions qui renvoient le type de touche pressé
            if (key.getKeyType() == KeyType.ArrowLeft) {

                //Si la touche pressée est <- alors renvoie gauche
                return "gauche";

            } else if (key.getKeyType() == KeyType.ArrowRight) {

                //Si la touche pressée est -> alors renvoie droite
                return "droite";

            } else if (key.getKeyType() == KeyType.ArrowUp) {

                //Si la touche pressée est flèche du haut alors renvoie sauter
                return "sauter";

            } else if (key.getKeyType() == KeyType.Escape) {

                //Si la touche pressée est echap alors renvoie exit
                return "exit";

            } else if (key.getKeyType() == KeyType.Character) {
                char c = key.getCharacter();

                //Si la touche pressée est m alors renvoie map
                if (c == 'm') {
                    return "map";

                } else if (c == 'a') {

                    //Si la touche pressée est c du haut alors renvoie aliens
                    return "aliens";

                } else if (c == 'd') {

                    //Si la touche pressée est d du haut alors renvoie attaquer
                    return "attaquer";

                } else if (c == ' ') {

                    //Si la touche pressée est espace du haut alors renvoie passer
                    return "passer";

                } else if (c == 'i') {

                    //Si la touche pressée est i du haut alors renvoie passer
                    return "info";

                } else {
                    //Sinon renvoie rien
                    return null;
                }

            } else {

                //Sinon renvoie rien
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*Méthode permettant l'affichage du terrain de dans le terminal*/
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

                //Permet d'écrire seulement les aliens W d'une certaine couleur
                if (symbolForTile(x, y, gameManager).equals("W")) {
                    drawColored(x, y + 5, symbolForTile(x, y, gameManager), TextColor.ANSI.RED);
                } else {

                    //Permet de remettre à la couleur d'origine de l'affichage
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                    //Dessine chaque case avec le symbole correspondant
                    drawString(x, y + 5, symbolForTile(x, y, gameManager));
                }
            }
        }

        drawString(0, height + 6, "─".repeat(60));
        drawString(0, height + 7, "Vous avez sous vos yeuxx le terrain du jeu");
        drawString(0, height + 8, "W représente les aliens");
        drawString(0, height + 9, "X représente le terrain en lui-même");
        drawString(0, height + 10, "Pour plus d'info appuie sur la touche i");
        drawString(0, height + 11, "─".repeat(60));

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permettant d'envoyer le bon caractère à la position donnée */
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

    /*Methode permettant d'afficher les stats de l'alien active*/
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

    /*Méthode permettant la représentation de la barre de vie d'un alien*/
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

    /*Méthode permettant d'afficher les stats des équipes*/
    public void printAliensStats(GameManager gameManager) {

        //Coordonnées d'affichage par défaut à modifier selon où on veut placer l'affichage
        int x = 1;
        int y = 1;

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

    /*Méthode permettant d'afficher les stats de chaque alien d'une équipe*/
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

    /*Méthode permettant de les stats d'une équipe + stats de l'alien actif*/
    public void printGameState(GameManager gameManager) {

        //Coordonnées d'affichage par défaut à modifier selon où on veut placer l'affichage
        int x = 5;
        int y = 4;

        //drawString(x, y + 1, "─".repeat(60));
        //y++;

        //drawString(x, y + 1, "📊 " + gameManager.toString());
        //y++;

        //drawString(x, y + 1, "─".repeat(60));
        //y++;

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats de l'équipe
        y = printTeamStats(gameManager, x, 26);

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats de l'alien aciif
        y = printActiveAlienStats(gameManager, x, 26);

        drawString(x, y, "─".repeat(60));

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permettant les stats d'une équipe*/
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

    /*Méthode permmettant d'afficher le message de déplacement d'un alien*/
    public void printMovementResult(boolean success, String direction, GameManager gameManager) {
        Alien alien = gameManager.getActiveAlien();

        //Vide puis redessine le terraine et les aliens
        //Pour prendre en compte leur déplacement
        this.clear();
        printAliensStats(gameManager);

        //Envoie le message de déplacement à la nouvelle position de l'alien
        // si non colision/hors limites
        if (success) {
            drawString(0, 0, "✅ Déplacement à " + direction + " réussi");

        } else {
            drawString(0, 0, "❌ Déplacement impossible "
                    + "(collision ou hors limites)");
        }

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permmettant d'afficher le message de déplacement d'un alien(saut)*/
    public void printJumpResult(boolean success, GameManager gameManager) {
        Alien alien = gameManager.getActiveAlien();
        Position pos = alien.getPosition();

        //Vide puis redessine le terraine et les aliens
        //Pour prendre en compte leur déplacement
        this.clear();
        printAliensStats(gameManager);

        //Envoie le message de déplacement(saut) à la nouvelle position de l'alien si possible
        if (success) {
            drawString(pos.getX(), pos.getY(), "✅ L'alien a sauté !");

        } else {
            drawString(pos.getX(), pos.getY(), "❌ Saut impossible");
        }

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permettant d'afficher le message après une attaque*/
    public void printAttackResult(Alien target, int x, int y, Team activeTeam) {

        //Vérifie si la cible n'existe pas alors :
        if (target == null) {
            drawString(x, y, "❌ Aucun alien à la position (" + x + ", " + y + ")");

            //affiche le rendu
            this.refresh();

            //Met fin à la méthode
            return;
        }

        //Boolean si oui ou non la cible appartient à l'équipe active
        boolean isAlly = (target.getTeam() == activeTeam);

        //Boolean si oui ou non la cible est vivante
        boolean alienDead = !target.isAlive();

        //Vérifie si la cible appartient à l'équique active
        if (isAlly) {

            //Si la cible appartient à l'équipe active et est morte alors :
            if (alienDead) {
                drawString(x, y, "💀 TIR AMI FATAL ! Vous avez tué votre propre alien !");

            } else {

                //Si la cible survit alors :
                drawString(x, y, "⚠️  TIR AMI ! Votre alien a été touché (PV restants: "
                        + target.getHealthPoints() + ")");

            }

        //Si la cible n'appartient pas à l'équique active
        } else {

            //Si la cible n'appartient pas à l'équipe active et est morte alors :
            if (alienDead) {
                drawString(x, y, "💀 Alien ennemi éliminé !");

            } else {

                //Si la cible survit alors :
                drawString(x, y, "💥 Alien ennemi touché ! Il lui reste "
                        + target.getHealthPoints() + " PV");

            }
        }

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permettant d'afficher le message de fin de partie et l'équipe gagnante*/
    public void printGameOver(GameManager gameManager) {

        //Vide l'écran car on affiche seulement le message
        this.clear();

        //Coordonnée d'affichage par défaut
        int x = 0;
        int y = 0;

        //Affiche le message de fin de partie
        drawString(x, y, "=".repeat(60));
        y++;
        drawString(x, y, "🎉 PARTIE TERMINÉE ! 🎉");
        y++;
        drawString(x, y, "=".repeat(60));
        y++;

        Team gagnante = gameManager.getWinningTeam();

        //Vérifie qu'il y est une équipe gagnante
        if (gagnante != null) {

            //Affiche l'équipe gagnante
            drawString(x, y, "🏆 " + gagnante.getName() + " a gagné ! 🏆");
            y++;
        }

        //Renvoie la coordonnée de la colonne d'affichage + affiche les stats de l'équipe
        y = printTeamStats(gameManager, x, y);
        drawString(x, y, "=".repeat(60));

        //Affiche le rendu
        this.refresh();
    }

    /*Méthode permettant d'afficher le message des aliens après la chute (avec leurs stats) */
    public void printStateAfterFall(GameManager gameManager) {

        //Coordonnées par défaut d'affichage
        int x = 0;
        int y = 0;

        drawString(x, y, "💥 ÉTAT DES ALIENS APRÈS LA CHUTE :");

        for (int i = 0; i < 2; i++) {

            //Affiche le noms des équipes
            drawString(x, y, "🔹 " + gameManager.getTeam(i).getName() + " :");

            Alien[] aliens = gameManager.getTeam(i).getAliens();
            for (int j = 0; j < aliens.length; j++) {
                Alien a = aliens[j];

                //Affiche les aliens vivants + leurs stats
                if (a != null && a.isAlive()) {
                    drawString(x, y, "   → Alien #" + (j + 1) + " : PV = "
                            + a.getHealthPoints() + "/100");
                }
                y++;
            }
        }

        //Affiche leur rendu
        this.refresh();
    }

    public void printTurnSkipped() {
        drawString(0, 0, "⏭️  Tour passé");
        refresh();
    }

    public void printGameAbandoned() {
        this.clear();
        drawString(2, 2, "❌ Partie abandonnée.");
        refresh();
    }

    public void printUnknownCommand() {
        this.clear();
        drawString(2, 2, "❌ Commande inconnue ! Appuyer sur i pour l'aide.");
        refresh();
    }

    /*Méthode permettant de d'afficher le message de fin de partie + fermeture du terminal*/
    public void close() {
        //Gestion des exceptions
        try {
            //vide le terminal
            this.clear();

            //affiche le message de fin
            drawString(0, 0, "👋 Merci d'avoir joué !");

            //affiche le rendu
            this.refresh();

            Thread.sleep(2000);
            screen.stopScreen();

            //Permet la fermeture du terminal
            //screen.close();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*Méthode permmettant à l'utilisateur de choisir les dégats provoqués*/
    public int chooseDamage() {
        //Dégat par défaut
        int degat = 20;


        while (true) {

            //Affiche dans un terminal vide
            clear();
            drawString(2, 2, "💥 Combien de dégâts ? : " + degat);
            drawString(2, 4, "<- -> pour changer, Espace pour valider");
            refresh();

            KeyStroke key = null;
            try {
                key = screen.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //<- baisse les dégats de -5
            if (key.getKeyType() == KeyType.ArrowLeft) {
                degat = degat - 5;

            } else if (key.getKeyType() == KeyType.ArrowRight) {

                //-> augmente les dégats de +5
                degat = degat + 5;

            } else if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' ') {
                //Espace permet de sélectionner les dégats
                return degat;
            }

        }
    }

    /*Méthode permmettant à l'utilisateur de choisir la coordonnée en x*/
    public int chooseX() {
        //Coordonnée x par défaut
        int x = 1;

        while (true) {

            //Affiche dans un terminal vide
            clear();
            drawString(2, 2, "🎯 Coordonnée X de la cible : " + x);
            drawString(2, 4, "<- -> pour changer, Espace pour valider");
            refresh();

            KeyStroke key = null;
            try {
                key = screen.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //<- baisse la coordonnée de -1
            if (key.getKeyType() == KeyType.ArrowLeft) {
                x = x - 1;

            } else if (key.getKeyType() == KeyType.ArrowRight) {

                //-> augmente la coordonnée de +1
                x = x + 1;

            } else if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' ') {
                //Espace permet de sélectionner la coordonnée x
                return x;
            }

        }
    }

    /*Méthode permmettant à l'utilisateur de choisir la coordonnée en y*/
    public int chooseY() {

        //Coordonnée y par défaut
        int y = 1;

        while (true) {

            //Affiche dans un terminal vide
            clear();
            drawString(2, 2, "🎯 Coordonnée Y de la cible : " + y);
            drawString(2, 4, "<- -> pour changer, Espace pour valider");
            refresh();

            KeyStroke key = null;
            try {
                key = screen.readInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //<- baisse la coordonnée de -1
            if (key.getKeyType() == KeyType.ArrowLeft) {
                y = y - 1;

            } else if (key.getKeyType() == KeyType.ArrowRight) {
                //-> augmente la coordonnée de +1
                y = y + 1;

            } else if (key.getKeyType() == KeyType.Character && key.getCharacter() == ' ') {
                //Espace permet de sélectionner la coordonnée y
                return y;
            }

        }
    }
}