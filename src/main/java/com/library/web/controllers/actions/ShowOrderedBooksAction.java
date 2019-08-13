package com.library.web.controllers.actions;

import com.library.repository.dto.LoanDto;
import com.library.repository.entity.User;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controllers.PaginationHelper;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for showing ordered by user books
 */
public class ShowOrderedBooksAction extends Action {

    private LoanService loanService;

    /**
     * Gets all unapproved loans of user and adds them to request
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the ordered books page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long userId = ((User) request.getSession().getAttribute("loggedInUser")).getId();
        List<LoanDto> loans = getUnapprovedLoans(userId, request, loanService, paginationHelper);
        request.setAttribute("loans", loans);

        addPaginationToRequest(userId, request, paginationHelper);

        return resources.getForward("ShowOrderedBooksPage");
    }

    /**
     * Gives a {@code List} with paginated part of all unapproved loans
     */
    private List<LoanDto> getUnapprovedLoans(long userId, HttpServletRequest request, LoanService loanService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int previousRecordNumber = paginationHelper.getPreviousRecordNumber(request, recordsPerPage);

        return loanService
                .getUnapprovedLoansByUser(userId, recordsPerPage, previousRecordNumber);
    }

    /**
     * Adds pagination to request
     */
    private void addPaginationToRequest(long userId, HttpServletRequest request, PaginationHelper paginationHelper) {
        long recordsQuantity = loanService.getUnapprovedLoansByUserQuantity(userId);
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }

}
