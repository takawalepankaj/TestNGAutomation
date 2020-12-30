package com.lib;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.lib.EnvSetup.LOGGER_PROPERTY_FILE;

public class InitLog4j {

    // Define a constructor

    /**
     * Initialize Log4j Logger.
     * @param sSuiteName
     */
    public InitLog4j(String sSuiteName) {
        Properties logProperties = new Properties();

        try {
            // load our log4j properties / configuration file
            System.setProperty("Suite_logfile",EnvSetup.LOG_FOLDER_PATH+sSuiteName);
            System.setProperty("LOG_LEVEL",EnvSetup.LOG_LEVEL);
            logProperties.load(new FileInputStream(LOGGER_PROPERTY_FILE));
            PropertyConfigurator.configure(logProperties);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load logging property " + LOGGER_PROPERTY_FILE);
        }
    }

}