package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.*;
import com.library.repository.dto.BookDto;
import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.entity.Keyword;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.*;
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

    @Mock
    BookDto mockBookDto;

    @Mock
    BookDao mockBookDao;

    @Mock
    AuthorDao mockAuthorDao;

    @Mock
    KeywordDao mockKeywordDao;

    @Mock
    AuthorBookDao mockAuthorBookDao;

    @Mock
    BookKeywordDao mockBookKeywordDao;

    @Mock
    Book mockBook;

    @Before
    public void initMocks() throws SQLException {
        mockService = spy(new BookService(new DaoManagerFactory()));
        when(mockManager.getBookDao()).thenReturn(mockBookDao);
        when(mockBookDto.getBook()).thenReturn(mockBook);
        when(mockManager.getAuthorDao()).thenReturn(mockAuthorDao);
        when(mockManager.getKeywordDao()).thenReturn(mockKeywordDao);
        when(mockManager.getAuthorBookDao()).thenReturn(mockAuthorBookDao);
        when(mockManager.getBookKeywordDao()).thenReturn(mockBookKeywordDao);
    }

    @Test
    public void saveAuthors() throws SQLException {

        Author author1 = mock(Author.class);
        when(author1.getId()).thenReturn(0L);

        Author author2 = mock(Author.class);
        when(author2.getId()).thenReturn(0L);
        when(author2.getFirstName()).thenReturn("2");

        Author author3 = mock(Author.class);
        when(author3.getId()).thenReturn(1L);

        when(mockAuthorDao.save(author1)).thenReturn(4L);
        when(mockAuthorDao.save(author2)).thenReturn(-1L);

        HashSet<Author> authors = Stream.of(author1, author2, author3).collect(Collectors.toCollection(HashSet::new));

        mockService.saveAuthors(mockManager, authors);
        verify(mockAuthorDao, times(2)).save(any(Author.class));
        verify(author1, times(1)).setId(4L);
        verify(author2, never()).setId(anyLong());
        verify(author3, never()).setId(anyLong());

    }

    @Test
    public void saveKeywords() throws SQLException {

        Keyword keyword1 = mock(Keyword.class);
        when(keyword1.getId()).thenReturn(0L);

        Keyword keyword2 = mock(Keyword.class);
        when(keyword2.getId()).thenReturn(0L);
        when(keyword2.getWord()).thenReturn("2");

        Keyword keyword3 = mock(Keyword.class);
        when(keyword3.getId()).thenReturn(1L);

        when(mockKeywordDao.save(keyword1)).thenReturn(4L);
        when(mockKeywordDao.save(keyword2)).thenReturn(-1L);

        HashSet<Keyword> keywords = Stream.of(keyword1, keyword2, keyword3).collect(Collectors.toCollection(HashSet::new));

        mockService.saveKeywords(mockManager, keywords);
        verify(mockKeywordDao, times(2)).save(any(Keyword.class));
        verify(keyword1, times(1)).setId(4L);
        verify(keyword2, never()).setId(anyLong());
        verify(keyword3, never()).setId(anyLong());

    }

    @Test
    public void saveBookShouldSetIdAndReturnTrue() throws SQLException {

        long id = 3;

        BookDao mockDao = mock(BookDao.class);
        Book mockBook = mock(Book.class);
        when(mockManager.getBookDao()).thenReturn(mockDao);
        when(mockDao.save(mockBook)).thenReturn(id);

        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(Long.class);

        doNothing().when(mockBook).setId((Long)valueCapture.capture());

        boolean result = mockService.saveBook(mockManager, mockBook);

        assertEquals(id, valueCapture.getValue());
        assertTrue(result);
    }

    @Test
    public void saveBookShouldReturnFalse() throws SQLException {

        long id = -1;

        BookDao mockDao = mock(BookDao.class);
        Book mockBook = mock(Book.class);
        when(mockManager.getBookDao()).thenReturn(mockDao);
        when(mockDao.save(mockBook)).thenReturn(id);

        boolean result = mockService.saveBook(mockManager, mockBook);

        assertFalse(result);
    }

    @Test
    public void testCreateBookDtoFromBook() throws SQLException {

        Author mockAuthor = mock(Author.class);
        List<Author> authorList = new ArrayList<>();
        authorList.add(mockAuthor);
        when(mockBook.getId()).thenReturn(3L);
        when(mockAuthorDao.getByBookId(mockBook.getId())).thenReturn(authorList);

        Keyword mockKeyword = mock(Keyword.class);
        List<Keyword> keywordList = new ArrayList<>();
        keywordList.add(mockKeyword);
        when(mockKeywordDao.getByBookId(mockBook.getId())).thenReturn(keywordList);

        BookDto expectedDto = BookDto.builder()
                .book(mockBook)
                .authors(new HashSet<>(authorList))
                .keywords(new HashSet<>(keywordList))
                .build();

        assertEquals("Created BookDto must be equal to the test BookDto",
                expectedDto, mockService.createBookDtoFromBook(mockManager, mockBook));
    }

    @Test
    public void testDeleteAuthorsShouldDeleteTwoAuthors() throws SQLException {

        Author author1 = mock(Author.class);
        Author author2 = mock(Author.class);
        Author author3 = mock(Author.class);
        Collection<Author> authors = Stream.of(author1, author2, author3).collect(Collectors.toSet());

        when(mockAuthorBookDao.doesAuthorHaveBooks(author1)).thenReturn(false);
        when(mockAuthorBookDao.doesAuthorHaveBooks(author2)).thenReturn(true);
        when(mockAuthorBookDao.doesAuthorHaveBooks(author3)).thenReturn(false);

        mockService.deleteAuthors(mockManager, authors);

        verify(mockAuthorDao, times(2)).delete(any(Author.class));

    }

    @Test
    public void testDeleteKeywordsShouldDeleteTwoKeywords() throws SQLException {

        Keyword keyword1 = mock(Keyword.class);
        Keyword keyword2 = mock(Keyword.class);
        Keyword keyword3 = mock(Keyword.class);
        Collection<Keyword> keywords = Stream.of(keyword1, keyword2, keyword3).collect(Collectors.toSet());

        when(mockBookKeywordDao.doesKeywordBelongToBook(keyword1)).thenReturn(false);
        when(mockBookKeywordDao.doesKeywordBelongToBook(keyword2)).thenReturn(true);
        when(mockBookKeywordDao.doesKeywordBelongToBook(keyword3)).thenReturn(false);

        mockService.deleteKeywords(mockManager, keywords);

        verify(mockKeywordDao, times(2)).delete(any(Keyword.class));

    }

    @Test
    public void testGetAuthorByName() {
        String firstName = "George R.R.";
        String lastName = "Martin";
        long id = 3;
        Optional<Author> exceptedOptional = Optional.of(Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .id(id)
                .build());
        when(mockAuthorDao.getByName(firstName, lastName)).thenReturn(exceptedOptional);

        assertEquals("getAuthorByName() should return expected optional",
                exceptedOptional, mockService.getAuthorByName(mockAuthorDao, firstName, lastName));
        assertNotEquals("getAuthorByName() should not return expected optional",
                exceptedOptional, mockService.getAuthorByName(mockAuthorDao, "Another name", lastName));
    }

    @Test
    public void testGetKeywordByWord() {
        String word = "test";
        long id = 3;

        Optional<Keyword> expected = Optional.of(Keyword.builder()
                .word(word)
                .id(id)
                .build());

        when(mockKeywordDao.getByWord(word)).thenReturn(expected);

        assertEquals("getKeywordByWord() should return expected optional",
                expected, mockService.getKeywordByWord(mockKeywordDao, word));

        assertNotEquals("getKeywordByWord() should not return expected optional",
                expected, mockService.getKeywordByWord(mockKeywordDao, "anotherWord"));
    }

    @Test
    public void addBookToCatalogueCommandShouldExecuteSavingMethodsAndReturnTrue() throws SQLException {

        doReturn(true).when(mockService).saveBook(mockManager, mockBook);

        when(mockManager.getAuthorBookDao()).thenReturn(mockAuthorBookDao);
        when(mockManager.getBookKeywordDao()).thenReturn(mockBookKeywordDao);

        boolean result = mockService.addBookToCatalogueCommand(mockManager, mockBookDto);

        verify(mockService, times(1)).saveBook(mockManager, mockBook);
        verify(mockService, times(1)).saveAuthors(eq(mockManager), anySet());
        verify(mockService, times(1)).saveKeywords(eq(mockManager), anySet());
        verify(mockAuthorBookDao, times(1)).saveAuthorBookJunction(eq(mockBook), anySet());
        verify(mockBookKeywordDao, times(1)).saveBookKeywordsJunction(eq(mockBook), anySet());

        assertTrue(result);
    }

    @Test
    public void addBookToCatalogueCommandShouldReturnFalse() throws SQLException {

        doReturn(false).when(mockService).saveBook(mockManager, mockBook);

        boolean result = mockService.addBookToCatalogueCommand(mockManager, mockBookDto);

        assertFalse(result);
    }

    @Test
    public void removeBookFromCatalogueCommandShouldReturnFalse() throws SQLException {

        doReturn(false).when(mockService).deleteBook(mockManager, mockBook);

        boolean result = mockService.removeBookFromCatalogueCommand(mockManager, mockBookDto);

        assertFalse(result);
    }

    @Test
    public void removeBookFromCatalogueCommandShouldExecuteSavingMethodsAndReturnTrue() throws SQLException {

        doReturn(true).when(mockService).deleteBook(mockManager, mockBook);

        boolean result = mockService.removeBookFromCatalogueCommand(mockManager, mockBookDto);

        verify(mockService, times(1)).deleteAuthors(eq(mockManager), anyCollection());
        verify(mockService, times(1)).deleteKeywords(eq(mockManager), anyCollection());
        assertTrue(result);
    }

    @Test
    public void testUpdateBookPropertiesCommand() throws SQLException {

        boolean result = mockService.updateBookPropertiesCommand(mockManager, mockBookDto);

        verify(mockBookDao, times(1)).update(mockBook);
        assertTrue(result);
    }

    @Test
    public void testUpdateBookAuthorsSetCommand() throws SQLException {

        Author authorDeleted = Author.builder().firstName("deleted").id(1).build();
        Author authorRemainedInLibrary = Author.builder().firstName("remainedInLib").id(2).build();
        Author authorRemainedInBook = Author.builder().firstName("remainedInBook").id(3).build();
        Author authorNewInBook = Author.builder().firstName("newInBook").id(4).build();
        Author authorNewInLibrary = Author.builder().firstName("newInLib").id(0).build();

        List<Author> oldAuthors = Stream.of(authorDeleted, authorRemainedInLibrary, authorRemainedInBook).collect(Collectors.toList());
        when(mockAuthorDao.getByBookId(mockBook.getId())).thenReturn(oldAuthors);

        Set<Author> newAuthors = Stream.of(authorRemainedInBook, authorNewInBook, authorNewInLibrary).collect(Collectors.toSet());
        when(mockBookDto.getAuthors()).thenReturn(newAuthors);

        ArgumentCaptor<Collection<Author>> capturedDeletedAuthorsCollection = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Collection<Author>> capturedNewAuthorsCollection = ArgumentCaptor.forClass(Collection.class);

        doNothing().when(mockService).deleteAuthors(eq(mockManager), capturedDeletedAuthorsCollection.capture());
        doNothing().when(mockService).saveAuthors(eq(mockManager), capturedNewAuthorsCollection.capture());

        boolean result = mockService.updateBookAuthorsSetCommand(mockManager, mockBookDto);

        verify(mockAuthorBookDao, times(2)).deleteAuthorBookJunction(any(Author.class), eq(mockBook));
        verify(mockAuthorBookDao, times(1)).saveAuthorBookJunction(mockBook, (Set<Author>) capturedNewAuthorsCollection.getValue());
        assertEquals(2, capturedDeletedAuthorsCollection.getValue().size());
        assertEquals(1, capturedNewAuthorsCollection.getValue().size());
        assertTrue(result);
    }

    @Test
    public void testUpdateBookKeywordsSetCommand() throws SQLException {

        Keyword keywordDeleted = Keyword.builder().word("deleted").id(1).build();
        Keyword keywordRemainedInLibrary = Keyword.builder().word("remainedInLib").id(2).build();
        Keyword keywordRemainedInBook = Keyword.builder().word("remainedInBook").id(3).build();
        Keyword keywordNewInBook = Keyword.builder().word("newInBook").id(4).build();
        Keyword keywordNewInLibrary = Keyword.builder().word("newInLib").id(0).build();

        List<Keyword> oldKeywords = Stream.of(keywordDeleted, keywordRemainedInLibrary, keywordRemainedInBook).collect(Collectors.toList());
        when(mockKeywordDao.getByBookId(mockBook.getId())).thenReturn(oldKeywords);

        Set<Keyword> newKeywords = Stream.of(keywordRemainedInBook, keywordNewInBook, keywordNewInLibrary).collect(Collectors.toSet());
        when(mockBookDto.getKeywords()).thenReturn(newKeywords);

        ArgumentCaptor<Collection<Keyword>> capturedDeletedKeywordsCollection = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Collection<Keyword>> capturedNewKeywordsCollection = ArgumentCaptor.forClass(Collection.class);

        doNothing().when(mockService).deleteKeywords(eq(mockManager), capturedDeletedKeywordsCollection.capture());
        doNothing().when(mockService).saveKeywords(eq(mockManager), capturedNewKeywordsCollection.capture());

        boolean result = mockService.updateBookKeywordsSetCommand(mockManager, mockBookDto);

        verify(mockBookKeywordDao, times(2)).deleteBookKeywordJunction(any(Keyword.class), eq(mockBook));
        verify(mockBookKeywordDao, times(1)).saveBookKeywordsJunction(mockBook, (Set<Keyword>) capturedNewKeywordsCollection.getValue());
        assertEquals(2, capturedDeletedKeywordsCollection.getValue().size());
        assertEquals(1, capturedNewKeywordsCollection.getValue().size());
        assertTrue(result);
    }

    @Test
    public void testGetAllKeywordsCommand() throws SQLException {
        List mockList = mock(ArrayList.class);
        when(mockKeywordDao.getAll()).thenReturn(mockList);

        List result = mockService.getAllKeywordsCommand(mockManager);

        assertEquals(mockList, result);
    }

    @Test
    public void testGetAllAuthorsCommand() throws SQLException {
        List mockList = mock(ArrayList.class);
        when(mockAuthorDao.getAll()).thenReturn(mockList);

        List result = mockService.getAllAuthorsCommand(mockManager);

        assertEquals(mockList, result);
    }

    @Test
    public void testFindBooksCommandWithExpectedList() throws SQLException {

        List<Book> bookList = new ArrayList<>();
        bookList.add(mockBook);
        bookList.add(mockBook);

        when(mockBookDao.getAllBookParameterized(-1, -1, "")).thenReturn(bookList);
        doReturn(mockBookDto).when(mockService).createBookDtoFromBook(mockManager, mockBook);

        List<BookDto> expectedList = new ArrayList<>();
        expectedList.add(mockBookDto);
        expectedList.add(mockBookDto);

        assertEquals("findBooksCommand(...) should return expected list",
                expectedList, mockService.findBooksCommand(mockManager, -1, -1, ""));
    }

    @Test
    public void testFindBooksCommandWithNotExpectedList() throws SQLException {

        List<Book> bookList = new ArrayList<>();
        bookList.add(mockBook);
        bookList.add(mockBook);

        when(mockBookDao.getAllBookParameterized(-1, -1, "")).thenReturn(bookList);
        doReturn(mockBookDto).when(mockService).createBookDtoFromBook(mockManager, mockBook);

        List<BookDto> notExpectedList = new ArrayList<>();
        notExpectedList.add(mockBookDto);
        notExpectedList.add(mockBookDto);
        notExpectedList.add(mockBookDto);

        assertNotEquals("findBooksCommand(...) should not return notExpectedList list",
                notExpectedList, mockService.findBooksCommand(mockManager, -1, -1, ""));
    }
}