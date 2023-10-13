package com.dsd.st.util;

import net.minecraft.server.MinecraftServer;

public class ServerManager {

    private static ServerManager instance;
    private MinecraftServer server;

    private ServerManager() {
        // Private constructor to prevent instantiation from outside
    }

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        if (server == null) {
            throw new IllegalStateException("Server has not been set yet");
        }
        return server;
    }
}
