package com.library.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaginationHelperTest {

    @Mock
    PaginationHelper paginationHelper;

    @Before
    public void initMock() {
        paginationHelper = spy(new PaginationHelper());
    }

    @Test
    public void addPaginationToRequestShouldProperlyAddRequestParameters() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        ArgumentCaptor<Integer> pageCapturedValue = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pagesQuantityValue = ArgumentCaptor.forClass(Integer.class);

        final int TEST_PAGE_NUMBER = 11;
        doReturn(TEST_PAGE_NUMBER).when(paginationHelper).getCurrentPageNumber(request);

        long TEST_RECORDS_QUANTITY = 3L;
        int TEST_PAGES_QUANTITY = 17;
        doReturn(TEST_PAGES_QUANTITY).when(paginationHelper).getPagesQuantity(eq(TEST_RECORDS_QUANTITY), anyInt());
        doNothing().when(request).setAttribute(eq("currentPage"), pageCapturedValue.capture());
        doNothing().when(request).setAttribute(eq("pagesQuantity"), pagesQuantityValue.capture());

        paginationHelper.addPaginationToRequest(request, TEST_RECORDS_QUANTITY);

        assertEquals(TEST_PAGE_NUMBER, pageCapturedValue.getValue().intValue());
        assertEquals(TEST_PAGES_QUANTITY, pagesQuantityValue.getValue().intValue());
    }

    @Test
    public void getCurrentPageNumberShouldReturnTestPageNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String TEST_PAGE_NUMBER_STRING = "11";
        when(request.getParameter("page")).thenReturn(TEST_PAGE_NUMBER_STRING);

        int result = paginationHelper.getCurrentPageNumber(request);

        final int TEST_PAGE_NUMBER = 11;
        assertEquals(TEST_PAGE_NUMBER, result);
    }

    @Test
    public void getCurrentPageNumberShouldReturnOne() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("page")).thenReturn(null);

        int result = paginationHelper.getCurrentPageNumber(request);
        assertEquals(1, result);
    }

    @Test
    public void getPagesQuantityShouldReturnFive() {
        int result = paginationHelper.getPagesQuantity(21, 5);
        assertEquals(5, result);
    }
}