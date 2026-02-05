package org.example.model.monsters;

import org.example.model.*;
import org.example.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Goblin extends Monster {
    private Map<Item, Double> possibleLoot = new HashMap<>();

    public Goblin() {
        super("Gobelin", 75, 12, 2, "Goblin");
        // Items intermédiaires/rares avec taux de drop de base
        possibleLoot.put(new Weapon("Dague Empoisonnée", "Une dague couverte de poison", 15, "Dague"), 0.50);
        possibleLoot.put(new Weapon("Massue Cloutée", "Une massue artisanale dangereuse", 12, "Massue"), 0.45);
        possibleLoot.put(new Armor("Veste en Cuir Renforcé", "Armure légère mais résistante", 12, "Légère"), 0.40);
        possibleLoot.put(new Armor("Gants Volés", "Des gants de qualité volés", 8, "Mains"), 0.35);
        possibleLoot.put(new Weapon("Hache de Guerre", "Une hache gobeline bien affûtée", 18, "Hache"), 0.25);

        // Extensions: Potions et Matériaux
        possibleLoot.put(new Potion("Potion de Vie", "Restaure 50 PV", 50), 0.45);
        possibleLoot.put(new Potion("Potion de Vie Moyenne", "Restaure 35 PV", 35), 0.40);
        possibleLoot.put(new Material("Cuir de Gobelin", "Peau résistante", 1), 0.50);
        possibleLoot.put(new Material("Dent de Gobelin", "Matériau rare", 1), 0.30);
    }

    @Override
    public void generateRandomLoot() {
        // Goblin = monstre intermédiaire, 60% de chance global de drop
        setExperienceReward(25*level);
        double globalDropChance = 0.60;

        if (Math.random() < globalDropChance) {
            Logger.logInfo("💰 " + name + " laisse tomber du butin !");

            int itemsDropped = 0;
            double dropPenalty = 0.0; // Pénalité cumulative

            // Génère le loot avec système de pénalité progressive
            for (Map.Entry<Item, Double> entry : possibleLoot.entrySet()) {
                Item item = entry.getKey();
                Double baseDropChance = entry.getValue();

                // Applique la pénalité : chaque item droppé réduit les chances des suivants
                double adjustedChance = baseDropChance * (1.0 - dropPenalty);

                if (Math.random() < adjustedChance) {
                    addItemToInventory(item);
                    itemsDropped++;
                    Logger.logInfo("✨ " + item.getName() + " ajouté (chance: " + String.format("%.1f", adjustedChance * 100) + "%)");

                    // Augmente la pénalité : -20% par item droppé pour les monstres intermédiaires
                    dropPenalty += 0.20;

                    // Maximum 4 items pour un Goblin
                    if (itemsDropped >= 4) {
                        Logger.logInfo("📦 Limite d'items atteinte pour " + name);
                        break;
                    }
                }
            }

            // Garantir au moins 1 item pour les Goblins
            if (getInventory().items().isEmpty() && !possibleLoot.isEmpty()) {
                List<Item> itemList = new ArrayList<>(possibleLoot.keySet());
                Item guaranteedItem = itemList.get((int) (Math.random() * itemList.size()));
                addItemToInventory(guaranteedItem);
                Logger.logInfo("✨ " + guaranteedItem.getName() + " (garanti) ajouté au butin");
            }
        } else {
            Logger.logInfo("😢 " + name + " ne laisse aucun butin cette fois...");
        }
    }

    @Override
    public List<Item> getLoot() {
        List<Item> droppedLoot = new ArrayList<>(getInventory().items());

        if (!droppedLoot.isEmpty()) {
            Logger.logInfo("🎁 Butin récupéré de " + name + ": " + droppedLoot.size() + " item(s)");
            for (Item item : droppedLoot) {
                Logger.logInfo("  - " + item.getName());
            }
        }

        return droppedLoot;
    }
}