package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.ItemDropConfig;
import com.dsd.st.util.CustomLogger;
import com.dsd.st.util.EnumTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID,  bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobInteractionEventHandler {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        DamageSource source = event.getSource();
        World world = entity.level;

        if (source.getDirectEntity() instanceof PlayerEntity || source.getEntity() instanceof PlayerEntity) {
            if (entity instanceof MonsterEntity) {
                MonsterEntity mob = (MonsterEntity) entity;
                ItemStack headItem = mob.getItemBySlot(EquipmentSlotType.HEAD);
                EnumTypes.SkullDropMapping mobType = EnumTypes.SkullDropMapping.fromHeadItem(headItem);

                ItemDropConfig.Drop dropItem = getItemForMobType(mobType);

                PlayerEntity player = (PlayerEntity) source.getDirectEntity();
                int lootingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, player.getMainHandItem());
                CustomLogger.getInstance().debug(String.format("Player Entity %s - Has Looting Level %d",player.getDisplayName(),lootingLevel));
                int dropAmount = calculateDropAmount(dropItem, lootingLevel);

                if( dropItemInWorld(world,entity, dropItem.getItem(), dropAmount)) {
                    CustomLogger.getInstance().debug(String.format("Dropped %d of %s",dropAmount,dropItem.getItem()));
                    if (event.isCancelable()) event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static boolean dropItemInWorld(World world, LivingEntity entity, String itemName, int quantity) {
        // Get the Item object from the string
        ResourceLocation itemResourceLocation = new ResourceLocation(itemName);
        Item item = Registry.ITEM.getOptional(itemResourceLocation).orElse(Items.AIR);  // Default to air if item is not found
        if (item == Items.AIR) {
            // Log a warning if the item name is invalid
            CustomLogger.getInstance().warn(String.format("Invalid item name: %s", itemName));
            return false;
        }

        // Create an ItemStack with the specified quantity
        ItemStack itemStack = new ItemStack(item, quantity);
        // Create an ItemEntity and spawn it in the world at the entity's location
        ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), itemStack);
        world.addFreshEntity(itemEntity);
        return true;
    }

    private static int calculateDropAmount(ItemDropConfig.Drop drop, int lootingLevel) {
        // Get the base quantity
        int baseQuantity = drop.getQuantity() + lootingLevel;
        // Generate a random quantity between 1 and the base quantity (inclusive)
        int finalDropAmount= new Random().nextInt(baseQuantity) + 1;
        return finalDropAmount;
    }

    private static ItemDropConfig.Drop getItemForMobType(EnumTypes.SkullDropMapping mobType) {
        ItemDropConfig.Drop itemToDrop = null;
        ItemDropConfig itemDrops = ConfigManager.getInstance().getItemDropConfigContainer().getItemDropConfig();
        if (itemDrops.getDrops().isEmpty()) {
            CustomLogger.getInstance().error("No Drop Items configured for Baby Zombie to drop setting to Rotten Flesh");
            itemToDrop = new ItemDropConfig.Drop("rotten_flesh", 1);
        }else {
            if(mobType == EnumTypes.SkullDropMapping.UNKNOWN){
                itemToDrop = itemDrops.getRandomDrop();
            }else{
                itemToDrop = mobType.getDropItemAsDropConfig(1);
            }
        }
        if(itemToDrop == null){
            CustomLogger.getInstance().error("ItemToDrop is null, this is incorrect - setting to Rotten Flesh");
            itemToDrop = new ItemDropConfig.Drop("rotten_flesh", 1);
        }
        return itemToDrop;
    }


}
