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

        // Define the weights- *change to be from the config file when this is working
        final int NONE_WEIGHT = 90;
        final int BLAZE_WEIGHT = 5;
        final int ENDERMAN_WEIGHT = 5;

        // Ensure the total weight is 100
        assert NONE_WEIGHT + BLAZE_WEIGHT + ENDERMAN_WEIGHT == 100;

        for (MobSpawnConfig.MobOverride mobOverride : mobOverrides) {
            ResourceLocation mobTypeResourceLocation = new ResourceLocation(mobOverride.getMobType());
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(mobTypeResourceLocation);

            // Ensure entityType is not null before creating OverriddenMobType instances
            if (entityType != null) {
                for (int i = 0; i < NONE_WEIGHT; i++) {
                    overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.NONE));
                }
                for (int i = 0; i < BLAZE_WEIGHT; i++) {
                    overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.BLAZE));
                }
                for (int i = 0; i < ENDERMAN_WEIGHT; i++) {
                    overriddenMobs.add(new OverriddenMobType(entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.ENDERMAN));
                }
            } else {
                // Handle the case where entityType is null, e.g., log an error
            }
        }

        return overriddenMobs;
    }


}
