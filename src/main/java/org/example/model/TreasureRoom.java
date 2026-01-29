package org.example.model;

/**
 * Salle de trésor contenant des objets précieux.
 * Peut être piégée ou gardée par un monstre.
 */
public class TreasureRoom extends Room {
    private boolean isLooted;
    private boolean isTrapped;
    private int trapDamage;

    // Constructeur sans-arg pour Jackson
    public TreasureRoom() {
        super();
        this.isLooted = false;
        this.isTrapped = false;
        this.trapDamage = 0;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de la salle
     * @param description description de la salle
     */
    public TreasureRoom(String name, String description) {
        super(name, description);
        this.isLooted = false;
        this.isTrapped = false;
        this.trapDamage = 0;
    }

    /**
     * Constructeur avec piège.
     *
     * @param name        nom de la salle
     * @param description description de la salle
     * @param isTrapped   si la salle est piégée
     * @param trapDamage  dégâts du piège
     */
    public TreasureRoom(String name, String description, boolean isTrapped, int trapDamage) {
        super(name, description);
        this.isLooted = false;
        this.isTrapped = isTrapped;
        this.trapDamage = trapDamage;
    }

    @Override
    public void onEnter(Player player) {
        displayDescription();

        if (isLooted) {
            System.out.println("\n💰 Cette salle a déjà été pillée.");
            return;
        }

        if (hasAliveEnemies()) {
            System.out.println("\n⚔️  Un gardien protège le trésor!");
            return;
        }

        if (isTrapped && !isVisited) {
            System.out.println("\n💥 PIÈGE! Vous déclenchez un mécanisme!");
            player.takeDamage(trapDamage);
            System.out.println("   Vous perdez " + trapDamage + " HP!");
            isTrapped = false; // Le piège ne fonctionne qu'une fois
        }

        if (!items.isEmpty()) {
            System.out.println("\n✨ Vous trouvez un trésor!");
        }
    }

    @Override
    public void onExit(Player player) {
        if (items.isEmpty() && !hasAliveEnemies()) {
            isLooted = true;
        }
        System.out.println("→ Vous quittez la salle au trésor.");
    }

    /**
     * Pille la salle (ramasse tous les objets).
     *
     * @param player le joueur qui pille
     */
    public void loot(Player player) {
        if (isLooted) {
            System.out.println("❌ Cette salle a déjà été pillée.");
            return;
        }

        if (hasAliveEnemies()) {
            System.out.println("❌ Vous ne pouvez pas piller avec des ennemis vivants!");
            return;
        }

        if (items.isEmpty()) {
            System.out.println("❌ Il n'y a rien à piller ici.");
            return;
        }

        System.out.println("💰 Vous pillez le trésor:");
        items.forEach(item -> System.out.println("  + " + item.getName()));
        // Note: L'ajout à l'inventaire doit être géré par le GameEngine
        isLooted = true;
    }

    // Getters et setters

    public boolean isLooted() {
        return isLooted;
    }

    public void setLooted(boolean looted) {
        isLooted = looted;
    }

    public boolean isTrapped() {
        return isTrapped;
    }

    public void setTrapped(boolean trapped) {
        isTrapped = trapped;
    }

    public int getTrapDamage() {
        return trapDamage;
    }

    public void setTrapDamage(int trapDamage) {
        this.trapDamage = Math.max(0, trapDamage);
    }
}
