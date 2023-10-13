package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.InitialGearConfig;
import com.dsd.st.config.PlayerConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID playerUUID = player.getUUID();
        ConfigManager configManager = SurvivalTrials.configManager;  // Assuming you have a way to access ConfigManager
        Path playerConfigFilePath = SurvivalTrials.getPlayerDataDirectory().resolve(playerUUID.toString() + ".json");
        PlayerConfig playerConfig;
        if (Files.exists(playerConfigFilePath)) {
            // Player config file exists, load it
            playerConfig = configManager.getPlayerConfig(playerUUID);
            // Do something with playerConfig if necessary
        } else {
            // Player config file does not exist, give them initial gear
            givePlayerGear(player);
            // Create new player config object
            playerConfig = new PlayerConfig(playerUUID);
        }
        SurvivalTrials.addPlayerConfig(playerConfig);
        SurvivalTrials.configManager.savePlayerConfig(playerUUID,playerConfig);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID playerUUID = player.getUUID();
        PlayerConfig playerConfig = SurvivalTrials.getPlayerConfig(playerUUID);
        ConfigManager configManager = SurvivalTrials.configManager;
        configManager.savePlayerConfig(playerUUID,playerConfig);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        // Your code here if necessary
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // Your code here if necessary
    }

    /********************************* Internal Helper Methods ****************************************/
    private static void givePlayerGear(PlayerEntity player) {
        SurvivalTrials.LOGGER.debug("Loading initial gear...");
        if (SurvivalTrials.configManager == null) {
            SurvivalTrials.LOGGER.error("Config manager is null");
            return;
        }
        if (SurvivalTrials.configManager.getInitialGearConfigContainer().getInitialGearConfig() == null) {
            SurvivalTrials.LOGGER.error("Initial gear config is null");
            return;
        }
        for (InitialGearConfig.GearItem gearItem : SurvivalTrials.configManager.getInitialGearConfigContainer().getInitialGearConfig().getInitialGear()) {
            ResourceLocation itemResourceLocation = new ResourceLocation(gearItem.getItem());
            Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);
            if (item == null | item == Items.AIR) {
                SurvivalTrials.LOGGER.error("Item not found: {}", itemResourceLocation);
            } else {
                ItemStack stack = new ItemStack(item);
                Map<Enchantment, Integer> enchantments = new HashMap<>();
                for (Map.Entry<String, Integer> enchantmentEntry : gearItem.getEnchantments().entrySet()) {
                    ResourceLocation enchantmentResourceLocation = new ResourceLocation(enchantmentEntry.getKey());
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantmentResourceLocation);
                    if (enchantment != null) {
                        int validLevel = getValidEnchantmentLevel(enchantment, enchantmentEntry.getValue());
                        enchantments.put(enchantment, enchantmentEntry.getValue());
                    }
                }
                EnchantmentHelper.setEnchantments(enchantments, stack);
                player.inventory.add(stack);
            }
        }
        SurvivalTrials.LOGGER.debug("Finished with Config");
    }

    private static int getValidEnchantmentLevel(Enchantment enchantment, int level) {
        int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel) {
            SurvivalTrials.LOGGER.warn("Enchantment level " + level + " for " + enchantment.getRegistryName() +
                    " is too high. Maximum level is " + maxLevel +
                    ". Adjusting level to " + maxLevel + ".");
            return maxLevel;
        }
        return level;
    }
}