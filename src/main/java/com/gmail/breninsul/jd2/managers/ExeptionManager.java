package com.gmail.breninsul.jd2.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExeptionManager {

    public static void log(Throwable e, Class clazz) {
        Logger logger = LogManager.getLogger(clazz);
        logger.error(e.getStackTrace(), e);
    }

    public static void log(String message, int level, Class clazz) {
        Logger logger = LogManager.getLogger(clazz);
        switch (level) {
            case 1:
                logger.info(message);
                break;
            case 2:
                logger.debug(message);
                break;
            case 3:
                logger.warn(message);
                break;
            case 4:
                logger.error(message);
                break;
            case 5:
                logger.fatal(message);
        }
    }

}
