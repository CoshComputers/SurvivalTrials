package com.dsd.st.config;

import com.google.gson.annotations.SerializedName;

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
}
