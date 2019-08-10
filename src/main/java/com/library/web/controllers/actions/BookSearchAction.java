package com.library.web.controllers.actions;

import com.library.services.BookService;
import com.library.repository.dto.BookDto;
import com.library.services.Service;
import com.library.web.controllers.PaginationHelper;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.BookSearchForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for searching book for ordering
 */
public class BookSearchAction extends Action {

    private BookService bookService;

    /**
     * Finds paginated part of books which fits to user's book search
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

        long authorId = getIdFromString(((BookSearchForm)form).getAuthorId());
        long keywordId = getIdFromString(((BookSearchForm)form).getKeywordId());
        String bookTitle = ((BookSearchForm)form).getBookTitle();

        List<BookDto> bookDtoList = getBooksList(request, authorId, keywordId, bookTitle, bookService, paginationHelper);

        setRequestAttributes(request, authorId, keywordId, bookTitle, bookDtoList);
        addPaginationToRequest(request, bookService, authorId, keywordId, bookTitle, paginationHelper);

        return resources.getForward("ShowBookSearchPage");
    }

    /**
     * Saves in request book searching parameters and searching result
     * @param request for storing parameters
     * @param authorId - author's ID of target books
     * @param keywordId - keyword's ID of target books
     * @param bookTitle - part of title or whole title of target books
     * @param bookDtoList - list with target books
     */
    private void setRequestAttributes(HttpServletRequest request, long authorId, long keywordId,
                                      String bookTitle, List<BookDto> bookDtoList) {
        request.setAttribute("authorId", authorId);
        request.setAttribute("keywordId", keywordId);
        request.setAttribute("bookTitle", bookTitle);
        request.setAttribute("books", bookDtoList);
    }

    /**
     * Takes user's book searching parameters and asks to
     * {@code BookService} to search for books.
     * @param request holds pagination data
     * @param authorId - ID of target book's author
     * @param keywordId - ID of target book's keyword
     * @param bookTitle - title or it part of target book
     * @param bookService for getting books
     * @param paginationHelper for helping in pagination
     * @return list with searching results
     */
    private List<BookDto> getBooksList(HttpServletRequest request, long authorId, long keywordId,
                                       String bookTitle, BookService bookService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int currentPageNumber = paginationHelper.getCurrentPageNumber(request);
        int previousRecordNumber = (currentPageNumber-1)*recordsPerPage;

        return bookService.findBooks(authorId, keywordId, bookTitle, recordsPerPage, previousRecordNumber);
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

    /**
     * Adds pagination to request
     */
    private void addPaginationToRequest(HttpServletRequest request, BookService bookService, long authorId, long keywordId, String bookTitle, PaginationHelper paginationHelper) {
        long recordsQuantity = bookService.getBookSearchResultCount(authorId, keywordId, bookTitle);
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }
}
