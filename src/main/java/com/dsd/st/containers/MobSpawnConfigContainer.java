package com.dsd.st.containers;


import com.dsd.st.config.MobSpawnConfig;

public class MobSpawnConfigContainer {
    private final MobSpawnConfig mobSpawnConfig;

    public MobSpawnConfigContainer(MobSpawnConfig mobSpawnConfig) {
        this.mobSpawnConfig = mobSpawnConfig;
    }

    public MobSpawnConfig getMobSpawnConfig() {
        return mobSpawnConfig;
    }



}