package org.example.model;

/**
 * Classe représentant une arme.
 * Les armes augmentent la puissance d'attaque du joueur lorsqu'elles sont équipées.
 */
public class Weapon extends Item {
    private int damageBonus;
    private String weaponType; // ex: "Épée", "Arc", "Hache"
    private boolean isEquipped;

    // Constructeur sans-arg requis pour Jackson
    public Weapon() {
        super();
        this.isEquipped = false;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de l'arme
     * @param description description de l'arme
     * @param damageBonus bonus de dégâts
     * @param weaponType  type d'arme (Épée, Arc, Hache, etc.)
     */
    public Weapon(String name, String description, int damageBonus, String weaponType) {
        super(name, description);
        this.damageBonus = Math.max(0, damageBonus);
        this.weaponType = weaponType;
        this.isEquipped = false;
    }

    /**
     * Utilise l'arme (équipe ou déséquipe).
     */
    @Override
    public void use() {
        if (isEquipped) {
            unequip();
        } else {
            equip();
        }
    }

    /**
     * Équipe l'arme.
     */
    public void equip() {
        if (isEquipped) {
            System.out.println("⚔️  " + name + " est déjà équipée!");
            return;
        }

        isEquipped = true;
        System.out.println("⚔️  " + name + " équipée! (+" + damageBonus + " ATK)");
    }

    /**
     * Déséquipe l'arme.
     */
    public void unequip() {
        if (!isEquipped) {
            System.out.println("❌ " + name + " n'est pas équipée!");
            return;
        }

        isEquipped = false;
        System.out.println("⚔️  " + name + " déséquipée.");
    }

    // Getters et setters pour Jackson
    public int getDamageBonus() {
        return damageBonus;
    }

    public void setDamageBonus(int damageBonus) {
        this.damageBonus = Math.max(0, damageBonus);
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    /**
     * Retourne le bonus de dégâts si l'arme est équipée.
     *
     * @return bonus de dégâts ou 0 si non équipée
     */
    public int getActiveDamageBonus() {
        return isEquipped ? damageBonus : 0;
    }

    @Override
    public String toString() {
        String status = isEquipped ? " [ÉQUIPÉE]" : "";
        return name + status + " (" + weaponType + ", +" + damageBonus + " ATK) - " + description;
    }
}
