package org.example.model.items;

import java.util.HashMap;
import java.util.Map;

public class Inventaire {
    private final Map<Utilisable, Integer> stockMunitions;

    public Inventaire() {
        this.stockMunitions = new HashMap<>();
        // à completer : stockMunitions.put(new Bazooka(), 50);
        // ...
    }


}
