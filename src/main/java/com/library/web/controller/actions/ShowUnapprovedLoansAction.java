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
 * Action for showing unapproved loans
 */
public class ShowUnapprovedLoansAction extends Action {

    private LoanService loanService;

    /**
     * Gets paginated part of unapproved loans and saves this
     * info as request parameter
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to page with unapproved loans table
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        List<LoanDto> loans = getUnapprovedLoans(request, loanService, paginationHelper);
        request.setAttribute("loans", loans);

        addPaginationToRequest(request, paginationHelper);

        return resources.getForward("showUnapprovedLoans");
    }

    /**
     * Gives a {@code List} with paginated part of all unapproved loans
     */
    private List<LoanDto> getUnapprovedLoans(HttpServletRequest request, LoanService loanService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int currentPageNumber = paginationHelper.getCurrentPageNumber(request);
        int previousRecordNumber = (currentPageNumber-1)*recordsPerPage;

        return loanService
                .getUnapprovedLoans(recordsPerPage, previousRecordNumber);
    }

    /**
     * Adds pagination to request
     */
    private void addPaginationToRequest(HttpServletRequest request, PaginationHelper paginationHelper) {
        long recordsQuantity = loanService.getUnapprovedLoansQuantity();
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }
}
