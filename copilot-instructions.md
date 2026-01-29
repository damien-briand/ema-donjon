# Ema Donjon - Context for GitHub Copilot

## Project Overview
A Java-based D&D-style dungeon crawler RPG with CLI interface.
Backend-focused project demonstrating OOP principles, generics, exceptions, reflection, and functional programming.

**Development Time:** 7 hours total | **Group Size:** Individual/Flexible

## Tech Stack
- **Java 17+**
- **Maven** (build tool)
- **Jackson 3.0.4** (`tools.jackson.core:jackson-databind:3.0.4`)
- **CLI Interface** (Scanner-based, no GUI)
- **Logging:** Java Util Logging (JUL) or SLF4J Simple

## Project Structure
```
src/main/java/com/rpg/
├── Main.java                    # Entry point with CLI
├── model/
│   ├── Creature.java           # Abstract class
│   ├── Player.java             # Extends Creature
│   ├── Monster.java            # Extends Creature, implements Lootable
│   ├── Item.java               # Abstract class
│   ├── Potion.java             # Extends Item
│   ├── Weapon.java             # Extends Item
│   ├── Armor.java              # Extends Item
│   └── Room.java               # Abstract class
├── interfaces/
│   ├── Attackable.java         # Combat interface
│   ├── Healable.java           # Healing interface
│   └── Lootable.java           # Loot drops interface
├── inventory/
│   └── Inventory.java          # Generic class <T extends Item>
├── exceptions/
│   ├── InsufficientManaException.java  # Custom exception 
│   ├── InvalidMoveException.java       # Custom exception 
│   ├── InvalidCommandException.java    # Custom exception 
│   └── InventoryFullException.java     # Custom exception 
├── engine/
│   ├── GameEngine.java         # Uses reflection 
│   ├── CombatSystem.java       # Uses functional programming 
│   └── SaveManager.java        # Data persistence 
└── util/
    ├── JsonLoader.java         # Jackson utilities
    └── Logger.java             # Error logging to file 

src/main/resources/
├── monsters.json               # Monster configurations (persistence)
├── items.json                  # Item database (persistence)
└── saves/                      # Player save files (persistence)

logs/
└── game.log                    # Error and event logging
```

---

## 📋 CAHIER DES CHARGES (REQUIREMENTS FROM PROFESSOR)

### 1. User Interaction ✅
**Requirement:** The project must have a way to interact with users

**Implementation:** CLI (Command Line Interface)
- Use `Scanner` for user input
- Display clear menus and prompts in French
- Handle invalid commands gracefully with error messages
- **DO NOT CRASH** on incorrect input
- Validate all user inputs before processing

```java
    // Example pattern
    Scanner scanner = new Scanner(System.in);
    try {
        String command = scanner.nextLine().trim().toLowerCase();
        validateCommand(command); // Throws InvalidCommandException
        executeCommand(command);
    } catch (InvalidCommandException e) {
        System.out.println("❌ Commande invalide: " + e.getMessage());
        Logger.logError("Invalid command", e);
    }
```

### 2. Data Persistence ✅
**Requirement:** The project must manage data persistence (database or files)

**Implementation:** JSON files with Jackson 3.0.4
- **Monster database:** `src/main/resources/monsters.json`
- **Item database:** `src/main/resources/items.json`
- **Save games:** `src/main/resources/saves/player_save.json`
- **Configuration:** `src/main/resources/config.json`
```java
// Save game state
SaveManager.saveGame(player, "saves/player_save.json");

// Load game state
Player player = SaveManager.loadGame("saves/player_save.json");
```

### 3. Error Handling ✅
**Requirement:** The project must handle errors and unexpected cases correctly

**Implementation:**
1. **Display user-friendly error messages**
```java
   System.out.println("❌ Erreur: " + e.getMessage());
```

2. **Log all errors to a file**
```java
   Logger.logError("Error during combat", exception);
   // Writes to logs/game.log with timestamp
```

3. **Never crash** - wrap critical sections in try-catch
4. **Provide context** in error messages for debugging

---

## 🎓 COURSE ELEMENTS (MANDATORY)

### 1. Object-Oriented Programming ✅

#### Classes
- Use classes for all game entities
- Organize code logically by responsibility

#### Encapsulation ✅
- **All fields MUST be `private` or `protected`**
- Provide public getters/setters only when needed
- Hide implementation details
```java
public class Player extends Creature {
    private int mana;              // ✅ private
    private Inventory<Item> inventory; // ✅ private
    
    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = mana; }
}
```

#### Abstract Classes (minimum 1) ✅
- `Creature` (abstract)
- `Item` (abstract)
- `Room` (abstract)
```java
public abstract class Creature {
    protected abstract int calculateDamage(); // Force implementation
}
```

