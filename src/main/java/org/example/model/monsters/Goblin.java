package org.example.model.monsters;

import org.example.model.Creature;
import org.example.model.Item;
import org.example.model.Monster;
import org.example.model.Potion;

import java.util.ArrayList;
import java.util.List;

public class Goblin extends Monster {
    public Goblin() {
        super("jean-pierre", 30, 5, 1, "Goblin");
    }

    @Override
    public List<Item> getLoot() {
        List<Item> loot = new ArrayList<>();
        loot.add(new Potion("Petite Potion", "Restaure 20 HP", 20));
        return loot;
    }
}
