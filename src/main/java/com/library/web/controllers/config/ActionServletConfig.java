package com.library.web.controllers.config;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * Container for servlet's parameters which is read
 * from {@code ActionServlet} config file
 */
@Getter
@Setter
@XmlRootElement(name = "servlet-config")
@XmlAccessorType(XmlAccessType.FIELD)
class ActionServletConfig {

    /**
     * Forms needed for servlet's {@code Action}
     */
    @XmlElementWrapper(name = "forms")
    @XmlElement(name = "form")
    List<FormConfig> forms;

    /**
     * Actions needed for {@code ActionServlet}
     */
    @XmlElementWrapper(name = "action-mappings")
    @XmlElement(name = "action")
    List<ActionConfig> actions;

    /**
     * Mappings for forwards to jsp pages
     */
    HashMap<String, String> forwards;

    @XmlElementWrapper(name = "security-constraint")
    @XmlElement(name = "resource")
    List<ResourceAccessConfig> resourceAccessConfigs;

}
