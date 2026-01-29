package org.example.model;

/**
 * Classe représentant une potion de soin.
 * Les potions sont des objets consommables qui restaurent des points de vie.
 */
public class Potion extends Item {
    private int healAmount;
    private boolean isConsumed;

    // Constructeur sans-arg requis pour Jackson
    public Potion() {
        super();
        this.isConsumed = false;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de la potion
     * @param description description de la potion
     * @param healAmount  quantité de HP restaurés
     */
    public Potion(String name, String description, int healAmount) {
        super(name, description);
        this.healAmount = Math.max(0, healAmount);
        this.isConsumed = false;
    }

    /**
     * Utilise la potion sur une créature pour la soigner.
     * La potion ne peut être utilisée qu'une seule fois.
     */
    @Override
    public void use() {
        if (isConsumed) {
            System.out.println("❌ Cette potion a déjà été utilisée!");
            return;
        }

        System.out.println("✓ Utilisation de " + name + " - Restaure " + healAmount + " HP");
        isConsumed = true;
    }

    /**
     * Utilise la potion sur une cible spécifique.
     *
     * @param target la créature à soigner
     */
    public void useOn(Creature target) {
        if (isConsumed) {
            System.out.println("❌ Cette potion a déjà été utilisée!");
            return;
        }

        if (target == null || !target.isAlive()) {
            System.out.println("❌ Cible invalide!");
            return;
        }

        target.heal(healAmount);
        isConsumed = true;
        System.out.println("✓ " + name + " utilisée sur " + target.getName());
    }

    // Getters et setters pour Jackson
    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = Math.max(0, healAmount);
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
    }

    /**
     * Vérifie si la potion peut encore être utilisée.
     *
     * @return true si la potion n'a pas été consommée
     */
    public boolean isUsable() {
        return !isConsumed;
    }

    @Override
    public String toString() {
        String status = isConsumed ? " (consommée)" : " (+" + healAmount + " HP)";
        return name + status + " - " + description;
    }
}
