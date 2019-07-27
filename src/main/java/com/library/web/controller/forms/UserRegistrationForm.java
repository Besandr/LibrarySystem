package com.library.web.controller.forms;

import com.library.web.controller.ActionErrors;
import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class represents user registration html-form
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationForm extends ActionForm {

    //Form fields
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(HttpServletRequest request) {
        firstName = request.getParameter("firstName") == null ? "" : request.getParameter("firstName");
        lastName = request.getParameter("lastName") == null ? "" : request.getParameter("lastName");
        email = request.getParameter("email") == null ? "" : request.getParameter("email");
        phone = request.getParameter("phone") == null ? "" : request.getParameter("phone");
        password = request.getParameter("password") == null ? "" : request.getParameter("password");
        confirmPassword = request.getParameter("confirmPassword") == null ? "" : request.getParameter("confirmPassword");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionErrors validate() {

        ActionErrors errors = new ActionErrors();

        String namePattern = "^[\\p{Alpha}]+$";
        Pattern pattern = Pattern.compile(namePattern, Pattern.UNICODE_CHARACTER_CLASS);

        Matcher matcher = pattern.matcher(firstName);
        if (!matcher.matches()) {
            errors.addError("firstName", "registration.error.firstName");
        }

        matcher = pattern.matcher(lastName);
        if (!matcher.matches()) {
            errors.addError("lastName", "registration.error.lastName");
        }

        if (!email.matches("^[A-z]+@[A-z]+\\.[A-z]+$")) {
            errors.addError("email", "registration.error.email");
        }

        if (!phone.matches("^\\d{10,15}$")) {
            errors.addError("phone", "registration.error.phone");
        }

        if (password.length() < 5) {
            errors.addError("password", "registration.error.password");
            eraseWrongPasswords();
        }

        if (!password.equals(confirmPassword)) {
            errors.addError("confirmPassword", "registration.error.confirmPassword");
            eraseWrongPasswords();
        }

        return errors;
    }

    /**
     * Erases passwords data from form if some
     * errors with passwords occurred
     */
    private void eraseWrongPasswords() {
        password = "";
        confirmPassword = "";
    }
}
