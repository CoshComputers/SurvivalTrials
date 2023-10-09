package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.InitialGearConfig;
import com.dsd.st.config.InitialGearConfig.GearItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class PlayerJoinHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerLoggedInEvent event) {
        SurvivalTrials.LOGGER.debug("Player joined: {}", event.getPlayer().getName().getString());

        PlayerEntity player = event.getPlayer();
        World world = player.getCommandSenderWorld();

        if (!world.isClientSide) {  // Ensure this runs on the server side
            SurvivalTrials.LOGGER.debug("Loading initial gear...");
            if (SurvivalTrials.configManager == null) {
                SurvivalTrials.LOGGER.error("Config manager is null");
                return;
            }
            if (SurvivalTrials.configManager.initialGearConfig == null) {
                SurvivalTrials.LOGGER.error("Initial gear config is null");
                return;
            }
            for (GearItem gearItem : SurvivalTrials.configManager.initialGearConfig.getInitialGear()) {
                ResourceLocation itemResourceLocation = new ResourceLocation(gearItem.getItem());
                Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);
                if (item == null | item == Items.AIR) {
                    SurvivalTrials.LOGGER.error("Item not found: {}", itemResourceLocation);
                }else {
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
                SurvivalTrials.LOGGER.debug("Finished with Config");
            }
        }
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
