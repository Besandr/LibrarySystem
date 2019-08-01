package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data for showing book
 * borrowers
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveBookLoansForm extends ActionForm {

    /**
     * ID of book for showing its active loans
     */
    private long bookId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        String bookIdString = getPropertyFromRequest(request, "bookId");
        try {
            bookId = Long.parseLong(bookIdString);
        } catch (NumberFormatException e) {
            bookId = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        return null;
    }
}
