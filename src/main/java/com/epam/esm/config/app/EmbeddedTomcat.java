package com.epam.esm.config.app;


import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Date;

/**
 * Class for managing Tomcat server.
 *
 * @author Danylo Proshyn
 */

public class EmbeddedTomcat {

    private static final Logger log = LogManager.getLogger(EmbeddedTomcat.class);

    private static final String WEB_CONTENT_FOLDER = "src/main/";
    private static final int    PORT               = 8080;

    public static void start() throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.addWebapp("", new File(WEB_CONTENT_FOLDER).getAbsolutePath());
        tomcat.getConnector();

        tomcat.start();

        log.info("Tomcat Server Started at " + new Date());
        tomcat.getServer().await();
    }
}