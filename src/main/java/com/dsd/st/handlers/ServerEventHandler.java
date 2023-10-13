package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class ServerEventHandler {
    private static final ConfigManager configManager = ConfigManager.getInstance();
    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        for (PlayerConfig playerConfig : PlayerManager.getInstance().getAllPlayerConfigs().values()) {
            configManager.savePlayerConfig(playerConfig.getPlayerUuid(),playerConfig);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ServerManager.getInstance().setServer(server);
        Path serverDir = server.getWorldPath(FolderName.ROOT);
        FileAndDirectoryManager.initialize(serverDir);

        CustomLogger.getInstance().info(String.format("Config Directory = %s",FileAndDirectoryManager.getInstance().getModDirectory().toString()));
        CustomLogger.getInstance().info(String.format("Player Directory = %s",FileAndDirectoryManager.getInstance().getPlayerDataDirectory().toString()));

        configManager.loadMobConfig();
        // Create Overridden Mobs using the loaded Mob Config
        List<OverriddenMobType> overriddenMobTypes = MobSpawnManager.createOverriddenMobs(
                configManager.getMobSpawnConfigContainer().getMobSpawnConfig().getMobSpawnOverrides()
        );

        if (!overriddenMobTypes.isEmpty()) {
            int overriddenMobCount = overriddenMobTypes.size();
            MobSpawnManager.getInstance().setOverriddenMobTypes(overriddenMobTypes);
            CustomLogger.getInstance().info(String.format("Number of Mobs Overridden = %s", overriddenMobCount));
            CustomLogger.getInstance().info(String.format("First Overridden Mob = %s", overriddenMobTypes.get(0).toString()));
        } else {
            CustomLogger.getInstance().warn("No overridden mobs found or an error occurred while loading mob overrides.");
        }

        configManager.loadSurvivalTrialsConfig();
        boolean isDebugOn = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig().isDebugOn();
        CustomLogger.getInstance().setDebugOn(isDebugOn);

        configManager.loadGearConfig();
        CustomLogger.getInstance().info(String.format("Initial Gear Config: %s", configManager.getInitialGearConfigContainer().getInitialGearConfig()));

        configManager.loadItemDropConfig();
        configManager.getItemDropConfigContainer().getItemDropConfig().debugItemDrops();


    }
}