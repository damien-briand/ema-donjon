package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant une salle dans le donjon.
 * Chaque salle peut contenir des monstres, des objets, et des connexions vers d'autres salles.
 */
public abstract class Room {
    protected String name;
    protected String description;
    protected List<Item> items;
    protected List<Creature> creatures;
    protected boolean isVisited;
    protected boolean isCleared;

    // Connexions vers d'autres salles (nord, sud, est, ouest)
    protected Room northRoom;
    protected Room southRoom;
    protected Room eastRoom;
    protected Room westRoom;

    // Constructeur sans-arg requis pour Jackson
    public Room() {
        this.items = new ArrayList<>();
        this.creatures = new ArrayList<>();
        this.isVisited = false;
        this.isCleared = false;
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param name        nom de la salle
     * @param description description de la salle
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.creatures = new ArrayList<>();
        this.isVisited = false;
        this.isCleared = false;
    }

    /**
     * Méthode abstraite appelée quand le joueur entre dans la salle.
     * Chaque type de salle définit son propre comportement.
     *
     * @param player le joueur qui entre dans la salle
     */
    public abstract void onEnter(Player player);

    /**
     * Méthode abstraite appelée quand le joueur quitte la salle.
     *
     * @param player le joueur qui quitte la salle
     */
    public abstract void onExit(Player player);

    /**
     * Affiche la description de la salle.
     */
    public void displayDescription() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ " + name);
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println(description);

        if (!isVisited) {
            System.out.println("✨ Vous découvrez cette salle pour la première fois.");
            isVisited = true;
        }

        if (isCleared) {
            System.out.println("✓ Cette salle a déjà été nettoyée.");
        }

        // Afficher l'ASCII art de la salle
        displayRoomAsciiArt();

        // Afficher les créatures
        if (!creatures.isEmpty()) {
            System.out.println("\n⚔️  Créatures présentes:");
            creatures.stream()
                .filter(Creature::isAlive)
                .forEach(c -> System.out.println("  - " + c.getName() + " (HP: " + c.getHealth() + ")"));
        }

        // Afficher les objets
        if (!items.isEmpty()) {
            System.out.println("\n💰 Objets au sol:");
            items.forEach(item -> System.out.println("  - " + item.getName()));
        }

