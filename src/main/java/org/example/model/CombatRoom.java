package org.example.model;

/**
 * Salle de combat standard contenant des ennemis.
 * Le joueur doit combattre pour passer.
 */
public class CombatRoom extends Room {
    private boolean combatInitiated;

    // Constructeur sans-arg pour Jackson
    public CombatRoom() {
        super();
        this.combatInitiated = false;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de la salle
     * @param description description de la salle
     */
    public CombatRoom(String name, String description) {
        super(name, description);
        this.combatInitiated = false;
    }

    @Override
    public void onEnter(Player player) {
        displayDescription();

        if (hasAliveEnemies() && !combatInitiated) {
            System.out.println("\n⚔️  Des ennemis vous attaquent!");
            combatInitiated = true;
        } else if (isCleared) {
            System.out.println("\n✓ Cette salle est sécurisée.");
        }
    }

    @Override
    public void onExit(Player player) {
        if (hasAliveEnemies()) {
            System.out.println("⚠️  Vous fuyez le combat!");
        } else {
            System.out.println("→ Vous quittez la salle.");
        }
    }

    public boolean isCombatInitiated() {
        return combatInitiated;
    }

    public void setCombatInitiated(boolean combatInitiated) {
        this.combatInitiated = combatInitiated;
    }
}
