package com.library.web.controller.actions;

import com.library.repository.dto.LoanDto;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controller.PaginationHelper;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for showing all active loans
 */
public class ShowActiveLoansAction extends Action {

    private LoanService loanService;

    /**
     * Gets paginated part of all active loans as {@code List}
     * and saves it as request parameter
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path to the showing active loans page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        List<LoanDto> activeLoans = getActiveLoans(request, loanService, paginationHelper);

        request.setAttribute("activeLoans", activeLoans);

        addPaginationToRequest(request, paginationHelper);

        return resources.getForward("showActiveLoansPage");
    }

    /**
     * Gives a {@code List} with paginated part of all active loans
     */
    private List<LoanDto> getActiveLoans(HttpServletRequest request, LoanService loanService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int currentPageNumber = paginationHelper.getCurrentPageNumber(request);
        int previousRecordNumber = (currentPageNumber-1)*recordsPerPage;

        return loanService
                .getActiveLoans(recordsPerPage, previousRecordNumber);
    }

    /**
     * Adds pagination to request
     */
    private void addPaginationToRequest(HttpServletRequest request, PaginationHelper paginationHelper) {
        long recordsQuantity = loanService.getActiveLoansQuantity();
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }

}
