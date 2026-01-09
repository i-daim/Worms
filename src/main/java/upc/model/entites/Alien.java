package upc.model.entites;

import upc.assets.SoundManager;
import upc.model.entites.projectiles.Projectile;
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
    private int aimAngle;
    private int shootPower;

    private final Inventory inventory;
    private int selectedItemIndex = 0;

    // Champs statiques
    private static final int SPEED = 5;
    private static final int INITIAL_HP = 100;

    // Etat de l'alien (pour la partie graphique)
    public enum State {
        IDLE,
        WALK,
        JUMP_START,
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
        direction = true;
        if (state != State.JUMP && state != State.JUMP_START) {
            setState(State.WALK);
        }
    }

    public void moveLeft() {
        alienPhysics.moveAlien(this, -1);
        direction = false;
        if (state != State.JUMP && state != State.JUMP_START) {
            setState(State.WALK);
        }
    }

    public void jump() {
        alienPhysics.jumpAlien(this);
        setState(State.JUMP_START);
    }

    public void idle() {
        alienPhysics.moveAlien(this, 0);
        if (state != State.JUMP && state != State.JUMP_START) {
            setState(State.IDLE);
        }
    }


    ////    Autres Méthodes    ////
    public Projectile useEquippedItem() {
        Item item = inventory.getItemByIndex(selectedItemIndex);
        Item.Type equippedItem = Inventory.getItemTypeAt(selectedItemIndex);

        if (item == null || !inventory.isAvailable(equippedItem)) {
            return null;
        }
        if (equippedItem == Item.Type.JETPACK && inventory.isAvailable(equippedItem)) {
            alienPhysics.flyAlien(this);
            setState(State.JUMP);
        }
        SoundManager.playEffect(item.getName());
        inventory.useAmmo(equippedItem, 1);
        return item.useItem(world, this);
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

    public void incrementPower() {
        shootPower++;
        if (shootPower > 100) {
            shootPower = 100;
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
        return inventory.getItemByIndex(selectedItemIndex);
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
