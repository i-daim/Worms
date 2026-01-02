package upc.model.entites;

import upc.assets.SoundManager;
import upc.model.items.Item;
import upc.model.save.GameSave;
import upc.model.jeu.GameWorld;
import upc.model.jeu.Inventory;
import upc.model.jeu.Team;
import upc.model.physics.AlienPhysics;

public class Alien extends Entity {
    // Attributs
    private final GameWorld world;
    private final AlienPhysics alienPhysics;
    private final Team team;

    private int healthPoints;
    private boolean alive;
    private boolean direction;
    private int aimAngle = 0;
    private int shootPower = 50;

    private final Inventory inventory;
    private int selectedItemIndex = 0;

    // Champs statiques
    private static final int SPEED = 5;
    private static final int INITIAL_HP = 100;

    // Etat de l'alien (pour la partie graphique)
    public enum State {
        IDLE,
        WALK,
        JUMP
    }
    private State state;

    // Constructeur
    public Alien(GameWorld gameWorld, Team team, Position position) {
        super(position, SPEED);
        this.healthPoints = INITIAL_HP;
        this.world = gameWorld;
        this.alienPhysics = world.getPhysicsEngine().getAlienPhysics();

        this.team = team;
        this.inventory = team.getInventory();

        this.alive = true;
        this.state = State.IDLE;
        this.direction = true;
        this.aimAngle = 0;
    }

    //Constructeur pour charger un alien
    public Alien(GameSave.AlienSave alienSave, GameWorld gameWorld, Team team) {
        super(alienSave.getPosition(), SPEED);
        this.world = gameWorld;
        this.alienPhysics = world.getPhysicsEngine().getAlienPhysics();

        this.team = team;
        this.inventory = team.getInventory();

        this.healthPoints = alienSave.getHealthPoints();
        this.alive = alienSave.getAlive();
        this.direction = alienSave.getDirection();
        this.aimAngle = alienSave.getAimAngle();
        this.shootPower = alienSave.getShootPower();

        this.state = State.IDLE;
    }

////    Mouvements de l'alien    ////
    public void moveRight() {
        alienPhysics.moveAlien(this, 1);
        setState(State.WALK);
        direction = true;
    }

    public void moveLeft() {
        alienPhysics.moveAlien(this, -1);
        setState(State.WALK);
        direction = false;
    }

    public void jump() {
        alienPhysics.jumpAlien(this);
        setState(State.JUMP);
    }

    public void idle() {
        alienPhysics.moveAlien(this, 0);
        setState(State.IDLE);
    }


////    Autres Méthodes    ////
    public void useEquippedItem() {
        Item.Type equippedItem = Inventory.ITEMS[selectedItemIndex];

        if (!inventory.isAvailable(equippedItem)) {
            return;
        }
        Item item = inventory.getItem(equippedItem);
        item.useItem(world, this);
        inventory.useAmmo(equippedItem, 1);
        if (equippedItem == Item.Type.JETPACK && inventory.isAvailable(equippedItem)) {
            alienPhysics.flyAlien(this);
            setState(State.JUMP);
        }
    }

    public void receiveDmg(int damage) {
        if (!alive) {
            return;
        }
        healthPoints -= Math.max(0, damage);
        SoundManager.playEffect("Damage");
        if (healthPoints <= 0) {
            healthPoints = 0;
            alive = false;
        }
    }

////    GETTERS & SETTERS    ////
    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean isAlive() {
        return alive;
    }

    public Team getTeam() {
        return team;
    }

    public int getAimAngle() {
        return aimAngle;
    }

    public Item getEquippedItem() {
        return inventory.getItem(Inventory.ITEMS[selectedItemIndex]);
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int index) {
        this.selectedItemIndex = index;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getShootPower() {
        return shootPower;
    }

    public void setShootPower(int power) {
        this.shootPower = power;
    }

    public void setAimAngle(int angle) {
        this.aimAngle = angle;
    }
}
