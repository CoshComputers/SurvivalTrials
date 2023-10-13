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

        SurvivalTrials.LOGGER.info("Config Directory = {}",SurvivalTrials.getModDirectory().toString());
        SurvivalTrials.LOGGER.info("Player Directory = {}",SurvivalTrials.getPlayerDataDirectory().toString());


        ConfigManager configManager = SurvivalTrials.getConfigManager();
        configManager.loadMobConfig();
        // Create Overridden Mobs using the loaded Mob Config
        List<OverriddenMobType> overriddenMobTypes = MobSpawnManager.createOverriddenMobs(
                configManager.getMobSpawnConfigContainer().getMobSpawnConfig().getMobSpawnOverrides()
        );

        if (!overriddenMobTypes.isEmpty()) {
            int overriddenMobCount = overriddenMobTypes.size();
            SurvivalTrials.setOverriddenMobTypes(overriddenMobTypes);
            SurvivalTrials.LOGGER.info("Number of Mobs Overridden = {}", overriddenMobCount);
            SurvivalTrials.LOGGER.info("First Overridden Mob = {}", overriddenMobTypes.get(0).toString());
        } else {
            SurvivalTrials.LOGGER.warn("No overridden mobs found or an error occurred while loading mob overrides.");
        }



        configManager.loadGearConfig();
        SurvivalTrials.LOGGER.info("Initial Gear Config: {}", configManager.getInitialGearConfigContainer().getInitialGearConfig());

        configManager.loadItemDropConfig();
        configManager.getItemDropConfigContainer().getItemDropConfig().debugItemDrops();
    }
}