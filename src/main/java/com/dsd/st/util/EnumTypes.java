package com.dsd.st.util;

import com.dsd.st.config.ItemDropConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumTypes {

    public enum SkullDropMapping {
        BLAZE("MHF_Blaze", "minecraft:blaze_rod"),
        ENDERMAN("MHF_Enderman", "minecraft:ender_pearl"),
        UNKNOWN("", null);

        private final String skullOwner;
        private final String dropItem;

        SkullDropMapping(String skullOwner, String dropItem) {
            this.skullOwner = skullOwner;
            this.dropItem = dropItem;
        }

        public String getSkullOwner() {
            return skullOwner;
        }

        public String getDropItem() {
            return dropItem;
        }

        public static SkullDropMapping fromHeadItem(ItemStack headItem) {
            if (!headItem.isEmpty() && headItem.getItem() == Items.PLAYER_HEAD) {
                CompoundNBT tag = headItem.getTag();
                if (tag != null) {
                    String skullOwner = tag.getString("SkullOwner");
                    for (SkullDropMapping type : values()) {
                        if (type.skullOwner != null && type.skullOwner.equals(skullOwner)) {
                            return type;
                        }
                    }
                }
            }
            return UNKNOWN;
        }
        public ItemDropConfig.Drop getDropItemAsDropConfig(int quantity) {
            if (this.dropItem != null) {
                return new ItemDropConfig.Drop(this.dropItem, quantity);
            } else {
                // Handle the UNKNOWN case, which has a null dropItem.
                // You could return a default Drop object or null, depending on your use case.
                return null;
            }
        }
    }

    public enum ModConfigOption {
        OVERRIDE_MOBS("overrideMobs"),
        SPAWN_GIANTS("spawnGiants"),
        GIVE_INITIAL_GEAR("giveInitialGear"),
        GIVE_SPECIAL_LOOT("giveSpecialLoot"),
        USE_PLAYER_HEADS("usePlayerHeads"),
        DEBUG_ON("debugOn");

        private final String optionName;

        ModConfigOption(String optionName) {
            this.optionName = optionName;
        }

        public String getOptionName() {
            return optionName;
        }

        public static ModConfigOption fromOptionName(String optionName) {
            for (ModConfigOption option : values()) {
                if (option.getOptionName().equalsIgnoreCase(optionName)) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Unknown option name: " + optionName);
        }

        public static List<String> getAllOptionNames() {
            return Arrays.stream(values())
                    .map(ModConfigOption::getOptionName)
                    .collect(Collectors.toList());
        }

        public String getName() {
            return optionName;
        }
    }


    // ... other utility enums or methods ...
}
