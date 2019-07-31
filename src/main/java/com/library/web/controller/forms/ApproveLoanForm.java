package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data for approving loan
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveLoanForm extends ActionForm{

    /**
     * ID of loan for approving
     */
    private long loanId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        String loanIdString = getPropertyFromRequest(request, "loanId");
        try {
            loanId = Long.parseLong(loanIdString);
        } catch (NumberFormatException e) {
            loanId = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }
}
