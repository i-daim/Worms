package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import upc.model.items.Grenade;
import upc.model.items.Inventory;

import static org.junit.jupiter.api.Assertions.*;

public class TestInventory {

    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }

    @Test
    public void testAjouterMunitionSimple() {
        Grenade grenade = new Grenade("Grenade");
        inventory.ajouterMunition(grenade, 10);

        assertEquals(10, inventory.getStockMunitions().get(grenade));
    }

    @Test
    public void testAjouterMunitionPlusieurs() {
        Grenade grenade = new Grenade("Grenade");
        inventory.ajouterMunition(grenade, 10);
        inventory.ajouterMunition(grenade, 5);

        assertEquals(15, inventory.getStockMunitions().get(grenade));
    }

    @Test
    public void testAjouterMunitionNegative() {
        Grenade grenade = new Grenade("Grenade");
        inventory.ajouterMunition(grenade, -5);

        assertNull(inventory.getStockMunitions().get(grenade));
    }

    @Test
    public void testAjouterMunitionZero() {
        Grenade grenade = new Grenade("Grenade");
        inventory.ajouterMunition(grenade, 0);

        assertNull(inventory.getStockMunitions().get(grenade));
    }

}
