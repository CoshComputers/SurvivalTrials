package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ItemDropConfig {

    private static final Random RANDOM = new Random();
    private final List<Drop> drops;

    public ItemDropConfig(List<Drop> initialDrops) {
        this.drops = new CopyOnWriteArrayList<>(initialDrops);
    }

    public List<Drop> getDrops() {
        return Collections.unmodifiableList(drops);

    }

    public Drop getDropForItem(String itemName) {

        for (Drop drop : drops) {
            if (drop.getItem().equalsIgnoreCase(itemName)) {
                return drop;
            }
        }
        return null;

    }
    public Drop getRandomDrop() {

        if (drops.isEmpty()) {
            SurvivalTrials.getModLogger().error("No Drops to fetch a random drop from. Assigning to default drop");
            return new Drop("rotten_flesh", 1);
        }
        int randomIndex = RANDOM.nextInt(drops.size());
        return drops.get(randomIndex);

    }

    public void updateDrops(List<Drop> newDrops) {
        drops.clear();
        drops.addAll(new CopyOnWriteArrayList<>(newDrops));

    }


    public synchronized void debugItemDrops() {

        //SurvivalTrials.LOGGER.info("************* Drops List debug output *************** ");
        synchronized (this.drops) {  // Synchronize on the drops list
            SurvivalTrials.getModLogger().info(String.format("Drops List Size = %d", this.drops.size()));
            for (int i = 0; i < drops.size(); i++) {
                if (drops.get(i) == null) {
                    SurvivalTrials.getModLogger().error(String.format("Null drop at index %d", i));
                }
            }
        }
    }


    public static final class Drop {

        @SerializedName("item")
        private final String item;

        @SerializedName("quantity")
        private final int quantity;

        public Drop(String item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public String getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
