package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.commands.ModConfigCommand;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.GiantConfig;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.config.SurvivalTrialsConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.util.*;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.nio.file.Path;
import java.util.List;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler {
    private static final ConfigManager configManager = ConfigManager.getInstance();

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        for (PlayerConfig playerConfig : PlayerManager.getInstance().getAllPlayerConfigs().values()) {
            configManager.savePlayerConfig(playerConfig.getPlayerUuid(),playerConfig);
        }

        CustomLogger.getInstance().outputBulkToConsole();
        CustomLogger.getInstance().outputtimerLogToConsole();
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        CustomLogger.getInstance().info(("****** Invoked the FMLDedicatedServerSetupEvent!! *********"));
        CommandDispatcher<CommandSource> commandDispatcher = event.getServer().getCommands().getDispatcher();
        ModConfigCommand.register(commandDispatcher);

        //-------COMMENT OUT BEFORE PUBLISHING--------------------------
        for (ServerWorld world : event.getServer().getAllLevels()) {
            // Set the time to midnight (18000 ticks)
            world.setDayTime(18000);
        }
        //-------------------------------------------------------------

    }

    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        CustomLogger.getInstance().info(("Survival Trials Mod Server Start Method invoked"));
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
        SurvivalTrialsConfig.MainConfig modConfig = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();
        CustomLogger.getInstance().info(String.format("Loaded Main Mod Config = %s",modConfig.toString()));
        boolean isDebugOn = modConfig.isDebugOn();
        CustomLogger.getInstance().setDebugOn(isDebugOn);

        configManager.loadGearConfig();
        CustomLogger.getInstance().info(String.format("Initial Gear Config: %s", configManager.getInitialGearConfigContainer().getInitialGearConfig()));

        configManager.loadItemDropConfig();
        //configManager.getItemDropConfigContainer().getItemDropConfig().debugItemDrops();

        configManager.loadGiantConfig();
        GiantConfig gcf = configManager.getGiantConfigContainer().getGiantConfig();
        CustomLogger.getInstance().debug(String.format("Giant Config in Memory is \n%s",gcf));



    }

    public void createPortal(World world, Block blockType) {
        BlockPos worldSpawn = new BlockPos(12,12,12);
        ServerWorld sw = (ServerWorld) world;

        int x = worldSpawn.getX();
        int y = worldSpawn.getY();
        int z = worldSpawn.getZ();
        Block edgingBlock;

        if (blockType.equals(Blocks.END_PORTAL)) {
            z += 10;
            edgingBlock = Blocks.END_STONE;
        } else if (blockType.equals(Blocks.NETHER_PORTAL)) {
            z -= 10;
            edgingBlock = Blocks.NETHERRACK;
        } else {
            System.out.println("Invalid block type for portal creation.");
            return;
        }

        for (int i = -1; i <= 3; i++) {
            for (int j = -1; j <= 3; j++) {
                if (i >= 0 && i < 3 && j >= 0 && j < 3) {
                   // world.setBlockState(new BlockPos(x + i, y, z + j), blockType.getDefaultState());
                } else {
                   // world.setBlockState(new BlockPos(x + i, y, z + j), edgingBlock.getDefaultState());
                }
            }
        }
    }

}