package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains html-form data for showing user profile
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileForm extends ActionForm {

    private long userId;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        userId = getLongPropertyFromRequest(request, "userId");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {
        return new ActionErrors();
    }


}
