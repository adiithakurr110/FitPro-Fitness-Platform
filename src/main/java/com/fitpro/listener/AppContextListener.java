package com.fitpro.listener;

import com.fitpro.service.NotificationService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Stops background threads cleanly when the web app is undeployed. */
@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        NotificationService.getInstance().shutdown();
    }
}
