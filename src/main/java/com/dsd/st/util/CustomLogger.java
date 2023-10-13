package com.dsd.st.util;

import com.dsd.st.SurvivalTrials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomLogger {

    private final Logger logger;
    private boolean isDebugOn;
    public CustomLogger() {
        this.logger = LogManager.getLogger(SurvivalTrials.MOD_ID);
        this.isDebugOn = false; //assuming no debug until config is loaded.
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
}

