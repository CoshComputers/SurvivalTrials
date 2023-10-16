package com.dsd.st.config;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.UUID;

public class PlayerConfig {
    @SerializedName("playerUuid")
    private final UUID playerUuid;

    @SerializedName("lastLogin")
    private Instant lastLogin;


    private String playerName;
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

    public void setPlayerName(String pName){
            this.playerName = pName;
    }
    public String getPlayerName(){
        return playerName;
    }

    /*public ItemStack getPlayerHead() {
        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);  // Ensure PLAYER_HEAD is the correct item for your Minecraft version
        CompoundNBT nbt = new CompoundNBT();
        UUID testUUID = UUID.fromString("0db1ebd5-50e2-46e9-95fb-ffd49efcf79c");
        nbt.put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), new GameProfile(testUUID, this.playerName)));
        playerHead.setTag(nbt);
        CustomLogger.getInstance().debug(String.format("player [%s] head NBT = %s",this.playerName,playerHead.getTag().toString()));
        return playerHead;
    }*/

    @Override
    public String toString(){
        String s = "Player Name: [" + this.playerName + "] " +
                "Player UUID: [" + this.playerUuid + "] " +
                "Player Last Login: [" + this.lastLogin + "] ";

        return s;
    }


}
