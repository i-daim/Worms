package upc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upc.model.items.Item;
import upc.model.jeu.Inventory;
import upc.model.items.Item.Type;


import static org.junit.jupiter.api.Assertions.*;
/**
 * Classe de tests unitaires pour la classe {@link Inventory}.
 * Elle vérifie la gestion des munitions, la disponibilité des objets
 * et l'accès aux items par type ou par index.
 */
public class TestInventory {

    private Inventory inventory;
    /**
     * Méthode exécutée avant chaque test.
     * Initialise un inventaire avec les valeurs par défaut.
     * Toutes les munitions sont initialisées à 50.
     */
    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        // PS: Toutes les munitions sont initialisées à 50
    }
    /**
     * Vérifie l'ajout simple de munitions à un type d'objet.
     */
    @Test
    public void testAjouterMunitionSimple() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertEquals(60, inventory.getAmmoStock().get(Type.GRENADE));
    }
    /**
     * Vérifie l'ajout successif de plusieurs quantités de munitions.
     */
    @Test
    public void testAddAmmoPlusieurs() {
        inventory.addAmmo(Type.GRENADE, 10);
        inventory.addAmmo(Type.GRENADE, 5);

        assertEquals(65, inventory.getAmmoStock().get(Type.GRENADE));
    }
    /**
     * Vérifie que l'ajout de munitions négatives
     * n'a aucun effet sur le stock.
     */

    @Test
    public void testAddAmmoNegative() {
        inventory.addAmmo(Type.GRENADE, -5);

        assertEquals(50, inventory.getAmmoStock().get(Type.GRENADE));
    }
    /**
     * Vérifie que l'ajout de zéro munition
     * ne modifie pas le stock.
     */
    @Test
    public void testAddAmmoZero() {
        inventory.addAmmo(Type.GRENADE, 0);

        assertEquals(50, inventory.getAmmoStock().get(Type.GRENADE));
    }
    /**
     * Vérifie qu'un objet est disponible lorsqu'il reste du stock.
     */
    @Test
    public void testIsAvailableAvecStock() {
        inventory.addAmmo(Type.GRENADE, 10);

        assertTrue(inventory.isAvailable(Type.GRENADE));
    }
    /**
     * Vérifie qu'un objet n'est plus disponible
     * lorsque son stock est vide.
     */
    @Test
    public void testIsAvailableSansStock() {
        inventory.useAmmo(Type.GRENADE, 50);

        assertFalse(inventory.isAvailable(Type.GRENADE));
    }
    /**
     * Vérifie qu'un objet avec des munitions illimitées
     * est toujours considéré comme disponible.
     */
    @Test
    public void testIsAvailableIllimite() {
        inventory.getAmmoStock().put(Type.UZI, -1);

        assertTrue(inventory.isAvailable(Type.UZI));
    }
    /**
     * Vérifie que la méthode getAmmoStock retourne une structure valide.
     */
    @Test
    public void testGetAmmoStock() {
        assertNotNull(inventory.getAmmoStock());
        assertFalse(inventory.getAmmoStock().isEmpty());
    }
    /**
     * Vérifie que l'utilisation d'une quantité négative de munitions
     * ne modifie pas le stock.
     */
    @Test
    public void testUseAmmoNegative() {
        int ammoStockUzi = inventory.getAmmoStock().get(Type.UZI);

        inventory.useAmmo(Type.UZI, -10);
        assertEquals(ammoStockUzi, inventory.getAmmoStock().get(Type.UZI));
    }

    @Test
    public void testUseAmmoZero() {
        int ammoStockUzi = inventory.getAmmoStock().get(Type.UZI);

        inventory.useAmmo(Type.UZI, 0);
        assertEquals(ammoStockUzi, inventory.getAmmoStock().get(Type.UZI));
    }

    @Test
    public void testUseAmmoSansStock() {
        inventory.useAmmo(Type.SHOTGUN, 50);
        int ammoStockShotgun = inventory.getAmmoStock().get(Type.SHOTGUN);
        inventory.useAmmo(Type.SHOTGUN, 50);
        assertEquals(ammoStockShotgun, inventory.getAmmoStock().get(Type.SHOTGUN));
    }

    @Test
    public void testGetItemTypeAt(){
        Item item = inventory.getItem(Item.Type.SHOTGUN);
        assertEquals(item, inventory.getItem(inventory.getItemTypeAt(0)));
        assertNull(inventory.getItemTypeAt(-1));
    }

    @Test
    public void testGetItemByIndex(){
        assertNull(inventory.getItemByIndex(-1));
        Item item = inventory.getItem(Item.Type.SHOTGUN);
        assertEquals(item, inventory.getItemByIndex(0));
    }
}