#### Interfaces (as many as needed) ✅
- `Attackable` - entities that can attack/be attacked
- `Healable` - entities that can heal
- `Lootable` - entities that drop items

### 2. Generics ✅

**Requirement:** Use at least one generic method or class

**Implementation:**
```java
public class Inventory<T extends Item> {
    private List<T> items;
    
    // Generic method with reflection ✅
    public <U extends T> List<U> findItemsByType(Class<U> type) {
        return items.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .collect(Collectors.toList());
    }
}
```

### 3. Exceptions ✅

**Requirement:** Create at least one custom exception + handle as many error cases as possible

**Custom Exceptions:**
```java
public class InsufficientManaException extends Exception {
    private final int required;
    private final int available;
    
    public InsufficientManaException(int required, int available) {
        super("Mana insuffisant! Requis: " + required + ", Disponible: " + available);
        this.required = required;
        this.available = available;
    }
    
    // Getters for debugging
    public int getRequired() { return required; }
    public int getAvailable() { return available; }
}
```

**Error Cases to Handle:**
- Invalid user commands → `InvalidCommandException`
- Insufficient mana for spells → `InsufficientManaException`
- Invalid moves in dungeon → `InvalidMoveException`
- Full inventory → `InventoryFullException`
- File not found → `IOException` (built-in)
- JSON parsing errors → `JsonProcessingException` (Jackson)
- Reflection failures → `ClassNotFoundException`, `NoSuchMethodException`

**Clean Exception Handling:**
```java
try {
    Monster monster = loadMonster("Goblin");
} catch (ClassNotFoundException e) {
    Logger.logError("Monster class not found", e);
    System.out.println("❌ Type de monstre inconnu: " + e.getMessage());
} catch (IOException e) {
    Logger.logError("Failed to load monster data", e);
    System.out.println("❌ Erreur de chargement: " + e.getMessage());
}
```

### 4. Reflection ✅

**Requirement:** Use reflection at least once

**Implementation:** Dynamic monster loading from JSON
```java
public Monster loadMonsterByType(String monsterType, String name, int health, int attack) 
        throws ClassNotFoundException, NoSuchMethodException, 
               InvocationTargetException, InstantiationException, IllegalAccessException {
    
    // Load class dynamically using reflection ✅
    Class<?> monsterClass = Class.forName("com.rpg.model." + monsterType);
    
    // Get constructor
    Constructor<?> constructor = monsterClass.getDeclaredConstructor(
        String.class, int.class, int.class
    );
    
    // Create instance dynamically
    return (Monster) constructor.newInstance(name, health, attack);
}
```

**Usage example:**
```json
{
  "type": "Dragon",
  "name": "Fire Drake",
  "health": 200,
  "attackPower": 50
}
```

### 5. Functional Programming ✅

**Requirement:** Use at least one functional programming concept

**Implementation:**

1. **Streams** for inventory filtering
```java
List<Potion> healthPotions = inventory.getItems().stream()
    .filter(item -> item instanceof Potion)
    .map(item -> (Potion) item)
    .filter(potion -> potion.getHealAmount() > 20)
    .collect(Collectors.toList());
```

2. **Lambdas** for damage calculation
```java
// Damage modifier as lambda
Function<Integer, Integer> criticalHit = damage -> damage * 2;
Function<Integer, Integer> weakAttack = damage -> damage / 2;

int finalDamage = criticalHit.apply(baseDamage);
```

3. **Method references**
```java
inventory.getItems().forEach(System.out::println);
items.stream().filter(Item::isUsable).forEach(Item::use);
```

4. **Optional** for safe operations
```java
Optional<Weapon> bestWeapon = inventory.findItemsByType(Weapon.class).stream()
    .max(Comparator.comparing(Weapon::getDamageBonus));

bestWeapon.ifPresent(weapon -> 
    System.out.println("Meilleure arme: " + weapon.getName())
);
```

5. **Collectors** for aggregations
```java
Map<String, List<Item>> itemsByType = inventory.getItems().stream()
    .collect(Collectors.groupingBy(item -> item.getClass().getSimpleName()));
```

---

## Jackson 3.0.4 Specifics

### ⚠️ CRITICAL: Correct Imports
```java
import tools.jackson.databind.ObjectMapper;         // NOT com.fasterxml
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.annotation.*;
```

### Requirements for Jackson
- Every model class MUST have a **no-arg constructor**
- Every model class MUST have **getters and setters**
- Use `ObjectMapper` for JSON parsing

---

## Code Style Conventions

### Naming
- Classes: `PascalCase`
- Methods/variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: `lowercase`

### Language
- **Comments:** French
- **Variable names:** English
- **User output:** French
- **Logs:** English (for debugging)

