package com.library.web.controller.actions;

import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.LoanIdForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for returning book to the library by an user
 */
public class ReturnBookAction extends Action {

    private LoanService loanService;

    /**
     * Returns book from an user to the library
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the user's cabinet page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long loanId = ((LoanIdForm) form).getLoanId();

        boolean returningResult = loanService.returnBook(loanId);

        if (returningResult) {
            request.setAttribute("actionResult", "userCabinet.returnBook.returningSuccessful");
        } else {
            request.setAttribute("actionResult", "userCabinet.returnBook.returningFailed");
        }

        return resources.getForward("ShowUserCabinetPage");
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }

}
