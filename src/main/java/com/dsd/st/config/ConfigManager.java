package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConfigManager {
    private Gson defaultGson;
    private Gson mobOverrideGson;
    private Gson playerGson;
    private InitialGearConfigContainer gearConfigContainer;
    private MobSpawnConfigContainer mobOverrideConfigContainer;
    private ItemDropConfigContainer itemDropConfigContainer;

    public ConfigManager() {
        defaultGson = new Gson();
        mobOverrideGson = new GsonBuilder()
                .registerTypeAdapter(MobSpawnConfigContainer.class, new MobOverrideConfigDeserializer())
                .create();
        playerGson = new GsonBuilder().setPrettyPrinting().create();

    }

    public void loadGearConfig() {
        Path gearConfigPath = SurvivalTrials.getModDirectory().resolve("gearConfig.json");
        SurvivalTrials.LOGGER.info("Loading Gear Config from: {}",gearConfigPath.toString());
        loadConfig(gearConfigPath, InitialGearConfigContainer.class, defaultGson)
                .ifPresent(configContainer -> this.gearConfigContainer = configContainer);
    }

    public void loadMobConfig() {
        Path mobConfigPath = SurvivalTrials.getModDirectory().resolve("mobOverrideConfig.json");
        SurvivalTrials.LOGGER.info("Loading Mob Config from: {}",mobConfigPath.toString());
        loadConfig(mobConfigPath, MobSpawnConfigContainer.class, mobOverrideGson)
                .ifPresent(configContainer -> this.mobOverrideConfigContainer = configContainer);
    }

    public void loadItemDropConfig() {
        Path itemDropConfigPath = SurvivalTrials.getModDirectory().resolve("itemDropOverrideConfig.json");
        SurvivalTrials.LOGGER.info("Loading Item Drop Config from: {}", itemDropConfigPath.toString());
        loadConfig(itemDropConfigPath, ItemDropConfigContainer.class, defaultGson)
                .ifPresent(configContainer -> this.itemDropConfigContainer = configContainer);
    }
    public PlayerConfig getPlayerConfig(UUID playerUuid) {
        Path configPath = SurvivalTrials.getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
        SurvivalTrials.LOGGER.info("Loading Player Config from: {}",configPath.toString());
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
        Path configPath = SurvivalTrials.getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
        try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            playerGson.toJson(playerConfig, writer);
            SurvivalTrials.LOGGER.info("Writing config file to: " + configPath.toAbsolutePath().toString());
        } catch (IOException e) {
            SurvivalTrials.LOGGER.error("Failed to write player config", e);
        }
    }

    private <T> Optional<T> loadConfig(Path path, Class<T> configClass, Gson gson) {
        try {
            if (!Files.exists(path)) {
                SurvivalTrials.LOGGER.error("Could not find {}", path);
                return Optional.empty();
            }
            try (InputStream inputStream = Files.newInputStream(path)) {
                try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                    T config = gson.fromJson(reader, configClass);
                    return Optional.of(config);
                }
            }
        } catch (IOException e) {
            SurvivalTrials.LOGGER.error("Failed to load {}", path, e);
            return Optional.empty();
        }
    }
    /*********************************** Getters ****************************************/
    public InitialGearConfigContainer getInitialGearConfigContainer() {
        return gearConfigContainer;
    }
    public MobSpawnConfigContainer getMobSpawnConfigContainer() {
        return mobOverrideConfigContainer;
    }
    public ItemDropConfigContainer getItemDropConfigContainer() {
        return itemDropConfigContainer;
    }


    /********************************************** CONTAINER CLASSES ********************************************/
    public static class InitialGearConfigContainer {
        public InitialGearConfig initialGearConfig;

        public InitialGearConfig getInitialGearConfig() {
            return initialGearConfig;
        }

    }

    public static class MobSpawnConfigContainer {
        public MobSpawnConfig mobSpawnConfig;

        public MobSpawnConfig getMobSpawnConfig() {
            return mobSpawnConfig;
        }

        public void setMobSpawnConfig(MobSpawnConfig mobSpawnConfig) {
            this.mobSpawnConfig = mobSpawnConfig;
        }


    }

    public static class ItemDropConfigContainer {
        private final ItemDropConfig itemDropConfig;

        public ItemDropConfigContainer(ItemDropConfig itemDropConfig) {
            this.itemDropConfig = itemDropConfig;
        }

        public ItemDropConfig getItemDropConfig() {
            return itemDropConfig;
        }

        public ItemDropConfigContainer withUpdatedDrops(List<ItemDropConfig.Drop> newDrops) {
            return new ItemDropConfigContainer(new ItemDropConfig(newDrops));
        }
    }

}
