package com.dsd.st.entities;


import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class GiantZombieRenderer extends MobRenderer<GiantZombieEntity, ZombieModel<GiantZombieEntity>> {

    private static final ResourceLocation GIANT_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");

    public GiantZombieRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ZombieModel<>(0.0F, false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantZombieEntity entity) {
        return GIANT_ZOMBIE_TEXTURES;
    }
}

