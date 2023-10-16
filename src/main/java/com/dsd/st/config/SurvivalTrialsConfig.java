package com.dsd.st.config;

import com.google.gson.annotations.SerializedName;

public class SurvivalTrialsConfig {

    @SerializedName("survivalTrialsConfig")
    private MainConfig survivalTrialsConfig;

    public MainConfig getSurvivalTrialsMainConfig() {
        return survivalTrialsConfig;
    }

    public void setSurvivalTrialsMainConfig(MainConfig mainConfig) {
        this.survivalTrialsConfig = mainConfig;
    }

    public static class MainConfig {
        @SerializedName("overrideMobs")
        private boolean overrideMobs;
        @SerializedName("spawnGiants")
        private boolean spawnGiants;
        @SerializedName("giveInitialGear")
        private boolean giveInitialGear;
        @SerializedName("giveSpecialLoot")
        private boolean giveSpecialLoot;
        @SerializedName("usePlayerHeads")
        private boolean usePlayerHeads;

        @SerializedName("debugOn")
        private boolean debugOn;

        public boolean isoverrideMobs() {
            return overrideMobs;
        }

        public void setoverrideMobs(boolean overrideMobs) {
            this.overrideMobs = overrideMobs;
        }

        public boolean isSpawnGiants() {
            return spawnGiants;
        }

        public void setSpawnGiants(boolean spawnGiants) {
            this.spawnGiants = spawnGiants;
        }

        public boolean isGiveInitialGear() {
            return giveInitialGear;
        }

        public void setGiveInitialGear(boolean giveInitialGear) {
            this.giveInitialGear = giveInitialGear;
        }

        public boolean isGiveSpecialLoot() {
            return giveSpecialLoot;
        }

        public void setGiveSpecialLoot(boolean giveSpecialLoot) {
            this.giveSpecialLoot = giveSpecialLoot;
        }

        public boolean isUsePlayerHeads() {
            return usePlayerHeads;
        }

        public void setUsePlayerHeads(boolean usePlayerHeads) {
            this.usePlayerHeads = usePlayerHeads;
        }

        public boolean isDebugOn() {
            return debugOn;
        }

        public void setDebugOn(boolean debugOn) {
            this.debugOn = debugOn;
        }
        @Override
        public String toString() {
            return "survivalTrialsConfig{" +
                    "\toverrideMobs:" + overrideMobs +
                    "\tspawnGiants:" + spawnGiants +
                    "\tgiveInitialGear :" + giveInitialGear +
                    "\tgiveSpecialLoot :" + giveSpecialLoot +
                    "\tusePlayerHeads :" + usePlayerHeads +
                    "\tdebugOn :" + debugOn + "}";
        }

    }
}
