package com.library.web.controllers.actions;

import com.library.repository.entity.User;
import com.library.services.LoanService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.OrderBookForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for ordering a book by an user
 */
public class OrderBookAction extends Action {

    private LoanService loanService;

    /**
     * Takes target book's ID from {@code form} and creates
     * a loan for this book and for user stored in current
     * session
     * @param request {@inheritDoc}
     * @param response {@inheritDoc}
     * @param form - {@inheritDoc}
     * @param resources - {@inheritDoc}
     * @return path for forwarding to the show book page with
     * ordering result info
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        if (makeApplyForBook(request, form)) {
            request.setAttribute("orderResult", "bookSearch.result.successfulOrder");
        } else {
            request.setAttribute("orderResult", "bookSearch.result.failedOrder");
        }

        return resources.getForward("ShowOrderedBooksAction");
    }

    /**
     * Tries to make an apply for target book for current user
     * @param request - the request with user info stored in session
     * @param form - form with target book's ID
     * @return boolean result of making application
     */
    boolean makeApplyForBook(HttpServletRequest request, ActionForm form) {

        long bookId = ((OrderBookForm) form).getBookId();
        long userId = ((User) request.getSession().getAttribute("loggedInUser")).getId();

        return loanService.saveApplyForBook(bookId, userId);
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }
}
