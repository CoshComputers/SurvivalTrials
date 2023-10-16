package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.InitialGearConfig;
import com.dsd.st.config.PlayerConfig;
import com.dsd.st.util.CustomLogger;
import com.dsd.st.util.FileAndDirectoryManager;
import com.dsd.st.util.PlayerManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
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
    private static final ConfigManager configManager = ConfigManager.getInstance();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID playerUUID = player.getUUID();
        Path playerConfigFilePath = FileAndDirectoryManager.getInstance().getPlayerDataDirectory().resolve(playerUUID.toString() + ".json");
        PlayerConfig playerConfig;
        if (Files.exists(playerConfigFilePath)) {
            // Player config file exists, load it
            playerConfig = configManager.getPlayerConfig(playerUUID);

            if(playerConfig == null){
                CustomLogger.getInstance().error("playerConfig File Exists, but playConfig not initialised.");
            }
            // Do something with playerConfig if necessary
        } else {
            // Player config file does not exist, give them initial gear
            if(ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getSurvivalTrialsMainConfig().isGiveInitialGear()){
                givePlayerGear(player);
            }
            // Create new player config object
            playerConfig = new PlayerConfig(playerUUID);
            if(playerConfig == null){
                CustomLogger.getInstance().error("playerConfig File Didn't Exist but new didnt initialise properly");

            }
        }
        playerConfig.setPlayerName(String.valueOf(player.getName().getString()));
        PlayerManager.getInstance().addPlayerConfig(playerConfig);
        configManager.savePlayerConfig(playerUUID,playerConfig);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID playerUUID = player.getUUID();
        PlayerConfig playerConfig = PlayerManager.getInstance().getPlayerConfig(playerUUID);
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
        CustomLogger.getInstance().debug("Loading initial gear...");
        if (configManager == null) {
            CustomLogger.getInstance().error("Config manager is null - it should have been initialised. Something is not behaving.");
            return;
        }
        if (configManager.getInitialGearConfigContainer().getInitialGearConfig() == null) {
            CustomLogger.getInstance().error("Initial gear config is null - it should have been initialised. Something is not behaving.");
            return;
        }
        for (InitialGearConfig.GearItem gearItem : configManager.getInitialGearConfigContainer().getInitialGearConfig().getInitialGear()) {
            ResourceLocation itemResourceLocation = new ResourceLocation(gearItem.getItem());
            Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);
            if (item == null | item == Items.AIR) {
                CustomLogger.getInstance().error(String.format("Item not found: %s", itemResourceLocation));
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
                addGearToPlayer(player,stack);
            }
        }
        CustomLogger.getInstance().debug("Finished with Config");
    }

    private static void addGearToPlayer(PlayerEntity player, ItemStack stack){
        Item item = stack.getItem();
        EquipmentSlotType slotType = null;
        if(item instanceof ArmorItem){
            ArmorItem armorItem = (ArmorItem) item;
            EquipmentSlotType eqp = armorItem.getSlot();
            CustomLogger.getInstance().debug(String.format("--------------EQP = [%s]", eqp.getName()));
            if(eqp == null){
                CustomLogger.getInstance().error(String.format("Armour Item [%s] Has no Slot, ignoring Item",armorItem.getName(stack)));
                return;
            }
            switch (eqp) {
                case HEAD:
                    slotType = EquipmentSlotType.HEAD;
                    break;
                case CHEST:
                    slotType = EquipmentSlotType.CHEST;
                    break;
                case LEGS:
                    slotType = EquipmentSlotType.LEGS;
                    break;
                case FEET:
                    slotType = EquipmentSlotType.FEET;
                    break;
            }
        }
        if(slotType != null) {
            player.setItemSlot(slotType,stack);
        }else{
            player.inventory.add(stack);
        }

    }

    private static int getValidEnchantmentLevel(Enchantment enchantment, int level) {
        int maxLevel = enchantment.getMaxLevel();
        if (level > maxLevel) {
            CustomLogger.getInstance().warn("Enchantment level " + level + " for " + enchantment.getRegistryName() +
                    " is too high. Maximum level is " + maxLevel +
                    ". Adjusting level to " + maxLevel + ".");
            return maxLevel;
        }
        return level;
    }
}