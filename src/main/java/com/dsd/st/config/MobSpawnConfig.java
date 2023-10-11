package com.dsd.st.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MobSpawnConfig {
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