package com.dsd.st.customisations;

import com.dsd.st.SurvivalTrials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class OverriddenMobType {
    public enum Appearance {
        NONE,
        BLAZE,
        ENDERMAN
    }
    private final EntityType<?> entityType;
    private final boolean isBaby;
    private final Appearance appearance;

    public OverriddenMobType(EntityType<?> entityType,
                             boolean isBaby, Appearance appearance) {
        this.entityType = entityType;
        this.isBaby = isBaby;
        this.appearance = appearance;
    }

    // ... other methods
    public Entity createEntity(World world) {
        try {
            Entity entity = this.entityType.create(world);
            if (entity != null) {
                if (entity instanceof ZombieEntity && this.isBaby) {
                    ((ZombieEntity) entity).setBaby(true);
                }
                if (entity instanceof MonsterEntity) {
                    setAppearance((MonsterEntity) entity);
                }
                return entity;
            } else {
                // Log an error message if the entity could not be created
                SurvivalTrials.LOGGER.error("Failed to create entity of type {}", this.entityType);
                return null;
            }
        } catch (Exception e) {
            // Log the exception
            SurvivalTrials.LOGGER.error("Exception occurred while creating entity of type {}: {}", this.entityType, e.getMessage());
            return null;
        }
    }

    private void setAppearance(MonsterEntity monsterEntity) {

        ItemStack head = ItemStack.EMPTY;
        ItemStack handItem = ItemStack.EMPTY;

        switch(this.appearance) {
            case BLAZE:  // Blaze head and blaze rod
                head = getSkull("MHF_Blaze");
                handItem = new ItemStack(Items.BLAZE_ROD);
                break;
            case ENDERMAN:  // Enderman head and ender pearl
                head = getSkull("MHF_Enderman");
                handItem = new ItemStack(Items.ENDER_PEARL);
                break;
            default:
                break;
        }

        if (!head.isEmpty()) {
            monsterEntity.setItemSlot(EquipmentSlotType.HEAD, head);
        }
        if (!handItem.isEmpty()) {
            monsterEntity.setItemSlot(EquipmentSlotType.MAINHAND, handItem);
        }
    }

    private ItemStack getSkull(String skullOwner) {
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
        CompoundNBT tag = skull.getOrCreateTag();
        tag.putString("SkullOwner", skullOwner);
        return skull;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OverriddenMobType {");
        sb.append("entityType: ").append(entityType.getRegistryName());
        sb.append(", isBaby: ").append(isBaby);
        sb.append(", appearance: ").append(appearance.name());
        sb.append(" }");

        return sb.toString();
    }
}
