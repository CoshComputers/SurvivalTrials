package com.dsd.st.util;

import com.dsd.st.config.SurvivalTrialsConfig;
import com.dsd.st.config.ConfigManager.SurvivalTrialsConfigContainer;
import com.google.gson.*;
import java.lang.reflect.Type;

public class SurvivalTrialsConfigDeserializer implements JsonDeserializer<SurvivalTrialsConfigContainer> {

    @Override
    public SurvivalTrialsConfigContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject survivalTrialsConfigObject = jsonObject.getAsJsonObject("survivalTrialsConfig");

        boolean overrideMods = survivalTrialsConfigObject.get("overrideMods").getAsBoolean();
        boolean spawnGiants = survivalTrialsConfigObject.get("spawnGiants").getAsBoolean();
        boolean giveInitialGear = survivalTrialsConfigObject.get("giveInitialGear").getAsBoolean();
        boolean giveSpecialLoot = survivalTrialsConfigObject.get("giveSpecialLoot").getAsBoolean();
        boolean usePlayerHeads = survivalTrialsConfigObject.get("usePlayerHeads").getAsBoolean();
        boolean debugOn = survivalTrialsConfigObject.get("debugOn").getAsBoolean();

        SurvivalTrialsConfig.MainConfig mainConfig = new SurvivalTrialsConfig.MainConfig();
        mainConfig.setOverrideMods(overrideMods);
        mainConfig.setSpawnGiants(spawnGiants);
        mainConfig.setGiveInitialGear(giveInitialGear);
        mainConfig.setGiveSpecialLoot(giveSpecialLoot);
        mainConfig.setUsePlayerHeads(usePlayerHeads);
        mainConfig.setDebugOn(debugOn);

        SurvivalTrialsConfig survivalTrialsConfig = new SurvivalTrialsConfig();
        survivalTrialsConfig.setSurvivalTrialsConfig(mainConfig);  // Assume you have this setter method

        SurvivalTrialsConfigContainer configContainer = new SurvivalTrialsConfigContainer();
        configContainer.setSurvivalTrialsConfig(survivalTrialsConfig);

        return configContainer;
    }
}
