package org.example.inventory;

import org.example.model.Item;

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