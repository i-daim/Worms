package upc.model.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import upc.model.entites.Alien;
import upc.model.entites.Position;
import upc.model.items.Item;
import upc.model.jeu.GameManager;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Inventory;
import upc.model.jeu.Team;
import upc.model.jeu.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameSave {

    //Attribut minimal pour sauvegarder le jeu
    private final int teamSize;
    private final TeamSave[] teams;
    private final Terrain terrain;

    //Constructeur
    public GameSave(GameManager gameManager) {
        this.teamSize = gameManager.getTeamSize();
        this.teams = new TeamSave[gameManager.getTeams().length];
        for (int i = 0; i < gameManager.getTeams().length; i++) {
            this.teams[i] = new TeamSave(gameManager.getTeams()[i]);
        }
        this.terrain = gameManager.getTerrain();
    }

    /** Méthode permettant de sauvegarder le jeu
     */
    public void save() throws IOException {

        //Crée un répertoire saveData contenant les fichiers de sauvegarde
        File saveDir = new File("saveData");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        //Permet l'implémentation d'une classe de configuration setPrettyPrinting()
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        //Implémente un fichier de sauvegarde saveGame dans le répertoire saveData avec GameSave
        try (FileWriter writer = new FileWriter("saveData/saveGame.json")) {
            gson.toJson(this, writer);
        }
    }

    /** Méthode permettant de charger la sauvegarde du jeu
     * @return L'objet GameSave contenant les données de la sauvegarde
     */
    public static GameSave load() throws IOException {

        //renvoie l'objet gameSave implémenter dans le fichier saveGame
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("saveData/saveGame.json")) {
            return gson.fromJson(reader, GameSave.class);
        }
    }

    /**
     * Méthode permettant de créer un GameManager à partir de la sauvegarde
     * @return Le GameManager restauré
     */
    public GameManager toGameManager() throws IOException {
        //Permet de récupérer l'image de fond de la map
        String cheminBackground = "/maps/" + terrain.getMapName() + "_background.png";
        BufferedImage imageBackground = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminBackground)));
        terrain.setBackground(imageBackground);

        //Permet de récupérer l'image de la map
        String cheminImage = "/maps/" + terrain.getMapName() + ".png";
        BufferedImage image = ImageIO.read(Objects.requireNonNull(
                getClass().getResource(cheminImage)));
        terrain.setImage(image);

        //Pour mettre à jour la map en fonction des collisions de la sauvegarde
        terrain.updateMapImage();

        // Création du GameManager
        GameManager gameManager = new GameManager(terrain, teamSize);

        //Implémente les équipes sauvegardées
        Team[] gameTeams = new Team[teams.length];
        for (int i = 0; i < teams.length; i++) {
            gameTeams[i] = teams[i].toTeam(gameManager.getWorld());
        }

        //Permet de récupérer les équipes sauvegardées
        gameManager.setTeams(gameTeams);

        // Réinitialise la physique avec les nouveaux aliens
        gameManager.getWorld().initializePhysics();

        return gameManager;
    }

    //Classe permettant de générér une équipe avec les informations minimales à sauvegarder
    public static class TeamSave {

        //Attribut minimal pour sauvegarder d'une équipe
        private final String name;
        private final InventorySave inventory;
        private final List<AlienSave> aliens;

        //Constructeur
        public TeamSave(Team team) {
            this.name = team.getName();
            this.inventory = new InventorySave(team.getInventory());
            this.aliens = new ArrayList<>();
            for (Alien alien : team.getAliens()) {
                if (alien != null && alien.isAlive()) {
                    this.aliens.add(new AlienSave(alien));
                }
            }
        }

        /**
         * Méthode permettant de convertir une TeamSave en Team
         * @param world : Le monde de jeu auquel les Aliens appartiennent
         * @return : L'objet Team avec les données sauvegardées
         */
        public Team toTeam(GameWorld world) {
            Team team = new Team(name, aliens.size());
            team.setInventory(inventory.toInventory());
            for (int i = 0; i < aliens.size(); i++) {
                team.getAliens()[i] = aliens.get(i).toAlien(world, team);
            }
            return team;
        }
    }

    //Classe permettant de générér un inventaire avec les informations minimales à sauvegarder
    public static class InventorySave {

        //Attribut minimal pour sauvegarder d'un inventaire
        private final HashMap<Item.Type, Integer> ammoStock;

        //Constructeur
        public InventorySave(Inventory inventory) {
            this.ammoStock = inventory.getAmmoStock();
        }

        /** Méthode permettant de convertir un InventorySave en Inventory
         * @return L'objet Inventory avec les données sauvegardées
         */
        public Inventory toInventory() {
            return new Inventory(this);
        }

        /// Getter ///
        public HashMap<Item.Type, Integer> getAmmoStock() {
            return ammoStock;
        }
    }


    //Classe permettant de générér un alien avec les informations minimales à sauvegarder
    public static class AlienSave {

        //Attributs minimal pour la sauvegarde d'un alien
        private final int healthPoints;
        private final boolean alive;
        private final boolean direction;
        private final int aimAngle;
        private final int shootPower;
        private final Position position;

        //Constructeur
        public AlienSave(Alien alien) {
            this.healthPoints = alien.getHealthPoints();
            this.alive = alien.isAlive();
            this.direction = alien.getDirection();
            this.aimAngle = alien.getAimAngle();
            this.shootPower = alien.getShootPower();
            this.position = alien.getPosition();
        }

        /** Méthode permettant de convertir un AlienSave en Alien
         * @param world : Le monde de jeu auquel l'alien appartient
         * @param team : L'équipe à laquelle l'alien appartient
         * @return : L'objet Alien avec les données sauvegardées
         */
        public Alien toAlien(GameWorld world, Team team) {
            return new Alien(this, world, team);
        }

        /// Getters ///

        public int getHealthPoints() {
            return healthPoints;
        }

        public boolean getAlive() {
            return alive;
        }

        public boolean getDirection() {
            return direction;
        }

        public int getAimAngle() {
            return aimAngle;
        }

        public int getShootPower() {
            return shootPower;
        }

        public Position getPosition() {
            return position;
        }
    }
}
