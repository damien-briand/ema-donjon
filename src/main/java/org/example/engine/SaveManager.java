package org.example.engine;

import org.example.model.Player;
import org.example.util.JsonLoader;
import org.example.util.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Gestionnaire de sauvegarde et de chargement des parties.
 * Utilise JsonLoader pour la persistance des données.
 */
public class SaveManager {

    /**
     * Sauvegarde une partie (joueur) dans un fichier JSON.
     *
     * @param player   le joueur à sauvegarder
     * @param filePath le chemin du fichier de sauvegarde
     * @throws IOException si une erreur d'écriture se produit
     */
    public static void saveGame(Player player, Path filePath) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Le joueur ne peut pas être null");
        }

        try {
            JsonLoader.saveObject(player, filePath.toString());
            Logger.logInfo("Game saved successfully to: " + filePath);
        } catch (IOException e) {
            Logger.logError("Failed to save game to: " + filePath, e);
            throw e;
        }
    }

    /**
     * Charge une partie (joueur) depuis un fichier JSON.
     *
     * @param filePath le chemin du fichier de sauvegarde
     * @return le joueur chargé
     * @throws IOException si une erreur de lecture se produit
     */
    public static Player loadGame(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être null");
        }

        if (!Files.exists(filePath)) {
            throw new IOException("Le fichier de sauvegarde n'existe pas: " + filePath);
        }

        try {
            Player player = JsonLoader.loadObject(filePath.toString(), Player.class);
            Logger.logInfo("Game loaded successfully from: " + filePath);
            return player;
        } catch (IOException e) {
            Logger.logError("Failed to load game from: " + filePath, e);
            throw e;
        }
    }

    /**
     * Vérifie si une sauvegarde existe.
     *
     * @param filePath le chemin du fichier de sauvegarde
     * @return true si la sauvegarde existe
     */
    public static boolean saveExists(Path filePath) {
        return Files.exists(filePath);
    }

    /**
     * Sauvegarde automatique (quick save).
     *
     * @param player le joueur à sauvegarder
     * @throws IOException si une erreur d'écriture se produit
     */
    public static void autoSave(Player player) throws IOException {
        Path autoSavePath = Path.of("saves", "autosave.json");
        saveGame(player, autoSavePath);
        Logger.logInfo("Auto-save completed");
    }

    /**
     * Charge la sauvegarde automatique.
     *
     * @return le joueur chargé
     * @throws IOException si une erreur de lecture se produit
     */
    public static Player loadAutoSave() throws IOException {
        Path autoSavePath = Path.of("saves", "autosave.json");
        return loadGame(autoSavePath);
    }
}
