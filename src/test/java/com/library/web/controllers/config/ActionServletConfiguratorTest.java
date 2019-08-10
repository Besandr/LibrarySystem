package com.library.web.controllers.config;

import com.library.web.controllers.*;
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
    ActionConfig mockActionConfig;

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
        when(mockActionConfig.getFormName()).thenReturn("testName");
        when(mockActionConfig.getPath()).thenReturn("testPath");

        new ActionServletConfigurator().addFormToFormFactory(mockActionConfig, actionServletConfig, formFactory);

        verify(formFactory, times(1)).addFormClass("testPath", "testClassName");
    }

    @Test(expected = RuntimeException.class)
    public void addFormToFormFactoryShouldThrowRuntimeException() {
        FormConfig testForm = new FormConfig();
        testForm.setName("testName");
        List<FormConfig> formConfigs = Collections.singletonList(testForm);

        when(actionServletConfig.getForms()).thenReturn(formConfigs);
        when(mockActionConfig.getFormName()).thenReturn("anotherTestName");

        new ActionServletConfigurator().addFormToFormFactory(mockActionConfig, actionServletConfig, formFactory);
    }

    @Test
    public void setUpFactoriesShouldAddActionAndForm() {
        ActionServletConfigurator configurator = spy(new ActionServletConfigurator());
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setInput("testInput");
        actionConfig.setPath("testPath");
        actionConfig.setValidate("true");
        actionConfig.setType("testType");
        actionConfig.setServiceDependencyList(Collections.emptyList());
        actionConfig.setFormName("testForm");
        List<ActionConfig> configs = Collections.singletonList(actionConfig);

        when(actionServletConfig.getActions()).thenReturn(configs);
        doNothing().when(configurator).addFormToFormFactory(any(), any(), any());

        configurator.setUpFactories(actionServletConfig, actionFactory, formFactory);

        verify(actionFactory)
                .addAction("testPath", "testType", true, "testInput", Collections.emptyList());

        verify(configurator).addFormToFormFactory(actionConfig, actionServletConfig, formFactory);
    }

    @Test
    public void setUpFactoriesShouldAddActionAndDoesNotAddForm() {
        ActionServletConfigurator configurator = spy(new ActionServletConfigurator());
        ActionConfig actionConfig = new ActionConfig();
        actionConfig.setPath("testPath");
        actionConfig.setValidate("false");
        actionConfig.setType("testType");
        actionConfig.setServiceDependencyList(Collections.emptyList());

        List<ActionConfig> configs = Collections.singletonList(actionConfig);

        when(actionServletConfig.getActions()).thenReturn(configs);

        configurator.setUpFactories(actionServletConfig, actionFactory, formFactory);

        verify(actionFactory)
                .addAction("testPath", "testType", false, null, Collections.emptyList());

        verify(configurator, never()).addFormToFormFactory(actionConfig, actionServletConfig, formFactory);
    }
}