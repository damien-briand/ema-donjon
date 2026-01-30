package org.example.model.monsters;

import org.example.model.*;
import org.example.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skeleton extends Monster {
    private Map<Item, Double> possibleLoot = new HashMap<>();

    public Skeleton() {
        super("Squelette", 50, 8, 1, "Skeleton");
        // Items communs avec taux de drop de base
        possibleLoot.put(new Weapon("Épée Rouillée", "Une vieille épée usée par le temps", 8, "Épée"), 0.40);
        possibleLoot.put(new Weapon("Arc Ancien", "Un arc en bois vermoulu", 6, "Arc"), 0.30);
        possibleLoot.put(new Armor("Casque Fissuré", "Un casque en mauvais état", 5, "Tête"), 0.35);
        possibleLoot.put(new Armor("Bouclier Ébréché", "Un petit bouclier abîmé", 4, "Bouclier"), 0.25);

        // Extensions: Potions et Matériaux
        possibleLoot.put(new Potion("Petite Potion de Vie", "Restaure 20 PV", 20), 0.50);
        possibleLoot.put(new Material("Os", "Matériau de craft commun", 1), 0.60);
        possibleLoot.put(new Material("Poussière d'Os", "Poudre magique faible", 1), 0.45);
    }

    @Override
    public void generateRandomLoot() {
        // Skeleton = monstre basique, 45% de chance global de drop
        double globalDropChance = 0.45;

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

                    // Augmente la pénalité : -25% par item droppé pour les monstres communs
                    dropPenalty += 0.25;

                    // Maximum 3 items pour un Skeleton
                    if (itemsDropped >= 3) {
                        Logger.logInfo("📦 Limite d'items atteinte pour " + name);
                        break;
                    }
                }
            }

            if (getInventory().getItems().isEmpty()) {
                Logger.logInfo("😢 Le butin s'est désintégré...");
            }
        } else {
            Logger.logInfo("😢 " + name + " ne laisse aucun butin cette fois...");
        }
    }

    @Override
    public List<Item> getLoot() {
        List<Item> droppedLoot = new ArrayList<>(getInventory().getItems());

        if (!droppedLoot.isEmpty()) {
            Logger.logInfo("🎁 Butin récupéré de " + name + ": " + droppedLoot.size() + " item(s)");
            for (Item item : droppedLoot) {
                Logger.logInfo("  - " + item.getName());
            }
        }

        return droppedLoot;
    }
}