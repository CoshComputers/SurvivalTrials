package com.dsd.st.containers;

import com.dsd.st.config.InitialGearConfig;
import com.dsd.st.config.InitialGearConfigWrapper;

public class InitialGearConfigContainer {
    private final InitialGearConfig initialGearConfig;

    public InitialGearConfigContainer(InitialGearConfigWrapper wrapper) {
        this.initialGearConfig = wrapper.getInitialGearConfig();
    }

    public InitialGearConfig getInitialGearConfig() {
        return initialGearConfig;
    }

}
