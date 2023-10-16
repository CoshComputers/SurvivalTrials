package com.dsd.st.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InitialGearConfigWrapper {
    private InitialGearConfig initialGearConfig;

    public InitialGearConfig getInitialGearConfig() {
        return initialGearConfig;
    }

    public void setInitialGearConfig(InitialGearConfig initialGearConfig) {
        this.initialGearConfig = initialGearConfig;
    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}



