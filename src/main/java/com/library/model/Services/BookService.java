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

public class BookService {

    private final static BookService instance = new BookService();

    public boolean addBookToCatalogue(BookDto bookDto) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Boolean) daoManager.executeTransaction(manager -> addBookToCatalogueCommand(manager, bookDto));

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

    protected synchronized void saveAuthors(DaoManager manager, Set<Author> authorSet) throws SQLException {

        AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();

        for (Author author : authorSet) {
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

    public static BookService getInstance() {
        return instance;
    }

    private BookService(){}
}
