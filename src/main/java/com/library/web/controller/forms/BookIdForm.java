package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data with book ID
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIdForm extends ActionForm {

    /**
     * ID of book
     */
    private long bookId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        bookId = getLongPropertyFromRequest(request, "bookId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        ActionErrors errors = new ActionErrors();
        if (bookId == 0) {
            errors.addError("bookId", "no book id");
        }
        return errors;
    }
}
