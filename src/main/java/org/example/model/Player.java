package org.example.model;

import org.example.util.Logger;

public class Player extends Creature {
    private Integer mana;
    private Integer maxMana;
    private final boolean hasMana;
    private int manaUsageCount;
    private int experience;
    private int experienceToNextLevel;
    private static final int USES_FOR_MAX_MANA_INCREASE = 50; // Très lent: tous les 50 usages
    private static final double MAX_MANA_INCREASE_PERCENTAGE = 0.02; // Augmentation de 2% du mana max actuel

    public Player(String name, int health, int attackPower, boolean hasMana) { // GUERRIER
        super(name, health, attackPower, 1 , 10);
        this.hasMana = hasMana;
        this.mana = hasMana ? 100 : null;
        this.maxMana = hasMana ? 100 : null;
        this.manaUsageCount = 0;
        this.experience = 0;
        this.experienceToNextLevel = 25;
    }

    public Player(String name, int health, int attackPower, boolean hasMana, int initialMaxMana) { // MAGE
        super(name, health, attackPower, 1, 0);
        this.hasMana = hasMana;
        this.mana = hasMana ? initialMaxMana : null;
        this.maxMana = hasMana ? initialMaxMana : null;
        this.manaUsageCount = 0;
        this.experience = 0;
        this.experienceToNextLevel = 25;
    }

    public boolean hasMana() {
        return hasMana;
    }

    public Integer getMana() {
        return mana;
    }

    public Integer getMaxMana() {
        return maxMana;
    }

    public void setMana(int mana) {
        if (hasMana && maxMana != null) {
            this.mana = Math.min(mana, maxMana);
        }
    }

    public void addMana(int amount) {
        if (hasMana && amount > 0 && maxMana != null) {
            this.mana = Math.min(this.mana + amount, maxMana);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public boolean useMana(int amount) {
        if (hasMana && mana != null && mana >= amount) {
            this.mana -= amount;
            manaUsageCount++;

            if (manaUsageCount >= USES_FOR_MAX_MANA_INCREASE) {
                increaseMaxMana();
                manaUsageCount = 0;
            }

            return true;
        }
        return false;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public void setExperienceToNextLevel(int experienceToNextLevel) {
        this.experienceToNextLevel = experienceToNextLevel;
    }

    public void gainExperience(int xp) {
        if (xp <= 0) {
            return;
        }

        experience += xp;
        Logger.logInfo(name + " gagne " + xp + " XP (" + experience + "/" + experienceToNextLevel + ")");

        // Vérifier si le joueur monte de niveau
        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= experienceToNextLevel;

        // Augmentation des stats
        int healthIncrease = 10 + (level * 2); // Augmente avec le niveau
        int attackIncrease = 3 + level; // Augmente avec le niveau

        maxHealth += healthIncrease;
        health = maxHealth; // Restaure la vie complète
        attackPower += attackIncrease;

        // Augmentation du mana si applicable
        if (hasMana && maxMana != null) {
            int manaIncrease = 10 + (level * 3);
            maxMana += manaIncrease;
            mana = maxMana; // Restaure le mana complet
            Logger.logInfo("🎉 " + name + " monte au niveau " + level + "! PV: +" + healthIncrease +
                    ", ATK: +" + attackIncrease + ", Mana: +" + manaIncrease);
        } else {
            Logger.logInfo("🎉 " + name + " monte au niveau " + level + "! PV: +" + healthIncrease +
                    ", ATK: +" + attackIncrease);
        }

        // Calcul de l'XP nécessaire pour le prochain niveau (progression exponentielle)
        experienceToNextLevel = (int) (experienceToNextLevel * 1.5);
    }

    private void increaseMaxMana() {
        if (maxMana != null) {
            int increase = Math.max(1, (int) (maxMana * MAX_MANA_INCREASE_PERCENTAGE));
            maxMana += increase;
            Logger.logInfo("✨ " + name + " maîtrise mieux sa magie! Mana max: " + maxMana + " (+" + increase + ")");
        }
    }

    @Override
    public String toString() {
        String baseInfo = super.toString();
        if (hasMana) {
            return baseInfo + ", Mana: " + mana + "/" + maxMana;
        }
        return baseInfo;
    }

    @Override
    protected int calculateDamage() {
        // Dégâts de base = attackPower * multiplicateur de niveau
        double levelMultiplier = 1.0 + (level - 1) * 0.1; // +10% par niveau
        double baseDamage = attackPower * levelMultiplier;

        // Variation aléatoire de ±10% pour plus de dynamisme
        double variation = 0.9 + (Math.random() * 0.2); // Entre 0.9 et 1.1
        int finalDamage = (int) (baseDamage * variation);

        return Math.max(1, finalDamage); // Minimum 1 dégât
    }
}
