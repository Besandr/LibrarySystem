package com.library.web.controllers.actions;

import com.library.services.Service;
import com.library.services.UserService;
import com.library.repository.entity.User;
import com.library.web.controllers.ActionErrors;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.UserRegistrationForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Action for registering user
 */
public class RegisterUserAction extends Action {

    private UserService userService;

    /**
     * Uses {@code UserService} for registering a new user. After
     * creating the user adds it to the session as attribute
     * (automatically login user).
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return a path to {@code ShowPersonalCabinetAction} or path
     * to registering page for re-entering user data if user
     * is already exist in DB
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response,
                          ActionForm form, ServletResources resources) {

        String redirectPath;

        Optional<User> userOptional = createUser(form);

        //Checking is user created
        if (userOptional.isPresent()) {
            redirectPath = getRedirectPath(request.getSession(), resources);
            request.getSession().setAttribute("loggedInUser", userOptional.get());
        } else {
            //User is not created. Adds errors to the request and forward to
            //user for the next registration try.
            setRequestErrorAttributes(request, form);
            redirectPath = resources.getForward("ShowRegistrationPage");
        }

        return redirectPath;
    }

    /**
     * It is possible a situation when not logged in user will try to
     * access a constrained resource and he will be redirected to
     * login/registration page. If it is so the session will have a path(postponed)
     * to the requested constrained resource. After successful login
     * we need to restore this path and forward an user to it.
     */
    String getRedirectPath(HttpSession session, ServletResources resources) {
        String postponedPath = (String) session.getAttribute("postponedRequestUrl");
        if (postponedPath != null) {
            return resources.createRedirectPath(postponedPath);
        } else {
            return resources.getForward("ShowPersonalCabinetAction");
        }
    }

    /**
     * Creates an user from form data
     */
    Optional<User> createUser(ActionForm form) {
        UserRegistrationForm userForm = (UserRegistrationForm) form;
        return userService.createNewUser(
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getEmail(),
                userForm.getPhone(),
                userForm.getPassword());
    }

    /**
     * Sets in request errors attribute
     */
    void setRequestErrorAttributes(HttpServletRequest request, ActionForm form) {
        ActionErrors errors = new ActionErrors();
        errors.addError("userError", "registration.error.userExist");
        request.setAttribute("errors", errors);

        ((UserRegistrationForm) form).setPassword("");
        request.setAttribute("form", form);
    }

    public void setUserService(Service userService) {
        this.userService = (UserService) userService;
    }
}
