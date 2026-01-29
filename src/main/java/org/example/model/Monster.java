package org.example.model;

public class Monster extends Creature {
    private int level;
    private int experienceReward;
    private String type; // Type de monstre (Goblin, Dragon, etc.)

    // Constructeur complet
    public Monster(String name, int maxHealth, int attackPower, int level, String type) {
        super(name, maxHealth, attackPower);
        this.level = level;
        this.type = type;
        this.experienceReward = level * 10; // XP = niveau * 10
    }

    // Constructeur avec expérience personnalisée
    public Monster(String name, int maxHealth, int attackPower, int level, String type, int experienceReward) {
        super(name, maxHealth, attackPower);
        this.level = level;
        this.type = type;
        this.experienceReward = experienceReward;
    }

    // Constructeur pour Jackson (no-arg constructor)
    public Monster() {
        super("Unknown", 10, 1);
        this.level = 1;
        this.type = "Unknown";
        this.experienceReward = 10;
    }

    @Override
    protected int calculateDamage() {
        // Dégâts de base = attackPower + bonus de niveau
        int baseDamage = attackPower + (level * 2);

        // Variation aléatoire de ±15% pour plus de dynamisme
        double variation = 0.85 + (Math.random() * 0.3); // Entre 0.85 et 1.15
        int finalDamage = (int) (baseDamage * variation);

        return Math.max(1, finalDamage); // Minimum 1 dégât
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);

        // Message spécial si le monstre meurt
        if (!isAlive()) {
            System.out.println("💀 " + name + " est vaincu !");
        }
    }

    // Méthode pour générer du loot aléatoire basé sur le niveau
    public void generateRandomLoot() {
        // Probabilité de drop augmente avec le niveau
        double dropChance = Math.min(0.5 + (level * 0.05), 0.95); // Max 95%

        if (Math.random() < dropChance) {
            // TODO: Ajouter des items aléatoires selon le type de monstre dans son inventaire (pour apres getInventory dessus pour recup le loot)
            System.out.println("💰 " + name + " laisse tomber du butin !");
        }
    }

    // Getters et Setters (nécessaires pour Jackson)
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(int experienceReward) {
        this.experienceReward = experienceReward;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - Niveau %d | HP: %d/%d | ATK: %d | XP: %d",
                name, type, level, health, maxHealth, attackPower, experienceReward);
    }
}
