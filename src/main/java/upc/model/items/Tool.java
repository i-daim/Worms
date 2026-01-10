package upc.model.items;

public abstract class Tool implements Item {

    /**Attributs représentants le nom d'une arme*/
    private String name;

    /**
     * Constructeur pour {@code Tool}
     * @param name : Nom de l'outil
     */
    public Tool(String name) {
        this.name = name;
    }

    /**
     * Retourne le nom de l'outil
     * @return l'attribut name
     */
    public String getName() {
        return name;
    }

}
