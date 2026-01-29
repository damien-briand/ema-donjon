package org.example.engine;

import org.example.model.Player;
import org.example.util.JsonLoader;
import org.example.util.Logger;

import java.io.IOException;

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
    public static void saveGame(Player player, String filePath) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Le joueur ne peut pas être null");
        }

        try {
            JsonLoader.saveObject(player, filePath);
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
    public static Player loadGame(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être vide");
        }

        if (!JsonLoader.fileExists(filePath)) {
            throw new IOException("Le fichier de sauvegarde n'existe pas: " + filePath);
        }

        try {
            Player player = JsonLoader.loadObject(filePath, Player.class);
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
    public static boolean saveExists(String filePath) {
        return JsonLoader.fileExists(filePath);
    }

    /**
     * Sauvegarde automatique (quick save).
     *
     * @param player le joueur à sauvegarder
     * @throws IOException si une erreur d'écriture se produit
     */
    public static void autoSave(Player player) throws IOException {
        String autoSavePath = "saves/autosave.json";
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
        String autoSavePath = "saves/autosave.json";
        return loadGame(autoSavePath);
    }
}
