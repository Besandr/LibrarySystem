package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data for adding bookcase
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBookcaseForm extends ActionForm {

    private int shelfQuantity;
    private int cellQuantity;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        shelfQuantity = (int) getLongPropertyFromRequest(request, "shelfQuantity");
        cellQuantity = (int) getLongPropertyFromRequest(request, "cellQuantity");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        ActionErrors errors = new ActionErrors();

        if (shelfQuantity < 1) {
            errors.addError("shelfQuantity", "bookcaseManagement.addingBookcase.error.shelfQuantity");
        }

        if (cellQuantity < 1) {
            errors.addError("cellQuantity", "bookcaseManagement.addingBookcase.error.cellQuantity");
        }
        return errors;
    }
}
