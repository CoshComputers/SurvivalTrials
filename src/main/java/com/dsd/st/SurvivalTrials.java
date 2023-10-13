package com.dsd.st;

import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.util.CustomLogger;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("survival_trials")
public class SurvivalTrials {
    private static Path modDirectory;
    private static Path playerDataDirectory;
    public static final String MOD_ID = "survival_trials";
    private static CustomLogger modLogger;
    public static ConfigManager configManager;
    private static final Map<UUID, PlayerConfig> playerConfigs = new HashMap<>();
    public static List<OverriddenMobType> overriddenMobTypes = new ArrayList<>();

    public SurvivalTrials() {

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
    public static CustomLogger getModLogger(){ return modLogger;}
    private void setup(final FMLCommonSetupEvent event) {
        modLogger = new CustomLogger();
        modLogger.info("We have loaded the Mod's Config");
    }


    private void doClientStuff(final FMLClientSetupEvent event) {


    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("survivaltrials", "helloworld", () -> {
           // LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {


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
    public static void setupDirectories(File rootDir) {
        Path rootPath = rootDir.toPath();
        Path modDir = rootPath.resolve("SurvivalTrials");

        try {
            Files.createDirectories(modDir);

        } catch (IOException e) {
            modLogger.error(String.format("Failed to create Root Directory at %sz\n%s", modDir.toString(), e));
        }

        setModDirectory(modDir);
        copyDefaultConfigs(modDir);

        Path playerDataDir = modDir.resolve("playerdata");

        try {
            Files.createDirectories(playerDataDir);

        } catch (IOException e) {
            modLogger.error(String.format("Failed to create PlayerData Directory at %s\n%s", playerDataDir.toString(), e));
        }

        setPlayerDirectory(playerDataDir);
    }


    public static void copyDefaultConfigs(Path modDirectory) {
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
                        modLogger.info(String.format("Copying default config file: %s", entry.getFileName()));
                        Files.copy(entry, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (Exception e) {
            modLogger.error(String.format("Failed to copy default config files\n%s", e));
        }
    }

    /*************************** GETTER/SETTER METHODS *************************************/
    public static void setOverriddenMobTypes(List<OverriddenMobType> overriddenMobTypes) {
        SurvivalTrials.overriddenMobTypes = overriddenMobTypes;
    }
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
