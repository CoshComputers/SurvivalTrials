package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.entities.GiantZombieEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SurvivalTrials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SurvivalTrials.MOD_ID);
    public static final RegistryObject<EntityType<GiantZombieEntity>> GIANT_ZOMBIE = ENTITY_TYPES.register("giant_zombie", () ->
            EntityType.Builder.of(GiantZombieEntity::new, EntityClassification.MONSTER)
                    .sized(1.4F, 2.7F)
                    .build(new ResourceLocation(SurvivalTrials.MOD_ID, "giant_zombie").toString())
    );

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}