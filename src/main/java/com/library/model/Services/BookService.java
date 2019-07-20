package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.dto.BookDto;
import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BookService extends Service{

    private final static BookService instance = new BookService();

    public boolean addBookToCatalogue(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBookToCatalogueCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    public boolean removeBookFromCatalogue(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> removeBookFromCatalogueCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    public boolean updateBookProperties(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> updateBookPropertiesCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    public boolean updateBookAuthorsSet(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> updateBookAuthorsSetCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    public List<Keyword> getAllKeywords(){
        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<Keyword>) daoManager.executeAndClose(manager -> getAllKeywordsCommand(daoManager));
    }

    public List<Author> getAllAuthors(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<Author>) daoManager.executeAndClose(manager -> getAllAuthorsCommand(manager));
    }

    public List<BookDto> findBooks(Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<BookDto>) daoManager.executeAndClose(manager -> findBooksCommand(manager, author, keyword, partOfTitle));
    }

    protected synchronized boolean addBookToCatalogueCommand(DaoManager manager, BookDto bookDto) throws SQLException {

        boolean isSavingBookSuccessful = saveBook(manager, bookDto.getBook());
        if (!isSavingBookSuccessful) {
            return false;
        }

        saveAuthors(manager, bookDto.getAuthors());
        saveKeywords(manager, bookDto.getKeywords());
        //Filling junction tables author_book & book_keyword
        manager.getAuthorBookDao().saveAuthorBookJunction(bookDto.getBook(), bookDto.getAuthors());
        manager.getBookKeywordDao().saveBookKeywordsJunction(bookDto.getBook(), bookDto.getKeywords());

        return true;
    }

    private synchronized boolean removeBookFromCatalogueCommand(DaoManager manager, BookDto bookDto) throws SQLException {

        boolean isDeletingBookSuccessful = deleteBook(manager, bookDto.getBook());
        if (!isDeletingBookSuccessful) {
            return false;
        }

        deleteAuthors(manager, bookDto.getAuthors());
        deleteKeywords(manager, bookDto.getKeywords());

        return true;
    }

    protected List<Keyword> getAllKeywordsCommand(DaoManager manager) throws SQLException {
        return manager.getKeywordDao().getAll();
    }

    protected List<Author> getAllAuthorsCommand(DaoManager manager) throws SQLException {
        return manager.getAuthorDao().getAll();
    }

    protected List<BookDto> findBooksCommand(DaoManager manager, Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();
        List<Book> bookList = bookDao.getAllBookParameterized(author, keyword, partOfTitle);

        List<BookDto> bookDtos = new ArrayList<>();

        for (Book book : bookList) {
            bookDtos.add(createBookDtoFromBook(manager, book));
        }

        return bookDtos;
    }

    protected synchronized boolean updateBookPropertiesCommand(DaoManager manager, BookDto bookDto) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();

        bookDao.update(bookDto.getBook());
        // If updating fails the manager in caller method will return null.
        return true;

    }

    protected synchronized boolean updateBookAuthorsSetCommand(DaoManager manager, BookDto bookDto) throws SQLException {
        //Updated authors set doesn't contain removed from book authors
        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();
        //Getting old book authors list
        List<Author> deletedAuthors = authorDao.getByBook(bookDto.getBook());
        //Filtering it to remain only deleted from book authors
        deletedAuthors.removeAll(bookDto.getAuthors());
        AuthorBookDao authorBookDao = manager.getAuthorBookDao();
        //Deleting records from junction author_book table
        for (Author author : deletedAuthors) {
            authorBookDao.deleteAuthorBookJunction(author, bookDto.getBook());
        }

        deleteAuthors(manager, deletedAuthors);

        //Updated authors set may contain new authors
        Set<Author> newAuthors = bookDto.getAuthors().stream()
                .filter(a -> a.getId() == 0)
                .collect(Collectors.toCollection(HashSet::new));
        saveAuthors(manager, newAuthors);
        //Filling a junction table author_book
        manager.getAuthorBookDao().saveAuthorBookJunction(bookDto.getBook(), newAuthors);

        return true;
    }

    protected BookDto createBookDtoFromBook(DaoManager manager, Book book) throws SQLException {

        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();
        Set<Author> authorsSet = new HashSet<>(authorDao.getByBook(book));

        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();
        Set<Keyword> keywordsSet = new HashSet<>(keywordDao.getByBook(book));

        return BookDto.builder()
                .book(book)
                .authors(authorsSet)
                .keywords(keywordsSet)
                .build();
    }

    /**
     * Iterates through given collection, finds new authors and saves them
     * @param manager - {@DaoManager} for accessing needed {@code Dao}
     * @param authors - collection of authors need to be checked and saved
     * @throws SQLException
     */
    protected synchronized void saveAuthors(DaoManager manager, Collection<Author> authors) throws SQLException {

        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();

        for (Author author : authors) {
            // Checks are there the book authors was in the DB when the book
            // was creating (they must have an id)
            if (author.getId() == 0) {
                // If there are not - try to save them and set their id received from dao
                long authorId = authorDao.save(author);
                // It is possible that while book was creating someone else created
                // the same authors. So we need to check it
                if (authorId > 0) {
                    author.setId(authorId);
                } else {
                    // Current author is already in the DB. We need get him and takes
                    // his id.
                    Optional<Author> dbAuthor = getAuthorByName(authorDao, author);
                    if (dbAuthor.isPresent()) {
                        author.setId(dbAuthor.get().getId());
                    }
                }
            }
        }
    }

    /**
     * Checks each author in the given {@Collection} on having a book in the catalogue. If authors doesn't have any
     * book - deletes him.
     * @param manager - {@DaoManager} for accessing needed {@code Dao}
     * @param authors - collection of authors need to be checked
     * @throws SQLException
     */
    protected synchronized void deleteAuthors(DaoManager manager, Collection<Author> authors) throws SQLException {

        AuthorBookDao authorBookDao = manager.getAuthorBookDao();
        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();

        for (Author author : authors) {
            if (!authorBookDao.doesAuthorHasBooks(author)) {
                authorDao.delete(author);
            }
        }
    }

    protected synchronized void deleteKeywords(DaoManager manager, Set<Keyword> keywords) throws SQLException {

        BookKeywordDao bookKeywordDao = manager.getBookKeywordDao();
        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();

        for (Keyword keyword : keywords) {
            if (!bookKeywordDao.doesKeywordBelongToBook(keyword)) {
                keywordDao.delete(keyword);
            }
        }
    }

    protected Optional<Author> getAuthorByName(AuthorDao dao, Author author) {

        return dao.getByName(author.getFirstName(), author.getLastName());

    }

    protected void saveKeywords(DaoManager manager, Set<Keyword> keywordSet) throws SQLException {

        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();

        for (Keyword keyword : keywordSet) {
            // Checks are there the book authors was the DB when book
            // was creating (they must have an id)
            if (keyword.getId() == 0) {
                // If there are not - try to save them and set their id received from dao
                long keywordId = keywordDao.save(keyword);
                // It is possible that while book was creating someone else created
                // the same keywords. So we need to check it
                if (keywordId > 0) {
                    keyword.setId(keywordId);
                } else {
                    // Current keyword is already in the DB. We need get it and takes
                    // its id.
                    Optional<Keyword> dbKeyword = getKeywordByWord(keywordDao, keyword);
                    if (dbKeyword.isPresent()) {
                        keyword.setId(dbKeyword.get().getId());
                    }
                }
            }
        }
    }

    protected Optional<Keyword> getKeywordByWord(KeywordDao dao, Keyword keyword) {

        return dao.getByWord(keyword.getWord());

    }

    protected boolean saveBook(DaoManager manager, Book book) throws SQLException {

        long bookId = manager.getBookDao().save(book);
        if (bookId < 0) {
            //There is such a book in the DB
            return false;
        } else {
            book.setId(bookId);
            return true;
        }
    }

    private boolean deleteBook(DaoManager manager, Book book) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();

        bookDao.delete(book);
        // If deleting fails the manager in caller method will return null.
        return true;
    }

    public static BookService getInstance() {
        return instance;
    }

    private BookService(){}
}
