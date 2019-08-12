package com.library.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServletResourcesTest {

    @Test
    public void getForwardShouldReturnCorrectPath() {
        HashMap<String, String> forwards = new HashMap<>();
        forwards.put("testForward", "testPath");

        ServletResources resources = new ServletResources(null, null, forwards, null);
        String result = resources.getForward("testForward");
        assertEquals("testPath", result);
    }

    @Test
    public void getForwardShouldReturn404Path() {
        HashMap<String, String> forwards = new HashMap<>();
        forwards.put("testForward", "testPath");

        ServletResources resources = new ServletResources(null, null, forwards, null);
        String result = resources.getForward("wrongForward");
        assertEquals("/WEB-INF/jsp/404.jsp", result);
    }
}