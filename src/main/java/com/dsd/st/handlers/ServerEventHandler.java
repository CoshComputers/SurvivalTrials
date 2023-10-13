package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.util.MobSpawnManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.io.File;
import java.util.List;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class ServerEventHandler {

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        ConfigManager configManager = SurvivalTrials.configManager;
        for (PlayerConfig playerConfig : SurvivalTrials.getAllPlayerConfigs().values()) {
            configManager.savePlayerConfig(playerConfig.getPlayerUuid(),playerConfig);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {

        MinecraftServer server = event.getServer();
        File serverDir = server.getWorldPath(FolderName.ROOT).toFile().getParentFile();
        SurvivalTrials.setupDirectories(serverDir);
        ConfigManager configManager = SurvivalTrials.getConfigManager();

        SurvivalTrials.getModLogger().info(String.format("Config Directory = %s",SurvivalTrials.getModDirectory().toString()));
        SurvivalTrials.getModLogger().info(String.format("Player Directory = %s",SurvivalTrials.getPlayerDataDirectory().toString()));

        configManager.loadMobConfig();
        // Create Overridden Mobs using the loaded Mob Config
        List<OverriddenMobType> overriddenMobTypes = MobSpawnManager.createOverriddenMobs(
                configManager.getMobSpawnConfigContainer().getMobSpawnConfig().getMobSpawnOverrides()
        );

        if (!overriddenMobTypes.isEmpty()) {
            int overriddenMobCount = overriddenMobTypes.size();
            SurvivalTrials.setOverriddenMobTypes(overriddenMobTypes);
            SurvivalTrials.getModLogger().info(String.format("Number of Mobs Overridden = %s", overriddenMobCount));
            SurvivalTrials.getModLogger().info(String.format("First Overridden Mob = %s", overriddenMobTypes.get(0).toString()));
        } else {
            SurvivalTrials.getModLogger().warn("No overridden mobs found or an error occurred while loading mob overrides.");
        }
        configManager.loadGearConfig();
        SurvivalTrials.getModLogger().info(String.format("Initial Gear Config: %s", configManager.getInitialGearConfigContainer().getInitialGearConfig()));

        configManager.loadItemDropConfig();
        configManager.getItemDropConfigContainer().getItemDropConfig().debugItemDrops();

        configManager.loadSurvivalTrialsConfig();
        boolean isDebugOn = SurvivalTrials.getConfigManager().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig().isDebugOn();
        SurvivalTrials.getModLogger().setDebugOn(isDebugOn);
    }
}