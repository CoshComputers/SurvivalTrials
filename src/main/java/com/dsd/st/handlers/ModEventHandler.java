package com.dsd.st.handlers;

import com.dsd.st.SurvivalTrials;
import com.dsd.st.entities.GiantZombieEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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
            EntityType.Builder.of(GiantZombieEntity::createForRegistration, EntityClassification.MONSTER)
                    .build(new ResourceLocation(SurvivalTrials.MOD_ID, "giant_zombie").toString()));




    static {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(GIANT_ZOMBIE.get(), GiantZombieEntity.setCustomAttributes().build());
    }
}
