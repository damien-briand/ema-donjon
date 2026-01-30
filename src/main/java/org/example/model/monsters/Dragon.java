package org.example.model.monsters;

import org.example.model.Armor;
import org.example.model.Item;
import org.example.model.Monster;
import org.example.model.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Dragon extends Monster {
    public Dragon() {
        super("jean-cule", 120, 8, 3, "Dragon");
    }

    @Override
    public List<Item> getLoot() {
        List<Item> loot = new ArrayList<>();
        loot.add(new Weapon("Croc de Dragon", "Une arme légendaire", 35, "Dague"));
        loot.add(new Armor("Écailles de Dragon", "Armure légendaire", 40, "Lourde"));
        return loot;
    }
}
