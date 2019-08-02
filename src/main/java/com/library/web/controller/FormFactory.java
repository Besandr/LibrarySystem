package com.library.web.controller;

import com.library.web.controller.forms.ActionForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Creates {@code ActionForm}
 * Need to be configured through {@code addFormClass} method
 * before application start
 */
public class FormFactory {

    private static Logger log = LogManager.getLogger(FormFactory.class);

    private static FormFactory ourInstance = new FormFactory();

    /**
     * Contains classes for different {@code ActionForm} and paths with
     * with them bound
     */
    private HashMap<String, String> formClasses = new HashMap<>();

    /**
     * Creates a {@code ActionForm} bound to the given action Path
     * @param actionPath - the path with which target {@code ActionForm}
     *                   is bound
     * @return target {@code ActionForm} or {@code null} if {@code Action}
     * bound to the given path doesn't require any {@code ActionForm}
     */
    ActionForm getForm(String actionPath) {
        String actionClassName = formClasses.get(actionPath);
        if (actionClassName != null) {
            try {
                Class actionClass = Class.forName(actionClassName);
                return (ActionForm) actionClass.newInstance();

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                log.error(String.format("Can't create action class by name: %s. Cause: %s", actionClassName, e.getMessage()), e);
            }
        }
        return null;
    }

    /**
     * Adds full qualified name of {@code ActionForm} class and action path which is bound to it
     * @param actionPath - the path with which target {@code ActionForm}
     *                   is bound
     * @param formClassName - the full qualified name of {@code ActionForm} class
     */
    public void addFormClass(String actionPath, String formClassName) {
        formClasses.put(actionPath, formClassName);
    }

    public static FormFactory getInstance() {
        return ourInstance;
    }

    private FormFactory() {
    }
}
