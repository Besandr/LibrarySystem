package com.library.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Request wrapper provides possibility of adding parameters to
 * request
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String[]> modifiableParameters;
    private Map<String, String[]> allParameters = null;

    /**
     * Create a new request wrapper that will merge additional parameters into
     * the request object without prematurely reading parameters from the
     * original request.
     *
     * @param request - the original request
     * @param additionalParams - additional request parameters
     */
    public ParameterRequestWrapper(final HttpServletRequest request,
                                     final Map<String, String[]> additionalParams)
    {
        super(request);
        modifiableParameters = new HashMap<>();
        modifiableParameters.putAll(additionalParams);
    }

    /**
     * Gives a target parameter from local parameters map.
     * If it hasn't any gives a target parameter from parameters
     * map of wrapped request.
     * @param name of target parameter
     * @return target parameter
     */
    @Override
    public String getParameter(final String name)
    {
        String[] strings = getParameterMap().get(name);
        if (strings != null)
        {
            return strings[0];
        }
        return super.getParameter(name);
    }

    /**
     * Gives a local parameter map merged with wrapped request
     * parameter map
     * @return a local parameter map merged with wrapped request
     *      * parameter map
     */
    @Override
    public Map<String, String[]> getParameterMap()
    {
        if (allParameters == null)
        {
            allParameters = new HashMap<>();
            allParameters.putAll(super.getParameterMap());
            allParameters.putAll(modifiableParameters);
        }
        //Return an unmodifiable collection because we need to uphold the interface contract.
        return Collections.unmodifiableMap(allParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<String> getParameterNames()
    {
        return Collections.enumeration(getParameterMap().keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getParameterValues(final String name)
    {
        return getParameterMap().get(name);
    }
}
