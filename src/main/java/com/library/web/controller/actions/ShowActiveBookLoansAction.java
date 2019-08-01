package com.library.web.controller.actions;

import com.library.repository.dto.LoanDto;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.ActiveBookLoansForm;

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
     * @return
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long bookId = ((ActiveBookLoansForm) form).getBookId();

        List<LoanDto> bookLoans = loanService.getActiveLoansByBook(bookId);

        request.setAttribute("bookLoans", bookLoans);

        return resources.getForward("showActiveBookLoansPage");
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }

}
