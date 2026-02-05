package org.example.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.exceptions.InventoryFullException;
import org.example.model.Item;
import org.example.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record Inventory<T extends Item>(@JsonProperty("items") List<T> items,
                                        @JsonProperty("maxCapacity") int maxSize) {
    @JsonCreator
    public Inventory(@JsonProperty("items") List<T> items,
                     @JsonProperty("maxCapacity") int maxSize) {
        this.items = (items != null) ? items : new ArrayList<>();
        this.maxSize = maxSize;
    }

    public Inventory(int maxSize) {
        this(new ArrayList<>(), maxSize);
    }

    public void addItem(T item) throws InventoryFullException {
        if (items.size() >= maxSize) {
            throw new InventoryFullException("Inventaire plein! (" + maxSize + ")");
        }
        items.add(item);
        Logger.logInfo("✓ " + item.getName() + " ajouté à l'inventaire");
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    // Méthode générique avec réflexion
    public <U extends T> List<U> findItemsByType(Class<U> type) {
        return items.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> items() {
        return new ArrayList<>(items);
    }

    public void display() {
        Logger.logInfo("\n=== INVENTAIRE ===");
        items.forEach(item ->
                Logger.logInfo("- " + item.getName() + ": " + item.getDescription())
        );
    }

    public boolean dropItem(T item) {
        if (items.remove(item)) {
            Logger.logInfo("✓ " + item.getName() + " retiré de l'inventaire");
            return true;
        }
        Logger.logWarning("❌ " + item.getName() + " non trouvé dans l'inventaire");
        return false;
    }

    public T getItemByIndex(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public int size() {
        return items.size();
    }

    @Override
    public int maxSize() {
        return maxSize;
    }
}