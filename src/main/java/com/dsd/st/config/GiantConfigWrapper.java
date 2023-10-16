package com.dsd.st.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GiantConfigWrapper {
    private GiantConfig giantConfig;

    public GiantConfig getGiantConfig() {
        return giantConfig;
    }

    public void setGiantConfig(GiantConfig giantConfig) {
        this.giantConfig = giantConfig;
    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
