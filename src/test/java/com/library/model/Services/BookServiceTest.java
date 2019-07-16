package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.dao.AuthorDao;
import com.library.model.data.dao.BookDao;
import com.library.model.data.dao.KeywordDao;
import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    BookService mockService;

    @Mock
    DaoManager mockManager;

    @Before
    public void initMocks() {
        mockService = spy(BookService.getInstance());
    }

    @Test
    public void addBookToCatalogueCommandShouldReturnTrue() throws SQLException {

        BookDao mockDao = mock(BookDao.class);
        Book mockBook = mock(Book.class);
        when(mockManager.getBookDao()).thenReturn(mockDao);
        when(mockDao.save(mockBook)).thenReturn(1L);

        boolean result = mockService.addBookToCatalogueCommand(mockManager, mockBook);

        verify(mockService, times(1)).saveAuthors(eq(mockManager), anySet());
        verify(mockService, times(1)).saveKeywords(eq(mockManager), anySet());
        verify(mockDao, times(1)).save(mockBook);
        assertTrue(result);
    }

    @Test
    public void addBookToCatalogueCommandShouldReturnFalse() throws SQLException {

        BookDao mockDao = mock(BookDao.class);
        Book mockBook = mock(Book.class);
        when(mockManager.getBookDao()).thenReturn(mockDao);
        when(mockDao.save(mockBook)).thenReturn(-1L);

        boolean result = mockService.addBookToCatalogueCommand(mockManager, mockBook);

        assertFalse(result);
    }

    @Test
    public void saveAuthors() throws SQLException {
        AuthorDao mockDao = mock(AuthorDao.class);
        when(mockManager.getAuthorDao()).thenReturn(mockDao);

        Author author1 = mock(Author.class);
        when(author1.getId()).thenReturn(0L);
        when(author1.getFirstName()).thenReturn("1");

        Author author2 = mock(Author.class);
        when(author2.getId()).thenReturn(0L);
        when(author2.getFirstName()).thenReturn("2");

        Author author3 = mock(Author.class);
        when(author3.getId()).thenReturn(1L);
        when(author3.getFirstName()).thenReturn("3");

        when(mockDao.save(author1)).thenReturn(4L);
        when(mockDao.save(author2)).thenReturn(-1L);

        HashSet<Author> authors = Stream.of(author1, author2, author3).collect(Collectors.toCollection(HashSet::new));

        mockService.saveAuthors(mockManager, authors);
        verify(mockDao, times(2)).save(any(Author.class));
        verify(author1, times(1)).setId(4L);
        verify(author2, never()).setId(anyLong());
        verify(author3, never()).setId(anyLong());

    }

    @Test
    public void saveKeywords() throws SQLException {

        KeywordDao mockDao = mock(KeywordDao.class);
        when(mockManager.getKeywordDao()).thenReturn(mockDao);

        Keyword keyword1 = mock(Keyword.class);
        when(keyword1.getId()).thenReturn(0L);
        when(keyword1.getWord()).thenReturn("1");

        Keyword keyword2 = mock(Keyword.class);
        when(keyword2.getId()).thenReturn(0L);
        when(keyword2.getWord()).thenReturn("2");

        Keyword keyword3 = mock(Keyword.class);
        when(keyword3.getId()).thenReturn(1L);
        when(keyword3.getWord()).thenReturn("3");

        when(mockDao.save(keyword1)).thenReturn(4L);
        when(mockDao.save(keyword2)).thenReturn(-1L);

        HashSet<Keyword> keywords = Stream.of(keyword1, keyword2, keyword3).collect(Collectors.toCollection(HashSet::new));

        mockService.saveKeywords(mockManager, keywords);
        verify(mockDao, times(2)).save(any(Keyword.class));
        verify(keyword1, times(1)).setId(4L);
        verify(keyword2, never()).setId(anyLong());
        verify(keyword3, never()).setId(anyLong());

    }
}