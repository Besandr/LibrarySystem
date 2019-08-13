package com.library.web.controllers.actions;

import com.library.repository.entity.User;
import com.library.services.UserService;
import com.library.web.controllers.ActionErrors;
import com.library.web.controllers.ServletResources;
import com.library.web.controllers.forms.UserRegistrationForm;
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
public class RegisterUserActionTest {

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

    private RegisterUserAction action;
    private UserRegistrationForm form;
    private User user;

    @Before
    public void init() {
        action = spy(new RegisterUserAction());
        form = new UserRegistrationForm();
        action.setUserService(userService);
        user = User.builder()
                .id(3L)
                .firstName("testFirst")
                .lastName("testLast")
                .phone("testPhone")
                .email("testMail")
                .password("testPassword")
                .build();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void executeShouldForwardToRegistrationPageAndSetErrorsAttribute() {
        when(action.createUser(form)).thenReturn(Optional.empty());
        when(resources.getForward("ShowRegistrationPage")).thenReturn("testPath");

        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
        verify(action).setRequestErrorAttributes(request, form);
    }

    @Test
    public void executeShouldForwardToTargetPathAndSetLogInAttribute() {
        when(action.createUser(form)).thenReturn(Optional.of(user));
        doReturn("testPath").when(action).getRedirectPath(session, resources);

        String returnedPath = action.execute(request, response, form, resources);
        assertEquals("testPath", returnedPath);
        verify(session).setAttribute("loggedInUser", user);
    }

    @Test
    public void getRedirectPathShouldReturnPostponedPath() {
        when(session.getAttribute("postponedRequestUrl")).thenReturn("postponedPath");
        when(resources.createRedirectPath("postponedPath")).thenReturn("testPath");
        String returnedPath = action.getRedirectPath(session, resources);
        assertEquals("testPath", returnedPath);
    }

    @Test
    public void getRedirectPathShouldReturnPathToShowPersonalCabinetAction() {
        when(session.getAttribute("postponedRequestUrl")).thenReturn(null);
        when(resources.getForward("ShowPersonalCabinetAction")).thenReturn("testPath");
        String returnedPath = action.getRedirectPath(session, resources);
        assertEquals("testPath", returnedPath);
    }

    @Test
    public void createUserShouldCorrectlyCreateUserFromFormData() {
        form.setFirstName("testFirst");
        form.setLastName("testLast");
        form.setPassword("testPassword");
        form.setPhone("testPhone");
        form.setEmail("testMail");

        action.createUser(form);
        verify(userService).createNewUser("testFirst", "testLast"
                , "testMail", "testPhone", "testPassword");
    }

    @Test
    public void setRequestErrorAttributesShouldCreateErrorsAndSetErrorAttributes() {
        ArgumentCaptor<ActionErrors> errors = ArgumentCaptor.forClass(ActionErrors.class);
        doNothing().when(request).setAttribute(eq("errors"), errors.capture());

        action.setRequestErrorAttributes(request, form);
        verify(request).setAttribute("form", form);
        assertEquals("registration.error.userExist", errors.getValue().getErrorsMap().get("userError"));
    }
}