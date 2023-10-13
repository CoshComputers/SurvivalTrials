package com.dsd.st.config;

import com.dsd.st.SurvivalTrials;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;

import java.time.Instant;
import java.util.UUID;

public class PlayerConfig {
    @SerializedName("playerUuid")
    private final UUID playerUuid;

    @SerializedName("lastLogin")
    private Instant lastLogin;

    // No-arg constructor for Gson
    private PlayerConfig() {
        this.playerUuid = null;  // Gson will override this
    }

    // Constructor for new players
    public PlayerConfig(UUID playerUuid) {
        this.playerUuid = playerUuid;
        updateLastLogin();
    }

    // Method to update the lastLogin field
    public void updateLastLogin() {
        this.lastLogin = Instant.now();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public ItemStack getPlayerHead() {
        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);  // Ensure PLAYER_HEAD is the correct item for your Minecraft version
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), new GameProfile(this.playerUuid, null)));
        playerHead.setTag(nbt);
        return playerHead;
    }

    public String getPlayerName(){
        String playerName = "Not Available";
        MinecraftServer server = SurvivalTrials.getServer();

        PlayerEntity player = server.getPlayerList().getPlayer(this.getPlayerUuid());
        if (player != null) {
            playerName = player.getName().getString();
        }
        return playerName;
    }
}
