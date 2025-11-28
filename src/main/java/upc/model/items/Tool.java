package upc.model.items;

public abstract class Tool implements Usable {
    private String nom;

    public Tool(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
