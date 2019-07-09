package com.library.web.listeners;

import com.library.web.controller.commands.RequestParameterNamesDict;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Configures application on startup
 */
public class StartApplicationListener implements ServletContextListener{

    // Public constructor is required by servlet spec
    public StartApplicationListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();

        //Set application scope attribute with request parameters names
        context.setAttribute("requestParameterNamesDict", new RequestParameterNamesDict());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
