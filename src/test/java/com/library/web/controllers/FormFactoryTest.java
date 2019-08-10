package com.library.web.controllers;

import com.library.web.controllers.forms.ActionForm;
import com.library.web.controllers.forms.UserRegistrationForm;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormFactoryTest {

    @Test
    public void getFormShouldReturnNull() {
        FormFactory factory = FormFactory.getInstance();
        assertNull(factory.getForm("nonexistent path"));
    }

    @Test
    public void getFormShouldReturnCorrectForm() {
        FormFactory factory = FormFactory.getInstance();
        factory.addFormClass("test", "com.library.web.controllers.forms.UserRegistrationForm");
        ActionForm form = factory.getForm("test");
        assertTrue(form instanceof UserRegistrationForm);
    }
}