package com.library.web.controller;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Container for action's parameters which is read
 * from {@code ActionServlet} config file
 */
@XmlRootElement(name = "action")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
class ActionConfig {

    /**
     * Path mapped with current action
     */
    private String path;

    /**
     * Full qualified name of current action class
     */
    private String type;

    /**
     * Action name for inner configs bindings
     */
    private String name;

    /**
     * Need this action in validated form
     */
    private String validate;

    /**
     * Path from what coming request to this action
     */
    private String input;

    /**
     * Service dependencies need to be injected in Action
     */
    @XmlElementWrapper(name = "service-dependencies")
    @XmlElement(name = "dependency")
    private List<ServiceDependencyConfig> serviceDependencyList;

}
