package com.library.web.controller;

import com.library.repository.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Configurator of {@code ActionServlet}.
 *
 * Main stages of configuring:
 * - load servlet configuration from xml config file
 * - set up {@code ActionFactory} and {@code FormFactory}
 * - create facade class {@code ServletResources} and
 * give it to {@code ActionServlet}
 */
class ActionServletConfigurator {

    private static final Logger log = LogManager.getLogger(ActionServletConfigurator.class);

    /**
     * Creates {@code ServletResources} from xml config file.
     * @return created {@code ServletResources}
     */
    ServletResources createServletResources(){
        ActionServletConfig actionServletConfig = loadConfigFromFile();

        setUpFactories(actionServletConfig,
                ActionFactory.getInstance(),
                FormFactory.getInstance());

        Map<String, List<Role>> securityConstraints =
                createSecurityConstraints(actionServletConfig.getResourceAccessConfigs());
        return new ServletResources(
                ActionFactory.getInstance(),
                FormFactory.getInstance(),
                Collections.unmodifiableMap(actionServletConfig.getForwards()),
                securityConstraints);
    }

    /**
     * Loads configuration repository from xml config file and converts.
     * it into {@code ActionServletConfig} object and initialize
     * variable {@code actionServletConfig} by it.
     * {@code RuntimeException} may be thrown if during loading and
     * convertation errors occurred
     * @return created config object
     */
    private ActionServletConfig loadConfigFromFile(){
        InputStream is = ActionServletConfigurator.class.getClassLoader().getResourceAsStream("servlet-config.xml");
        try {
            JAXBContext jContext = JAXBContext.newInstance(ActionServletConfig.class);
            Unmarshaller jUnmarshaller = jContext.createUnmarshaller();
            return (ActionServletConfig) jUnmarshaller.unmarshal(is);
        } catch (JAXBException e) {
            log.fatal("Can't load servlet configuration!");
            throw new ServletConfigException("Can't load servlet configuration!",e);
        }
    }

    /**
     * Takes the configuration repository from {@code actionServletConfig} and
     * uses it for setting up {@code ActionFactory} and {@code FormFactory} by
     * adding to them {@code Action} objects and {@code ActionForm} class names.
     * @param actionServletConfig - object which contains servlet configuration repository
     * @param actionFactory - the {@code ActionFactory} instance
     * @param formFactory - the {@code FormFactory} instance
     */
    void setUpFactories(ActionServletConfig actionServletConfig, ActionFactory actionFactory, FormFactory formFactory){
        //Setting up an ActionFactory
        for (ActionConfig actionConfig : actionServletConfig.getActions()) {

            boolean needValidate = Boolean.parseBoolean(actionConfig.getValidate());

            actionFactory.addAction(actionConfig.getPath(),
                    actionConfig.getType(),
                    needValidate,
                    actionConfig.getInput(),
                    actionConfig.getServiceDependencyList());

            if (actionConfig.getFormName() != null) {
                //Adding form info needed to this action to {@code FormFactory}
                addFormToFormFactory(actionConfig, actionServletConfig, formFactory);
            }
        }
    }

    /**
     * Takes the configuration repository from {@code actionConfig} and
     * uses it for adding {@code ActionForm} class info to {@code FormFactory}
     * {@code ServletConfigException} may be thrown if configuration objects
     * contains errors
     * @param actionConfig - the config object which contains information
     *                     needed for adding {@code ActionForm} to {@code FormFactory}
     * {@code ServletConfigException} can be thrown if configuration contains errors
     * @param actionServletConfig - object which contains servlet configuration repository
     * @param formFactory - the {@code FormFactory} instance
     */
    void addFormToFormFactory(ActionConfig actionConfig, ActionServletConfig actionServletConfig, FormFactory formFactory){
        Optional<FormConfig> formConfigOptional = actionServletConfig.getForms()
                .stream()
                .filter(f -> actionConfig.getFormName().equals(f.getName()))
                .findFirst();

        if (formConfigOptional.isPresent()) {
            formFactory.addFormClass(actionConfig.getPath(), formConfigOptional.get().getType());
        } else {
            String errorText = "In servlet config file action with validation doesn't have matching form";
            log.fatal(errorText);
            throw new ServletConfigException(errorText);
        }
    }

    /**
     * Create mapping between constrained resources and roles which
     * has access to them
     * @param resourceConfigs - list with security configuration of
     *                        each constrained resource
     * @return {@code Map} with URL-patterns of constrained resources
     * bound with {@code Role} which has access to them
     */
    private Map<String, List<Role>> createSecurityConstraints(List<ResourceAccessConfig> resourceConfigs) {
        Map<String, List<Role>> securityConstraints = new HashMap<>();

        for (ResourceAccessConfig config : resourceConfigs) {
            List<Role> roleList = createRoleList(config.getRoles());
            String urlPattern = config.getUrlPattern();
            securityConstraints.put(urlPattern, roleList);
        }
        return Collections.unmodifiableMap(securityConstraints);
    }

    /**
     * Convert list with {@code Role} names to {@code List} with
     * roles.
     * @param roles list with {@code Role} names
     * @return - list with roles converted.
     */
    private List<Role> createRoleList(List<String> roles) {
        return roles.stream().map(Role::valueOf).collect(Collectors.toList());
    }

}
