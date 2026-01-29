package org.example.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire de logging pour enregistrer les erreurs et événements dans un fichier.
 * Tous les logs sont écrits dans logs/game.log avec un timestamp.
 */
public class Logger {
    private static final String LOG_FILE = "logs/game.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Initialisation du fichier de log
    static {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                boolean created = logDir.mkdirs();
                if (!created) {
                    System.err.println("Impossible de créer le dossier logs");
                }
            }

            File logFile = new File(LOG_FILE);
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (!created) {
                    System.err.println("Impossible de créer le fichier de log");
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de créer le fichier de log: " + e.getMessage());
        }
    }

    /**
     * Enregistre une erreur avec son exception dans le fichier de log.
     *
     * @param message message décrivant l'erreur
     * @param e       exception à logger
     */
    public static void logError(String message, Exception e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("[%s] ERROR: %s - %s%n",
                LocalDateTime.now().format(FORMATTER),
                message,
                e.getMessage()
            );
            e.printStackTrace(pw);
            pw.println("----------------------------------------");

        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }

    /**
     * Enregistre une erreur simple dans le fichier de log.
     *
     * @param message message d'erreur
     */
    public static void logError(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("[%s] ERROR: %s%n",
                LocalDateTime.now().format(FORMATTER),
                message
            );
            pw.println("----------------------------------------");

        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }

    /**
     * Enregistre un message d'information dans le fichier de log.
     *
     * @param message message d'information
     */
    public static void logInfo(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("[%s] INFO: %s%n",
                LocalDateTime.now().format(FORMATTER),
                message
            );

            System.out.println(message);

        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }

    /**
     * Enregistre un message de debug dans le fichier de log.
     *
     * @param message message de debug
     */
    public static void logDebug(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("[%s] DEBUG: %s%n",
                LocalDateTime.now().format(FORMATTER),
                message
            );

        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }

    /**
     * Enregistre un avertissement dans le fichier de log.
     *
     * @param message message d'avertissement
     */
    public static void logWarning(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.printf("[%s] WARNING: %s%n",
                LocalDateTime.now().format(FORMATTER),
                message
            );

        } catch (IOException ex) {
            System.err.println("Failed to write to log file: " + ex.getMessage());
        }
    }

    /**
     * Efface le contenu du fichier de log.
     */
    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            fw.write(""); // Efface le contenu
            logInfo("Log file cleared");
        } catch (IOException ex) {
            System.err.println("Failed to clear log file: " + ex.getMessage());
        }
    }
}
