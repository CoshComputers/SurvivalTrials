package com.dsd.st.util;

import com.dsd.st.config.MobSpawnConfig;
import com.dsd.st.customisations.OverriddenMobType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MobSpawnManager {
    private MobSpawnManager(MobSpawnConfig mobSpawnConfig) {

    }



    public static List<OverriddenMobType> createOverriddenMobs(List<MobSpawnConfig.MobOverride> mobOverrides) {
        List<OverriddenMobType> overriddenMobs = new ArrayList<>();

        for (MobSpawnConfig.MobOverride mobOverride : mobOverrides) {
            ResourceLocation mobTypeResourceLocation = new ResourceLocation(mobOverride.getMobType());
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(mobTypeResourceLocation);

            // Ensure entityType is not null before creating OverriddenMobType instances
            if (entityType != null) {
                overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.NONE));
                overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.BLAZE));
                overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.ENDERMAN));
            } else {
                // Handle the case where entityType is null, e.g., log an error
            }
        }

        return overriddenMobs;
    }



    // ... other methods if necessary
}
