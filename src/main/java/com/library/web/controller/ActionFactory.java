package com.library.web.controller;

import com.library.web.controller.actions.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Creates {@code Action}.
 * Need to be configured through {@code addAction} method before
 * application start.
 */
public class ActionFactory {

    private static final Logger log = LogManager.getLogger(ActionFactory.class);

    private static ActionFactory ourInstance = new ActionFactory();

    /**
     * Contains {@code Action} for  paths bound with them.
     */
    private HashMap<String, Action> actions = new HashMap<>();

    /**
     * Gets an action bound with given action path.
     * @param actionPath the path bound with target {@code Action}
     * @return an {@code Action} bound to the given path
     * or {@code null} if there is no {@code Action} bound to
     * the given path
     */
    Action getAction(String actionPath) {
        return actions.get(actionPath);
    }

    /**
     * Adds created from given parameters {@code Action} to the {@code actions} map.
     * @param actionPath - the path to the {@code Action} which is need to be added
     * @param actionClassName - the full qualified class name of target {@code Action}
     * @param needValidate - is target {@code Action} needs a {@code ActionForm} marker
     * @param inputPath - path from where request to target {@code Action} comes from
     */
    void addAction(String actionPath, String actionClassName, boolean needValidate, String inputPath) {
        Action action = createAction(actionClassName, needValidate, inputPath);
        actions.put(actionPath, action);
    }

    /**
     * Creates an {@code Action} configured by given parameters.
     * @param actionClassName - the full qualified class name of target {@code Action}
     * @param needValidate - is target {@code Action} needs a {@code ActionForm} marker
     * @param inputPath - path from where request to target {@code Action} comes from
     * @return target {@code Action} or throws {@code IllegalArgumentException} if
     * exceptions occurred during creating of the action
     */
    Action createAction(String actionClassName, boolean needValidate, String inputPath) {
        try {
            Class actionClass = Class.forName(actionClassName);
            Action action = (Action) actionClass.newInstance();

            action.setNeedValidate(needValidate);
            action.setInputPath(inputPath);

            return action;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.fatal(String.format("Can't create action class by name: %s. Cause: %s", actionClassName, e.getMessage()));
            throw new IllegalArgumentException(e);
        }
    }

    public static ActionFactory getInstance() {
        return ourInstance;
    }

    private ActionFactory() {}
}
