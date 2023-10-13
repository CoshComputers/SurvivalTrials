package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.customisations.OverriddenMobType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class MobSpawnEventHandler {

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        EntityType<?> entityType = null;

        if (event.isSpawner()) {
            //Thread.dumpStack();
            SurvivalTrials.LOGGER.info("[My CheckSpawn]Event came from spawner so bypassing");
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;

        if (!world.isClientSide && entity instanceof MonsterEntity && !(entity instanceof EnderDragonEntity)) {
            List<OverriddenMobType> overriddenMobTypes = SurvivalTrials.getOverriddenMobTypes();

            // If we have mobs to use.
            if (!overriddenMobTypes.isEmpty()) {
                BlockPos pos = entity.blockPosition();
                Random random = new Random();
                OverriddenMobType randomMobType = overriddenMobTypes.get(random.nextInt(overriddenMobTypes.size()));
                Entity newEntity = (LivingEntity) randomMobType.createEntity(world);

                if(newEntity != null){
                    newEntity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.yRot, entity.xRot);
                    boolean success = world.addFreshEntity(newEntity);
                    if (!success) {
                       SurvivalTrials.LOGGER.error("[MY CHECKSPAWN]Failed to spawn entity " + randomMobType.toString());
                    }
                    //still setting to stop other spawns as need to know this is working.
                    event.setResult(Event.Result.DENY);
                    if (event.isCancelable()) event.setCanceled(true);
                }else{
                    SurvivalTrials.LOGGER.error("[MY CHECKSPAWN][E] Failed to create Overriden Entity");
                }
            }
        }
    }
}
