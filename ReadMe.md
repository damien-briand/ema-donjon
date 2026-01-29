Parfait ! On reste full CLI pour l'instant. Voici un **plan d'action concret** pour démarrer :

## 🚀 Setup Initial (15 min)

### 1. Créer le projet Maven

```bash
mvn archetype:generate -DgroupId=com.rpg -DartifactId=dungeon-crawler -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
cd dungeon-crawler
```

### 2. Modifier `pom.xml`

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.rpg</groupId>
    <artifactId>dungeon-crawler</artifactId>
    <version>1.0</version>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>
</project>
```

## 📋 Code Starter (Phase 1 - H0 à H2)

### `model/Creature.java` (Classe abstraite)

```java
package com.rpg.model;

import com.rpg.interfaces.Attackable;
import com.rpg.interfaces.Healable;

public abstract class Creature implements Attackable, Healable {
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int attackPower;
    
    public Creature(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }
    
    @Override
    public void attack(Attackable target) {
        int damage = calculateDamage();
        target.takeDamage(damage);
        System.out.println(name + " attaque pour " + damage + " dégâts!");
    }
    
    @Override
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        System.out.println(name + " reçoit " + damage + " dégâts! HP: " + health);
    }
    
    @Override
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
        System.out.println(name + " récupère " + amount + " HP! HP: " + health);
    }
    
    @Override
    public boolean isAlive() {
        return health > 0;
    }
    
    protected abstract int calculateDamage(); // Polymorphisme!
    
    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
}
```

### `interfaces/Attackable.java`

```java
package com.rpg.interfaces;

public interface Attackable {
    void attack(Attackable target);
    void takeDamage(int damage);
    boolean isAlive();
}
```

### `interfaces/Healable.java`

```java
package com.rpg.interfaces;

public interface Healable {
    void heal(int amount);
}
```

### `interfaces/Lootable.java`

```java
package com.rpg.interfaces;

import com.rpg.model.Item;
import java.util.List;

public interface Lootable {
    List<Item> getLoot();
}
```

### `model/Item.java` (Classe abstraite)

```java
package com.rpg.model;

public abstract class Item {
    protected String name;
    protected String description;
    
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public abstract void use(); // Polymorphisme
    
    public String getName() { return name; }
    public String getDescription() { return description; }
}
```

### `inventory/Inventory.java` (Générique ✨)

```java
package com.rpg.inventory;

import com.rpg.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory<T extends Item> {
    private final List<T> items;
    private final int maxSize;
    
    public Inventory(int maxSize) {
        this.items = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    public void addItem(T item) throws InventoryFullException {
        if (items.size() >= maxSize) {
            throw new InventoryFullException("Inventaire plein! (" + maxSize + ")");
        }
        items.add(item);
        System.out.println("✓ " + item.getName() + " ajouté à l'inventaire");
    }
    
    public void removeItem(T item) {
        items.remove(item);
    }
    
    // Méthode générique avec réflexion ✨
    public <U extends T> List<U> findItemsByType(Class<U> type) {
        return items.stream()
                .filter(type::isInstance) // Fonctionnel ✨
                .map(type::cast)
                .collect(Collectors.toList());
    }
    
    public List<T> getItems() {
        return new ArrayList<>(items);
    }
    
    public void display() {
        System.out.println("\n=== INVENTAIRE ===");
        items.forEach(item -> 
            System.out.println("- " + item.getName() + ": " + item.getDescription())
        ); // Fonctionnel ✨
    }
}
```

### `exceptions/InventoryFullException.java`

```java
package com.rpg.exceptions;

public class InventoryFullException extends Exception {
    public InventoryFullException(String message) {
        super(message);
    }
}
```

### `exceptions/InsufficientManaException.java`

```java
package com.rpg.exceptions;

public class InsufficientManaException extends Exception {
    private final int required;
    private final int available;
    
    public InsufficientManaException(int required, int available) {
        super("Mana insuffisant! Requis: " + required + ", Disponible: " + available);
        this.required = required;
        this.available = available;
    }
    
    public int getRequired() { return required; }
    public int getAvailable() { return available; }
}
```

## 🎯 Prochaines Étapes (H2-H4)

**Phase 2** : Je te prépare le code pour :
1. `Player.java` avec système de mana
2. `Monster.java` avec loot
3. `CombatSystem.java` avec lambdas pour modificateurs de dégâts
4. Items concrets (Potion, Weapon, Armor)

**Phase 3** : GameEngine avec chargement JSON + réflexion

Tu veux que je continue avec la Phase 2 maintenant, ou tu préfères d'abord tester ce code de base ?