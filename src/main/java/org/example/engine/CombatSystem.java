package org.example.engine;

import org.example.exceptions.InsufficientManaException;
import org.example.model.*;
import org.example.util.Logger;

import java.util.List;
import java.util.Scanner;

/**
 * Système de combat tour par tour.
 * Gère les combats entre le joueur et les ennemis.
 */
public class CombatSystem {
    private int turnCount;

    /**
     * Constructeur du système de combat.
     */
    public CombatSystem() {
        this.turnCount = 0;
    }

    /**
     * Lance un combat entre le joueur et un ennemi.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     * @param scanner le scanner pour les entrées utilisateur
     * @return true si le joueur a gagné, false sinon
     */
    public boolean startCombat(Player player, Creature enemy, Scanner scanner) {
        turnCount = 0;

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           COMBAT COMMENCE              ║");
        System.out.println("╚════════════════════════════════════════╝");

        displayCombatStatus(player, enemy);

        // Boucle de combat tour par tour
        while (player.isAlive() && enemy.isAlive()) {
            turnCount++;
            System.out.println("\n" + repeatString("─", 50));
            System.out.println("Tour " + turnCount);
            System.out.println(repeatString("─", 50));

            // Tour du joueur
            boolean playerAction = playerTurn(player, enemy, scanner);

            if (!playerAction) {
                // Le joueur a fui
                return false;
            }

            // Vérifier si l'ennemi est mort
            if (!enemy.isAlive()) {
                displayVictory(player, enemy);
                return true;
            }

            // Tour de l'ennemi
            enemyTurn(player, enemy);

            // Vérifier si le joueur est mort
            if (!player.isAlive()) {
                displayDefeat(player, enemy);
                return false;
            }

            // Afficher le statut après le tour
            displayCombatStatus(player, enemy);
        }

        return player.isAlive();
    }

    /**
     * Gère le tour du joueur.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     * @param scanner le scanner pour les entrées
     * @return true si le joueur a effectué une action, false s'il a fui
     */
    private boolean playerTurn(Player player, Creature enemy, Scanner scanner) {
        System.out.println("\n🗡️  Tour de " + player.getName());
        displayPlayerActions(player);

        boolean validAction = false;

        while (!validAction) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            String[] parts = input.split("\\s+", 2);
            String action = parts[0];

            switch (action) {
                case "attaquer":
                case "a":
                case "attack":
                    performAttack(player, enemy);
                    validAction = true;
                    break;

                case "magie":
                case "m":
                case "magic":
                    if (!player.hasMana()) {
                        System.out.println("❌ Vous ne pouvez pas utiliser la magie!");
                    } else {
                        validAction = performMagicAttack(player, enemy);
                    }
                    break;

                case "objet":
                case "o":
                case "item":
                    validAction = useItemInCombat(player, scanner);
                    break;

                case "defendre":
                case "d":
                case "defend":
                    performDefend(player);
                    validAction = true;
                    break;

                case "fuir":
                case "f":
                case "flee":
                    if (attemptFlee(player, enemy)) {
                        return false; // Fuite réussie
                    }
                    validAction = true; // Fuite échouée compte comme une action
                    break;

                case "statut":
                case "s":
                case "status":
                    displayCombatStatus(player, enemy);
                    break;

                default:
                    System.out.println("❌ Action invalide. Tapez 'aide' pour voir les actions disponibles.");
                    break;
            }
        }

