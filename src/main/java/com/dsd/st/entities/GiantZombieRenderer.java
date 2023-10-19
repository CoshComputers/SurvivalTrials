package com.dsd.st.entities;


import com.dsd.st.util.CustomLogger;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class GiantZombieRenderer extends MobRenderer<GiantZombieEntity, ZombieModel<GiantZombieEntity>> {

    private static final ResourceLocation GIANT_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final CustomLogger logger = CustomLogger.getInstance();
    public GiantZombieRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ZombieModel<>(0.0F, false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantZombieEntity entity) {
        return GIANT_ZOMBIE_TEXTURES;
    }

    @Override
    public void render(GiantZombieEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {

        logger.preCall("Giant render- pushPose");
        matrixStack.pushPose();
        logger.postCall();
        logger.outputtimerLogToConsole();
        float scaleFactor = 2;//entity.getScaleFactor();  // Assuming getScaleFactor is a method on your GiantZombieEntity class
        logger.preCall("Giant render- matrixScale");
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        logger.postCall();
        logger.outputtimerLogToConsole();
        //Vector3d position = entity.position();
        logger.preCall("Giant render- super");
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        logger.postCall();
        logger.outputtimerLogToConsole();
        logger.preCall("Giant render- popPose");
        matrixStack.popPose();
        logger.postCall();
        logger.outputtimerLogToConsole();

    }
}

