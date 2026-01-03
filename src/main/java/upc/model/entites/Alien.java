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
        //Immobile
        IDLE,

        //En marche
        WALK,

        //En saut
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

    /** Méthode permettant de déplacer un alien vers la droite
     */
    public void moveRight() {
        alienPhysics.moveAlien(this, 1);
        setState(State.WALK);
        direction = true;
    }

    /** Méthode permettant de déplacer un alien vers la gauche
     */
    public void moveLeft() {
        alienPhysics.moveAlien(this, -1);
        setState(State.WALK);
        direction = false;
    }

    /** Méthode permettant à un alien de sauter
     */
    public void jump() {
        alienPhysics.jumpAlien(this);
        setState(State.JUMP);
    }

    /** Méthode permettant de rendre alien immobile
     */
    public void idle() {
        alienPhysics.moveAlien(this, 0);
        setState(State.IDLE);
    }


////    Autres Méthodes    ////

    /**Méthode en lien avec l'utilisation d'item
     */
    public void useEquippedItem() {
        
        Item.Type equippedItem = Inventory.ITEMS[selectedItemIndex];

        if (!inventory.isAvailable(equippedItem)) {
            return;
        }

        Item item = inventory.getItem(equippedItem);
        item.useItem(world, this);

        //Décremente de un le nombre de munitions présent dans l'arme
        inventory.useAmmo(equippedItem, 1);

        //Si l'item équipé est le jetpack et le nombre de munition suffisant
        if (equippedItem == Item.Type.JETPACK && inventory.isAvailable(equippedItem)) {
            alienPhysics.flyAlien(this);

            //Met l'alien dans l'état saut
            setState(State.JUMP);
        }
    }

    /**Méthode permettant d'infliger des dégats à un alien
     * @param damage : Dégats à infliger à l'alien
     */
    public void receiveDmg(int damage) {
        if (!alive) {
            return;
        }

        //Décremente les points de vie de l'alien selon les dégats
        healthPoints -= Math.max(0, damage);

        //Lance le son lié quand un alien subit un dégat
        SoundManager.playEffect("Damage");

        //Change l'attribut alive de l'alien si les points de vie de l'alien sont inférieurs à 0
        if (healthPoints <= 0) {
            healthPoints = 0;
            alive = false;
        }
    }

////    GETTERS & SETTERS    ////

    /**
     * Retourne les points de vie
     * @return L'attribut heathPoints
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Retourne un boolean si un alien est vivant
     * @return L'attribut alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Retourne l'équipe de l'alien
     * @return L'attribut team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Retourne l'angle de visée de l'alien
     * @return L'attribut aimAngle
     */
    public int getAimAngle() {
        return aimAngle;
    }

    /**
     * Retourne l'item équipé de l'alien
     * @return l'item depuis l'inventaire
     */
    public Item getEquippedItem() {
        return inventory.getItem(Inventory.ITEMS[selectedItemIndex]);
    }

    /**
     * Retourne l'indice de l'item selectionné
     * @return L'attribut selectedItemIndex
     */
    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    /**
     * Modifie l'indice de l'item selectionné
     * @param index : Pour sélectionner un autre item
     */
    public void setSelectedItemIndex(int index) {
        this.selectedItemIndex = index;
    }

    /**
     * Retourne l'état de déplacement de l'alien
     * @return L'attribut state
     */
    public State getState() {
        return state;
    }

    /**
     * Modifie l'état de déplacement de l'alien
     * @param state : Pour sélectionner un autre état de déplacement
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Retourne le sens de déplacement de l'alien
     * @return L'attribut direction
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * Modifie le sens de déplacement de l'alien
     * @param direction : Pour sélectionner un autre sens de déplacement
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * Retourne la puissance de tir de l'alien
     * @return L'attribut shootPower
     */
    public int getShootPower() {
        return shootPower;
    }

    /**
     * Modifie la puissance de tir de l'alien
     * @param power : Pour sélectionner la puissance de tir
     */
    public void setShootPower(int power) {
        this.shootPower = power;
    }

    /**
     * Modifie l'angle de tir de l'alien
     * @param angle : Pour sélectionner l'angle de tir
     */
    public void setAimAngle(int angle) {
        this.aimAngle = angle;
    }

    /**
     * Retourne l'inventaire de l'alien
     * @return L'attribut inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
