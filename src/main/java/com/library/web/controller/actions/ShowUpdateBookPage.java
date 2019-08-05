package com.library.web.controller.actions;

import com.library.repository.dto.BookDto;
import com.library.repository.entity.Author;
import com.library.repository.entity.Keyword;
import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.BookCreationForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action for preparing before showing book update page
 */
public class ShowUpdateBookPage extends Action {

    private BookService bookService;

    /**
     * Creates {@code BookCreationForm} object and fills it
     * with book information for its further updating
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the book updating page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {
        BookCreationForm updateForm = (BookCreationForm) form;
        BookDto bookDto = (BookDto) request.getSession().getAttribute("bookDto");

        setUpUpdateForm(updateForm, bookDto);

        List<Author> authors = bookService.getAllAuthors();
        List<Keyword> keywords = bookService.getAllKeywords();

        request.setAttribute("authors", authors);
        request.setAttribute("keywords", keywords);
        request.setAttribute("form", form);

        return resources.getForward("ShowBookUpdatingPage");
    }

    /**
     * Fills {@code form} fields with {@code BookDto} information
     * @param updateForm - form with book data
     * @param bookDto contains book data need to be set into form
     */
    private void setUpUpdateForm(BookCreationForm updateForm, BookDto bookDto) {

        updateForm.setTitle(bookDto.getBook().getTitle());
        updateForm.setYearString(String.valueOf(bookDto.getBook().getYear()));
        updateForm.setDescription(bookDto.getBook().getDescription());
        updateForm.setOldAuthorsId(bookDto.getAuthors().stream()
                .map(Author::getId).collect(Collectors.toList()));
        updateForm.setOldKeywordsId(bookDto.getKeywords().stream()
                .map(Keyword::getId).collect(Collectors.toList()));
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }

}
