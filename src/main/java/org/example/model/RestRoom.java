package org.example.model;

/**
 * Salle de repos permettant au joueur de se soigner.
 * Le joueur peut se reposer pour récupérer des HP et du mana.
 */
public class RestRoom extends Room {
    private int healAmount;
    private int manaRestoreAmount;
    private boolean hasBeenUsed;

    // Constructeur sans-arg pour Jackson
    public RestRoom() {
        super();
        this.healAmount = 50;
        this.manaRestoreAmount = 30;
        this.hasBeenUsed = false;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de la salle
     * @param description description de la salle
     */
    public RestRoom(String name, String description) {
        super(name, description);
        this.healAmount = 50;
        this.manaRestoreAmount = 30;
        this.hasBeenUsed = false;
    }

    /**
     * Constructeur complet.
     *
     * @param name               nom de la salle
     * @param description        description de la salle
     * @param healAmount         quantité de HP restaurés
     * @param manaRestoreAmount  quantité de mana restauré
     */
    public RestRoom(String name, String description, int healAmount, int manaRestoreAmount) {
        super(name, description);
        this.healAmount = healAmount;
        this.manaRestoreAmount = manaRestoreAmount;
        this.hasBeenUsed = false;
    }

    @Override
    public void onEnter(Player player) {
        displayDescription();

        if (hasBeenUsed) {
            System.out.println("\n💤 Cette salle de repos a déjà été utilisée.");
        } else {
            System.out.println("\n💤 Vous pouvez vous reposer ici pour récupérer.");
            System.out.println("   Tapez 'repos' ou 'rest' pour vous reposer.");
        }
    }

    @Override
    public void onExit(Player player) {
        System.out.println("→ Vous quittez la salle de repos.");
    }

    /**
     * Permet au joueur de se reposer.
     *
     * @param player le joueur qui se repose
     */
    public void rest(Player player) {
        if (hasBeenUsed) {
            System.out.println("❌ Vous ne pouvez vous reposer qu'une seule fois ici.");
            return;
        }

        System.out.println("\n💤 Vous vous reposez...");

        // Soigner le joueur
        int currentHealth = player.getHealth();
        player.heal(healAmount);
        int healthRestored = player.getHealth() - currentHealth;

        if (healthRestored > 0) {
            System.out.println("   ❤️  +" + healthRestored + " HP restaurés");
        }

        // Restaurer le mana si le joueur en a
        if (player.hasMana()) {
            int currentMana = player.getMana();
            player.setMana(manaRestoreAmount);
            int manaRestored = player.getMana() - currentMana;

            if (manaRestored > 0) {
                System.out.println("   ✨ +" + manaRestored + " mana restaurés");
            }
        }

        hasBeenUsed = true;
        System.out.println("✓ Vous vous sentez revigoré!");
    }

    // Getters et setters

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = Math.max(0, healAmount);
    }

    public int getManaRestoreAmount() {
        return manaRestoreAmount;
    }

    public void setManaRestoreAmount(int manaRestoreAmount) {
        this.manaRestoreAmount = Math.max(0, manaRestoreAmount);
    }

    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }

    public void setHasBeenUsed(boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }
}
