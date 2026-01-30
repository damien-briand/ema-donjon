package org.example.exceptions;

import java.util.List;

/**
 * Exception levée lorsqu'une commande utilisateur est invalide ou inconnue.
 * Fournit des informations sur la commande erronée et les suggestions possibles.
 */
public class InvalidCommandException extends RuntimeException {
    private final String invalidCommand;
    private final List<String> suggestedCommands;

    /**
     * Constructeur simple avec message uniquement
     * @param message Le message d'erreur
     */
    public InvalidCommandException(String message) {
        super(message);
        this.invalidCommand = null;
        this.suggestedCommands = null;
    }

    /**
     * Constructeur complet avec commande invalide et suggestions
     * @param invalidCommand La commande invalide entrée par l'utilisateur
     * @param suggestedCommands Liste des commandes valides suggérées
     */
    public InvalidCommandException(String invalidCommand, List<String> suggestedCommands) {
        super(buildMessage(invalidCommand, suggestedCommands));
        this.invalidCommand = invalidCommand;
        this.suggestedCommands = suggestedCommands;
    }

    /**
     * Constructeur avec message personnalisé et détails
     * @param message Message d'erreur personnalisé
     * @param invalidCommand La commande invalide
     * @param suggestedCommands Liste des commandes suggérées
     */
    public InvalidCommandException(String message, String invalidCommand, List<String> suggestedCommands) {
        super(message);
        this.invalidCommand = invalidCommand;
        this.suggestedCommands = suggestedCommands;
    }

    /**
     * Construit un message d'erreur détaillé avec suggestions
     */
    private static String buildMessage(String invalidCommand, List<String> suggestedCommands) {
        StringBuilder sb = new StringBuilder();
        sb.append("Commande invalide: '").append(invalidCommand).append("'");

        if (suggestedCommands != null && !suggestedCommands.isEmpty()) {
            sb.append("\nCommandes disponibles: ");
            sb.append(String.join(", ", suggestedCommands));
        }

        return sb.toString();
    }

    /**
     * @return La commande invalide entrée par l'utilisateur
     */
    public String getInvalidCommand() {
        return invalidCommand;
    }

    /**
     * @return Liste des commandes suggérées (peut être null)
     */
    public List<String> getSuggestedCommands() {
        return suggestedCommands;
    }

    /**
     * @return true si des suggestions sont disponibles
     */
    public boolean hasSuggestions() {
        return suggestedCommands != null && !suggestedCommands.isEmpty();
    }
}
