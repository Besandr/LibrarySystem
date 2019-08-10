package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
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
        loanId = getLongPropertyFromRequest(request, "loanId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }
}
