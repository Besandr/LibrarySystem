package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookService {

    private final static BookService instance = new BookService();

    public boolean addBookToCatalogue(Book book) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Boolean) daoManager.executeTransaction(manager -> addBookToCatalogueCommand(manager, book));

    }



    protected synchronized boolean addBookToCatalogueCommand(DaoManager manager, Book book) throws SQLException {

        saveAuthors(manager, book.getAuthors());

        saveKeywords(manager, book.getKeywords());

        BookDao bookDao = (BookDao) manager.getBookDao();
        bookDao.save(book);

        return true;
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


    public static BookService getInstance() {
        return instance;
    }

    private BookService(){}

    public static void main(String[] args) {

        BookService bookService = new BookService();

        Author first = Author.builder().firstName("Миша").lastName("Соломенный").build();
        Author second = Author.builder().firstName("Andrew").lastName("Loving").build();

        HashSet<Author> authors = Stream.of(first, second).collect(Collectors.toCollection(HashSet::new));

        Keyword k1 = Keyword.builder().word("жызнь!").build();
        Keyword k2 = Keyword.builder().word("love").build();

        HashSet<Keyword> keys = Stream.of(k1, k2).collect(Collectors.toCollection(HashSet::new));

        Book book = Book.builder()
                .title("Different stories")
                .year(2019)
                .description("Many stories from two extra different people")
                .authors(authors)
                .keywords(keys)
                .build();

        System.out.println(bookService.addBookToCatalogue(book));

    }
}
