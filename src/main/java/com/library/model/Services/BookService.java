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

    /**
     * Adds a new book to the library's book catalogue
     * @param bookDto - DTO object contains the new book
     * @return - the boolean type result of executing this method
     */
    public boolean addBookToCatalogue(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> addBookToCatalogueCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Removes a book from the library's book catalogue
     * @param bookDto - DTO object contains the book need to be removed
     * @return - the boolean type result of executing this method
     */
    public boolean removeBookFromCatalogue(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> removeBookFromCatalogueCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Updates a book's fields properties
     * @param bookDto - DTO object contains the book which properties
     *                is need to be updated
     * @return - the boolean type result of executing this method
     */
    public boolean updateBookProperties(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> updateBookPropertiesCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Updates a book's authors
     * @param bookDto - DTO object contains the book which authors
     *                is need to be updated
     * @return - the boolean type result of executing this method
     */
    public boolean updateBookAuthorsSet(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> updateBookAuthorsSetCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Updates a book's keywords
     * @param bookDto - DTO object contains the book which keywords
     *                is need to be updated
     * @return - the boolean type result of executing this method
     */
    public boolean updateBookKeywordsSet(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> updateBookKeywordsSetCommand(manager, bookDto));

        return checkAndCastExecutingResult(executingResult);
    }

    /**
     * Gets the list with all keywords in the library
     * @return the list with all keywords in the library
     */
    public List<Keyword> getAllKeywords(){
        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getAllKeywordsCommand(daoManager));

        return checkAndCastObjectToList(executingResult);
    }

    /**
     * Gets the list with all authors in the library
     * @return the list with all authors in the library
     */
    public List<Author> getAllAuthors(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> getAllAuthorsCommand(manager));

        return checkAndCastObjectToList(executingResult);
    }

    /**
     * Finds all the books which fits to the given combinations
     * of criteria: author, keyword, part of title. Any of this
     * parameters may present or may not.
     * @param author - author of target books
     * @param keyword - keyword of target books
     * @param partOfTitle - part of title or whole title of target books
     * @return a list with {@code BookDto} contains the target books
     */
    public List<BookDto> findBooks(Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> findBooksCommand(manager, author, keyword, partOfTitle));

        return checkAndCastObjectToList(executingResult);
    }

    //Commands which is needed to be executed in corresponding public service methods
    synchronized boolean addBookToCatalogueCommand(DaoManager manager, BookDto bookDto) throws SQLException {
        //trying to save the new book to the library's catalogue
        boolean isSavingBookSuccessful = saveBook(manager, bookDto.getBook());
        if (!isSavingBookSuccessful) {
            return EXECUTING_FAILED;
        }
        //saving authors and keywords of the new book
        saveAuthors(manager, bookDto.getAuthors());
        saveKeywords(manager, bookDto.getKeywords());
        //Filling junction tables author_book & book_keyword
        manager.getAuthorBookDao().saveAuthorBookJunction(bookDto.getBook(), bookDto.getAuthors());
        manager.getBookKeywordDao().saveBookKeywordsJunction(bookDto.getBook(), bookDto.getKeywords());

        return EXECUTING_SUCCESSFUL;
    }

    synchronized boolean removeBookFromCatalogueCommand(DaoManager manager, BookDto bookDto) throws SQLException {
        //trying to delete the book from the library's catalogue
        boolean isDeletingBookSuccessful = deleteBook(manager, bookDto.getBook());
        if (!isDeletingBookSuccessful) {
            return EXECUTING_FAILED;
        }
        //saving authors and keywords of the new book
        deleteAuthors(manager, bookDto.getAuthors());
        deleteKeywords(manager, bookDto.getKeywords());

        return EXECUTING_SUCCESSFUL;
    }

    List<Keyword> getAllKeywordsCommand(DaoManager manager) throws SQLException {
        return manager.getKeywordDao().getAll();
    }

    List<Author> getAllAuthorsCommand(DaoManager manager) throws SQLException {
        return manager.getAuthorDao().getAll();
    }

    List<BookDto> findBooksCommand(DaoManager manager, Optional<Author> author, Optional<Keyword> keyword, String partOfTitle) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();
        List<Book> bookList = bookDao.getAllBookParameterized(author, keyword, partOfTitle);

        List<BookDto> bookDtos = new ArrayList<>();

        for (Book book : bookList) {
            bookDtos.add(createBookDtoFromBook(manager, book));
        }

        return bookDtos;
    }

    synchronized boolean updateBookPropertiesCommand(DaoManager manager, BookDto bookDto) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();

        bookDao.update(bookDto.getBook());
        // If updating fails the manager in caller method will return null.
        return EXECUTING_SUCCESSFUL;

    }

    synchronized boolean updateBookAuthorsSetCommand(DaoManager manager, BookDto bookDto) throws SQLException {
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

        return EXECUTING_SUCCESSFUL;
    }

    synchronized boolean updateBookKeywordsSetCommand(DaoManager manager, BookDto bookDto) throws SQLException {
        //Updated keywords set doesn't contain removed from book keywords
        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();
        //Getting old book keywords list
        List<Keyword> deletedKeywords = keywordDao.getByBook(bookDto.getBook());
        //Filtering it to remain only deleted from book keywords
        deletedKeywords.removeAll(bookDto.getKeywords());
        BookKeywordDao keywordBookDao = manager.getBookKeywordDao();
        //Deleting records from junction book_keyword table
        for (Keyword keyword : deletedKeywords) {
            keywordBookDao.deleteBookKeywordJunction(keyword, bookDto.getBook());
        }

        deleteKeywords(manager, deletedKeywords);

        //Updated keywords set may contain new keywords
        Set<Keyword> newKeywords = bookDto.getKeywords().stream()
                .filter(a -> a.getId() == 0)
                .collect(Collectors.toCollection(HashSet::new));
        saveKeywords(manager, newKeywords);
        //Filling a junction table keyword_book
        manager.getBookKeywordDao().saveBookKeywordsJunction(bookDto.getBook(), newKeywords);

        return EXECUTING_SUCCESSFUL;
    }

    /**
     * Creates a {@code BookDto}(with Authors and Keywords) from a given book.
     * @param manager - {@code DaoManager} for accessing daos needed
     * @param book - book from which DTO is need to created
     * @return the object which contains given book and its authors and keywords
     * @throws SQLException - if manager can't give a Dao needed
     */
    BookDto createBookDtoFromBook(DaoManager manager, Book book) throws SQLException {

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
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param authors - collection of authors need to be checked and saved
     * @throws SQLException - if manager can't give a Dao needed
     */
    synchronized void saveAuthors(DaoManager manager, Collection<Author> authors) throws SQLException {

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
                    Optional<Author> dbAuthor = getAuthorByName(authorDao, author.getFirstName(), author.getLastName());
                    if (dbAuthor.isPresent()) {
                        author.setId(dbAuthor.get().getId());
                    }
                }
            }
        }
    }

    /**
     * Checks each author in the given {@code Collection} on having a book in the catalogue. If authors doesn't have any
     * book - deletes him.
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param authors - collection of authors need to be checked
     * @throws SQLException - if manager can't give a Dao needed
     */
    synchronized void deleteAuthors(DaoManager manager, Collection<Author> authors) throws SQLException {

        AuthorBookDao authorBookDao = manager.getAuthorBookDao();
        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();

        for (Author author : authors) {
            if (!authorBookDao.doesAuthorHasBooks(author)) {
                authorDao.delete(author);
            }
        }
    }

    /**
     * Checks each keyword in the given {@code Collection} on having a book in the catalogue. If keywords doesn't have any
     * book - deletes him.
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param keywords - collection of keywords need to be checked
     * @throws SQLException - if manager can't give a Dao needed
     */
    synchronized void deleteKeywords(DaoManager manager, Collection<Keyword> keywords) throws SQLException {

        BookKeywordDao bookKeywordDao = manager.getBookKeywordDao();
        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();

        for (Keyword keyword : keywords) {
            if (!bookKeywordDao.doesKeywordBelongToBook(keyword)) {
                keywordDao.delete(keyword);
            }
        }
    }

    /**
     * Gets an author(with id) from the storage by its first and last names
     * @param dao - dao for fetching the author data from the storage
     * @param firstName - the authors first name
     * @param lastName - the authors second name
     * @return - the target author
     */
    Optional<Author> getAuthorByName(AuthorDao dao, String firstName, String lastName) {

        return dao.getByName(firstName, lastName);

    }

    /**
     * Iterates through given collection, finds new keywords and saves them
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param keywordSet - collection of keywords need to be checked and saved
     * @throws SQLException - if the manager can't give a Dao needed
     */
    void saveKeywords(DaoManager manager, Collection<Keyword> keywordSet) throws SQLException {

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
                    Optional<Keyword> dbKeyword = getKeywordByWord(keywordDao, keyword.getWord());
                    if (dbKeyword.isPresent()) {
                        keyword.setId(dbKeyword.get().getId());
                    }
                }
            }
        }
    }

    /**
     * Gets the keyword(with id) object from the storage by its word
     * @param dao - dao for fetching the keywords data from the storage
     * @param word - the keyword's word
     * @return - the target keyword
     */
    Optional<Keyword> getKeywordByWord(KeywordDao dao, String word) {
        return dao.getByWord(word);
    }

    /**
     * Tries to save the given book in the storage
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param book - book needed to be saved
     * @return - the boolean representation of saving operation result
     * @throws SQLException - if the manager can't give a Dao needed
     */
    boolean saveBook(DaoManager manager, Book book) throws SQLException {

        long bookId = manager.getBookDao().save(book);
        if (bookId < 0) {
            //There is such a book in the DB
            return EXECUTING_SUCCESSFUL;
        } else {
            book.setId(bookId);
            return EXECUTING_FAILED;
        }
    }

    /**
     * Tries to delete the given book from storage
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param book - book needed to be deleted
     * @return - {@code true} if deleting is successful. If deleting fails
     * a {@code DaoException} will be thrown and handled by {@code DaoManager}
     * @throws SQLException - if the manager can't give a Dao needed
     */
    private boolean deleteBook(DaoManager manager, Book book) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();

        bookDao.delete(book);
        // If deleting fails the manager in caller method will return null.
        return EXECUTING_SUCCESSFUL;
    }

    public static BookService getInstance() {
        return instance;
    }

    private BookService(){}
}
