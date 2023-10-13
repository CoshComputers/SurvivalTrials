package com.dsd.st.util;

import com.dsd.st.SurvivalTrials;
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

        // Ensure the total weight is 100
        int NONE_WEIGHT;
        int BLAZE_WEIGHT;
        int ENDERMAN_WEIGHT;

        for (MobSpawnConfig.MobOverride mobOverride : mobOverrides) {
            BLAZE_WEIGHT = mobOverride.getBlazeWeighting();
            ENDERMAN_WEIGHT = mobOverride.getEndermenWeighting();
            if(BLAZE_WEIGHT < 0) BLAZE_WEIGHT = 10;
            if(ENDERMAN_WEIGHT < 0) ENDERMAN_WEIGHT = 10;
            NONE_WEIGHT = 100 - BLAZE_WEIGHT - ENDERMAN_WEIGHT;

            SurvivalTrials.getModLogger().debug(String.format("Weighting: Blaze [%d], Endermen [%d], Normal [%d]", BLAZE_WEIGHT,ENDERMAN_WEIGHT,NONE_WEIGHT));

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
