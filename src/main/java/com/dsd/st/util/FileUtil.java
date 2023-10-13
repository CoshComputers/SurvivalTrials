package com.dsd.st.util;

import com.dsd.st.SurvivalTrials;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public static void logFileContents(Path filePath) {
        try {
            String content = new String(Files.readAllBytes(filePath));
            SurvivalTrials.getModLogger().info(String.format("File content: %s", content));
        } catch (IOException e) {
            SurvivalTrials.getModLogger().error(String.format("Failed to read file: %s\n%s", filePath, e));
        }
    }
}

