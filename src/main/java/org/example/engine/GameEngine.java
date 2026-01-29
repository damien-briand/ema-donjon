package org.example.engine;

import org.example.exceptions.InventoryFullException;
import org.example.model.*;
import org.example.util.JsonLoader;
import org.example.util.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Moteur principal du jeu.
 * Utilise la réflexion pour charger dynamiquement les monstres.
 * Gère la boucle de jeu, le donjon, et les interactions.
 */
public class GameEngine {
    private Player player;
    private Room currentRoom;
    private Map<String, Room> dungeon;
    private CombatSystem combatSystem;
    private Scanner scanner;
    private boolean isRunning;

    // Constantes pour les chemins de fichiers
    private static final String MONSTERS_JSON = "monsters.json";
    private static final String ITEMS_JSON = "items.json";
    private static final String SAVE_PATH = "saves/player_save.json";

    /**
     * Constructeur du GameEngine.
     */
    public GameEngine() {
        this.dungeon = new HashMap<>();
        this.combatSystem = new CombatSystem();
        this.scanner = new Scanner(System.in);
        this.isRunning = false;
    }

    /**
     * Démarre le jeu.
     */
    public void start() {
        displayWelcome();

        // Initialiser le joueur
        if (!initializePlayer()) {
            return;
        }

        // Initialiser le donjon
        initializeDungeon();

        // Lancer la boucle de jeu
        isRunning = true;
        gameLoop();
    }

