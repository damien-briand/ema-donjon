package org.example.model;

import org.example.interfaces.Attackable;
import org.example.interfaces.Healable;

/**
 * Classe abstraite représentant une créature du jeu.
 * Contient champs de base et implémentation de heal(int).
 */

public abstract class Creature implements Healable, Attackable {
    // Champs protégés pour permettre l'accès par les sous-classes
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;

    // Constructeur sans-arg requis pour Jackson
    public Creature() {
    }

    // Constructeur pratique
    public Creature(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = Math.max(1, maxHealth);
        this.health = this.maxHealth;
        this.attackPower = Math.max(0, attackPower);
    }

    // --- Getters et setters (nécessaires pour Jackson et encapsulation) ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, this.maxHealth));
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
        // Ensure current health is not above new max
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = Math.max(0, attackPower);
    }

    // --- Healable implementation ---
    /**
     * Soigne la créature d'une quantité donnée.
     * Ne dépasse pas maxHealth. Valeurs négatives ignorées.
     *
     * @param amount quantité de soin
     */
    @Override
    public void heal(int amount) {
        if (amount <= 0) {
            return;
        }
        this.health = Math.min(this.maxHealth, this.health + amount);
    }

    // --- Utilitaires de combat communs (implémentation par défaut) ---
    /**
     * Applique des dégâts à la créature.
     *
     * @param damage montant des dégâts (doit être >= 0)
     */
    public void takeDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Indique si la créature est toujours vivante.
     *
     * @return true si health > 0
     */
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, ATK: %d)", name, health, maxHealth, attackPower);
    }
}
