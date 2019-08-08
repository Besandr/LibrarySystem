package com.library.web.controller.actions;

import com.library.repository.dto.LoanDto;
import com.library.repository.entity.User;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controller.PaginationHelper;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for preparing data for showing user's
 * borrowed books
 */
public class ShowBorrowedBooksAction extends Action {

    private LoanService loanService;

    /**
     * Gets all active loans of user and adds them to request
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the borrowed books page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long userId = ((User) request.getSession().getAttribute("loggedInUser")).getId();
        List<LoanDto> loans = getActiveLoans(userId, request, loanService, paginationHelper);
        request.setAttribute("loans", loans);

        addPaginationToRequest(userId, request, paginationHelper);

        return resources.getForward("ShowBorrowedBooksPage");
    }

    /**
     * Gives a {@code List} with paginated part of all active loans
     */
    private List<LoanDto> getActiveLoans(long userId, HttpServletRequest request, LoanService loanService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int currentPageNumber = paginationHelper.getCurrentPageNumber(request);
        int previousRecordNumber = (currentPageNumber-1)*recordsPerPage;

        return loanService
                .getActiveLoansByUser(userId, recordsPerPage, previousRecordNumber);
    }

    /**
     * Adds pagination to request
     */
    private void addPaginationToRequest(long userId, HttpServletRequest request, PaginationHelper paginationHelper) {
        long recordsQuantity = loanService.getActiveLoansByUserQuantity(userId);
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }
}

