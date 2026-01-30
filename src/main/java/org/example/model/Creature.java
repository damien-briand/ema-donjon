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
    protected int level;
    protected int defense;
    protected Inventory<Item> inventory; // Inventaire de la créature

    public Creature(String name, int maxHealth, int attackPower, int level, int defense) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
        this.level = level;
        this.defense = defense;
        this.inventory = new Inventory<>(20); // Inventaire avec capacité de 20 items par défaut
    }

    @Override
    public void attack(Attackable target) {
        int damage = calculateDamage();

        // Coup critique : 10% de chance de faire x2 dégâts
        boolean isCritical = Math.random() < 0.10;
        if (isCritical) {
            damage *= 2;
            Logger.logInfo("💥 COUP CRITIQUE! " + name + " attaque pour " + damage + " dégâts!");
        } else {
            Logger.logInfo(name + " attaque pour " + damage + " dégâts!");
        }

        // Esquive basée sur la différence de niveau (cible plus forte = plus de chance d'esquiver)
        if (target instanceof Creature targetCreature) {
            int levelDiff = targetCreature.getLevel() - this.level;

            if (levelDiff > 0) {
                double dodgeChance = Math.min(0.30, levelDiff * 0.05); // Max 30%, +5% par niveau de différence
                if (Math.random() < dodgeChance) {
                    Logger.logInfo("💨 " + targetCreature.getName() + " esquive l'attaque!");
                    return;
                }
            }

            // Réduction des dégâts par la défense de la cible
            int defense = targetCreature.getDefense();
            if (defense > 0) {
                double damageReduction = 100.0 / (100.0 + defense);
                int reducedDamage = (int) (damage * damageReduction);
                reducedDamage = Math.max(1, reducedDamage); // Minimum 1 dégât

                int blocked = damage - reducedDamage;
                Logger.logInfo("🛡️ La défense de " + targetCreature.getName() + " bloque " + blocked + " dégâts!");
                damage = reducedDamage;
            }
        }

        target.takeDamage(damage);
    }


    @Override
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
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
        System.out.println("\n📦 Inventaire (" + inventory.size() + "/" + 20 + "):");

        List<Item> items = inventory.getItems();
        if (items.isEmpty()) {
            System.out.println("   (vide)");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            StringBuilder itemInfo = new StringBuilder("   " + (i + 1) + ". " + item.getName() + " - " + item.getDescription());

            if (item instanceof Weapon weapon) {
                itemInfo.append(" [ATK: +").append(weapon.getDamageBonus()).append("]");
                if (weapon.isEquipped()) {
                    itemInfo.append(" ⚔️ ÉQUIPÉ");
                }
            } else if (item instanceof Armor armor) {
                itemInfo.append(" [DEF: +").append(armor.getDefenseBonus()).append("]");
                if (armor.isEquipped()) {
                    itemInfo.append(" 🛡️ ÉQUIPÉ");
                }
            }

            System.out.println(itemInfo);
        }
    }

    public String getName() { return name; }
    public int getHealth() { return health; }

    public int getMaxHealth() { return maxHealth; }

    public int getAttackPower() { return attackPower; }

    public void setAttackPower(int attackPower) {this.attackPower=attackPower;};


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
