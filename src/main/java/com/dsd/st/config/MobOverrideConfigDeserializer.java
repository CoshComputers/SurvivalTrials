package com.dsd.st.config;

import com.dsd.st.config.ConfigManager.MobOverrideConfigContainer;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MobOverrideConfigDeserializer implements JsonDeserializer<MobOverrideConfigContainer> {
    @Override
    public MobOverrideConfigContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray mobSpawnOverridesArray = jsonObject.getAsJsonArray("mobSpawnOverrides");

        List<MobSpawnConfig.MobOverride> mobSpawnOverrides = new ArrayList<>();
        for (JsonElement mobOverrideElement : mobSpawnOverridesArray) {
            JsonObject mobOverrideObject = mobOverrideElement.getAsJsonObject();
            String mobType = mobOverrideObject.get("mobType").getAsString();
            boolean isBaby = mobOverrideObject.get("isBaby").getAsBoolean();
            MobSpawnConfig.MobOverride mobOverride = new MobSpawnConfig.MobOverride();
            mobOverride.setMobType(mobType);
            mobOverride.setBaby(isBaby);
            mobSpawnOverrides.add(mobOverride);
        }

        MobSpawnConfig mobSpawnConfig = new MobSpawnConfig();
        mobSpawnConfig.setMobSpawnOverrides(mobSpawnOverrides);

        ConfigManager.MobOverrideConfigContainer configContainer = new ConfigManager.MobOverrideConfigContainer();
        configContainer.setMobSpawnConfig(mobSpawnConfig);

        return configContainer;
    }
}
