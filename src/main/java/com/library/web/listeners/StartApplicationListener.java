package com.library.web.listeners;

import com.library.model.enums.Languages;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        addLanguagesListToContext(context);
        setDefaultApplicationLanguage(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    /**
     * Adds languages list to the application(servlet) context
     * @param sc - the application(servlet) context
     */
    protected void addLanguagesListToContext(ServletContext sc) {
        List<Languages> languages = Stream
                .of(Languages.ENGLISH, Languages.RUSSIAN, Languages.UKRAINIAN)
                .collect(Collectors.toList());
        sc.setAttribute("languages", languages);
    }

    /**
     * Sets a default application language in the application(servlet) context
     * @param sc - the application(servlet) context
     */
    protected void setDefaultApplicationLanguage(ServletContext sc) {
        sc.setAttribute("language", Languages.ENGLISH.getCode());
    }
}
