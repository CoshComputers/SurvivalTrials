package com.dsd.st.util;

import com.dsd.st.SurvivalTrials;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;

public class FileAndDirectoryManager {
    private static FileAndDirectoryManager instance;
    private final Path modDirectory;
    private final Path playerDataDirectory;
    private FileAndDirectoryManager(Path rootPath) {
        modDirectory = rootPath.resolve("SurvivalTrials");
        playerDataDirectory = modDirectory.resolve("playerdata");
        setupDirectories();
    }

    public static void initialize(Path rootPath) {
        if (instance == null) {
            instance = new FileAndDirectoryManager(rootPath);
        }
    }

    public static FileAndDirectoryManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FileAndDirectoryManager is not initialized");
        }
        return instance;
    }

    public void setupDirectories() {

        try {
            Files.createDirectories(modDirectory);
        } catch (IOException e) {
            CustomLogger.getInstance().error(String.format("Failed to create Root Directory at %sz\n%s", modDirectory.toString(), e));
        }

        copyDefaultConfigs();

        try {
            Files.createDirectories(playerDataDirectory);

        } catch (IOException e) {
            CustomLogger.getInstance().error(String.format("Failed to create PlayerData Directory at %s\n%s", playerDataDirectory.toString(), e));
        }

       // setPlayerDirectory(playerDataDir);
    }

    /********************************** GETTER and SETTERS **********************************************/
    public Path getModDirectory() {
        return modDirectory;
    }

    public Path getPlayerDataDirectory() {
        return playerDataDirectory;
    }

    /******************************** HELPER FUNCTIONS *******************************************/
    public void copyDefaultConfigs() {
        try {
            // Get the URL of the resource/config directory.
            URL url = SurvivalTrials.class.getResource("/config");
            // Convert URL to URI to handle spaces in the path.
            URI uri = url.toURI();
            Path srcPath;
            if (uri.getScheme().equals("jar")) {
                // Handle JAR file path
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                srcPath = fileSystem.getPath("/config");
            } else {
                // Handle IDE file path
                srcPath = Paths.get(uri);
            }

            // Iterate through the resources in the /config directory.
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(srcPath)) {
                for (Path entry : stream) {
                    // Resolve the target path in the mod directory.
                    Path targetPath = modDirectory.resolve(srcPath.relativize(entry).toString());
                    // If the file does not exist in the mod directory, copy it over.
                    if (!Files.exists(targetPath)) {
                        CustomLogger.getInstance().info(String.format("Copying default config file: %s", entry.getFileName()));
                        Files.copy(entry, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            CustomLogger.getInstance().error(String.format("Failed to copy default config files\n%s", e));
        }
    }

    /********************************* LOG HELPERS *****************************************/
    public void logFileContents(Path filePath) {
        try {
            String content = new String(Files.readAllBytes(filePath));
            CustomLogger.getInstance().info(String.format("File content: %s", content));
        } catch (IOException e) {
            CustomLogger.getInstance().error(String.format("Failed to read file: %s\n%s", filePath, e));
        }
    }
}

