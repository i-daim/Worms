package upc;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import upc.model.entites.Alien;
import upc.model.items.Item;
import upc.model.items.Jetpack;
import upc.model.jeu.GameWorld;
import upc.model.jeu.GameManager;
import upc.model.jeu.Inventory;
import upc.model.jeu.Team;
import org.junit.jupiter.api.Test;
/**
 * Ces tests vérifient :
 * - l'état initial d'un alien
 * - la gestion des points de vie et de la mort
 * - les déplacements et états (walk, jump, idle)
 * - la direction et les paramètres de tir
 * - l'utilisation des objets de l'inventaire
 * Les tests utilisent JUnit 5.
 */
public class TestAlien {
    private Alien alien;
    private Team team;
    private GameWorld world;

    @BeforeEach
    /**
     * Initialise l'environnement de test avant chaque test.
     * Crée un GameManager, récupère le monde, l'équipe et un alien.
     */
    void setup() {
        GameManager gameManager = new GameManager();
        world = gameManager.getWorld();
        team = gameManager.getTeam(0);
        alien = team.getAliens()[0];

        assertNotNull(world);
        assertNotNull(team);
        assertNotNull(alien);
    }
    @Test
    /**
     * Vérifie l'état initial de l'alien :
     * - vivant
     * - 100 points de vie
     * - état IDLE
     * - direction vers la droite
     */
    void testEtatInitialAlien() {
        assertTrue(alien.isAlive());
        assertEquals(100, alien.getHealthPoints());
        assertEquals(Alien.State.IDLE, alien.getState());
        assertTrue(alien.getDirection());
    }
    @Test
    /**
     * Vérifie que les dégâts réduisent correctement les points de vie
     * et qu'un alien mort ne peut plus recevoir de dégâts.
     */
    void testReceiveDamage() {
        alien.receiveDmg(25);
        assertEquals(75, alien.getHealthPoints());
        assertTrue(alien.isAlive());

        alien.receiveDmg(75);
        int pointDeVie = alien.getHealthPoints();
        alien.receiveDmg(75);
        assertEquals(pointDeVie, alien.getHealthPoints());
    }
    @Test
    void testMortAlien() {
        alien.receiveDmg(200);
        assertEquals(0, alien.getHealthPoints());
        assertFalse(alien.isAlive());
    }

    @Test
    void testMoveRight(){
        alien.moveRight() ;
        assertEquals(Alien.State.WALK, alien.getState());
        assertTrue(alien.getDirection());
    }

    @Test
    void testMoveLeft() {
        alien.moveLeft();
        assertEquals(Alien.State.WALK, alien.getState());
        assertFalse(alien.getDirection());
    }
    @Test
    void testIdle() {
        alien.idle();
        assertEquals(Alien.State.IDLE, alien.getState());
    }
    @Test
    void testJump() {
        alien.jump();
        assertEquals(Alien.State.JUMP, alien.getState());
    }
    @Test
    void testAimAngle() {
        alien.setAimAngle(30);
        assertEquals(30, alien.getAimAngle());
    }
    @Test
    void testShootPower() {
        alien.setShootPower(90);
        assertEquals(90, alien.getShootPower());
    }
    @Test
    void testSelectedItemIndex() {
        alien.setSelectedItemIndex(0);
        assertEquals(0, alien.getSelectedItemIndex());
    }
    @Test
    void testGetTeam() {
        assertEquals(team, alien.getTeam());
    }

    @Test
    void testUseEquippedItemJetpack() {
        alien.setSelectedItemIndex(5);


        alien.useEquippedItem();

        assertEquals(Alien.State.JUMP, alien.getState());
    }

    @Test
    void testUseEquippedItemWeapon(){

        alien.setSelectedItemIndex(0);
        Item.Type equippedItem = Inventory.ITEMS[alien.getSelectedItemIndex()];
        int ammo = alien.getInventory().getAmmo(equippedItem);
        alien.useEquippedItem();
        assertEquals(alien.getInventory().getAmmo(equippedItem), ammo - 1);

        alien.setSelectedItemIndex(1);
        equippedItem = Inventory.ITEMS[alien.getSelectedItemIndex()];
        alien.getInventory().useAmmo(equippedItem, 500);
        ammo = alien.getInventory().getAmmo(equippedItem);
        alien.useEquippedItem();
        assertEquals(alien.getInventory().getAmmo(equippedItem), ammo);
    }

    @Test
    public void testSetDirection() {
        alien.setDirection(true);
        assertTrue(alien.getDirection());

        alien.setDirection(false);
        assertFalse(alien.getDirection());
    }

    @Test
    public void testGetEquippedItem() {
        assertNotNull(alien.getEquippedItem());
    }






}