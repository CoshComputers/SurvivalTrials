package com.dsd.st;

import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.MobSpawnConfig;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.util.MobSpawnManager;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("survival_trials")
public class SurvivalTrials {


    private static Path modDirectory;
    private static Path playerDataDirectory;
    public static final String MOD_ID = "survival_trials";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static ConfigManager configManager;

    private static final Map<UUID, PlayerConfig> playerConfigs = new HashMap<>();


    public static List<OverriddenMobType> overriddenMobTypes = new ArrayList<>();

    public SurvivalTrials() {
        LOGGER.info("[SurvivalTrials Constructor Called]");

        configManager = new ConfigManager();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("[SurvivalTrials Setup Active]");

        configManager.loadMobConfig();
        SurvivalTrials.LOGGER.info("Mob Override Config: {}", configManager.mobSpawnConfig);

        MobSpawnConfig.MobOverride firstOverride = configManager.mobSpawnConfig.getMobSpawnOverrides().get(0);
        SurvivalTrials.LOGGER.info("First override - Mob Type: {}, Is Baby: {}", firstOverride.getMobType(), firstOverride.isBaby());

        configManager.loadGearConfig();
        SurvivalTrials.LOGGER.info("Initial Gear Config: {}", configManager.initialGearConfig);

        List<MobSpawnConfig.MobOverride> tempListMobOverrides = configManager.mobSpawnConfig.getMobSpawnOverrides();
        overriddenMobTypes = MobSpawnManager.createOverriddenMobs(tempListMobOverrides);
        LOGGER.info("{} Overridden Mobs Created", overriddenMobTypes.size());

    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("survivaltrials", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("[SurvivalTrials] onServerStarting triggered");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here

        }
    }
    /************************ UTIL FUNCTIONS **************************************/
    public static void setupDirectories(File rootDir){
        File modDir = new File(rootDir,"SurvivalTrials");
        if(!modDir.exists()){
            modDir.mkdirs();
            copyDefaultConfigs(modDir);
        }
        setModDirectory(modDir.toPath());

        File playDir = new File(modDir, "playerdata");
        if(!playDir.exists()){
            playDir.mkdirs();
        }
        setPlayerDirectory(playDir.toPath());
    }

    private static void copyDefaultConfigs(File modDirectory) {
        // List of default config files
        String[] defaultConfigFiles = {"gearConfig.json", "mobOverrideConfig.json"};

        for (String configFileName : defaultConfigFiles) {
            File configFile = new File(modDirectory, configFileName);
            if (!configFile.exists()) {
                // The config file doesn't exist in the mod directory, copy it from resources
                try (InputStream resourceStream = SurvivalTrials.class.getResourceAsStream("/config/" + configFileName)) {
                    if (resourceStream != null) {
                        Files.copy(resourceStream, configFile.toPath());
                    } else {
                        LOGGER.error("Failed to find resource /config/{} for copying", configFileName);
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to copy default config file {}", configFileName, e);
                }
            }
        }
    }
    /*************************** GETTER/SETTER METHODS *************************************/
    public static Path getModDirectory() {
        return modDirectory;
    }
    private static void setModDirectory(Path modDir){
        modDirectory = modDir;
    }

    public static Path getPlayerDataDirectory() {
        return playerDataDirectory;
    }
    private static void setPlayerDirectory(Path playDir){
        playerDataDirectory = playDir;
    }
    public static List<OverriddenMobType> getOverriddenMobTypes() {
        return overriddenMobTypes;
    }

    public static void addPlayerConfig(PlayerConfig config) {
        playerConfigs.put(config.getPlayerUuid(), config);
    }

    public static PlayerConfig getPlayerConfig(UUID playerUuid) {
        return playerConfigs.get(playerUuid);
    }

    public static Map<UUID, PlayerConfig> getAllPlayerConfigs() {
        return Collections.unmodifiableMap(playerConfigs);
    }
}
