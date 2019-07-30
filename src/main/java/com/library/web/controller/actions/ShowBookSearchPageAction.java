package com.library.web.controller.actions;

import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for showing the book search page
 */
public class ShowBookSearchPageAction extends Action {

    private BookService bookService;

    /**
     * Attaches to the session attributes with authors
     * and keywords lists and shows the book searching page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path for forwarding to book searching page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        setAuthorsAttribute(request);
        setKeywordsAttribute(request);

        return resources.getForward("ShowBookSearchPage");
    }

    /**
     * Set authors session attribute with {@code List} with
     * all the authors
     * @param request - the request for access to user's session
     */
    private void setAuthorsAttribute(HttpServletRequest request) {
        request.getSession().setAttribute("authors", bookService.getAllAuthors());
    }

    /**
     * Set keywords session attribute with {@code List} with
     * all the keywords
     * @param request - the request for access to user's session
     */
    private void setKeywordsAttribute(HttpServletRequest request) {
        request.getSession().setAttribute("keywords", bookService.getAllKeywords());
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }
}
