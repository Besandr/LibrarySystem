package com.library.services;

import com.library.repository.DaoManager;
import com.library.repository.DaoManagerFactory;
import com.library.repository.dao.*;
import com.library.repository.dto.BookDto;
import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.entity.Keyword;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BookService extends Service{

    private DaoManagerFactory daoManagerFactory;

    BookService(DaoManagerFactory daoManagerFactory) {
        this.daoManagerFactory = daoManagerFactory;
    }

    /**
     * Adds a new book to the library's book catalogue
     * @param title of a new book
     * @param year of a new book
     * @param description of a new book
     * @param oldAuthorsId - IDs of existent in the library catalogue authors
     * @param oldKeywordsId - IDs of existent in the library catalogue keywords
     * @param newAuthorFirstNames - list with first names of new authors
     * @param newAuthorLastNames - list with last names of new authors
     * @param newKeywords  - list with new keywords
     * @return ID of created book or {@code 0} if book wasn't created
     */
    public long addBookToCatalogue(String title,
                                      int year,
                                      String description,
                                      List<Long> oldAuthorsId,
                                      List<Long> oldKeywordsId,
                                      List<String> newAuthorFirstNames,
                                      List<String> newAuthorLastNames,
                                      List<String> newKeywords) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Book book = Book.builder()
                .title(title)
                .year(year)
                .description(description)
                .build();

        Set<Author> authors = createAuthorsSet(oldAuthorsId, newAuthorFirstNames, newAuthorLastNames);
        Set<Keyword> keywords = createKeywordsSet(oldKeywordsId, newKeywords);

        Object executionResult = daoManager.executeTransaction(manager -> addBookToCatalogueCommand(manager, book, authors, keywords));

        return checkAndCastObjectToLong(executionResult);
    }

    /**
     * Removes a book from the library's book catalogue
     * @param bookDto - DTO object contains the book need to be removed
     * @return - the boolean type result of executing this method
     */
    public boolean removeBookFromCatalogue(BookDto bookDto) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeTransaction(manager -> removeBookFromCatalogueCommand(manager, bookDto));

        return checkAndCastExecutingResult(executionResult);
    }

    /**
     * Updates a book's fields properties
     *
     * @return - the boolean type result of executing this method
     */
    public boolean updateBookDtoProperties(long bookId,
                                           String title,
                                           int year,
                                           String description,
                                           List<Long> oldAuthorsId,
                                           List<Long> oldKeywordsId,
                                           List<String> newAuthorFirstNames,
                                           List<String> newAuthorLastNames,
                                           List<String> newKeywords) {

        Book book = Book.builder()
                .id(bookId)
                .title(title)
                .year(year)
                .description(description)
                .build();

        Set<Author> authors = createAuthorsSet(oldAuthorsId, newAuthorFirstNames, newAuthorLastNames);
        Set<Keyword> keywords = createKeywordsSet(oldKeywordsId, newKeywords);

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager -> updateBookDtoPropertiesCommand(manager, book, authors, keywords));

        return checkAndCastExecutingResult(executionResult);
    }

//    /**
//     * Updates a book's authors
//     * @param bookDto - DTO object contains the book which authors
//     *                is need to be updated
//     * @return - the boolean type result of executing this method
//     */
//    public boolean updateBookAuthorsSet(BookDto bookDto) {
//
//        DaoManager daoManager = daoManagerFactory.createDaoManager();
//
//        Object executionResult = daoManager.executeTransaction(manager -> updateBookAuthorsSetCommand(manager, bookDto));
//
//        return checkAndCastExecutingResult(executionResult);
//    }

