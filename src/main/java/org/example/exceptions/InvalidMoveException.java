package org.example.exceptions;

import java.util.List;

/**
 * Exception levée lorsqu'un mouvement dans le donjon est invalide.
 * Fournit des informations sur la direction tentée et les directions possibles.
 */
public class InvalidMoveException extends RuntimeException {
    private final String attemptedDirection;
    private final List<String> availableDirections;
    private final String currentRoomName;

    /**
     * Constructeur simple avec message uniquement
     * @param message Le message d'erreur
     */
    public InvalidMoveException(String message) {
        super(message);
        this.attemptedDirection = null;
        this.availableDirections = null;
        this.currentRoomName = null;
    }

    /**
     * Constructeur avec direction tentée et directions disponibles
     * @param attemptedDirection La direction tentée par le joueur
     * @param availableDirections Liste des directions disponibles
     */
    public InvalidMoveException(String attemptedDirection, List<String> availableDirections) {
        super(buildMessage(attemptedDirection, availableDirections, null));
        this.attemptedDirection = attemptedDirection;
        this.availableDirections = availableDirections;
        this.currentRoomName = null;
    }

    /**
     * Constructeur complet avec toutes les informations
     * @param attemptedDirection La direction tentée
     * @param availableDirections Liste des directions disponibles
     * @param currentRoomName Nom de la salle actuelle
     */
    public InvalidMoveException(String attemptedDirection, List<String> availableDirections, String currentRoomName) {
        super(buildMessage(attemptedDirection, availableDirections, currentRoomName));
        this.attemptedDirection = attemptedDirection;
        this.availableDirections = availableDirections;
        this.currentRoomName = currentRoomName;
    }

    /**
     * Construit un message d'erreur détaillé
     */
    private static String buildMessage(String attemptedDirection, List<String> availableDirections, String currentRoomName) {
        StringBuilder sb = new StringBuilder();

        if (currentRoomName != null) {
            sb.append("Depuis '").append(currentRoomName).append("', ");
        }

        sb.append("impossible d'aller vers '").append(attemptedDirection).append("'");

        if (availableDirections != null && !availableDirections.isEmpty()) {
            sb.append("\nDirections disponibles: ");
            sb.append(String.join(", ", availableDirections));
        } else {
            sb.append("\nAucune sortie disponible dans cette salle!");
        }

        return sb.toString();
    }

    /**
     * @return La direction tentée par le joueur
     */
    public String getAttemptedDirection() {
        return attemptedDirection;
    }

    /**
     * @return Liste des directions disponibles (peut être null)
     */
    public List<String> getAvailableDirections() {
        return availableDirections;
    }

    /**
     * @return Le nom de la salle actuelle (peut être null)
     */
    public String getCurrentRoomName() {
        return currentRoomName;
    }

    /**
     * @return true si des directions sont disponibles
     */
    public boolean hasAvailableDirections() {
        return availableDirections != null && !availableDirections.isEmpty();
    }
}
