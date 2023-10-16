package com.dsd.st.containers;

import com.dsd.st.config.GiantConfig;
import com.dsd.st.config.GiantConfigWrapper;

public class GiantConfigContainer {
    private final GiantConfig giantConfig;

    public GiantConfigContainer(GiantConfigWrapper wrapper) {
        this.giantConfig = wrapper.getGiantConfig();
    }

    public GiantConfig getGiantConfig() {
        return giantConfig;
    }
}
