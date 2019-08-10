package com.library.web.controllers;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class for implementing pagination
 */
public class PaginationHelper {

    private final int RECORDS_PER_PAGE = 5;

    /**
     * Adds pagination info as parameters to request
     * @param request for adding pagination parameters
     * @param recordsQuantity - quantity of records need to be paginated
     */
    public void addPaginationToRequest(HttpServletRequest request, long recordsQuantity) {
        int page = getCurrentPageNumber(request);

        request.setAttribute("pagesQuantity", getPagesQuantity(recordsQuantity, RECORDS_PER_PAGE));
        request.setAttribute("currentPage", page);
    }

    /**
     * Gives a number of current page
     * @param request with current page number info
     * @return a number of current page
     */
    public int getCurrentPageNumber(HttpServletRequest request) {
        final int DEFAULT_PAGE_NUMBER = 1;
        if (request.getParameter("page") != null) {
            return Integer.parseInt(request.getParameter("page"));
        } else {
            return DEFAULT_PAGE_NUMBER;
        }
    }

    /**
     * Calculates a pages quantity
     * @param recordsQuantity - quantity of all records
     * @param recordsPerPage - quantity of records per page
     * @return quantity of all pages
     */
    int getPagesQuantity(long recordsQuantity, int recordsPerPage) {
        return (int) Math.ceil(recordsQuantity * 1.0 / recordsPerPage);
    }

    public int getRecordsPerPage() {
        return RECORDS_PER_PAGE;
    }
}
