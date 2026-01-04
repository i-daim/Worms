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
        // PS: Toutes les munitions sont initialisées à 50
    }

    @Test
    public void testAjouterMunitionSimple() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertEquals(60, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoPlusieurs() {
        inventory.addAmmo(Type.GRENADE, 10);
        inventory.addAmmo(Type.GRENADE, 5);

        assertEquals(65, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoNegative() {
        inventory.addAmmo(Type.GRENADE, -5);

        assertEquals(50, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testAddAmmoZero() {
        inventory.addAmmo(Type.GRENADE, 0);

        assertEquals(50, inventory.getAmmoStock().get(Type.GRENADE));
    }

    @Test
    public void testIsAvailableAvecStock() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertTrue(inventory.isAvailable(Type.GRENADE));
    }

    @Test
    public void testIsAvailableSansStock() {
        inventory.useAmmo(Type.GRENADE, 50);

        assertFalse(inventory.isAvailable(Type.GRENADE));
    }

    @Test
    public void testIsAvailableIllimite() {
        inventory.getAmmoStock().put(Type.UZI, -1);

        assertTrue(inventory.isAvailable(Type.UZI));
    }

    @Test
    public void testGetAmmoStock() {
        assertNotNull(inventory.getAmmoStock());
        assertFalse(inventory.getAmmoStock().isEmpty());
    }

    @Test
    public void testUseAmmoNegative() {
        int ammoStockUzi = inventory.getAmmoStock().get(Type.UZI);

        inventory.useAmmo(Type.UZI, -10);
        assertEquals(ammoStockUzi, inventory.getAmmoStock().get(Type.UZI));
    }


}
