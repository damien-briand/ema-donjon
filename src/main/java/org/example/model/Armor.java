package org.example.model;

import org.example.util.Logger;

/**
 * Classe représentant une armure.
 * Les armures réduisent les dégâts reçus lorsqu'elles sont équipées.
 */
public class Armor extends Item {
    private int defenseBonus;
    private String armorType; // ex: "Légère", "Lourde", "Robe"
    private boolean isEquipped;

    // Constructeur sans-arg requis pour Jackson
    public Armor() {
        super();
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name         nom de l'armure
     * @param description  description de l'armure
     * @param defenseBonus bonus de défense
     * @param armorType    type d'armure (Légère, Lourde, Robe, etc.)
     */
    public Armor(String name, String description, int defenseBonus, String armorType) {
        super(name, description);
        this.defenseBonus = Math.max(0, defenseBonus);
        this.armorType = armorType;
        this.isEquipped = false;
    }

    /**
     * Utilise l'armure (équipe ou déséquipe).
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
     * Équipe l'armure.
     */
    public void equip() {
        if (isEquipped) {
            Logger.logInfo("🛡️  " + name + " est déjà équipée!");
            return;
        }

        isEquipped = true;
        Logger.logInfo("🛡️  " + name + " équipée! (+" + defenseBonus + " DEF)");
    }

    /**
     * Déséquipe l'armure.
     */
    public void unequip() {
        if (!isEquipped) {
            Logger.logInfo("❌ " + name + " n'est pas équipée!");
            return;
        }

        isEquipped = false;
        Logger.logInfo("🛡️  " + name + " déséquipée.");
    }

    /**
     * Calcule la réduction de dégâts basée sur le bonus de défense.
     *
     * @param incomingDamage dégâts entrants
     * @return dégâts réduits
     */
    public int calculateReducedDamage(int incomingDamage) {
        if (!isEquipped || incomingDamage <= 0) {
            return incomingDamage;
        }

        // Réduction proportionnelle: chaque point de défense réduit 1 dégât
        // Minimum 1 dégât si les dégâts entrants sont > 0
        return Math.max(1, incomingDamage - defenseBonus);
    }

    // Getters et setters pour Jackson
    public int getDefenseBonus() {
        return defenseBonus;
    }

    public void setDefenseBonus(int defenseBonus) {
        this.defenseBonus = Math.max(0, defenseBonus);
    }

    public String getArmorType() {
        return armorType;
    }

    public void setArmorType(String armorType) {
        this.armorType = armorType;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    /**
     * Retourne le bonus de défense si l'armure est équipée.
     *
     * @return bonus de défense ou 0 si non équipée
     */
    public int getActiveDefenseBonus() {
        return isEquipped ? defenseBonus : 0;
    }

    @Override
    public String toString() {
        String status = isEquipped ? " [ÉQUIPÉE]" : "";
        return name + status + " (" + armorType + ", +" + defenseBonus + " DEF) - " + description;
    }
}
