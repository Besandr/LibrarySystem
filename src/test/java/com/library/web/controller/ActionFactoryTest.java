package com.library.web.controller;

import com.library.web.controller.actions.Action;
import com.library.web.controller.actions.ChangeLanguageAction;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class ActionFactoryTest {

    @Test
    public void createActionShouldReturnCorrectAction() {
        ActionFactory factory = ActionFactory.getInstance();
        Action returnedAction = factory.createAction("com.library.web.controller.actions.ChangeLanguageAction", false, null, Collections.emptyList());
        assertTrue(returnedAction instanceof ChangeLanguageAction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createActionShouldThrowException() {
        ActionFactory.getInstance().createAction("false class name", false, null, Collections.emptyList());
    }

}