package org.example.model;

import org.example.interfaces.Attackable;
import org.example.interfaces.Healable;
import org.example.interfaces.Lootable;
import org.example.inventory.Inventory;
import org.example.util.Logger;

import java.util.List;

public abstract class Creature implements Attackable, Healable, Lootable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;
    protected Inventory<Item> inventory; // Inventaire de la créature

    public Creature(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
        this.inventory = new Inventory<>(20); // Inventaire avec capacité de 20 items par défaut
    }

    @Override
    public void attack(Attackable target) {
        int damage = calculateDamage();
        target.takeDamage(damage);
        Logger.logInfo(name + " attaque pour " + damage + " dégâts!");
    }

    @Override
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage); // TODO : faire en sorte de mettre une chance de pas mettre de degats en fonctions de l'écart du niveaux (et aussi coup critique)
        Logger.logInfo(name + " reçoit " + damage + " dégâts! HP: " + health);
    }

    @Override
    public void heal(int amount){
        health = Math.min(maxHealth, health + amount);
        Logger.logInfo(name + " récupère " + amount + " HP! HP : " + health);
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    protected abstract int calculateDamage(); // Polymorphisme !

    // Implémentation de Lootable
    @Override
    public List<Item> getLoot() {
        return inventory.getItems(); // Retourne les items de l'inventaire
    }

    // Méthodes pour gérer l'inventaire
    public void addItemToInventory(Item item) {
        try {
            inventory.addItem(item);
        } catch (Exception e) {
            Logger.logError("Impossible d'ajouter l'item: " + e.getMessage(), e);
        }
    }

    public void removeItemFromInventory(Item item) {
        inventory.removeItem(item);
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        Logger.logInfo("\n=== Inventaire de " + name + " ===");
        List<Item> items = inventory.getItems();
        if (items.isEmpty()) {
            Logger.logInfo("(vide)");
        } else {
            items.forEach(item ->
                    Logger.logInfo("- " + item.getName() + ": " + item.getDescription())
            );
        }
    }

    public String getName() { return name; }
    public int getHealth() { return health; }


}
