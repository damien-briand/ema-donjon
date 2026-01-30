package org.example.model.monsters;

import org.example.model.Creature;
import org.example.model.Item;
import org.example.model.Monster;
import org.example.model.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Skeleton extends Monster {
    public Skeleton() {
        super("jean-calcium", 30, 5, 1, "Skeleton");
    }

    @Override
    public List<Item> getLoot() {
        List<Item> loot = new ArrayList<>();
        loot.add(new Weapon("Épée Rouillée", "Une vieille épée", 12, "Épée"));
        return loot;
    }
}
