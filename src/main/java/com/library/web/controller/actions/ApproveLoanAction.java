package com.library.web.controller.actions;

import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.ApproveLoanForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for approving loans
 */
public class ApproveLoanAction extends Action {

    LoanService loanService;

    /**
     * Approves a loan and sets approving results as request parameters
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        long loanId = ((ApproveLoanForm) form).getLoanId();
        boolean isApprovingSuccessful = loanService.approveLoan(loanId);

        request.setAttribute("approvingResult", isApprovingSuccessful);
        request.setAttribute("loanId", loanId);

        return resources.getForward("ShowUnapprovedLoansAction");
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }
}
