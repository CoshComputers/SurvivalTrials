package com.dsd.st.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class GiantConfig {

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }


    @SerializedName("spawnFrequency")
    private float spawnFrequency;
    @SerializedName("scaleFactor")
    private float scaleFactor;

    @SerializedName("health")
    private float health;

    @SerializedName("damage")
    private float damage;

    @SerializedName("dropItems")
    private DropItem[] dropItems;

    @SerializedName("followRange")
    private float followRange;

    @SerializedName("aggressionLevel")
    private float aggressionLevel;

    @SerializedName("visibilityRange")
    private float visibilityRange;

    @SerializedName("xpPoints")
    private int xpPoints;


    public float getSpawnFrequency() {
        return spawnFrequency;
    }

    public void setSpawnFrequency(float spawnFrequency) {
        this.spawnFrequency = spawnFrequency;
    }
    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public DropItem[] getDropItems() {
        return dropItems;
    }

    public void setDropItems(DropItem[] dropItems) {
        this.dropItems = dropItems;
    }

    public float getFollowRange() {
        return followRange;
    }

    public void setFollowRange(float followRange) {
        this.followRange = followRange;
    }

    public float getAggressionLevel() {
        return aggressionLevel;
    }

    public void setAggressionLevel(float aggressionLevel) {
        this.aggressionLevel = aggressionLevel;
    }

    public float getVisibilityRange() {
        return visibilityRange;
    }

    public void setVisibilityRange(float visibilityRange) {
        this.visibilityRange = visibilityRange;
    }

    public int getXpPoints() {
        return xpPoints;
    }

    public void setXpPoints(int xpPoints) {
        this.xpPoints = xpPoints;
    }

    public static class DropItem {
        @SerializedName("item")
        private String item;

        @SerializedName("enchantments")
        private Enchantment[] enchantments;

        @SerializedName("values")
        private int[] values;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public Enchantment[] getEnchantments() {
            return enchantments;
        }

        public void setEnchantments(Enchantment[] enchantments) {
            this.enchantments = enchantments;
        }

        public int[] getValues() {
            return values;
        }

        public void setValues(int[] values) {
            this.values = values;
        }
    }

    public static class Enchantment {
        @SerializedName("type")
        private String type;

        @SerializedName("level")
        private int level;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}

