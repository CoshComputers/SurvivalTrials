package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.gson.reflect.TypeToken;


public class ConfigManager {
    private Gson defaultGson;
    private Gson mobOverrideGson;
    public InitialGearConfig initialGearConfig;
    public MobSpawnConfig mobSpawnConfig;

    public ConfigManager() {
        defaultGson = new Gson();
        mobOverrideGson = new GsonBuilder()
                .registerTypeAdapter(MobOverrideConfigContainer.class, new MobOverrideConfigDeserializer())
                .create();
    }
    public void loadGearConfig() {
        loadConfig("config/gearConfig.json", GearConfigContainer.class, defaultGson)
                .ifPresent(config -> this.initialGearConfig = config.initialGearConfig);
    }

    public void loadMobConfig() {
        loadConfig("config/mobOverrideConfig.json", MobOverrideConfigContainer.class, mobOverrideGson)
                .ifPresent(config -> this.mobSpawnConfig = config.mobSpawnConfig);
    }

    private <T> Optional<T> loadConfig(String path, Class<T> configClass, Gson gson) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                SurvivalTrials.LOGGER.error("Could not find {}", path);
                return Optional.empty();
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                T config = gson.fromJson(reader, configClass);
                return Optional.of(config);
            }
        } catch (IOException | JsonSyntaxException e) {
            SurvivalTrials.LOGGER.error("Failed to load {}", path, e);
            return Optional.empty();
        }
    }


    static class GearConfigContainer {
        public InitialGearConfig initialGearConfig;
    }

    public static class MobOverrideConfigContainer {
        public MobSpawnConfig mobSpawnConfig;

        public MobSpawnConfig getMobSpawnConfig() {
            return mobSpawnConfig;
        }

        public void setMobSpawnConfig(MobSpawnConfig mobSpawnConfig) {
            this.mobSpawnConfig = mobSpawnConfig;
        }


    }


    public static class MobSpawnConfig {
        @SerializedName("mobSpawnOverrides")
        private List<MobOverride> mobSpawnOverrides;

        public List<MobOverride> getMobSpawnOverrides() {
            return mobSpawnOverrides;
        }

        // Setter method for mobSpawnOverrides
        public void setMobSpawnOverrides(List<MobOverride> mobSpawnOverrides) {
            this.mobSpawnOverrides = mobSpawnOverrides;
        }

        // ... other methods if necessary
    }

    public static class MobOverride {
        @SerializedName("mobType")
        private String mobType;
        @SerializedName("isBaby")
        private boolean isBaby;

        public String getMobType() {
            return mobType;
        }
        public void setMobType(String mobType) {
            this.mobType = mobType;
        }

        public boolean isBaby() {
            return isBaby;
        }

        public void setBaby(boolean isBaby) {
            this.isBaby = isBaby;
        }
        // ... other methods if necessary
    }
}
