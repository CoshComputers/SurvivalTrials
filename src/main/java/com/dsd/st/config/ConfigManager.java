package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

public class ConfigManager {
    private Gson defaultGson;
    private Gson mobOverrideGson;
    private Gson playerGson;


    private Path playerConfigDir;

    public InitialGearConfig initialGearConfig;
    public MobSpawnConfig mobSpawnConfig;

    public ConfigManager() {
        defaultGson = new Gson();
        mobOverrideGson = new GsonBuilder()
                .registerTypeAdapter(MobOverrideConfigContainer.class, new MobOverrideConfigDeserializer())
                .create();
        playerGson = new GsonBuilder().setPrettyPrinting().create();
        playerConfigDir = Paths.get("resources", "config", "players");
        try {
            Files.createDirectories(playerConfigDir);
        } catch (IOException e) {
            SurvivalTrials.LOGGER.error("Could not create player config directory", e);
        }
    }

    public void loadGearConfig() {
        loadConfig("config/gearConfig.json", GearConfigContainer.class, defaultGson)
                .ifPresent(config -> this.initialGearConfig = config.initialGearConfig);
    }

    public void loadMobConfig() {
        loadConfig("config/mobOverrideConfig.json", MobOverrideConfigContainer.class, mobOverrideGson)
                .ifPresent(config -> this.mobSpawnConfig = config.mobSpawnConfig);
    }

    public PlayerConfig getPlayerConfig(UUID playerUuid) {
        Path configPath = playerConfigDir.resolve(playerUuid.toString() + ".json");
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                SurvivalTrials.LOGGER.info("Reading config file from: " + configPath.toAbsolutePath().toString());
                return playerGson.fromJson(reader, PlayerConfig.class);
            } catch (IOException e) {
                SurvivalTrials.LOGGER.error("Failed to read player config", e);
            }
        }
        return null;
    }

    public void savePlayerConfig(UUID playerUuid, PlayerConfig playerConfig) {
        Path configPath = playerConfigDir.resolve(playerUuid.toString() + ".json");
        try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            playerGson.toJson(playerConfig, writer);
            SurvivalTrials.LOGGER.info("Writing config file to: " + configPath.toAbsolutePath().toString());
        } catch (IOException e) {
            SurvivalTrials.LOGGER.error("Failed to write player config", e);
        }
    }

    private <T> Optional<T> loadConfig(String path, Class<T> configClass, Gson gson) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                SurvivalTrials.LOGGER.error("Could not find {}", path);
                return Optional.empty();
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                T config = gson.fromJson(reader, configClass);
                return Optional.of(config);
            }
        } catch (IOException | JsonSyntaxException e) {
            SurvivalTrials.LOGGER.error("Failed to load {}", path, e);
            return Optional.empty();
        }
    }

    public Path getPlayerConfigDir() {
        return playerConfigDir;
    }


    static class GearConfigContainer {
        public InitialGearConfig initialGearConfig;
    }

    public static class MobOverrideConfigContainer {
        public MobSpawnConfig mobSpawnConfig;

        public MobSpawnConfig getMobSpawnConfig() {
            return mobSpawnConfig;
        }

        public void setMobSpawnConfig(MobSpawnConfig mobSpawnConfig) {
            this.mobSpawnConfig = mobSpawnConfig;
        }


    }
}
