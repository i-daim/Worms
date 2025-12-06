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
    public void testAddAmmoSimple() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertEquals(10, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoPlusieurs() {
        inventory.addAmmo(Type.GRENADE, 10);
        inventory.addAmmo(Type.GRENADE, 5);

        assertEquals(15, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoNegative() {
        inventory.addAmmo(Type.GRENADE, -5);

        assertNull(inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoZero() {
        inventory.addAmmo(Type.GRENADE, 0);

        assertNull(inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testIsAvailableAvecStock() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertTrue(inventory.isAvailable(Type.GRENADE));
    }

    @Test
    public void testIsAvailableSansStock() {
        assertFalse(inventory.isAvailable(Type.GRENADE));
    }

    @Test
    public void testIsAvailableIllimite() {
        inventory.getAmmoStock().put(Type.UZI, -1);

        assertTrue(inventory.isAvailable(Type.UZI));
    }

    @Test
    public void testGetStockMunitionsVide() {
        assertNotNull(inventory.getAmmoStock());
        assertTrue(inventory.getAmmoStock().isEmpty());
    }



}
