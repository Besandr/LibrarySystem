package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data with loan ID
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanIdForm extends ActionForm {

    /**
     * ID of loan
     */
    private long loanId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        loanId = getLongPropertyFromRequest(request, "loanId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        ActionErrors errors = new ActionErrors();
        if (loanId == 0) {
            errors.addError("loanId", "no loan id");
        }
        return errors;
    }
}
