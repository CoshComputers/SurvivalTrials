package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.containers.InitialGearConfigContainer;
import com.dsd.st.containers.ItemDropConfigContainer;
import com.dsd.st.containers.MobSpawnConfigContainer;
import com.dsd.st.containers.SurvivalTrialsConfigContainer;
import com.dsd.st.util.CustomLogger;
import com.dsd.st.util.FileAndDirectoryManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConfigManager {
    private static ConfigManager instance;
    private final Gson mainGson;
    //private final Gson playerGson;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private InitialGearConfigContainer gearConfigContainer;
    private MobSpawnConfigContainer mobOverrideConfigContainer;
    private ItemDropConfigContainer itemDropConfigContainer;

    private SurvivalTrialsConfigContainer survivalTrialsConfigContainer;

    private final Logger LOGGER = LogManager.getLogger(SurvivalTrials.MOD_ID);


    private ConfigManager() {
        mainGson = new Gson();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void loadSurvivalTrialsConfig() {
        readWriteLock.readLock().lock();
        try {
            Path configPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("survivalTrialsConfig.json");
            this.LOGGER.info("Loading Survival Trials Config from: {}", configPath.toString());
            FileAndDirectoryManager.getInstance().logFileContents(configPath);
            loadConfig(configPath, SurvivalTrialsConfig.class, mainGson)
                    .ifPresent(config -> this.survivalTrialsConfigContainer = new SurvivalTrialsConfigContainer(config));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void loadGearConfig() {
        readWriteLock.readLock().lock();
        try {
            Path gearConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("gearConfig.json");
            this.LOGGER.info("Loading Gear Config from: {}", gearConfigPath.toString());
            loadConfig(gearConfigPath, InitialGearConfigWrapper.class, mainGson)
                    .ifPresent(wrapper -> this.gearConfigContainer = new InitialGearConfigContainer(wrapper));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void loadMobConfig() {
        readWriteLock.readLock().lock();
        try {
            Path mobConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("mobOverrideConfig.json");
            this.LOGGER.info("Loading Mob Config from: {}", mobConfigPath.toString());
            loadConfig(mobConfigPath, MobSpawnConfig.class, mainGson)
                    .ifPresent(config -> this.mobOverrideConfigContainer = new MobSpawnConfigContainer(config));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void loadItemDropConfig() {
        readWriteLock.readLock().lock();
        try {
            Path itemDropConfigPath = FileAndDirectoryManager.getInstance().getModDirectory().resolve("itemDropOverrideConfig.json");
            this.LOGGER.info("Loading Item Drop Config from: {}", itemDropConfigPath.toString());
            loadConfig(itemDropConfigPath, ItemDropConfigWrapper.class, mainGson)
                    .ifPresent(wrapper -> this.itemDropConfigContainer = new ItemDropConfigContainer(wrapper));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public PlayerConfig getPlayerConfig(UUID playerUuid) {
        readWriteLock.readLock().lock();
        try {
            Path configPath = FileAndDirectoryManager.getInstance().getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
            this.LOGGER.info("Loading Player Config from: {}", configPath.toString());
            if (Files.exists(configPath)) {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    this.LOGGER.info("Reading config file from: " + configPath.toAbsolutePath().toString());
                    return mainGson.fromJson(reader, PlayerConfig.class);
                } catch (IOException e) {
                    this.LOGGER.error("Failed to read player config", e);
                }
            }
            return null;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void savePlayerConfig(UUID playerUuid, PlayerConfig playerConfig) {
        readWriteLock.writeLock().lock();
        try {

            Path configPath = FileAndDirectoryManager.getInstance().getPlayerDataDirectory().resolve(playerUuid.toString() + ".json");
            try (Writer writer = Files.newBufferedWriter(configPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                mainGson.toJson(playerConfig, writer);
                this.LOGGER.info("Writing config file to: " + configPath.toAbsolutePath().toString());
            } catch (IOException e) {
                this.LOGGER.error("Failed to write player config", e);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public boolean saveSurvivalTrialsConfig() {
        boolean didSave = false;
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            // Serialize the updated configuration to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonConfig = gson.toJson(this.survivalTrialsConfigContainer.getSurvivalTrialsConfig());
            // Get the path to the configuration file
            FileAndDirectoryManager fileManager = FileAndDirectoryManager.getInstance();
            Path configFilePath = fileManager.getModDirectory().resolve("survivalTrialsConfig.json");

            // Write the updated configuration to disk
            try (BufferedWriter writer = Files.newBufferedWriter(configFilePath)) {
                writer.write(jsonConfig);
                didSave = true;
            } catch (IOException e) {
                // Log the exception and notify command runner if necessary
                CustomLogger.getInstance().error(String.format("Failed to save configuration - %s", e));
                // The method to notify the command runner can be placed here
                // ModConfigCommand.notifyCommandRunner("The command was successful, but the config file has not been updated.");
            }
        } finally {
            writeLock.unlock();
        }
        return didSave;
    }


    private <T> Optional<T> loadConfig(Path path, Class<T> configClass, Gson gson) {
        readWriteLock.readLock().lock();
        try {
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
        } finally {
            readWriteLock.readLock().unlock();
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

    public SurvivalTrialsConfigContainer getSurvivalTrialsConfigContainer() {
        return survivalTrialsConfigContainer;
    }

}
