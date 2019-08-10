package com.library.web.controllers.actions;

import com.library.repository.entity.Author;
import com.library.repository.entity.Keyword;
import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Action for creating a new book in library's catalogue
 */
public class ShowBookCreationPageAction extends Action {

    BookService bookService;

    /**
     * Gets all the {@code Author} and {@code Keyword} from the
     * {@code BookService} and saves it as a request attributes
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path for book creation page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        List<Author> authors = bookService.getAllAuthors();
        List<Keyword> keywords = bookService.getAllKeywords();

        request.setAttribute("authors", authors);
        request.setAttribute("keywords", keywords);

        return resources.getForward("showBookCreationPage");
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }
}
