package org.example.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Utilitaire pour charger et sauvegarder des données JSON avec Jackson 3.0.4.
 * Gère la lecture/écriture de fichiers JSON pour la persistance des données du jeu.
 */
public class JsonLoader {
    private static final ObjectMapper mapper;

    // Configuration statique du mapper Jackson
    static {
        mapper = new ObjectMapper();
        // Note: Pretty printing est géré via writerWithDefaultPrettyPrinter() lors de l'écriture
    }

    /**
     * Charge un objet unique depuis un fichier JSON.
     *
     * @param filePath chemin du fichier JSON
     * @param clazz    classe de l'objet à charger
     * @param <T>      type de l'objet
     * @return l'objet chargé
     * @throws IOException si erreur de lecture
     */
    public static <T> T loadObject(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            Logger.logError("File not found: " + filePath);
            throw new IOException("Fichier introuvable: " + filePath);
        }

        T object = mapper.readValue(file, clazz);
        Logger.logInfo("Object loaded from: " + filePath);
        return object;
    }

    /**
     * Charge un objet depuis un fichier JSON dans les ressources (resources/).
     *
     * @param resourcePath chemin de la ressource (ex: "monsters.json")
     * @param clazz        classe de l'objet à charger
     * @param <T>          type de l'objet
     * @return l'objet chargé
     * @throws IOException si erreur de lecture
     */
    public static <T> T loadObjectFromResource(String resourcePath, Class<T> clazz) throws IOException {
        try (InputStream is = JsonLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                Logger.logError("Resource not found: " + resourcePath);
                throw new IOException("Ressource introuvable: " + resourcePath);
            }

            T object = mapper.readValue(is, clazz);
            Logger.logInfo("Object loaded from resource: " + resourcePath);
            return object;
        } catch (IOException e) {
            Logger.logError("Failed to load object from resource: " + resourcePath, e);
            throw e;
        }
    }

    /**
     * Charge une liste d'objets depuis un fichier JSON.
     *
     * @param filePath      chemin du fichier JSON
     * @param typeReference référence de type pour la liste
     * @param <T>           type des objets dans la liste
     * @return la liste d'objets chargée
     * @throws IOException si erreur de lecture
     */
    public static <T> List<T> loadList(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            Logger.logError("File not found: " + filePath);
            throw new IOException("Fichier introuvable: " + filePath);
        }

        List<T> list = mapper.readValue(file, typeReference);
        Logger.logInfo("List loaded from: " + filePath + " (size: " + list.size() + ")");
        return list;
    }

    /**
     * Charge une liste d'objets depuis un fichier JSON dans les ressources.
     *
     * @param resourcePath  chemin de la ressource
     * @param typeReference référence de type pour la liste
     * @param <T>           type des objets dans la liste
     * @return la liste d'objets chargée
     * @throws IOException si erreur de lecture
     */
    public static <T> List<T> loadListFromResource(String resourcePath, TypeReference<List<T>> typeReference) throws IOException {
        try (InputStream is = JsonLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                Logger.logError("Resource not found: " + resourcePath);
                throw new IOException("Ressource introuvable: " + resourcePath);
            }

            List<T> list = mapper.readValue(is, typeReference);
            Logger.logInfo("List loaded from resource: " + resourcePath + " (size: " + list.size() + ")");
            return list;
        } catch (IOException e) {
            Logger.logError("Failed to load list from resource: " + resourcePath, e);
            throw e;
        }
    }

    /**
     * Sauvegarde un objet dans un fichier JSON.
     *
     * @param object   objet à sauvegarder
     * @param filePath chemin du fichier de destination
     * @throws IOException si erreur d'écriture
     */
    public static void saveObject(Object object, String filePath) throws IOException {
        File file = new File(filePath);

        // Créer les dossiers parents si nécessaire
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                Logger.logWarning("Failed to create parent directories for: " + filePath);
            }
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
        Logger.logInfo("Object saved to: " + filePath);
    }

    /**
     * Sauvegarde une liste d'objets dans un fichier JSON.
     *
     * @param list     liste à sauvegarder
     * @param filePath chemin du fichier de destination
     * @throws IOException si erreur d'écriture
     */
    public static void saveList(List<?> list, String filePath) throws IOException {
        File file = new File(filePath);

        // Créer les dossiers parents si nécessaire
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                Logger.logWarning("Failed to create parent directories for: " + filePath);
            }
        }

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
        Logger.logInfo("List saved to: " + filePath + " (size: " + list.size() + ")");
    }

    /**
     * Vérifie si un fichier JSON existe.
     *
     * @param filePath chemin du fichier
     * @return true si le fichier existe
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Vérifie si une ressource JSON existe.
     *
     * @param resourcePath chemin de la ressource
     * @return true si la ressource existe
     */
    public static boolean resourceExists(String resourcePath) {
        return JsonLoader.class.getClassLoader().getResource(resourcePath) != null;
    }

    /**
     * Obtient le mapper Jackson configuré (pour usage avancé).
     *
     * @return l'instance du mapper
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
