package org.example.exceptions;

/**
 * Exception levée lorsqu'on tente d'ajouter un item à un inventaire plein.
 * Fournit des informations sur la capacité actuelle et maximale.
 */
public class InventoryFullException extends Exception {
    private final int currentSize;
    private final int maxCapacity;
    private final String itemName;

    /**
     * Constructeur simple avec message uniquement
     * @param message Le message d'erreur
     */
    public InventoryFullException(String message) {
        super(message);
        this.currentSize = -1;
        this.maxCapacity = -1;
        this.itemName = null;
    }

    /**
     * Constructeur avec capacité actuelle et maximale
     * @param currentSize Nombre d'items actuellement dans l'inventaire
     * @param maxCapacity Capacité maximale de l'inventaire
     */
    public InventoryFullException(int currentSize, int maxCapacity) {
        super(buildMessage(currentSize, maxCapacity, null));
        this.currentSize = currentSize;
        this.maxCapacity = maxCapacity;
        this.itemName = null;
    }

    /**
     * Constructeur complet avec l'item qu'on tentait d'ajouter
     * @param currentSize Nombre d'items actuellement dans l'inventaire
     * @param maxCapacity Capacité maximale de l'inventaire
     * @param itemName Nom de l'item qu'on tentait d'ajouter
     */
    public InventoryFullException(int currentSize, int maxCapacity, String itemName) {
        super(buildMessage(currentSize, maxCapacity, itemName));
        this.currentSize = currentSize;
        this.maxCapacity = maxCapacity;
        this.itemName = itemName;
    }

    /**
     * Construit un message d'erreur détaillé
     */
    private static String buildMessage(int currentSize, int maxCapacity, String itemName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventaire plein! ");

        if (currentSize >= 0 && maxCapacity >= 0) {
            sb.append("(").append(currentSize).append("/").append(maxCapacity).append(")");
        }

        if (itemName != null) {
            sb.append("\nImpossible d'ajouter: ").append(itemName);
            sb.append("\nLibérez de l'espace avant d'ajouter de nouveaux items.");
        }

        return sb.toString();
    }

    /**
     * @return Le nombre d'items actuellement dans l'inventaire
     */
    public int getCurrentSize() {
        return currentSize;
    }

    /**
     * @return La capacité maximale de l'inventaire
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * @return Le nom de l'item qu'on tentait d'ajouter (peut être null)
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return Le nombre d'emplacements disponibles
     */
    public int getAvailableSlots() {
        if (currentSize < 0 || maxCapacity < 0) {
            return 0;
        }
        return Math.max(0, maxCapacity - currentSize);
    }

    /**
     * @return true si l'inventaire est complètement plein
     */
    public boolean isCompletelyFull() {
        return currentSize >= 0 && maxCapacity >= 0 && currentSize >= maxCapacity;
    }
}
