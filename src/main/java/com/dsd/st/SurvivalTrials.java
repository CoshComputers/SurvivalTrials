package com.dsd.st;

import com.dsd.st.util.CustomLogger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("survival_trials")
public class SurvivalTrials {
    public static final String MOD_ID = "survival_trials";

    public SurvivalTrials() {
        CustomLogger.getInstance().info("We have instantiated Survival Trials");
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


/*
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobSpawnEventHandler());
        MinecraftForge.EVENT_BUS.register(new MobInteractionEventHandler());
*/

    }


    private void setup(final FMLCommonSetupEvent event) {
        CustomLogger.getInstance().info("We have executed SETUP");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        CustomLogger.getInstance().info("We have executed doClientStuff");
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        CustomLogger.getInstance().info("We have executed enqueueIMC");
    }

    private void processIMC(final InterModProcessEvent event) {
        CustomLogger.getInstance().info("We have executed processIMC");
    }

}