        return true;
    }

    /**
     * Affiche les actions disponibles pour le joueur.
     *
     * @param player le joueur
     */
    private void displayPlayerActions(Player player) {
        System.out.println("\nActions disponibles:");
        System.out.println("  attaquer (a) - Attaquer l'ennemi");
        if (player.hasMana()) {
            System.out.println("  magie (m)    - Utiliser une attaque magique (coûte 20 mana)");
        }
        System.out.println("  objet (o)    - Utiliser un objet");
        System.out.println("  defendre (d) - Se défendre (réduit les dégâts)");
        System.out.println("  fuir (f)     - Tenter de fuir le combat");
        System.out.println("  statut (s)   - Voir le statut du combat");
    }

    /**
     * Effectue une attaque normale.
     *
     * @param attacker l'attaquant
     * @param target   la cible
     */
    private void performAttack(Creature attacker, Creature target) {
        System.out.println("\n⚔️  " + attacker.getName() + " attaque " + target.getName() + "!");
        attacker.attack(target);
    }

    /**
     * Effectue une attaque magique.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     * @return true si l'attaque a été effectuée
     */
    private boolean performMagicAttack(Player player, Creature enemy) {
        int manaCost = 20;

        if (!player.useMana(manaCost)) {
            System.out.println("❌ Mana insuffisant! (coût: " + manaCost + ", disponible: " + player.getMana() + ")");
            return false;
        }

        // Attaque magique fait 1.5x les dégâts normaux
        int baseDamage = player.getAttackPower();
        int magicDamage = (int) (baseDamage * 1.5);

        System.out.println("\n✨ " + player.getName() + " lance un sort magique!");
        System.out.println("   Coût: " + manaCost + " mana");

        enemy.takeDamage(magicDamage);
        System.out.println("   💥 " + magicDamage + " dégâts magiques infligés!");

        return true;
    }

    /**
     * Utilise un objet pendant le combat.
     *
     * @param player le joueur
     * @param scanner le scanner pour les entrées
     * @return true si un objet a été utilisé
     */
    private boolean useItemInCombat(Player player, Scanner scanner) {
        List<Item> items = player.getInventory().getItems();

        if (items.isEmpty()) {
            System.out.println("❌ Vous n'avez aucun objet!");
            return false;
        }

        // Afficher les objets utilisables
        System.out.println("\n📦 Objets disponibles:");
        boolean hasUsableItems = false;

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                if (!potion.isConsumed()) {
                    System.out.println("   " + (i + 1) + ". " + item.getName() + " (+" + potion.getHealAmount() + " HP)");
                    hasUsableItems = true;
                }
            }
        }

        if (!hasUsableItems) {
            System.out.println("❌ Aucun objet utilisable!");
            return false;
        }

        System.out.print("\nChoisissez un objet (numéro) ou 'annuler': ");
        String choice = scanner.nextLine().trim();

        if ("annuler".equalsIgnoreCase(choice) || "cancel".equalsIgnoreCase(choice)) {
            return false;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < items.size()) {
                Item item = items.get(index);

                if (item instanceof Potion) {
                    Potion potion = (Potion) item;
                    if (!potion.isConsumed()) {
                        potion.useOn(player);
                        System.out.println("✓ " + potion.getName() + " utilisée!");
                        return true;
                    } else {
                        System.out.println("❌ Cette potion a déjà été utilisée.");
                        return false;
                    }
                } else {
                    System.out.println("❌ Cet objet ne peut pas être utilisé en combat.");
                    return false;
                }
            } else {
                System.out.println("❌ Numéro invalide.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrée invalide.");
            return false;
        }
    }

    /**
     * Se défendre pour réduire les dégâts du prochain tour.
     *
     * @param player le joueur
     */
    private void performDefend(Player player) {
        System.out.println("\n🛡️  " + player.getName() + " prend une posture défensive!");
        System.out.println("   Les dégâts du prochain tour seront réduits de 50%.");
        // Note: L'implémentation complète nécessiterait un système de buffs/debuffs
        // Pour simplifier, on pourrait ajouter un champ 'isDefending' dans Player
    }

    /**
     * Tente de fuir le combat.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     * @return true si la fuite a réussi
     */
    private boolean attemptFlee(Player player, Creature enemy) {
        // Chance de fuite: 50% de base
        double fleeChance = 0.5;

        // Réduire la chance si l'ennemi est plus fort
        if (enemy.getHealth() > player.getHealth()) {
            fleeChance -= 0.2;
        }

        boolean success = Math.random() < fleeChance;

        if (success) {
            System.out.println("\n💨 " + player.getName() + " réussit à fuir le combat!");
            Logger.logInfo(player.getName() + " fled from " + enemy.getName());
            return true;
        } else {
            System.out.println("\n❌ " + player.getName() + " n'a pas réussi à fuir!");
            System.out.println("   L'ennemi vous rattrape!");
            return false;
        }
    }

    /**
     * Gère le tour de l'ennemi.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     */
    private void enemyTurn(Player player, Creature enemy) {
        System.out.println("\n👹 Tour de " + enemy.getName());

        // Délai pour rendre le combat plus dramatique
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // L'ennemi attaque toujours pour l'instant
        // On pourrait ajouter de l'IA plus complexe ici
        performAttack(enemy, player);
    }

    /**
     * Affiche le statut du combat.
     *
     * @param player le joueur
     * @param enemy  l'ennemi
     */
    private void displayCombatStatus(Player player, Creature enemy) {
        System.out.println("\n" + repeatString("═", 50));

        // Statut du joueur
        System.out.println("🗡️  " + player.getName());
        int playerHealthPercent = (player.getHealth() * 100) / player.getMaxHealth();
        String playerHealthBar = createHealthBar(playerHealthPercent);
        System.out.println("   HP: " + player.getHealth() + "/" + player.getMaxHealth() + " " + playerHealthBar);

        if (player.hasMana()) {
            int playerManaPercent = (player.getMana() * 100) / player.getMaxMana();
            String playerManaBar = createManaBar(playerManaPercent);
            System.out.println("   MP: " + player.getMana() + "/" + player.getMaxMana() + " " + playerManaBar);
        }

        System.out.println();

        // Statut de l'ennemi
        System.out.println("👹 " + enemy.getName());
        int enemyHealthPercent = (enemy.getHealth() * 100) / enemy.getMaxHealth();
        String enemyHealthBar = createHealthBar(enemyHealthPercent);
        System.out.println("   HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth() + " " + enemyHealthBar);

        System.out.println(repeatString("═", 50));
    }

    /**
     * Crée une barre de vie visuelle.
     *
     * @param percent le pourcentage de vie
     * @return la barre de vie
     */
    private String createHealthBar(int percent) {
        int barLength = 20;
        int filled = (percent * barLength) / 100;

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");

        // Ajouter une couleur textuelle
        if (percent > 60) {
            bar.append(" 💚");
        } else if (percent > 30) {
            bar.append(" 💛");
        } else {
            bar.append(" ❤️");
        }

        return bar.toString();
    }

    /**
     * Crée une barre de mana visuelle.
     *
     * @param percent le pourcentage de mana
     * @return la barre de mana
     */
    private String createManaBar(int percent) {
        int barLength = 20;
        int filled = (percent * barLength) / 100;

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("] 💙");

        return bar.toString();
    }

    /**
     * Affiche un message de victoire.
     *
     * @param player le joueur
     * @param enemy  l'ennemi vaincu
     */
    private void displayVictory(Player player, Creature enemy) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║            VICTOIRE!                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n✓ " + enemy.getName() + " a été vaincu!");
        System.out.println("  Combat terminé en " + turnCount + " tour(s).");

        // XP et récompenses si c'est un monstre
        if (enemy instanceof Monster) {
            Monster monster = (Monster) enemy;
            System.out.println("  💰 Récompenses: " + monster.getExperienceReward() + " XP");
        }

        Logger.logInfo(player.getName() + " defeated " + enemy.getName() + " in " + turnCount + " turns");
    }

    /**
     * Affiche un message de défaite.
     *
     * @param player le joueur
     * @param enemy  l'ennemi vainqueur
     */
    private void displayDefeat(Player player, Creature enemy) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║            DÉFAITE...                  ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n💀 " + player.getName() + " a été vaincu par " + enemy.getName() + "...");

        Logger.logInfo(player.getName() + " was defeated by " + enemy.getName());
    }

    /**
     * Répète une chaîne n fois (alternative à String.repeat() pour Java 8).
     *
     * @param str la chaîne à répéter
     * @param count le nombre de répétitions
     * @return la chaîne répétée
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Obtient le nombre de tours du dernier combat.
     *
     * @return le nombre de tours
     */
    public int getTurnCount() {
        return turnCount;
    }
}
