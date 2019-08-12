package com.library.web.controllers;

import com.library.web.controllers.actions.Action;
import com.library.web.controllers.actions.ChangeLanguageAction;
import com.library.web.controllers.config.ServletConfigException;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class ActionFactoryTest {

    @Test
    public void createActionShouldReturnCorrectAction() {
        ActionFactory factory = ActionFactory.getInstance();
        Action returnedAction = factory.createAction("com.library.web.controllers.actions.ChangeLanguageAction", false, null, Collections.emptyList());
        assertTrue(returnedAction instanceof ChangeLanguageAction);
    }

    @Test(expected = ServletConfigException.class)
    public void createActionShouldThrowException() {
        ActionFactory.getInstance().createAction("false class name", false, null, Collections.emptyList());
    }

    @Test
    public void createSetterNameShouldReturnExpectedName(){
        ActionFactory factory = ActionFactory.getInstance();
        String variableName = "test";
        String expectedSetterName = "setTest";
        String resultName = factory.createSetterName(variableName);
        assertEquals(expectedSetterName, resultName);
    }

}