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
import static org.mockito.ArgumentCaptor.*;
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
    public void addBookToCatalogueCommandShouldExecuteSavingMethodsAndReturnCorrectLong() throws SQLException {
        doReturn(3L).when(mockBookDao).save(mockBook);

        Set<Author> authors = new HashSet<>();
        Set<Keyword> keywords = new HashSet<>();
        keywords.add(Keyword.builder().build());


        long result = mockService.addBookToCatalogueCommand(mockManager, mockBook, authors, keywords);

        verify(mockBookDao, times(1)).save(mockBook);
        verify(mockService, times(1)).saveAuthors(mockManager, authors);
        verify(mockService, times(1)).saveKeywords(mockManager, keywords);
        verify(mockAuthorBookDao, times(1)).saveAuthorBookJunction(mockBook, authors);
        verify(mockBookKeywordDao, times(1)).saveBookKeywordsJunction(mockBook, keywords);

        assertEquals(3L, result);
    }

    @Test
    public void addBookToCatalogueCommandShouldNotExecuteSaveBookKeywordsJunction() throws SQLException {
        doReturn(3L).when(mockBookDao).save(mockBook);
        doNothing().when(mockService).saveAuthors(eq(mockManager), any());

        Set<Keyword> keywords = new HashSet<>();

        mockService.addBookToCatalogueCommand(mockManager, mockBook, null, keywords);

        verify(mockBookKeywordDao, never()).saveBookKeywordsJunction(mockBook, keywords);
    }

    @Test
    public void addBookToCatalogueCommandShouldReturnNegativeLong() throws SQLException {

        doReturn(-1L).when(mockBookDao).save(mockBook);

        long result = mockService.addBookToCatalogueCommand(mockManager, mockBook, null, null);

        assertEquals(-1, result);
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
    public void updateBookPropertiesCommandShouldCallAnotherMethodsAndReturnTrue() throws SQLException {
        Set<Author> authors = new HashSet<>();
        Set<Keyword> keywords = new HashSet<>();
        doNothing().when(mockService).updateBookProperties(mockManager, mockBook);
        doNothing().when(mockService).updateBookAuthorsSet(mockManager, mockBook, authors);
        doNothing().when(mockService).updateBookKeywordsSet(mockManager, mockBook, keywords);

        boolean result = mockService.updateBookDtoPropertiesCommand(mockManager, mockBook, authors, keywords);

        verify(mockService, times(1)).updateBookProperties(mockManager, mockBook);
        verify(mockService, times(1)).updateBookAuthorsSet(mockManager, mockBook, authors);
        verify(mockService, times(1)).updateBookKeywordsSet(mockManager, mockBook, keywords);
        assertTrue(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateBookAuthorsSet() throws SQLException {

        Author authorDeleted = Author.builder().firstName("deleted").id(1).build();
        Author authorRemainedInLibrary = Author.builder().firstName("remainedInLib").id(2).build();
        Author authorRemainedInBook = Author.builder().firstName("remainedInBook").id(3).build();
        Author authorNewInBook = Author.builder().firstName("newInBook").id(4).build();
        Author authorNewInLibrary = Author.builder().firstName("newInLib").id(0).build();

        List<Author> oldAuthors = Stream.of(authorDeleted, authorRemainedInLibrary, authorRemainedInBook).collect(Collectors.toList());
        when(mockAuthorDao.getByBookId(mockBook.getId())).thenReturn(oldAuthors);

        Set<Author> newAuthors = Stream.of(authorRemainedInBook, authorNewInBook, authorNewInLibrary).collect(Collectors.toSet());

        ArgumentCaptor<Collection<Author>> capturedDeletedAuthorsCollection = forClass(Collection.class);
        ArgumentCaptor<Collection<Author>> capturedNewAuthorsCollection = forClass(Collection.class);

        doNothing().when(mockService).deleteAuthors(eq(mockManager), capturedDeletedAuthorsCollection.capture());
        doNothing().when(mockService).saveAuthors(eq(mockManager), capturedNewAuthorsCollection.capture());

        mockService.updateBookAuthorsSet(mockManager, mockBook, newAuthors);

        verify(mockAuthorBookDao, times(2)).deleteAuthorBookJunction(any(Author.class), eq(mockBook));
        verify(mockAuthorBookDao, times(1)).saveAuthorBookJunction(mockBook, newAuthors);
        assertEquals(2, capturedDeletedAuthorsCollection.getValue().size());
        assertEquals(1, capturedNewAuthorsCollection.getValue().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateBookKeywordsSet() throws SQLException {

        Keyword keywordDeleted = Keyword.builder().word("deleted").id(1).build();
        Keyword keywordRemainedInLibrary = Keyword.builder().word("remainedInLib").id(2).build();
        Keyword keywordRemainedInBook = Keyword.builder().word("remainedInBook").id(3).build();
        Keyword keywordNewInBook = Keyword.builder().word("newInBook").id(4).build();
        Keyword keywordNewInLibrary = Keyword.builder().word("newInLib").id(0).build();

        List<Keyword> oldKeywords = Stream.of(keywordDeleted, keywordRemainedInLibrary, keywordRemainedInBook).collect(Collectors.toList());
        when(mockKeywordDao.getByBookId(mockBook.getId())).thenReturn(oldKeywords);

        Set<Keyword> newKeywords = Stream.of(keywordRemainedInBook, keywordNewInBook, keywordNewInLibrary).collect(Collectors.toSet());

        ArgumentCaptor<Collection<Keyword>> capturedDeletedKeywordsCollection = forClass(Collection.class);
        ArgumentCaptor<Collection<Keyword>> capturedNewKeywordsCollection = forClass(Collection.class);

        doNothing().when(mockService).deleteKeywords(eq(mockManager), capturedDeletedKeywordsCollection.capture());
        doNothing().when(mockService).saveKeywords(eq(mockManager), capturedNewKeywordsCollection.capture());

        mockService.updateBookKeywordsSet(mockManager, mockBook, newKeywords);

        verify(mockBookKeywordDao, times(2)).deleteBookKeywordJunction(any(Keyword.class), eq(mockBook));
        verify(mockBookKeywordDao, times(1)).saveBookKeywordsJunction(mockBook, newKeywords);
        assertEquals(2, capturedDeletedKeywordsCollection.getValue().size());
        assertEquals(1, capturedNewKeywordsCollection.getValue().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllKeywordsCommand() throws SQLException {
        List mockList = mock(ArrayList.class);
        when(mockKeywordDao.getAll()).thenReturn(mockList);

        List result = mockService.getAllKeywordsCommand(mockManager);

        assertEquals(mockList, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllAuthorsCommand() throws SQLException {
        List mockList = mock(ArrayList.class);
        when(mockAuthorDao.getAll()).thenReturn(mockList);

        List result = mockService.getAllAuthorsCommand(mockManager);

        assertEquals(mockList, result);
    }

    @Test
    public void testFindBooksCommandShouldReturnExpectedList() throws SQLException {

        List<Book> bookList = new ArrayList<>();
        bookList.add(mockBook);
        bookList.add(mockBook);

        when(mockBookDao.getAllBookParameterized(-1, -1, "", 100, 0)).thenReturn(bookList);
        doReturn(mockBookDto).when(mockService).createBookDtoFromBook(mockManager, mockBook);

        List<BookDto> expectedList = new ArrayList<>();
        expectedList.add(mockBookDto);
        expectedList.add(mockBookDto);

        assertEquals("findBooksCommand(...) should return expected list",
                expectedList, mockService.findBooksCommand(mockManager, -1, -1, "", 100, 0));
    }

    @Test
    public void testFindBooksCommandWithNotExpectedList() throws SQLException {

        List<Book> bookList = new ArrayList<>();
        bookList.add(mockBook);
        bookList.add(mockBook);

        when(mockBookDao.getAllBookParameterized(-1, -1, "", 100, 0)).thenReturn(bookList);
        doReturn(mockBookDto).when(mockService).createBookDtoFromBook(mockManager, mockBook);

        List<BookDto> notExpectedList = new ArrayList<>();
        notExpectedList.add(mockBookDto);
        notExpectedList.add(mockBookDto);
        notExpectedList.add(mockBookDto);

        assertNotEquals("findBooksCommand(...) should not return notExpectedList list",
                notExpectedList, mockService.findBooksCommand(mockManager, -1, -1, "", 100, 0));
    }

    @Test
    public void getBookDtoByIdCommandShouldReturnEmptyOptional() throws SQLException {
        when(mockBookDao.get(1L)).thenReturn(Optional.empty());

        Optional<BookDto> result = mockService.getBookDtoByIdCommand(mockManager, 1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void getBookDtoByIdCommandShouldReturnExpectedOptional() throws SQLException {
        when(mockBookDao.get(1L)).thenReturn(Optional.of(mockBook));
        doReturn(mockBookDto).when(mockService).createBookDtoFromBook(mockManager, mockBook);

        Optional<BookDto> result = mockService.getBookDtoByIdCommand(mockManager, 1L);

        assertEquals(Optional.of(mockBookDto), result);
    }

    @Test
    public void createAuthorsSetShouldCollectThreeListsInOneSet() {
        List<Long> oldAuthorsId = Arrays.asList(1L, 2L);
        List<String> newAuthorFirstNames = Arrays.asList("nameOne", "nameTwo");
        List<String> newAuthorLastNames = Arrays.asList("lastNameOne", "lastNameTwo");

        Set<Author> resultSet = mockService.createAuthorsSet(oldAuthorsId, newAuthorFirstNames, newAuthorLastNames);

        assertTrue(resultSet.contains(Author.builder().id(1L).build()));
        assertTrue(resultSet.contains(Author.builder().id(2L).build()));
        assertTrue(resultSet.contains(Author.builder().firstName("nameOne").lastName("lastNameOne").build()));
        assertTrue(resultSet.contains(Author.builder().firstName("nameTwo").lastName("lastNameTwo").build()));
    }

    @Test
    public void createKeywordsSetShouldCollectTwoListsInOneSet() {
        List<Long> oldKeywordsId = Arrays.asList(1L, 2L);
        List<String> newKeywords = Arrays.asList("wordOne", "wordTwo");

        Set<Keyword> resultSet = mockService.createKeywordsSet(oldKeywordsId, newKeywords);

        assertTrue(resultSet.contains(Keyword.builder().id(1L).build()));
        assertTrue(resultSet.contains(Keyword.builder().id(2L).build()));
        assertTrue(resultSet.contains(Keyword.builder().word("wordOne").build()));
        assertTrue(resultSet.contains(Keyword.builder().word("wordTwo").build()));
    }
}