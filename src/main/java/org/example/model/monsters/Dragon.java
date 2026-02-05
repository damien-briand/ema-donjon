package org.example.model.monsters;

import org.example.model.*;
import org.example.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dragon extends Monster {
    private Map<Item, Double> possibleLoot = new HashMap<>();

    public Dragon() {
        super("Dragon Ancien", 200, 25, 3, "Dragon");
        // Items légendaires avec leur taux de drop de base
        possibleLoot.put(new Weapon("Croc de Dragon", "Une dague forgée dans une dent de dragon", 35, "Dague"), 0.60);
        possibleLoot.put(new Weapon("Griffe Draconique", "Griffes acérées arrachées au dragon", 32, "Griffes"), 0.45);
        possibleLoot.put(new Armor("Écailles de Dragon", "Armure légendaire impénétrable", 40, "Lourde"), 0.50);
        possibleLoot.put(new Armor("Heaume du Dragon", "Casque forgé dans les flammes", 28, "Tête"), 0.35);
        possibleLoot.put(new Weapon("Souffle Éternel", "Bâton magique imprégné de l'essence du dragon", 40, "Bâton"), 0.20);

        // Extensions: Potions et Matériaux légendaires
        possibleLoot.put(new Potion("Élixir du Dragon", "Potion légendaire qui restaure complètement la vie", 999999999), 0.40);
        possibleLoot.put(new Potion("Sang de Dragon", "Essence magique pure", 75), 0.30);
        possibleLoot.put(new Material("Cœur de Dragon", "Matériau légendaire extrêmement rare", 1), 0.15);
        possibleLoot.put(new Material("Cristal de Flamme", "Gemme imprégnée de feu draconique", 1), 0.25);
    }

    @Override
    public void generateRandomLoot() {
        // Dragon = boss légendaire, donc 80% de chance global de drop
        setExperienceReward(50 * level);
        double globalDropChance = 0.80;

        if (Math.random() < globalDropChance) {
            Logger.logInfo("💰 " + name + " laisse tomber du butin légendaire !");

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

                    // Augmente la pénalité : -15% par item droppé pour les boss (moins punitif)
                    dropPenalty += 0.15;

                    // Maximum 5 items pour un Dragon (boss généreux)
                    if (itemsDropped >= 5) {
                        Logger.logInfo("📦 Limite d'items atteinte pour " + name);
                        break;
                    }
                }
            }

            // Garantir au moins 1 item si aucun n'a été ajouté
            if (getInventory().items().isEmpty() && !possibleLoot.isEmpty()) {
                // Sélectionne un item aléatoire parmi les possibles
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
        // Récupère tous les items via la méthode getItems() de Inventory
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