        // Afficher les sorties disponibles
        displayExits();
    }

    /**
     * Affiche les sorties disponibles de la salle.
     */
    public void displayExits() {
        System.out.println("\n🚪 Sorties disponibles:");
        List<String> exits = new ArrayList<>();

        if (northRoom != null) exits.add("Nord");
        if (southRoom != null) exits.add("Sud");
        if (eastRoom != null) exits.add("Est");
        if (westRoom != null) exits.add("Ouest");

        if (exits.isEmpty()) {
            System.out.println("  Aucune sortie (salle finale?)");
        } else {
            exits.forEach(exit -> System.out.println("  - " + exit));
        }
    }

    /**
     * Affiche un ASCII art représentant la salle actuelle avec ses portes.
     * Les salles visitées sont en vert, les non visitées en blanc.
     */
    public void displayRoomAsciiArt() {
        // Couleurs ANSI
        String GREEN = "\u001B[32m";
        String RESET = "\u001B[0m";
        String YELLOW = "\u001B[33m";

        String color = isVisited ? GREEN : RESET;
        String currentMarker = YELLOW + "●" + RESET; // Marqueur pour la salle actuelle

        System.out.println("\n📍 Position actuelle:");

        // Ligne du haut (porte nord)
        if (northRoom != null) {
            System.out.println("   " + color + "|" + RESET);
        } else {
            System.out.println("    ");
        }

        // Ligne du milieu (salle + portes est/ouest)
        String westDoor = westRoom != null ? color + "-" + RESET : " ";
        String eastDoor = eastRoom != null ? color + "-" + RESET : " ";
        String room = color + "|" + currentMarker + "|" + RESET;

        System.out.println(westDoor + room + eastDoor);

        // Ligne du bas (porte sud)
        if (southRoom != null) {
            System.out.println("   " + color + "|" + RESET);
        } else {
            System.out.println("    ");
        }
    }

    /**
     * Ajoute un objet dans la salle.
     *
     * @param item l'objet à ajouter
     */
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * Retire un objet de la salle.
     *
     * @param item l'objet à retirer
     * @return true si l'objet a été retiré
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Ajoute une créature dans la salle.
     *
     * @param creature la créature à ajouter
     */
    public void addCreature(Creature creature) {
        if (creature != null) {
            creatures.add(creature);
        }
    }

    /**
     * Retire une créature de la salle.
     *
     * @param creature la créature à retirer
     * @return true si la créature a été retirée
     */
    public boolean removeCreature(Creature creature) {
        return creatures.remove(creature);
    }

    /**
     * Vérifie si la salle contient des ennemis vivants.
     *
     * @return true si la salle contient au moins un ennemi vivant
     */
    public boolean hasAliveEnemies() {
        return creatures.stream().anyMatch(Creature::isAlive);
    }

    /**
     * Vérifie si tous les ennemis de la salle sont morts.
     */
    public void checkIfCleared() {
        if (!hasAliveEnemies() && !creatures.isEmpty()) {
            isCleared = true;
            System.out.println("✓ La salle a été nettoyée!");
        }
    }

    /**
     * Obtient la salle dans une direction donnée.
     *
     * @param direction la direction (nord, sud, est, ouest)
     * @return la salle dans cette direction, ou null si aucune
     */
    public Room getRoomInDirection(String direction) {
        switch (direction.toLowerCase()) {
            case "nord":
            case "north":
            case "n":
                return northRoom;
            case "sud":
            case "south":
            case "s":
                return southRoom;
            case "est":
            case "east":
            case "e":
                return eastRoom;
            case "ouest":
            case "west":
            case "o":
            case "w":
                return westRoom;
            default:
                return null;
        }
    }

    /**
     * Connecte cette salle à une autre salle dans une direction.
     *
     * @param direction la direction
     * @param room      la salle à connecter
     */
    public void connectRoom(String direction, Room room) {
        switch (direction.toLowerCase()) {
            case "nord":
            case "north":
            case "n":
                this.northRoom = room;
                break;
            case "sud":
            case "south":
            case "s":
                this.southRoom = room;
                break;
            case "est":
            case "east":
            case "e":
                this.eastRoom = room;
                break;
            case "ouest":
            case "west":
            case "o":
            case "w":
                this.westRoom = room;
                break;
        }
    }

    /**
     * Connecte bidirectionnellement deux salles.
     *
     * @param direction la direction depuis cette salle
     * @param room      la salle à connecter
     */
    public void connectRoomBidirectional(String direction, Room room) {
        connectRoom(direction, room);

        // Connecter dans la direction opposée
        String oppositeDirection;
        switch (direction.toLowerCase()) {
            case "nord":
            case "north":
            case "n":
                oppositeDirection = "sud";
                break;
            case "sud":
            case "south":
            case "s":
                oppositeDirection = "nord";
                break;
            case "est":
            case "east":
            case "e":
                oppositeDirection = "ouest";
                break;
            case "ouest":
            case "west":
            case "o":
            case "w":
                oppositeDirection = "est";
                break;
            default:
                oppositeDirection = "";
                break;
        }

        if (!oppositeDirection.isEmpty()) {
            room.connectRoom(oppositeDirection, this);
        }
    }

    // Getters et setters pour Jackson

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setCreatures(List<Creature> creatures) {
        this.creatures = creatures != null ? creatures : new ArrayList<>();
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }

    public Room getNorthRoom() {
        return northRoom;
    }

    public void setNorthRoom(Room northRoom) {
        this.northRoom = northRoom;
    }

    public Room getSouthRoom() {
        return southRoom;
    }

    public void setSouthRoom(Room southRoom) {
        this.southRoom = southRoom;
    }

    public Room getEastRoom() {
        return eastRoom;
    }

    public void setEastRoom(Room eastRoom) {
        this.eastRoom = eastRoom;
    }

    public Room getWestRoom() {
        return westRoom;
    }

    public void setWestRoom(Room westRoom) {
        this.westRoom = westRoom;
    }

    @Override
    public String toString() {
        return name + " - " + description +
               " [Visitée: " + isVisited + ", Nettoyée: " + isCleared + "]";
    }
}
