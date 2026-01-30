# Guide d'utilisation des exceptions personnalisées

## 1. InvalidCommandException

### Utilisation simple
```java
throw new InvalidCommandException("Commande vide");
```

### Utilisation avec suggestions
```java
String userCommand = "attque"; // Typo
List<String> validCommands = Arrays.asList("attaquer", "inventaire", "utiliser", "quitter");
throw new InvalidCommandException(userCommand, validCommands);
// Output: Commande invalide: 'attque'
//         Commandes disponibles: attaquer, inventaire, utiliser, quitter
```

### Utilisation dans GameEngine
```java
public void processCommand(String input) {
    try {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("Commande vide");
        }
        
        String command = input.trim().toLowerCase();
        
        if (!VALID_COMMANDS.contains(command)) {
            throw new InvalidCommandException(command, VALID_COMMANDS);
        }
        
        executeCommand(command);
        
    } catch (InvalidCommandException e) {
        Logger.logError("Invalid command", e);
        System.out.println("❌ " + e.getMessage());
        
        if (e.hasSuggestions()) {
            System.out.println("Essayez: " + String.join(", ", e.getSuggestedCommands()));
        }
    }
}
```

---

## 2. InvalidMoveException

### Utilisation simple
```java
throw new InvalidMoveException("Aucune sortie dans cette direction");
```

### Utilisation avec directions disponibles
```java
String direction = "est";
List<String> available = Arrays.asList("nord", "sud");
throw new InvalidMoveException(direction, available);
// Output: impossible d'aller vers 'est'
//         Directions disponibles: nord, sud
```

### Utilisation complète avec nom de salle
```java
String direction = "ouest";
List<String> available = Arrays.asList("nord", "est");
String currentRoom = "Salle des Trésors";
throw new InvalidMoveException(direction, available, currentRoom);
// Output: Depuis 'Salle des Trésors', impossible d'aller vers 'ouest'
//         Directions disponibles: nord, est
```

### Utilisation dans GameEngine
```java
public void movePlayer(String direction) {
    try {
        Room currentRoom = player.getCurrentRoom();
        
        if (!currentRoom.hasExit(direction)) {
            throw new InvalidMoveException(
                direction,
                currentRoom.getAvailableExits(),
                currentRoom.getName()
            );
        }
        
        Room nextRoom = currentRoom.getExit(direction);
        player.moveTo(nextRoom);
        Logger.logInfo("Player moved to: " + nextRoom.getName());
        
    } catch (InvalidMoveException e) {
        Logger.logError("Invalid move", e);
        System.out.println("❌ " + e.getMessage());
        
        if (e.hasAvailableDirections()) {
            System.out.println("💡 Directions disponibles: " + 
                String.join(", ", e.getAvailableDirections()));
        }
    }
}
```

---

## 3. InventoryFullException

### Utilisation simple
```java
throw new InventoryFullException("Inventaire plein!");
```

### Utilisation avec capacité
```java
int current = 10;
int max = 10;
throw new InventoryFullException(current, max);
// Output: Inventaire plein! (10/10)
```

### Utilisation complète avec item
```java
int current = 10;
int max = 10;
String itemName = "Épée de feu";
throw new InventoryFullException(current, max, itemName);
// Output: Inventaire plein! (10/10)
//         Impossible d'ajouter: Épée de feu
//         Libérez de l'espace avant d'ajouter de nouveaux items.
```

### Utilisation dans Inventory
```java
public class Inventory<T extends Item> {
    private List<T> items;
    private int maxCapacity;
    
    public void addItem(T item) throws InventoryFullException {
        if (items.size() >= maxCapacity) {
            throw new InventoryFullException(
                items.size(),
                maxCapacity,
                item.getName()
            );
        }
        
        items.add(item);
        Logger.logInfo("Item added: " + item.getName());
    }
}
```

### Gestion dans le code client
```java
try {
    player.getInventory().addItem(newSword);
    System.out.println("✓ Item ajouté: " + newSword.getName());
    
} catch (InventoryFullException e) {
    Logger.logError("Inventory full", e);
    System.out.println("❌ " + e.getMessage());
    
    if (e.isCompletelyFull()) {
        System.out.println("💡 Supprimez un item pour faire de la place.");
        System.out.println("   Emplacements disponibles: " + e.getAvailableSlots());
    }
}
```

---

## 4. InsufficientManaException (déjà existante)

### Utilisation
```java
public void castSpell(int manaCost) throws InsufficientManaException {
    if (currentMana < manaCost) {
        throw new InsufficientManaException(manaCost, currentMana);
        // Output: Mana insuffisant! Requis: 50, Disponible: 30
    }
    
    currentMana -= manaCost;
    // Cast spell...
}
```

### Gestion
```java
try {
    player.castSpell(50);
    System.out.println("✨ Sort lancé avec succès!");
    
} catch (InsufficientManaException e) {
    Logger.logError("Not enough mana", e);
    System.out.println("❌ " + e.getMessage());
    System.out.println("💡 Il vous manque " + 
        (e.getRequired() - e.getAvailable()) + " points de mana");
}
```

---

## Résumé des avantages

### 1. Messages d'erreur détaillés
- Contexte complet de l'erreur
- Suggestions automatiques
- Informations pour le debugging

### 2. Getters pour le debugging
- Accès aux détails de l'erreur
- Possibilité de créer des logs détaillés
- Facilite les tests unitaires

### 3. Méthodes utilitaires
- `hasSuggestions()` → vérifie si des suggestions existent
- `hasAvailableDirections()` → vérifie si des directions sont disponibles
- `getAvailableSlots()` → calcule les emplacements libres
- `isCompletelyFull()` → vérifie si l'inventaire est plein

### 4. Flexibilité
- Plusieurs constructeurs pour différents cas d'usage
- Messages générés automatiquement ou personnalisés
- Compatible avec Logger pour tracer les erreurs

---

## Pattern recommandé pour tous les cas

```java
try {
    // Action risquée
    performRiskyAction();
    
} catch (CustomException e) {
    // 1. Logger l'erreur
    Logger.logError("Description du contexte", e);
    
    // 2. Afficher le message à l'utilisateur
    System.out.println("❌ " + e.getMessage());
    
    // 3. Fournir des suggestions si disponibles
    if (e.hasSuggestions()) {
        System.out.println("💡 Suggestion: " + e.getSuggestion());
    }
    
    // 4. Optionnel: réessayer ou action alternative
}
```
