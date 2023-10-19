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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomLogger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private List<TimerLogEntry> timerLogList = new ArrayList<>();
    private List<String> bulkLogList = new ArrayList<>();
    private static final CustomLogger INSTANCE = new CustomLogger();
    private final Logger logger;
    private boolean isDebugOn;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
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
            logger.debug(ANSI_BLUE + message);
        }

    }
    // Similarly for other log levels...
    public void info(String message) {
        logger.info(ANSI_GREEN + message);
    }
    public void error(String message) {
        logger.error(ANSI_RED + message);
    }
    public void warn(String message) { logger.warn(ANSI_YELLOW + message);}

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

    public void bulkLog(String methodName, String message){
        StringBuilder sb = new StringBuilder();
        sb.append(ANSI_YELLOW).append("[").append(formatCurrentTime()).append("]");
        sb.append("[").append(methodName).append("]").append(message);
        this.bulkLogList.add(sb.toString());
    }
    public void preCall(String methodName) {
        // Store the method name and current time
        TimerLogEntry entry = new TimerLogEntry();
        entry.timeStampString = formatCurrentTime();
        entry.methodName = methodName;
        entry.startTime = System.nanoTime();
        timerLogList.add(entry);
    }
    public void postCall() {
        // Find the last log entry and update its end time
        if(!timerLogList.isEmpty()) {
            TimerLogEntry lastEntry = timerLogList.get(timerLogList.size() - 1);
            lastEntry.endTime = System.nanoTime();
        }else{
            logger.error("Attempting to store TimerLog in Post Call without PreCall first");
        }
    }

    public void outputBulkToConsole(){
        for (String l: bulkLogList) {
            logger.debug(l);
        }
        bulkLogList.clear();
    }
    public void outputtimerLogToConsole(){
        for (TimerLogEntry t: timerLogList) {
            logger.debug(t.generateLog());
        }
        timerLogList.clear();
    }
    private static String formatCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        return "[" + FORMATTER.format(currentTime) + "]";
    }

    public void playerLogEntry(PlayerEntity p){
        StringBuilder sb = new StringBuilder();
        sb.append(p.getName()).append(" Data Points:\n");
        sb.append("Block Position").append(p.blockPosition()).append("\n");
        sb.append(p.getDimensions(p.getPose())).append("\n");
        sb.append("Bounding Box ").append(p.getBoundingBox());

        bulkLog("Logging Player Joining Details",sb.toString());
    }

    private static class TimerLogEntry {
        String methodName;
        String timeStampString;
        long startTime;
        long endTime;

        public String generateLog(){
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(this.timeStampString).append("]");
            sb.append("Method [").append(this.methodName).append("]\t");
            sb.append("Elapsed Time [").append(this.endTime - this.startTime).append("] Nano Seconds");
            return sb.toString();
        }
    }
}

