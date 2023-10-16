package com.dsd.st.util;

import com.dsd.st.SurvivalTrials;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomLogger {
    private static final CustomLogger INSTANCE = new CustomLogger();
    private final Logger logger;
    private boolean isDebugOn;
    private static final Style ERROR_STYLE = Style.EMPTY.withColor(Color.fromRgb(0xFF0000));
    private static final Style WARN_STYLE = Style.EMPTY.withColor(Color.fromRgb(0xFFBF00));
    private static final Style INFO_STYLE = Style.EMPTY.withColor(Color.fromRgb(0x00FF00));
    private CustomLogger() {
        this.logger = LogManager.getLogger(SurvivalTrials.MOD_ID);
        this.isDebugOn = false; //assuming no debug until config is loaded.
    }

    public static CustomLogger getInstance() {
        return INSTANCE;
    }
    public void setDebugOn(boolean debugOn){
        this.isDebugOn = debugOn;
    }

    public void debug(String message) {
        if(this.isDebugOn) {
            logger.debug(message);
        }

    }
    // Similarly for other log levels...
    public void info(String message) {
        logger.info(message);
    }
    public void error(String message) {
        logger.error(message);
    }
    public void warn(String message) { logger.warn(message);}

    public void broadcastMessage(String message){
        TextComponent messageComponent = new StringTextComponent(message);
        messageComponent.setStyle(ERROR_STYLE);
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {

            for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
                player.sendMessage(messageComponent, Util.NIL_UUID);
            }
        }
    }

    public void sendPlayerMessage(PlayerEntity player, String message) {
        TextComponent messageComponent = new StringTextComponent(message);
        messageComponent.setStyle(ERROR_STYLE);
        player.sendMessage(messageComponent, Util.NIL_UUID);
    }
}

