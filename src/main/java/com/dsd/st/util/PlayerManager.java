package com.dsd.st.util;

import com.dsd.st.config.PlayerConfig;

import java.util.*;

public class PlayerManager {

    private static final PlayerManager instance = new PlayerManager();
    private final Map<UUID, PlayerConfig> playerConfigs = new HashMap<>();

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public synchronized void addPlayerConfig(PlayerConfig config) {
        playerConfigs.put(config.getPlayerUuid(), config);
    }

    public PlayerConfig getRandomPlayer() {
        if (!playerConfigs.isEmpty()) {
            List<UUID> playerUuids = new ArrayList<>(playerConfigs.keySet());
            UUID randomPlayerUuid = playerUuids.get(new Random().nextInt(playerUuids.size()));
            return playerConfigs.get(randomPlayerUuid);
        }
        return null;  // Return null or throw an exception if no players are available
    }

    public PlayerConfig getPlayerConfig(UUID playerUuid) {
        return playerConfigs.get(playerUuid);
    }

    public Map<UUID, PlayerConfig> getAllPlayerConfigs() {
        return Collections.unmodifiableMap(playerConfigs);
    }
}

