package com.library.web.controller.actions;

import com.library.model.services.Service;
import com.library.model.services.UserService;
import com.library.model.data.entity.User;
import com.library.web.controller.ActionErrors;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;
import com.library.web.controller.forms.UserLoginForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Action for login user in the system
 */
public class LoginAction extends Action {

    private UserService userService;

    public static final Logger log = LogManager.getLogger(LoginAction.class);

    /**
     * If user with given combination of e-mail and password exist gets it
     * from DB and sets it as session attribute
     * {@inheritDoc}
     * @return - a path to the page where user came from for login or path
     * to login page for re-entering user login data if user is not found in DB
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        String redirectPath;

        Optional<User> user = getUserAccount(form);
        if (user.isPresent()) {

            redirectPath = (String) request.getSession().getAttribute("referentUrl");
            request.getSession().removeAttribute("referentUrl");
            request.getSession().setAttribute("loginedUser", user.get());

        } else {
            //User is not found. Adds errors to the request and forward to
            //user for re-entering data
            setRequestErrorAttributes(request, form);
            redirectPath = resources.getForward("ShowLoginPage");
        }
        return redirectPath;
    }

    /**
     * Gives an {@code Optional} with {@code User} if user with given
     * combination of e-mail and password exist
     * @param form - form with users e-mail & password data
     * @return - the target {@code Optional} with {@code User} or
     * an empty {@code Optional}
     */
    private Optional<User> getUserAccount(ActionForm form) {
        UserLoginForm loginForm =(UserLoginForm) form;
        return userService.getUserByLoginInfo(loginForm.getEmail(), loginForm.getPassword());
    }

    /**
     * Sets in request errors attribute
     */
    private void setRequestErrorAttributes(HttpServletRequest request, ActionForm form) {
        ActionErrors errors = new ActionErrors();
        errors.addError("loginError", "login.error.noSuchUser");
        request.setAttribute("errors", errors);

        ((UserLoginForm) form).setPassword("");
        request.setAttribute("form", form);
    }

    public void setUserService(Service userService) {
        this.userService = (UserService) userService;
    }
}
