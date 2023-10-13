package com.dsd.st.containers;

import com.dsd.st.config.ItemDropConfig;
import com.dsd.st.config.ItemDropConfigWrapper;

public class ItemDropConfigContainer {
    private final ItemDropConfig itemDropConfig;

    public ItemDropConfigContainer(ItemDropConfigWrapper wrapper) {
        this.itemDropConfig = wrapper.getItemDropConfig();
    }

    public ItemDropConfig getItemDropConfig() {
        return itemDropConfig;
    }

}
