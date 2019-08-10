package com.library.web.controllers.actions;

import com.library.repository.dto.LoanDto;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.BookIdForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for showing active loans of particular book
 */
public class ShowActiveBookLoansAction extends Action {

    private LoanService loanService;

    /**
     * Gets active loans list with particular book and attaches
     * it to a request as attribute
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path to view with active book loans table
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long bookId = ((BookIdForm) form).getBookId();

        List<LoanDto> bookLoans = loanService.getActiveLoansByBook(bookId);

        request.setAttribute("bookLoans", bookLoans);

        return resources.getForward("showActiveBookLoansPage");
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }

}
