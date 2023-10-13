package com.dsd.st.util;

import com.dsd.st.config.MobSpawnConfig;
import com.dsd.st.config.ConfigManager.MobSpawnConfigContainer;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MobSpawnConfigDeserializer implements JsonDeserializer<MobSpawnConfigContainer> {

    @Override
    public MobSpawnConfigContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
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

        MobSpawnConfigContainer configContainer = new MobSpawnConfigContainer();
        configContainer.setMobSpawnConfig(mobSpawnConfig);

        return configContainer;
    }
}
