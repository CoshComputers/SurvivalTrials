package com.dsd.st.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ItemDropConfigWrapper {

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    private ItemDropConfig itemDropConfig;

    public ItemDropConfig getItemDropConfig() {
        return itemDropConfig;
    }

    public void setItemDropConfig(ItemDropConfig itemDropConfig) {
        this.itemDropConfig = itemDropConfig;
    }
}