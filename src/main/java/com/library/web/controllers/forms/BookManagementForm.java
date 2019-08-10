package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Class contains data for book management action
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookManagementForm extends ActionForm {

    private int booksQuantity;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        booksQuantity = (int) getLongPropertyFromRequest(request, "booksQuantity");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        ActionErrors errors = new ActionErrors();
        if (booksQuantity == 0) {
            errors.addError("booksQuantity", "zero books quantity");
        }
        return errors;
    }


}
