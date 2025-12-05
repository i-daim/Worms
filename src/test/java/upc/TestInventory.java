package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upc.model.jeu.Inventory;
import upc.model.items.Item.Type;


import static org.junit.jupiter.api.Assertions.*;

public class TestInventory {

    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }

    @Test
    public void testAjouterMunitionSimple() {
        inventory.ajouterMunition(Type.GRENADE, 10);

        assertEquals(10, inventory.getStockMunitions().get(Type.GRENADE));
    }

    @Test
    public void testAjouterMunitionPlusieurs() {
        inventory.ajouterMunition(Type.GRENADE, 10);
        inventory.ajouterMunition(Type.GRENADE, 5);

        assertEquals(15, inventory.getStockMunitions().get(Type.GRENADE));
    }

    @Test
    public void testAjouterMunitionNegative() {
        inventory.ajouterMunition(Type.GRENADE, -5);

        assertNull(inventory.getStockMunitions().get(Type.GRENADE));
    }

    @Test
    public void testAjouterMunitionZero() {
        inventory.ajouterMunition(Type.GRENADE, 0);

        assertNull(inventory.getStockMunitions().get(Type.GRENADE));
    }

    @Test
    public void testEstDisponibleAvecStock() {
        inventory.ajouterMunition(Type.GRENADE, 10);

        assertTrue(inventory.estDisponible(Type.GRENADE));
    }

    @Test
    public void testEstDisponibleSansStock() {
        assertFalse(inventory.estDisponible(Type.GRENADE));
    }

    @Test
    public void testEstDisponibleIllimite() {
        inventory.getStockMunitions().put(Type.UZI, -1);

        assertTrue(inventory.estDisponible(Type.UZI));
    }

    @Test
    public void testGetStockMunitionsVide() {
        assertNotNull(inventory.getStockMunitions());
        assertTrue(inventory.getStockMunitions().isEmpty());
    }



}
