package com.dsd.st.entities;

import com.dsd.st.config.ConfigManager;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class GiantZombieEntity extends ZombieEntity {

    private final float scaleFactor;

    public GiantZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
        super(type, worldIn);
        float minSize = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig().getMinSize();
        float maxSize = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig().getMaxSize();
        this.scaleFactor = ThreadLocalRandom.current().nextFloat() * (maxSize - minSize) + minSize;
        float inflationAmount = scaleFactor - 1;
        // Recalculating the bounding box based on scaleFactor.
        setBoundingBox(getBoundingBox().inflate(inflationAmount,inflationAmount,inflationAmount));
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return super.getStandingEyeHeight(poseIn, sizeIn) * scaleFactor;
    }
}
