package com.library.web.controllers.forms;

import com.library.web.controllers.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Class represents user login html-form
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginForm extends ActionForm {
    //Form fields
    String email;
    String password;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        email = getPropertyFromRequest(request, "email");
        password = getPropertyFromRequest(request, "password");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {

        ActionErrors errors = new ActionErrors();

        if (!email.matches("^[A-z\\d]+@[A-z]+\\.[A-z]+$")) {
            errors.addError("email", "login.error.email");
        }

        if (password.isEmpty()) {
            errors.addError("password", "login.error.password");
        }

        return errors;
    }
}
