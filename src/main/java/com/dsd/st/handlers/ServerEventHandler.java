package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.PlayerConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.io.File;

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
    }
}