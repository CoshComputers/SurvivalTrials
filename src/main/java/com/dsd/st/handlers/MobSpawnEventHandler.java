package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.ConfigManager.MobOverride;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID)
public class MobSpawnEventHandler {

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getEntity() instanceof MonsterEntity && !(event.getEntity() instanceof EnderDragonEntity)) {
            // Cancel the original spawn
            event.setResult(Result.DENY);
            // Get the world
            World world = (World) event.getWorld();

            // Get the list of mobs from the config
            List<MobOverride> mobOverrides = SurvivalTrials.configManager.mobSpawnConfig.getMobSpawnOverrides();

            if (!mobOverrides.isEmpty()) {
                // Choose a random mob override from the config
                Random random = new Random();
                MobOverride randomOverride = mobOverrides.get(random.nextInt(mobOverrides.size()));
                String mobType = randomOverride.getMobType();
                SurvivalTrials.LOGGER.debug(String.format("SPAWNING A [%s]",mobType));
                // Parse the mob type from the config
                ResourceLocation mobTypeResourceLocation = new ResourceLocation(mobType);
                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(mobTypeResourceLocation);

                if (entityType != null) {
                    // Create a new entity of the selected type
                    MonsterEntity newEntity = (MonsterEntity) entityType.create(world);

                    if (newEntity != null) {
                        // Set the baby status if applicable
                        if (newEntity instanceof ZombieEntity) {
                            ((ZombieEntity) newEntity).setBaby(randomOverride.isBaby());
                        }

                        // Set the new entity's position to the original spawn position
                        newEntity.setPos(event.getX(),event.getY(),event.getZ());
                        //newEntity.setRot(world.random.nextFloat()*360.0F,0.F);


                        // Spawn the new entity in the world
                        world.addFreshEntity(newEntity);
                    }
                }
            }
        }
    }
}

