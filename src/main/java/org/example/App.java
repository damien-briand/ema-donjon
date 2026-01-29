package org.example;

import org.example.engine.GameEngine;
import org.example.util.Logger;

/**
 * Point d'entrée principal de l'application.
 * Lance le moteur de jeu.
 */
public class App {
    public static void main(String[] args) {
        Logger.logInfo("=== Application started ===");

        try {
            GameEngine gameEngine = new GameEngine();
            gameEngine.start();
        } catch (Exception e) {
            Logger.logError("Fatal error in game engine", e);
            System.err.println("\n❌ Erreur fatale: " + e.getMessage());
            System.err.println("   Consultez le fichier logs/game.log pour plus de détails.");
        }

        Logger.logInfo("=== Application ended ===");
    }
}
