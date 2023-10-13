package com.dsd.st.util;

import com.dsd.st.config.ItemDropConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

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



    // ... other utility enums or methods ...
}
