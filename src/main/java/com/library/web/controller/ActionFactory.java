package com.library.web.controller;

import com.library.services.Service;
import com.library.services.ServiceFactory;
import com.library.web.controller.actions.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

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
     * @param serviceDependencyList - list with service dependencies need to be injected
     *                              in a new action
     */
    void addAction(String actionPath, String actionClassName, boolean needValidate, String inputPath, List<ServiceDependencyConfig> serviceDependencyList) {
        Action action = createAction(actionClassName, needValidate, inputPath, serviceDependencyList);
        actions.put(actionPath, action);
    }

    /**
     * Creates an {@code Action} configured by given parameters.
     * @param actionClassName - the full qualified class name of target {@code Action}
     * @param needValidate - is target {@code Action} needs a {@code ActionForm} marker
     * @param inputPath - path from where request to target {@code Action} comes from
     * @param serviceDependencyList  - list with service dependencies need to be injected
     *                               in a new action
     * @return target {@code Action} or throws {@code ServletConfigException} if
     * exceptions occurred during creating of the action
     */
    Action createAction(String actionClassName, boolean needValidate, String inputPath, List<ServiceDependencyConfig> serviceDependencyList) {
        try {
            Class actionClass = Class.forName(actionClassName);
            Action action = (Action) actionClass.newInstance();

            action.setNeedValidate(needValidate);
            action.setInputPath(inputPath);

            if (serviceDependencyList != null
                    && !serviceDependencyList.isEmpty()) {
                createActionDependencies(action, actionClass, serviceDependencyList, new ServiceFactory());
            }

            return action;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            String errorText = String.format("Can't create action class by name: %s. Cause: %s", actionClassName, e.getMessage());
            log.fatal(errorText);
            throw new ServletConfigException(errorText, e);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            String errorText = String.format("Can't set dependency in action class: %s. Cause: %s", actionClassName, e.getMessage());
            log.fatal(errorText);
            throw new ServletConfigException(errorText, e);
        }
    }

    /**
     * Sets local {@code Action} variables with {@code Service} classes given in a
     * {@code serviceDependencyList} via setter methods
     * @param action - the action for service dependency injection
     * @param actionClass - the action full qualified class name
     * @param serviceDependencyList - list with service dependencies
     *                              which contains {@code Service} class names
     *                              and local variables names
     * @param serviceFactory - factory for getting {@code Service} objects
     * @throws NoSuchMethodException if a setter method is not found.
     * @throws InvocationTargetException if the setter method
     *      *              throws an exception.
     * @throws IllegalAccessException if setter method is inaccessible
     */
    private void createActionDependencies(Action action, Class<?> actionClass, List<ServiceDependencyConfig> serviceDependencyList, ServiceFactory serviceFactory) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (ServiceDependencyConfig dependency: serviceDependencyList){
            String setterName = "set"
                    + dependency.serviceVarName.substring(0, 1).toUpperCase()
                    + dependency.serviceVarName.substring(1);
            Method setter = actionClass.getDeclaredMethod(setterName, Service.class);
            setter.invoke(action, serviceFactory.getService(dependency.serviceClass));
        }
    }

    static ActionFactory getInstance() {
        return ourInstance;
    }

    private ActionFactory() {}
}
