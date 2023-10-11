package com.dsd.st.customisations;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.config.MobSpawnConfig;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SurvivalTrialsSpawner extends AbstractSpawner {
    @Override
    public void broadcastEvent(int p_98267_1_) {

    }

    @Override
    public World getLevel() {
        return null;
    }

    @Override
    public BlockPos getPos() {
        return null;
    }

}