### Output Format
```java
System.out.println("╔════════════════════════╗");
System.out.println("║   DUNGEON CRAWLER RPG  ║");
System.out.println("╚════════════════════════╝");
System.out.println("✓ Action réussie");
System.out.println("❌ Erreur détectée");
System.out.println("⚔️  Combat en cours");
System.out.println("💰 Butin obtenu");
```

---

## Logging Implementation

### Logger Utility
```java
public class Logger {
    private static final String LOG_FILE = "logs/game.log";
    
    public static void logError(String message, Exception e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.printf("[%s] ERROR: %s - %s%n", 
                LocalDateTime.now(), 
                message, 
                e.getMessage()
            );
            e.printStackTrace(pw);
        } catch (IOException ex) {
            System.err.println("Failed to write to log file");
        }
    }
    
    public static void logInfo(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.printf("[%s] INFO: %s%n", LocalDateTime.now(), message);
        } catch (IOException ex) {
            System.err.println("Failed to write to log file");
        }
    }
}
```

---

## Input Validation Pattern

### Always Validate User Input
```java
public void processCommand(String input) {
    try {
        // 1. Validate
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("Commande vide");
        }
        
        String command = input.trim().toLowerCase();
        
        // 2. Parse
        String[] parts = command.split("\\s+");
        String action = parts[0];
        
        // 3. Execute
        switch (action) {
            case "attaquer", "attack" -> combat();
            case "inventaire", "inv" -> showInventory();
            case "utiliser", "use" -> useItem(parts);
            case "quitter", "quit" -> exit();
            default -> throw new InvalidCommandException("Commande inconnue: " + action);
        }
        
    } catch (InvalidCommandException e) {
        System.out.println("❌ " + e.getMessage());
        Logger.logError("Invalid command", e);
        displayHelp(); // Show available commands
    } catch (Exception e) {
        System.out.println("❌ Erreur inattendue: " + e.getMessage());
        Logger.logError("Unexpected error", e);
    }
}
```

---

## DO's and DON'Ts

### ✅ DO:
- Use Java 17 features (records, switch expressions, text blocks)
- Handle **ALL** exceptions gracefully
- Log every error to file
- Validate all user inputs
- Use streams and lambdas for collections
- Save game state regularly
- Provide clear error messages to users
- Use reflection for dynamic loading
- Implement proper encapsulation

### ❌ DON'T:
- Don't crash on invalid input
- Don't use raw types (use generics)
- Don't ignore exceptions
- Don't use `System.exit()` in game logic (except Main)
- Don't hardcode file paths (use constants)
- Don't write unit tests (time constraint)
- Don't expose internal fields (use getters/setters)

---

## Data Persistence Examples

### Save Game State
```java
public class SaveManager {
    public static void saveGame(Player player, String filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(new File(filepath), player);
        Logger.logInfo("Game saved: " + filepath);
    }
    
    public static Player loadGame(String filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Player player = mapper.readValue(new File(filepath), Player.class);
        Logger.logInfo("Game loaded: " + filepath);
        return player;
    }
}
```

---

## Time Management (7h total)

| Phase | Time | Tasks |
|-------|------|-------|
| **Setup** | 0:15 | Maven project, dependencies |
| **Phase 1** | 0:00-2:00 | Base classes, interfaces, inventory, exceptions |
| **Phase 2** | 2:00-4:00 | Player, Monster, Items, CombatSystem, Logger |
| **Phase 3** | 4:00-6:00 | GameEngine with reflection, SaveManager, CLI loop |
| **Phase 4** | 6:00-7:00 | Testing, bug fixes, polish, documentation |

**Priority:** Core functionality > Polish
**No unit tests required** (time constraint)

---

## Checklist Before Submission

### Cahier des Charges ✅
- [ ] User interaction (CLI with input validation)
- [ ] Data persistence (JSON files for monsters/items/saves)
- [ ] Error handling (try-catch everywhere)
- [ ] Error logging (to file)

### Course Elements ✅
- [ ] Classes used throughout
- [ ] Proper encapsulation (private/protected fields)
- [ ] At least one abstract class (Creature, Item, Room)
- [ ] Interfaces as needed (Attackable, Healable, Lootable)
- [ ] At least one generic class/method (Inventory)
- [ ] Custom exceptions (InsufficientManaException, etc.)
- [ ] Exception handling everywhere
- [ ] Reflection used (dynamic monster loading)
- [ ] Functional programming (Streams, lambdas, method references)

---

## Quick Reference: Jackson 3.0.4
```java
// Import
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

// Read JSON
ObjectMapper mapper = new ObjectMapper();
Monster monster = mapper.readValue(file, Monster.class);
List<Monster> monsters = mapper.readValue(file, new TypeReference<List<Monster>>(){});

// Write JSON
mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
```

---

**Current Phase:** Setting up base architecture
**Next:** Implement core game loop with CLI