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

    private final TeamSave[] teams;
    private final Terrain terrain;

    public GameSave(GameManager gameManager) {
        this.teams = new TeamSave[gameManager.getTeams().length];
        for (int i = 0; i < gameManager.getTeams().length; i++) {
            this.teams[i] = new TeamSave(gameManager.getTeams()[i]);
        }
        this.terrain = gameManager.getTerrain();
    }

    public void save() throws IOException {
        File saveDir = new File("saveData");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("saveData/saveGame.json")) {
            gson.toJson(this, writer);
        }
    }

    public static GameSave load() throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("saveData/saveGame.json")) {
            return gson.fromJson(reader, GameSave.class);
        }
    }

    /**
     * Méthode permettant d'appliquer les données enregistrées à un jeu
     * @param gameManager La partie de jeu sur laquelle appliquer le save
     */
    public void applyTo(GameManager gameManager) throws IOException {
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

        gameManager.setMap(terrain);

        Team[] gameTeams = new Team[teams.length];
        for (int i = 0; i < teams.length; i++) {
            gameTeams[i] = teams[i].toTeam(gameManager.getWorld());
        }
        gameManager.setTeams(gameTeams);
    }

    public static class TeamSave {
        private final String name;
        private final InventorySave inventory;
        private final List<AlienSave> aliens;

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

        public Team toTeam(GameWorld world) {
            Team team = new Team(name, aliens.size());
            team.setInventory(inventory.toInventory());
            for (int i = 0; i < aliens.size(); i++) {
                team.getAliens()[i] = aliens.get(i).toAlien(world, team);
            }
            return team;
        }
    }

    public static class InventorySave {
        private final HashMap<Item.Type, Integer> ammoStock;

        public InventorySave(Inventory inventory) {
            this.ammoStock = inventory.getAmmoStock();
        }

        public Inventory toInventory() {
            return new Inventory(this);
        }

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

        /* Méthode permettant de convertier un alienSave en alien */
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
