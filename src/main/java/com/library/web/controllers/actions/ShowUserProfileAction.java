package com.library.web.controllers.actions;

import com.library.repository.entity.User;
import com.library.services.Service;
import com.library.services.UserService;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.UserProfileForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Action for showing page with user's profile
 */
public class ShowUserProfileAction extends Action {

    private UserService userService;

    /**
     * Gets target user from {@code Service} and saves it as
     * a request parameter
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to showing user profile page or path to error page
     * if user getting is failed
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long userId = ((UserProfileForm) form).getUserId();

        Optional<User> userOptional = userService.getUserById(userId);

        String forwardPath;
        if (userOptional.isPresent()) {
            request.setAttribute("borrower", userOptional.get());
            forwardPath = resources.getForward("showUserProfilePage");
        } else {
            forwardPath = resources.getForward("showErrorPage");
        }

        return forwardPath;
    }

    public void setUserService(Service userService) {
        this.userService = (UserService) userService;
    }

}
