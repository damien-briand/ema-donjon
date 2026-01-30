package org.example.util;

import org.example.model.Room;

import java.util.*;

/**
 * Classe utilitaire pour gérer et afficher la carte du donjon.
 * Affiche une visualisation ASCII des salles explorées.
 */
public class DungeonMap {
    // Couleurs ANSI
    private static final String GREEN = "\u001B[32m";
    private static final String GRAY = "\u001B[90m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[36m";

    /**
     * Affiche la carte complète du donjon avec les salles explorées.
     *
     * @param allRooms    toutes les salles du donjon
     * @param currentRoom la salle actuelle du joueur
     */
    public static void displayMap(Collection<Room> allRooms, Room currentRoom) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         🗺️  CARTE DU DONJON           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Légende: " + GREEN + "■" + RESET + " = Exploré  " +
                          GRAY + "■" + RESET + " = Inexploré  " +
                          YELLOW + "●" + RESET + " = Position actuelle");
        System.out.println();

        // Organiser les salles par position (simplifié pour donjon linéaire)
        Map<Room, Position> positions = calculateRoomPositions(allRooms, currentRoom);

        // Trouver les dimensions de la grille
        int minX = positions.values().stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = positions.values().stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = positions.values().stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = positions.values().stream().mapToInt(p -> p.y).max().orElse(0);

        // Afficher la grille
        for (int y = maxY; y >= minY; y--) {
            // Ligne des portes nord
            for (int x = minX; x <= maxX; x++) {
                Room room = getRoomAtPosition(positions, x, y);
                if (room != null && room.getNorthRoom() != null) {
                    String color = room.isVisited() ? GREEN : GRAY;
                    System.out.print("   " + color + "|" + RESET + "   ");
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println();

            // Ligne des salles (avec portes ouest/est)
            for (int x = minX; x <= maxX; x++) {
                Room room = getRoomAtPosition(positions, x, y);
                if (room != null) {
                    String color = room.isVisited() ? GREEN : GRAY;
                    String westDoor = (room.getWestRoom() != null) ? color + "-" + RESET : " ";
                    String eastDoor = (room.getEastRoom() != null) ? color + "-" + RESET : " ";

                    String roomSymbol;
                    if (room == currentRoom) {
                        roomSymbol = YELLOW + "|●|" + RESET;
                    } else if (room.isVisited()) {
                        if (room.isCleared()) {
                            roomSymbol = GREEN + "|✓|" + RESET;
                        } else {
                            roomSymbol = GREEN + "|■|" + RESET;
                        }
                    } else {
                        roomSymbol = GRAY + "|?|" + RESET;
                    }

                    System.out.print(westDoor + roomSymbol + eastDoor + " ");
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println();

            // Ligne des portes sud
            for (int x = minX; x <= maxX; x++) {
                Room room = getRoomAtPosition(positions, x, y);
                if (room != null && room.getSouthRoom() != null) {
                    String color = room.isVisited() ? GREEN : GRAY;
                    System.out.print("   " + color + "|" + RESET + "   ");
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println();
        }

        System.out.println();

        // Afficher la liste des salles visitées
        System.out.println("📋 Salles explorées:");
        allRooms.stream()
            .filter(Room::isVisited)
            .forEach(room -> {
                String status = room.isCleared() ? GREEN + " ✓" + RESET : YELLOW + " ⚔" + RESET;
                String current = room == currentRoom ? YELLOW + " [ICI]" + RESET : "";
                System.out.println("  • " + room.getName() + status + current);
            });
    }

    /**
     * Calcule les positions des salles sur une grille 2D.
     *
     * @param allRooms    toutes les salles
     * @param startRoom   la salle de départ pour le calcul
     * @return map des positions par salle
     */
    private static Map<Room, Position> calculateRoomPositions(Collection<Room> allRooms, Room startRoom) {
        Map<Room, Position> positions = new HashMap<>();
        Set<Room> visited = new HashSet<>();
        Queue<RoomWithPosition> queue = new LinkedList<>();

        // Commencer à la position (0, 0)
        queue.add(new RoomWithPosition(startRoom, 0, 0));
        visited.add(startRoom);

        while (!queue.isEmpty()) {
            RoomWithPosition current = queue.poll();
            Room room = current.room;
            int x = current.x;
            int y = current.y;

            positions.put(room, new Position(x, y));

            // Explorer les voisins
            if (room.getNorthRoom() != null && !visited.contains(room.getNorthRoom())) {
                visited.add(room.getNorthRoom());
                queue.add(new RoomWithPosition(room.getNorthRoom(), x, y + 1));
            }
            if (room.getSouthRoom() != null && !visited.contains(room.getSouthRoom())) {
                visited.add(room.getSouthRoom());
                queue.add(new RoomWithPosition(room.getSouthRoom(), x, y - 1));
            }
            if (room.getEastRoom() != null && !visited.contains(room.getEastRoom())) {
                visited.add(room.getEastRoom());
                queue.add(new RoomWithPosition(room.getEastRoom(), x + 1, y));
            }
            if (room.getWestRoom() != null && !visited.contains(room.getWestRoom())) {
                visited.add(room.getWestRoom());
                queue.add(new RoomWithPosition(room.getWestRoom(), x - 1, y));
            }
        }

        return positions;
    }

    /**
     * Trouve la salle à une position donnée.
     *
     * @param positions map des positions
     * @param x         coordonnée x
     * @param y         coordonnée y
     * @return la salle à cette position, ou null
     */
    private static Room getRoomAtPosition(Map<Room, Position> positions, int x, int y) {
        return positions.entrySet().stream()
            .filter(entry -> entry.getValue().x == x && entry.getValue().y == y)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }

    /**
     * Classe interne pour représenter une position 2D.
     */
    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Classe interne pour associer une salle à sa position lors du parcours BFS.
     */
    private static class RoomWithPosition {
        Room room;
        int x;
        int y;

        RoomWithPosition(Room room, int x, int y) {
            this.room = room;
            this.x = x;
            this.y = y;
        }
    }
}
