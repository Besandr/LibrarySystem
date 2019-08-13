package com.library.web.controllers.actions;

import com.library.repository.entity.User;
import com.library.services.UserService;
import com.library.web.controllers.ActionErrors;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.UserLoginForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActionTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletResources resources;

    @Mock
    HttpSession session;

    @Mock
    UserService userService;

    private LoginAction action;
    private UserLoginForm form;
    private User user;

    @Before
    public void init() {
        action = spy(new LoginAction());
        form = new UserLoginForm();
        action.setUserService(userService);
        user = User.builder().id(3L).firstName("testUser").build();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void getPostponedPathShouldReturnCorrectPath() {
        when(session.getAttribute("postponedRequestUrl")).thenReturn("testPath");
        String result = action.getPostponedPath(session);
        assertEquals("testPath", result);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void getUserAccountShouldProperlyGetUserAccount() {
        form.setEmail("testEmail");
        form.setPassword("testPassword");
        when(userService.getUserByLoginInfo("testEmail", "testPassword")).thenReturn(Optional.of(user));
        Optional<User> result = action.getUserAccount(form);
        assertEquals(user, result.get());
    }

    @Test
    public void setRequestErrorAttributesShouldProperlySetRequestAttributes() {
        ArgumentCaptor<ActionErrors> errors = ArgumentCaptor.forClass(ActionErrors.class);
        doNothing().when(request).setAttribute(eq("errors"), errors.capture());

        action.setRequestErrorAttributes(request, form);

        verify(request).setAttribute("form", form);
        assertEquals("login.error.noSuchUser", errors.getValue().getErrorsMap().get("loginError"));
    }

    @Test
    public void executeShouldAddLoggedInUserToSession(){
        doReturn(Optional.of(user)).when(action).getUserAccount(form);
        action.execute(request, response, form, resources);
        verify(session).setAttribute("loggedInUser", user);
    }

    @Test
    public void executeShouldForwardToPostponedPath() {
        doReturn(Optional.of(user)).when(action).getUserAccount(form);
        when(action.getPostponedPath(session)).thenReturn("testPath");
        when(resources.createRedirectPath("testPath")).thenReturn("redirectedTestPath");

        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("redirectedTestPath", returnedPath);
    }

    @Test
    public void executeShouldForwardToPersonalCabinetActionWithExistentUserAndNonExistentPostponedPath() {
        doReturn(Optional.of(user)).when(action).getUserAccount(form);
        when(resources.getForward("ShowPersonalCabinetAction")).thenReturn("testPath");

        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
    }

    @Test
    public void executeShouldForwardToLoginPageWithNonExistentUserAndSetErrors() {
        when(resources.getForward("ShowLoginPage")).thenReturn("testPath");
        String returnedPath = action.execute(request, response, form, resources);

        assertEquals("testPath", returnedPath);
        verify(action).setRequestErrorAttributes(request, form);
    }
}