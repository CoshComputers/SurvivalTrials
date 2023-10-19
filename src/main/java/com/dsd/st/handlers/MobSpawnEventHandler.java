package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.ConfigManager;
import com.dsd.st.config.GiantConfig;
import com.dsd.st.config.SurvivalTrialsConfig;
import com.dsd.st.customisations.OverriddenMobType;
import com.dsd.st.entities.GiantZombieEntity;
import com.dsd.st.util.CustomLogger;
import com.dsd.st.util.MobSpawnManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobSpawnEventHandler {

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {

        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        BlockPos pos = entity.blockPosition(); //needed in a number of places.
        int terrainHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());
        SurvivalTrialsConfig.MainConfig mainConfig = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().
                getSurvivalTrialsConfig().getSurvivalTrialsMainConfig();

        //Spawning should only happen on the client side
        if (world.isClientSide) {
            return;
        }

        //If the spawning is triggered by a spawner special consideration is needed.
        if (event.isSpawner()) {
            //Thread.dumpStack();
            CustomLogger.getInstance().debug("Event came from spawner so bypassing");
            return;
        }

        if (entity instanceof MonsterEntity && !(entity instanceof EnderDragonEntity)) {
            List<OverriddenMobType> overriddenMobTypes = MobSpawnManager.getInstance().getOverriddenMobTypes();
            //If we get here we are on the server and not spawning because of a Spawner, and we're a
            //monster entity so we can first check to
            //see if Spawning Giants is turned on, and if so randomly spawn one.
            if (mainConfig.isSpawnGiants()) {
                GiantConfig giantConfig = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig();
                float random = world.random.nextFloat();
                if (random < giantConfig.getSpawnFrequency()) {
                    GiantZombieEntity giant = createGiant(world, pos);
                    if (giant != null) {
                        giant.setNoAi(true);
                        world.addFreshEntity(giant);
                    }
                }
                event.setResult(Event.Result.DENY);
                if (event.isCancelable()) event.setCanceled(true);
                return;
            }

            //check if the configuration for the Mod is set to override Mob spawning. If NOT, return.
            if (!mainConfig.isOverrideMobs()) {
                return;
            }
            // If we have mobs to use.
            if (!overriddenMobTypes.isEmpty()) {
                Random random = new Random();
                OverriddenMobType randomMobType = overriddenMobTypes.get(random.nextInt(overriddenMobTypes.size()));
                Entity newEntity = (LivingEntity) randomMobType.createEntity(world);

                if (newEntity != null) {
                    newEntity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.yRot, entity.xRot);
                    boolean success = world.addFreshEntity(newEntity);
                    if (!success) {
                        CustomLogger.getInstance().error(String.format("Failed to spawn entity %s", randomMobType.toString()));
                    }
                    //still setting to stop other spawns as need to know this is working.
                    event.setResult(Event.Result.DENY);
                    if (event.isCancelable()) event.setCanceled(true);
                } else {
                    CustomLogger.getInstance().error("Failed to create Overridden Entity");
                }
            }
        }
    }


    //Factory Method to create that is used in the Registration of the entity:
    private static synchronized GiantZombieEntity createGiant(World world, BlockPos pos) {
       // GiantConfig gcf = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig();
        GiantZombieEntity giant = ModEventHandler.GIANT_ZOMBIE.get().create(world);
        int terrainHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());
        BlockPos newPos = new BlockPos(pos.getX(), terrainHeight, pos.getZ());
        float lScaleFactor = 2; //gcf.getScaleFactor();
        // Synchronized method to ensure thread-safety when creating entities
        if (!isSuitableLocation(world, lScaleFactor, newPos)) {
            CustomLogger.getInstance().debug("The location to spawn the Giant in was unsuitable for spawning");
            return null;
        } else {
            //CustomLogger.getInstance().debug("Safe to Spawn the Giant, so setting scale and dimensions");
            giant.moveTo(newPos, 0, 0);
            return giant;
        }

    }

    public static boolean isSuitableLocation(World world, float scaleFactor, BlockPos pos) {
        float origHeight = 1.8f;

        double height = origHeight * scaleFactor;  // Get the original dimensions and scale them
        for (int y = 0; y < height; y++) {
            BlockState state = world.getBlockState(pos.above(y));
            if (!state.isAir()) {
                return false;  // Found a non-air block, location is not suitable
            }
        }
        return true;  // All checked blocks are air, location is suitable
    }

}
