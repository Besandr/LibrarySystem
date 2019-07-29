package com.library.web.controller.actions;

import com.library.model.services.BookService;
import com.library.model.data.dto.BookDto;
import com.library.model.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.BookSearchForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for searching book for ordering
 */
public class BookSearchAction extends Action {

    private BookService bookService;

    /**
     * Finds all book which fits to user's book search
     * parameters and sets {@code List} with searching
     * results as session attribute
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path for forwarding to the book search page
     * for showing the searching results
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        addBooksToSession(request, getBooksList(form));
        return resources.getForward("ShowBookSearchPage");
    }

    /**
     * Takes user's book searching parameters and asks to
     * {@code BookService} to seek for books.
     * @param form - form with book searching parameters
     * @return list with searching results
     */
    private List<BookDto> getBooksList(ActionForm form) {

        long author = getIdFromString(((BookSearchForm)form).getAuthorId());
        long keyword = getIdFromString(((BookSearchForm)form).getKeywordId());
        String bookTitle = ((BookSearchForm)form).getBookTitle();

        return bookService.findBooks(author, keyword, bookTitle);
    }

    /**
     * Adds list with books to user's session
     */
    private void addBooksToSession(HttpServletRequest request, List<BookDto> booksList) {
        request.getSession().setAttribute("books", booksList);
    }

    /**
     * Converts {@code String} with long value to {@code Long}.
     * @param stringId string representation of ID
     * @return {@code} long value of ID or {@code -1} if
     * given string can't be converted to {@code Long}
     */
    private long getIdFromString(String stringId) {
        try{
            return Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }
}
