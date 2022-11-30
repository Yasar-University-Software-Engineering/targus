package com.targus.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {
    private Logger logger;
    FileHandler fileHandler;

    public CustomLogger(String loggerName, String logFileName, boolean append) throws IOException {
        File file = new File(logFileName);
        if (!file.exists()) {
            boolean success = file.createNewFile();
            if (!success) {
                System.out.println("Log file was not created. Using the existing one.");
            }
        }

        fileHandler = new FileHandler(logFileName, append);
        logger = Logger.getLogger(loggerName);
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    public Logger getLogger() {
        return this.logger;
    }
}