//    /**
//     * Updates a book's keywords
//     * @param bookDto - DTO object contains the book which keywords
//     *                is need to be updated
//     * @return - the boolean type result of executing this method
//     */
//    public boolean updateBookKeywordsSet(BookDto bookDto) {
//
//        DaoManager daoManager = daoManagerFactory.createDaoManager();
//
//        Object executionResult = daoManager.executeTransaction(manager -> updateBookKeywordsSetCommand(manager, bookDto));
//
//        return checkAndCastExecutingResult(executionResult);
//    }

    /**
     * Gets the list with all keywords in the library
     * @return the list with all keywords in the library
     */
    public List<Keyword> getAllKeywords(){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager -> getAllKeywordsCommand(daoManager));

        return checkAndCastObjectToList(executionResult);
    }

    /**
     * Gets the list with all authors in the library
     * @return the list with all authors in the library
     */
    public List<Author> getAllAuthors(){

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager -> getAllAuthorsCommand(manager));

        return checkAndCastObjectToList(executionResult);
    }

    /**
     * Finds all the books which fits to the given combinations
     * of criteria: author, keyword, part of title. Any of this
     * parameters may present or may not.
     * @param authorId - author's ID of target books
     * @param keywordId - keyword's ID of target books
     * @param partOfTitle - part of title or whole title of target books
     * @param recordsQuantity
     * @param previousRecordNumber
     * @return a list with {@code BookDto} contains the target books
     */
    public List<BookDto> findBooks(long authorId, long keywordId, String partOfTitle, int recordsQuantity, int previousRecordNumber) {

        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager -> findBooksCommand(manager, authorId, keywordId, partOfTitle, recordsQuantity, previousRecordNumber));

        return checkAndCastObjectToList(executionResult);
    }

    /**
     * Counts all books in book search result
     * @param keywordId - keyword's ID of target books
     * @param partOfTitle - part of title or whole title of target books
     * @return quantity of all books in book search result
     */
    public long getBookSearchResultCount(long authorId, long keywordId, String partOfTitle){
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager ->
                ((BookDao) manager.getBookDao()).getBooSearchResultCount(authorId, keywordId, partOfTitle));

        return checkAndCastObjectToLong(executionResult);
    }

    /**
     * Gives an {@code Optional} with {@code BookDto} by
     * given book ID
     * @param bookId - ID of target book
     * @return an {@code Optional} with {@code BookDto}
     * or an empty {@code Optional}
     */
    public Optional<BookDto> getBookDtoById(long bookId) {
        DaoManager daoManager = daoManagerFactory.createDaoManager();

        Object executionResult = daoManager.executeAndClose(manager -> getBookDtoByIdCommand(manager, bookId));

        return checkAndCastObjectToOptional(executionResult);
    }

    //Commands which is needed to be executed in corresponding public service methods
    synchronized long addBookToCatalogueCommand(DaoManager manager, Book book, Set<Author> authors, Set<Keyword> keywords) throws SQLException {
        //trying to save the new book to the library's catalogue
        long bookId = manager.getBookDao().save(book);

        if (bookId < 0) {
            //There is such a book in the DB
            return bookId;
        } else {
            book.setId(bookId);
        }

        //saving authors and keywords of the new book
        saveAuthors(manager, authors);
        saveKeywords(manager, keywords);
        //Filling junction tables author_book & book_keyword
        manager.getAuthorBookDao().saveAuthorBookJunction(book, authors);
        if (!keywords.isEmpty()) {
            manager.getBookKeywordDao().saveBookKeywordsJunction(book, keywords);
        }

        return bookId;
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

    private List<BookDto> findBooksCommand(DaoManager manager, long authorId, long keywordId,
                                           String partOfTitle, int recordsQuantity, int previousRecordNumber) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();
        List<Book> bookList = bookDao.getAllBookParameterized(authorId, keywordId, partOfTitle, recordsQuantity, previousRecordNumber);

        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : bookList) {
            bookDtos.add(createBookDtoFromBook(manager, book));
        }

        return bookDtos;
    }

    synchronized boolean updateBookDtoPropertiesCommand(DaoManager manager, Book book, Set<Author> authors, Set<Keyword> keywords) throws SQLException {

        updateBookProperties(manager, book);
        updateBookAuthorsSet(manager, book, authors);
        updateBookKeywordsSet(manager, book, keywords);

        // If updating fails the manager in caller method will return null.
        return EXECUTING_SUCCESSFUL;

    }

    synchronized void updateBookAuthorsSet(DaoManager manager, Book book, Set<Author> authors) throws SQLException {
        // Updated authors set doesn't contain removed from book authors
        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();
        // Getting old book authors list
        List<Author> authorsBeforeUpdate = authorDao.getByBookId(book.getId());
        // Creating list with deletedAuthors by filtering old authors
        // to remain only deleted from book authors
        List<Author> deletedAuthors = new ArrayList<>(authorsBeforeUpdate);
        deletedAuthors.removeAll(authors);
        AuthorBookDao authorBookDao = manager.getAuthorBookDao();
        // Deleting records from junction author_book table
        for (Author author : deletedAuthors) {
            authorBookDao.deleteAuthorBookJunction(author, book);
        }
        deleteAuthors(manager, deletedAuthors);

        // Creating list with remained after book updating authors
        List<Author> remainedAuthors = new ArrayList<>(authorsBeforeUpdate);
        remainedAuthors.removeAll(deletedAuthors);
        // Remove from authors set remained in book authors
        // for creating a new junction for added to book authors
        authors.removeAll(remainedAuthors);

        // Updated authors set may contain new authors
        Set<Author> newAuthors = authors.stream()
                .filter(a -> a.getId() == 0)
                .collect(Collectors.toCollection(HashSet::new));
        saveAuthors(manager, newAuthors);
        // Filling a junction table author_book
        manager.getAuthorBookDao().saveAuthorBookJunction(book, authors);
    }

    synchronized void updateBookKeywordsSet(DaoManager manager, Book book, Set<Keyword> keywords) throws SQLException {
        //Updated keywords set doesn't contain removed from book keywords
        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();
        //Getting old book keywords list
        List<Keyword> keywordsBeforeUpdate = keywordDao.getByBookId(book.getId());
        // Creating list with deletedKeywords by filtering old keywords
        // to remain only deleted from book keywords
        List<Keyword> deletedKeywords = new ArrayList<>(keywordsBeforeUpdate);
        //Filtering it to remain only deleted from book keywords
        deletedKeywords.removeAll(keywords);
        BookKeywordDao keywordBookDao = manager.getBookKeywordDao();
        //Deleting records from junction book_keyword table
        for (Keyword keyword : deletedKeywords) {
            keywordBookDao.deleteBookKeywordJunction(keyword, book);
        }
        deleteKeywords(manager, deletedKeywords);

        // Creating list with remained after book updating keywords
        List<Keyword> remainedKeywords = new ArrayList<>(keywordsBeforeUpdate);
        remainedKeywords.removeAll(deletedKeywords);
        // Remove from keywords set remained in book keywords
        // for creating a new junction for added to book keywords
        keywords.removeAll(remainedKeywords);

        //Updated keywords set may contain new keywords
        Set<Keyword> newKeywords = keywords.stream()
                .filter(a -> a.getId() == 0)
                .collect(Collectors.toCollection(HashSet::new));
        saveKeywords(manager, newKeywords);
        //Filling a junction table keyword_book
        manager.getBookKeywordDao().saveBookKeywordsJunction(book, keywords);
    }

    private synchronized void updateBookProperties(DaoManager manager, Book book) throws SQLException {
        BookDao bookDao = (BookDao) manager.getBookDao();
        bookDao.update(book);
    }



    private Optional<BookDto> getBookDtoByIdCommand(DaoManager manager, long bookId) throws SQLException {
        BookDao bookDao = (BookDao) manager.getBookDao();
        Optional<Book> book = bookDao.get(bookId);

        if (!book.isPresent()) {
            return Optional.empty();
        }

        return Optional.ofNullable(createBookDtoFromBook(manager, book.get()));
    }

    /**
     * Converts and combines two given {@code List} to the
     * {@code Set} with {@code Author}
     * @param oldKeywordsId - IDs of existent in the library catalogue keywords
     * @param newKeywords  - list with new keywords
     * @return a converted and combined {@code Set} from two given {@code List}
     */
    private Set<Keyword> createKeywordsSet(List<Long> oldKeywordsId, List<String> newKeywords) {
        Set<Keyword> keywords = new HashSet<>();
        for (Long id : oldKeywordsId) {
            keywords.add(Keyword
                    .builder()
                    .id(id)
                    .build());
        }
        for (String word : newKeywords) {
            keywords.add(Keyword
                    .builder()
                    .word(word)
                    .build());
        }
        return keywords;
    }

    /**
     * Converts and combines three given {@code List} to the
     * {@code Set} with {@code Author}
     * @param oldAuthorsId - IDs of existent in the library catalogue authors
     * @param newAuthorFirstNames - list with first names of new authors
     * @param newAuthorLastNames - list with last names of new authors
     * @return a converted and combined {@code Set} from three given {@code List}
     */
    private Set<Author> createAuthorsSet(List<Long> oldAuthorsId, List<String> newAuthorFirstNames, List<String> newAuthorLastNames) {
        Set<Author> authors = new HashSet<>();
        for (Long id : oldAuthorsId) {
            authors.add(Author
                    .builder()
                    .id(id)
                    .build());
        }
        for (int i = 0; i < newAuthorLastNames.size(); i++) {
            authors.add(Author
                    .builder()
                    .firstName(newAuthorFirstNames.get(i))
                    .lastName(newAuthorLastNames.get(i))
                    .build());
        }
        return authors;
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
        Set<Author> authorsSet = new HashSet<>(authorDao.getByBookId(book.getId()));

        KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();
        Set<Keyword> keywordsSet = new HashSet<>(keywordDao.getByBookId(book.getId()));

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
                    dbAuthor.ifPresent(value -> author.setId(value.getId()));
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
            if (!authorBookDao.doesAuthorHaveBooks(author)) {
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
                    dbKeyword.ifPresent(value -> keyword.setId(value.getId()));
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
     * Tries to delete the given book from storage
     * @param manager - {@code DaoManager} for accessing needed {@code Dao}
     * @param book - book needed to be deleted
     * @return - {@code true} if deleting is successful. If deleting fails
     * a {@code DaoException} will be thrown and handled by {@code DaoManager}
     * @throws SQLException - if the manager can't give a Dao needed
     */
    boolean deleteBook(DaoManager manager, Book book) throws SQLException {

        BookDao bookDao = (BookDao) manager.getBookDao();

        bookDao.delete(book);
        // If deleting fails the manager in caller method will return null.
        return EXECUTING_SUCCESSFUL;
    }

}
