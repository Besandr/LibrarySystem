package com.library.services;

import com.library.repository.DaoManagerFactory;
import com.library.web.controller.config.ServletConfigException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Holds single instances of each {@code Service} and gives
 * them on demand.
 */
public class ServiceFactory {

    private static final Logger log = LogManager.getLogger(ServiceFactory.class);

    /**
     * Holds each type of {@code Service}
     */
    private HashMap<String, Service> services;

    public ServiceFactory(){
        services = createServices();
    }

    /**
     * Gives {@code Service} by its class name
     * @param serviceClass - full qualified name of {@code Service}
     *                     implementation class
     * @return instance of {@code Service}
     */
    public Service getService(String serviceClass) {
        Service service = services.get(serviceClass);
        if (service == null) {
            String errorText = "Can't find service class: " + serviceClass;
            log.fatal(errorText);
            throw new ServletConfigException(errorText);
        }
        return service;
    }

    /**
     * Creates each type of {@code Service} and puts them in the {@code HashMap}
     * @return new {@code HashMap} filled with {@code Service}s
     */
    private HashMap<String, Service> createServices() {
        HashMap<String, Service> services = new HashMap<>();
        DaoManagerFactory managerFactory = new DaoManagerFactory();

        services.put("com.library.services.BookService", new BookService(managerFactory));
        services.put("com.library.services.LoanService", new LoanService(managerFactory));
        services.put("com.library.services.LocationService", new LocationService(managerFactory));
        services.put("com.library.services.UserService", new UserService(managerFactory));

        return services;
    }
}
