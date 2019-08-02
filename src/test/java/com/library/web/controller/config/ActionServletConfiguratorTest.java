package com.library.web.controller.config;

import com.library.web.controller.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActionServletConfiguratorTest {

    @Mock
    ActionServletConfig actionServletConfig;

    @Mock
    ActionConfig actionConfig;

    @Mock
    ActionFactory actionFactory;

    @Mock
    FormFactory formFactory;

    @Before
    public void initMock(){

    }

    @Test
    public void addFormToFormFactoryShouldAddForm(){
        FormConfig testForm = new FormConfig();
        testForm.setName("testName");
        testForm.setType("testClassName");
        List<FormConfig> formConfigs = Collections.singletonList(testForm);

        when(actionServletConfig.getForms()).thenReturn(formConfigs);
        when(actionConfig.getFormName()).thenReturn("testName");
        when(actionConfig.getPath()).thenReturn("testPath");

        new ActionServletConfigurator().addFormToFormFactory(actionConfig, actionServletConfig, formFactory);

        verify(formFactory, times(1)).addFormClass("testPath", "testClassName");
    }

    @Test(expected = RuntimeException.class)
    public void addFormToFormFactoryShouldThrowRuntimeException() {
        FormConfig testForm = new FormConfig();
        testForm.setName("testName");
        List<FormConfig> formConfigs = Collections.singletonList(testForm);

        when(actionServletConfig.getForms()).thenReturn(formConfigs);
        when(actionConfig.getFormName()).thenReturn("anotherTestName");

        new ActionServletConfigurator().addFormToFormFactory(actionConfig, actionServletConfig, formFactory);
    }

    @Test
    public void setUpFactoriesShouldAddActionAndForm() {
        ActionServletConfigurator configurator = spy(new ActionServletConfigurator());
        ActionConfig action = new ActionConfig();
        action.setInput("testInput");
        action.setPath("testPath");
        action.setValidate("true");
        action.setType("testType");
        List<ActionConfig> configs = Collections.singletonList(action);

        when(actionServletConfig.getActions()).thenReturn(configs);
        doNothing().when(configurator).addFormToFormFactory(any(), any(), any());

        configurator.setUpFactories(actionServletConfig, actionFactory, formFactory);

        verify(actionFactory, times(1))
                .addAction("testPath", "testType", true, "testInput", actionConfig.getServiceDependencyList());

        verify(configurator, times(1)).addFormToFormFactory(action, actionServletConfig, formFactory);
    }

    @Test
    public void setUpFactoriesShouldAddActionAndNotAddForm() {
        ActionServletConfigurator configurator = spy(new ActionServletConfigurator());
        ActionConfig action = new ActionConfig();
        action.setPath("testPath");
        action.setValidate("false");
        action.setType("testType");
        List<ActionConfig> configs = Collections.singletonList(action);

        when(actionServletConfig.getActions()).thenReturn(configs);
        doNothing().when(configurator).addFormToFormFactory(any(), any(), any());

        configurator.setUpFactories(actionServletConfig, actionFactory, formFactory);

        verify(actionFactory, times(1))
                .addAction("testPath", "testType", false, null, actionConfig.getServiceDependencyList());

        verify(configurator, never()).addFormToFormFactory(action, actionServletConfig, formFactory);
    }
}