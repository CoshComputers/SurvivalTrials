package com.dsd.st.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDropConfig {
    private List<DropItem> itemDrops;

    public ItemDropConfig() {
        // Assuming DropItem has a default constructor
        itemDrops = new ArrayList<>();
    }

    public List<DropItem> getItemDrops() {
        return itemDrops;
    }

    public void setItemDrops(List<DropItem> itemDrops) {
        this.itemDrops = itemDrops;
    }

    public static class DropItem {
        private String item;
        private int maxAmount;
        private double dropChance;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public double getDropChance() {
            return dropChance;
        }

        public void setDropChance(double dropChance) {
            this.dropChance = dropChance;
        }
    }
}
