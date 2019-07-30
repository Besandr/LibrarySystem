package com.library.web.controller.actions;

import com.library.repository.entity.Role;
import com.library.repository.entity.User;
import com.library.web.controller.ServletResources;
import com.library.web.controller.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for showing to logged in user his personal cabinet
 */
public class ShowPersonalCabinetAction extends Action{

    /**
     * Redirects logged in user to his personal cabinet
     * depending on his role
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the cabinet which corresponds
     * with current user's role
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        User user = (User) request.getSession().getAttribute("loggedInUser");

        return getCabinetPathByRole(user.getRole(), resources);
    }

    /**
     * Gives a path depending on user's role
     * @param role of current user
     * @param resources for accessing forwardings
     * @return path to the cabinet which corresponds
     * with current user's role
     */
    private String getCabinetPathByRole(Role role, ServletResources resources) {
        switch (role) {
            case USER:
                return resources.getForward("ShowUserCabinetPage");
            case ADMINISTRATOR:
                return resources.getForward("ShowAdminCabinetPage");
            default:
                return resources.getForward("ShowTitlePage");
        }
    }

}
