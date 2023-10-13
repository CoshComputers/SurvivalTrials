package com.dsd.st.containers;


import com.dsd.st.config.SurvivalTrialsConfig;

public class SurvivalTrialsConfigContainer {
    private final SurvivalTrialsConfig survivalTrialsConfig;

    public SurvivalTrialsConfigContainer(SurvivalTrialsConfig survivalTrialsConfig) {
        this.survivalTrialsConfig = survivalTrialsConfig;
    }

    public SurvivalTrialsConfig getSurvivalTrialsConfig() {
        return survivalTrialsConfig;
    }
}