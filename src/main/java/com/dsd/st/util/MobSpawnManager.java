package com.dsd.st.util;

import com.dsd.st.config.MobSpawnConfig;
import com.dsd.st.customisations.OverriddenMobType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MobSpawnManager {

    private static final int TOTAL_WEIGHT = 100;
    private static final int DEFAULT_WEIGHT = 10;
    private static final MobSpawnManager instance = new MobSpawnManager();
    private final List<OverriddenMobType> overriddenMobTypes = new CopyOnWriteArrayList<>();
    private MobSpawnManager() {

    }

    public static MobSpawnManager getInstance() {
        return instance;
    }

    public synchronized void setOverriddenMobTypes(List<OverriddenMobType> newOverriddenMobTypes) {
        overriddenMobTypes.clear();
        overriddenMobTypes.addAll(newOverriddenMobTypes);
    }

    public List<OverriddenMobType> getOverriddenMobTypes() {

        return Collections.unmodifiableList(overriddenMobTypes);
    }

    public static List<OverriddenMobType> createOverriddenMobs(List<MobSpawnConfig.MobOverride> mobOverrides) {
        List<OverriddenMobType> newOverriddenMobs = new ArrayList<>();

        // Ensure the total weight is 100
        int noneWeight;
        int blazeWeight;
        int endermanWeight;

        for (MobSpawnConfig.MobOverride mobOverride : mobOverrides) {
            blazeWeight = mobOverride.getBlazeWeighting();
            endermanWeight = mobOverride.getEndermenWeighting();
            if(blazeWeight < 0) blazeWeight = DEFAULT_WEIGHT;
            if(endermanWeight < 0) endermanWeight = DEFAULT_WEIGHT;
            noneWeight = TOTAL_WEIGHT - blazeWeight - endermanWeight;

            CustomLogger.getInstance().info(String.format("Weighting: Blaze [%d], Endermen [%d], Normal [%d]", blazeWeight,endermanWeight,noneWeight));

            ResourceLocation mobTypeResourceLocation = new ResourceLocation(mobOverride.getMobType());
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(mobTypeResourceLocation);
            // Ensure entityType is not null before creating OverriddenMobType instances
            if (entityType != null) {
                addOverriddenMobs(newOverriddenMobs, entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.NONE, noneWeight);
                addOverriddenMobs(newOverriddenMobs, entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.BLAZE, blazeWeight);
                addOverriddenMobs(newOverriddenMobs, entityType, mobOverride.isBaby(), OverriddenMobType.Appearance.ENDERMAN, endermanWeight);
            } else {
                CustomLogger.getInstance().error(String.format("Entity type not found: %s", mobOverride.getMobType()));
            }
        }

        return newOverriddenMobs;
    }

    private static void addOverriddenMobs(List<OverriddenMobType> overriddenMobs, EntityType<?> entityType, boolean isBaby, OverriddenMobType.Appearance appearance, int weight) {
        for (int i = 0; i < weight; i++) {
            overriddenMobs.add(new OverriddenMobType(entityType, isBaby, appearance));
        }
    }

}
