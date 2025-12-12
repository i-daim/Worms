package upc.model.items;

public abstract class Tool implements Item {
    private String name;

    public Tool(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
