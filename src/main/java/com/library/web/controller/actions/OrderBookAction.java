package com.library.web.controller.actions;

import com.library.model.data.entity.User;
import com.library.model.services.LoanService;
import com.library.model.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.OrderBookForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        removeBookSearchAttributes(request.getSession());

        return resources.getForward("ShowBookSearchPage");
    }

    /**
     * Tries to make an apply for target book for current user
     * @param request - the request with user info stored in session
     * @param form - form with target book's ID
     * @return boolean result of making application
     */
    private boolean makeApplyForBook(HttpServletRequest request, ActionForm form) {
        long bookId = Long.parseLong(((OrderBookForm) form).getBookId());
        long userId = ((User) request.getSession().getAttribute("currentUser")).getId();

        return loanService.saveApplyForBook(bookId, userId);
    }

    /**
     * Removes attributes needed for book ordering
     * stored in session
     * @param session - current user's session with stored
     *                book searching results
     */
    private void removeBookSearchAttributes(HttpSession session) {
        session.removeAttribute("books");
    }

    public void setLoanService(Service loanService) {
        this.loanService = (LoanService) loanService;
    }
}