    /**
     * Affiche l'écran de bienvenue.
     */
    private void displayWelcome() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      DONJON CRAWLER - EMA RPG          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nBienvenue dans le donjon de l'école EMA!");
        System.out.println("Préparez-vous à affronter des créatures redoutables...\n");
    }

    /**
     * Initialise le joueur (nouveau ou chargement).
     *
     * @return true si l'initialisation a réussi
     */
    private boolean initializePlayer() {
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger une partie");
        System.out.print("\nVotre choix: ");

        try {
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                createNewPlayer();
                return true;
            } else if ("2".equals(choice)) {
                return loadPlayer();
            } else {
                System.out.println("❌ Choix invalide. Création d'une nouvelle partie...");
                createNewPlayer();
                return true;
            }
        } catch (Exception e) {
            Logger.logError("Error during player initialization", e);
            System.out.println("❌ Erreur lors de l'initialisation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crée un nouveau joueur.
     */
    private void createNewPlayer() {
        System.out.print("\nEntrez le nom de votre héros: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            name = "Aventurier";
        }

        System.out.println("\nChoisissez votre classe:");
        System.out.println("1. Guerrier (HP élevés, pas de mana)");
        System.out.println("2. Mage (HP moyens, utilise la magie)");
        System.out.print("\nVotre choix: ");

        String classChoice = scanner.nextLine().trim();
        boolean hasMana = "2".equals(classChoice);

        int health = hasMana ? 80 : 120;
        int attack = hasMana ? 10 : 15;

        player = new Player(name, health, attack, hasMana);

        // Initialiser l'inventaire avec quelques items de base
        try {
            player.getInventory().addItem(new Potion("Potion de Soin", "Restaure 30 HP", 30));
            player.getInventory().addItem(new Potion("Potion de Soin", "Restaure 30 HP", 30));
        } catch (InventoryFullException e) {
            Logger.logWarning("Could not add starting potions: " + e.getMessage());
        }

        System.out.println("\n✓ " + player.getName() + " a été créé!");
        System.out.println("   HP: " + player.getHealth() + "/" + player.getMaxHealth());
        if (hasMana) {
            System.out.println("   Mana: " + player.getMana() + "/" + player.getMaxMana());
        }
        System.out.println("   ATK: " + player.getAttackPower());

        Logger.logInfo("New player created: " + player.getName());
    }

    /**
     * Charge un joueur depuis un fichier de sauvegarde.
     *
     * @return true si le chargement a réussi
     */
    private boolean loadPlayer() {
        try {
            if (!JsonLoader.fileExists(SAVE_PATH)) {
                System.out.println("❌ Aucune sauvegarde trouvée.");
                System.out.println("   Création d'une nouvelle partie...");
                createNewPlayer();
                return true;
            }

            player = SaveManager.loadGame(SAVE_PATH);
            System.out.println("\n✓ Partie chargée!");
            System.out.println("   Bienvenue, " + player.getName() + "!");
            return true;

        } catch (IOException e) {
            Logger.logError("Failed to load save game", e);
            System.out.println("❌ Erreur lors du chargement: " + e.getMessage());
            System.out.println("   Création d'une nouvelle partie...");
            createNewPlayer();
            return true;
        }
    }

    /**
     * Initialise le donjon avec les salles et les connexions.
     */
    private void initializeDungeon() {
        System.out.println("\n🏰 Génération du donjon...");

        // Créer les salles
        CombatRoom entrance = new CombatRoom(
            "Entrée du Donjon",
            "Une porte massive s'ouvre sur un couloir sombre et humide."
        );

        CombatRoom hallway = new CombatRoom(
            "Couloir des Ombres",
            "Des torches vacillantes éclairent faiblement les murs de pierre."
        );

        RestRoom restArea = new RestRoom(
            "Salle de Repos",
            "Une petite alcôve avec un feu de camp abandonné.",
            40, 30
        );

        TreasureRoom treasury = new TreasureRoom(
            "Salle au Trésor",
            "Des coffres remplis de richesses scintillent dans la pénombre.",
            true, 15 // piégée avec 15 dégâts
        );

        CombatRoom bossRoom = new CombatRoom(
            "Salle du Boss",
            "Une vaste salle circulaire. Au centre, une créature imposante vous attend..."
        );

        // Connecter les salles
        entrance.connectRoomBidirectional("nord", hallway);
        hallway.connectRoomBidirectional("est", restArea);
        hallway.connectRoomBidirectional("ouest", treasury);
        hallway.connectRoomBidirectional("nord", bossRoom);

        // Peupler les salles avec des monstres (utilise la réflexion)
        populateRoom(entrance, "Goblin", 1);
        populateRoom(hallway, "Skeleton", 2);
        populateRoom(bossRoom, "Dragon", 1);

        // Ajouter des objets dans la salle au trésor
        treasury.addItem(new Weapon("Épée Enchantée", "Une lame magique puissante", 25, "Épée"));
        treasury.addItem(new Armor("Armure de Plaques", "Une armure lourde et résistante", 20, "Lourde"));
        treasury.addItem(new Potion("Potion Majeure", "Restaure 100 HP", 100));

        // Stocker les salles dans le donjon
        dungeon.put("entrance", entrance);
        dungeon.put("hallway", hallway);
        dungeon.put("rest", restArea);
        dungeon.put("treasury", treasury);
        dungeon.put("boss", bossRoom);

        // Définir la salle de départ
        currentRoom = entrance;

        System.out.println("✓ Donjon généré avec succès!");
        Logger.logInfo("Dungeon initialized with " + dungeon.size() + " rooms");
    }

    /**
     * Peuple une salle avec des monstres en utilisant la réflexion.
     * UTILISE LA RÉFLEXION ✅ (exigence du projet)
     *
     * @param room        la salle à peupler
     * @param monsterType le type de monstre
     * @param count       le nombre de monstres
     */
    private void populateRoom(Room room, String monsterType, int count) {
        try {
            for (int i = 0; i < count; i++) {
                Monster monster = createMonsterUsingReflection(monsterType);
                room.addCreature(monster);
            }
            Logger.logInfo("Room populated with " + count + " " + monsterType + "(s)");
        } catch (Exception e) {
            Logger.logError("Failed to populate room with " + monsterType, e);
            System.out.println("⚠️  Avertissement: Impossible de créer " + monsterType);
        }
    }

    /**
     * Crée un monstre en utilisant la réflexion.
     * UTILISE LA RÉFLEXION ✅ (exigence du projet)
     *
     * @param monsterType le type de monstre à créer
     * @return le monstre créé
     * @throws ClassNotFoundException si la classe n'existe pas
     * @throws NoSuchMethodException si le constructeur n'existe pas
     * @throws InvocationTargetException si l'invocation échoue
     * @throws InstantiationException si l'instanciation échoue
     * @throws IllegalAccessException si l'accès est interdit
     */
    private Monster createMonsterUsingReflection(String monsterType)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        // Charger la classe dynamiquement avec réflexion ✅
        String className = "org.example.model." + monsterType;
        Class<?> monsterClass = Class.forName(className);

        // Vérifier que c'est bien une sous-classe de Monster
        if (!Monster.class.isAssignableFrom(monsterClass)) {
            throw new IllegalArgumentException(monsterType + " n'est pas un monstre valide");
        }

        // Obtenir le constructeur avec les paramètres appropriés
        Constructor<?> constructor = monsterClass.getDeclaredConstructor();

        // Créer l'instance dynamiquement ✅
        Monster monster = (Monster) constructor.newInstance();

        Logger.logDebug("Monster created via reflection: " + monsterType);
        return monster;
    }

    /**
     * Boucle principale du jeu.
     */
    private void gameLoop() {
        // Afficher la salle de départ
        currentRoom.onEnter(player);

        while (isRunning && player.isAlive()) {
            System.out.println("\n" + "─");
            displayPlayerStatus();
            System.out.println("\nQue voulez-vous faire?");
            displayAvailableCommands();

            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            processCommand(input);

            // Vérifier si le joueur est toujours en vie
            if (!player.isAlive()) {
                gameOver();
                break;
            }
        }

        cleanup();
    }

    /**
     * Affiche le statut du joueur.
     */
    private void displayPlayerStatus() {
        System.out.println("📊 " + player.getName());
        System.out.println("   ❤️  HP: " + player.getHealth() + "/" + player.getMaxHealth());
        if (player.hasMana()) {
            System.out.println("   ✨ Mana: " + player.getMana() + "/" + player.getMaxMana());
        }
        System.out.println("   ⚔️  ATK: " + player.getAttackPower());
    }

    /**
     * Affiche les commandes disponibles.
     */
    private void displayAvailableCommands() {
        System.out.println("  regarder (r)    - Examiner la salle");
        System.out.println("  aller <dir>     - Se déplacer (nord/sud/est/ouest)");
        System.out.println("  attaquer (a)    - Attaquer un ennemi");
        System.out.println("  inventaire (i)  - Voir l'inventaire");
        System.out.println("  utiliser <item> - Utiliser un objet");
        System.out.println("  ramasser        - Ramasser les objets au sol");
        System.out.println("  repos           - Se reposer (si disponible)");
        System.out.println("  sauvegarder (s) - Sauvegarder la partie");
        System.out.println("  quitter (q)     - Quitter le jeu");
    }

    /**
     * Traite une commande utilisateur.
     *
     * @param input la commande entrée par l'utilisateur
     */
    private void processCommand(String input) {
        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.toLowerCase().split("\\s+", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        try {
            switch (command) {
                case "regarder":
                case "r":
                    currentRoom.displayDescription();
                    break;

                case "aller":
                case "go":
                    if (argument.isEmpty()) {
                        System.out.println("❌ Spécifiez une direction (nord/sud/est/ouest)");
                    } else {
                        move(argument);
                    }
                    break;

                case "nord":
                case "n":
                    move("nord");
                    break;
                case "sud":
                case "s":
                    move("sud");
                    break;
                case "est":
                case "e":
                    move("est");
                    break;
                case "ouest":
                case "o":
                case "w":
                    move("ouest");
                    break;

                case "attaquer":
                case "a":
                case "combat":
                    combat();
                    break;

                case "inventaire":
                case "i":
                case "inv":
                    showInventory();
                    break;

                case "utiliser":
                case "use":
                    if (argument.isEmpty()) {
                        System.out.println("❌ Spécifiez l'objet à utiliser");
                    } else {
                        useItem(argument);
                    }
                    break;

                case "ramasser":
                case "loot":
                case "piller":
                    lootRoom();
                    break;

                case "repos":
                case "rest":
                    rest();
                    break;

                case "sauvegarder":
                case "save":
                    saveGame();
                    break;

                case "quitter":
                case "quit":
                case "q":
                case "exit":
                    quit();
                    break;

                case "aide":
                case "help":
                case "h":
                    displayAvailableCommands();
                    break;

                default:
                    System.out.println("❌ Commande inconnue: " + command);
                    System.out.println("   Tapez 'aide' pour voir les commandes disponibles");
            }
        } catch (Exception e) {
            Logger.logError("Error processing command: " + input, e);
            System.out.println("❌ Erreur lors de l'exécution de la commande: " + e.getMessage());
        }
    }

    /**
     * Déplace le joueur dans une direction.
     *
     * @param direction la direction
     */
    private void move(String direction) {
        if (currentRoom.hasAliveEnemies()) {
            System.out.println("❌ Vous ne pouvez pas fuir avec des ennemis vivants!");
            return;
        }

        Room nextRoom = currentRoom.getRoomInDirection(direction);

        if (nextRoom == null) {
            System.out.println("❌ Il n'y a pas de sortie dans cette direction.");
            return;
        }

        currentRoom.onExit(player);
        currentRoom = nextRoom;
        currentRoom.onEnter(player);
    }

    /**
     * Lance un combat.
     */
    private void combat() {
        if (!currentRoom.hasAliveEnemies()) {
            System.out.println("❌ Il n'y a pas d'ennemis à combattre ici.");
            return;
        }

        // Obtenir le premier ennemi vivant
        Optional<Creature> enemyOpt = currentRoom.getCreatures().stream()
            .filter(Creature::isAlive)
            .findFirst();

        if (enemyOpt.isPresent()) {
            Creature enemy = enemyOpt.get();
            System.out.println("\n⚔️  Combat contre " + enemy.getName() + "!");

            boolean playerWon = combatSystem.startCombat(player, enemy, scanner);

            if (playerWon) {
                System.out.println("\n✓ Victoire! " + enemy.getName() + " a été vaincu!");

                // Loot si c'est un monstre
                if (enemy instanceof Monster) {
                    Monster monster = (Monster) enemy;
                    List<Item> loot = monster.getLoot();
                    if (!loot.isEmpty()) {
                        System.out.println("\n💰 Butin obtenu:");
                        loot.forEach(item -> {
                            currentRoom.addItem(item);
                            System.out.println("   + " + item.getName());
                        });
                    }
                }

                currentRoom.checkIfCleared();
            } else {
                // Le joueur est mort, géré dans gameLoop
            }
        }
    }

    /**
     * Affiche l'inventaire.
     */
    private void showInventory() {
        player.getInventory().display();
    }

    /**
     * Utilise un objet de l'inventaire.
     *
     * @param itemName le nom de l'objet
     */
    private void useItem(String itemName) {
        List<Item> items = player.getInventory().getItems();

        Optional<Item> itemOpt = items.stream()
            .filter(item -> item.getName().toLowerCase().contains(itemName.toLowerCase()))
            .findFirst();

        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                if (!potion.isConsumed()) {
                    potion.useOn(player);
                    System.out.println("✓ " + potion.getName() + " utilisée!");
                } else {
                    System.out.println("❌ Cette potion a déjà été utilisée.");
                }
            } else if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                weapon.use();
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                armor.use();
            } else {
                item.use();
            }
        } else {
            System.out.println("❌ Objet non trouvé: " + itemName);
        }
    }

    /**
     * Ramasse les objets dans la salle.
     */
    private void lootRoom() {
        if (currentRoom.getItems().isEmpty()) {
            System.out.println("❌ Il n'y a rien à ramasser ici.");
            return;
        }

        System.out.println("\n💰 Objets disponibles:");
        List<Item> items = new ArrayList<>(currentRoom.getItems());
        for (int i = 0; i < items.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + items.get(i).getName());
        }

        System.out.print("\nChoisissez un objet (numéro) ou 'tout' pour tout ramasser: ");
        String choice = scanner.nextLine().trim();

        if ("tout".equalsIgnoreCase(choice) || "all".equalsIgnoreCase(choice)) {
            for (Item item : items) {
                try {
                    player.getInventory().addItem(item);
                    currentRoom.removeItem(item);
                    System.out.println("✓ " + item.getName() + " ajouté à l'inventaire");
                } catch (InventoryFullException e) {
                    System.out.println("❌ Inventaire plein! " + item.getName() + " laissé au sol.");
                }
            }
        } else {
            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < items.size()) {
                    Item item = items.get(index);
                    player.getInventory().addItem(item);
                    currentRoom.removeItem(item);
                    System.out.println("✓ " + item.getName() + " ajouté à l'inventaire");
                } else {
                    System.out.println("❌ Numéro invalide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrée invalide.");
            } catch (InventoryFullException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    /**
     * Se repose dans une salle de repos.
     */
    private void rest() {
        if (currentRoom instanceof RestRoom) {
            RestRoom restRoom = (RestRoom) currentRoom;
            restRoom.rest(player);
        } else {
            System.out.println("❌ Vous ne pouvez pas vous reposer ici.");
        }
    }

    /**
     * Sauvegarde la partie.
     */
    private void saveGame() {
        try {
            SaveManager.saveGame(player, SAVE_PATH);
            System.out.println("✓ Partie sauvegardée avec succès!");
        } catch (IOException e) {
            Logger.logError("Failed to save game", e);
            System.out.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    /**
     * Quitte le jeu.
     */
    private void quit() {
        System.out.print("\nVoulez-vous sauvegarder avant de quitter? (o/n): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("o".equals(response) || "oui".equals(response) || "y".equals(response) || "yes".equals(response)) {
            saveGame();
        }

        System.out.println("\n👋 Merci d'avoir joué! À bientôt!");
        isRunning = false;
    }

    /**
     * Gère la fin du jeu (game over).
     */
    private void gameOver() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║            GAME OVER                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n💀 " + player.getName() + " est tombé au combat...");
        System.out.println("   Le donjon a eu raison de vous cette fois.");
        Logger.logInfo("Game over for player: " + player.getName());
    }

    /**
     * Nettoie les ressources avant de fermer.
     */
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
        Logger.logInfo("Game engine shut down");
    }

    // Getters pour les tests

    public Player getPlayer() {
        return player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
