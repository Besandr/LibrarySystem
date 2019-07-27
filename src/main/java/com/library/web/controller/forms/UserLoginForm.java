package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
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
        email = request.getParameter("email") == null ? "" : request.getParameter("email");
        password = request.getParameter("password") == null ? "" : request.getParameter("password");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {

        ActionErrors errors = new ActionErrors();

        if (!email.matches("^[A-z]+@[A-z]+\\.[A-z]+$")) {
            errors.addError("email", "login.error.email");
        }

        if (password.isEmpty()) {
            errors.addError("password", "login.error.password");
        }

        return errors;
    }
}
