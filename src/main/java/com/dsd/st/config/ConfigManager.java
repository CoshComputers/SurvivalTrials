package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.util.MobSpawnConfigDeserializer;
import com.dsd.st.util.SurvivalTrialsConfigDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Gson mainGson;
    private InitialGearConfigContainer gearConfigContainer;
    private MobSpawnConfigContainer mobOverrideConfigContainer;
    private ItemDropConfigContainer itemDropConfigContainer;

    private SurvivalTrialsConfigContainer survivalTrialsConfigContainer;

    private final Logger LOGGER = LogManager.getLogger(SurvivalTrials.MOD_ID);
    

    public ConfigManager() {
        defaultGson = new Gson();
        mobOverrideGson = new GsonBuilder()
                .registerTypeAdapter(MobSpawnConfigContainer.class, new MobSpawnConfigDeserializer())
                .create();
        playerGson = new GsonBuilder().setPrettyPrinting().create();
        mainGson = new GsonBuilder().registerTypeAdapter(SurvivalTrialsConfigContainer.class, new SurvivalTrialsConfigDeserializer())
                .create();


    }

    public void loadSurvivalTrialsConfig() {
        //SurvivalTrialsConfigContainer configContainer = null;
        Path configPath = SurvivalTrials.getModDirectory().resolve("survivalTrialsConfig.json");
        this.LOGGER.info("Loading Survival Trials Config from: {}", configPath.toString());
        loadConfig(configPath, SurvivalTrialsConfigContainer.class, mainGson)
                .ifPresent(configContainer -> this.survivalTrialsConfigContainer = configContainer);
    }
    public void loadGearConfig() {
        Path gearConfigPath = SurvivalTrials.getModDirectory().resolve("gearConfig.json");
        this.LOGGER.info("Loading Gear Config from: {}",gearConfigPath.toString());
        loadConfig(gearConfigPath, InitialGearConfigContainer.class, defaultGson)
                .ifPresent(configContainer -> this.gearConfigContainer = configContainer);
    }

    public void loadMobConfig() {
        Path mobConfigPath = SurvivalTrials.getModDirectory().resolve("mobOverrideConfig.json");
        this.LOGGER.info("Loading Mob Config from: {}",mobConfigPath.toString());
        loadConfig(mobConfigPath, MobSpawnConfigContainer.class, mobOverrideGson)
                .ifPresent(configContainer -> this.mobOverrideConfigContainer = configContainer);
    }

    public void loadItemDropConfig() {
        Path itemDropConfigPath = SurvivalTrials.getModDirectory().resolve("itemDropOverrideConfig.json");
        this.LOGGER.info("Loading Item Drop Config from: {}", itemDropConfigPath.toString());
        loadConfig(itemDropConfigPath, ItemDropConfigContainer.class, defaultGson)
                .ifPresent(configContainer -> this.itemDropConfigContainer = configContainer);
    }
    public PlayerConfig getPlayerConfig(UUID playerUuid) {
        Path configPath = SurvivalTrials.getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
        this.LOGGER.info("Loading Player Config from: {}",configPath.toString());
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                this.LOGGER.info("Reading config file from: " + configPath.toAbsolutePath().toString());
                return playerGson.fromJson(reader, PlayerConfig.class);
            } catch (IOException e) {
                this.LOGGER.error("Failed to read player config", e);
            }
        }
        return null;
    }

    public void savePlayerConfig(UUID playerUuid, PlayerConfig playerConfig) {
        Path configPath = SurvivalTrials.getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
        try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            playerGson.toJson(playerConfig, writer);
            this.LOGGER.info("Writing config file to: " + configPath.toAbsolutePath().toString());
        } catch (IOException e) {
            this.LOGGER.error("Failed to write player config", e);
        }
    }

    private <T> Optional<T> loadConfig(Path path, Class<T> configClass, Gson gson) {
        try {
            if (!Files.exists(path)) {
                this.LOGGER.error("Could not find {}", path);
                return Optional.empty();
            }
            try (InputStream inputStream = Files.newInputStream(path)) {
                try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                    T config = gson.fromJson(reader, configClass);
                    return Optional.of(config);
                }
            }
        } catch (IOException e) {
            this.LOGGER.error("Failed to load {}", path, e);
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
    public SurvivalTrialsConfigContainer getSurvivalTrialsConfigContainer() { return survivalTrialsConfigContainer; }
    /********************************************** CONTAINER CLASSES ********************************************/


    public static class SurvivalTrialsConfigContainer {
        public SurvivalTrialsConfig survivalTrialsConfig;

        public SurvivalTrialsConfig getSurvivalTrialsConfig() {
            return survivalTrialsConfig;
        }

        public void setSurvivalTrialsConfig(SurvivalTrialsConfig survivalTrialsConfig) {
            this.survivalTrialsConfig = survivalTrialsConfig;
        }
    }
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
