package com.library.web.controller.actions;

import com.library.repository.dto.BookDto;
import com.library.services.BookService;
import com.library.services.Service;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.BookIdForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Action for showing book management page
 */
public class ShowBookManagementAction extends Action {

    private BookService bookService;

    /**
     * Gets {@code BookDto} from {@code Service} by book ID,
     * stores it as request attribute and forwards to book management
     * page
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to book management page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long bookId = ((BookIdForm) form).getBookId();

        Optional<BookDto> bookDto = bookService.getBookDtoById(bookId);

        bookDto.ifPresent(dto -> request.setAttribute("bookDto", bookDto.get()));

        return resources.getForward("ShowBookManagementPage");
    }

    public void setBookService(Service bookService) {
        this.bookService = (BookService) bookService;
    }

}
