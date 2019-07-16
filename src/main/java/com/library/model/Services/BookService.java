package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookService {

    private final static BookService instance = new BookService();

    public boolean addBookToCatalogue(Book book) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        synchronized (this) {

            return (Boolean) daoManager.executeTransaction(manager -> {

                AuthorDao authorDao = (AuthorDao) manager.getAuthorDao();
                // Checks are there the book authors in the DB already (they must have an id)
                // If there are not - save them and set their id received from dao
                for (Author author : book.getAuthors()) {
                    if (author.getId() == 0) {
                        long authorId = authorDao.save(author);
                        author.setId(authorId);
                    }
                }

                KeywordDao keywordDao = (KeywordDao) manager.getKeywordDao();
                // Checks are there the book keywords in the DB already (they must have an id)
                // If there are not - save them and set their id received from dao
                for (Keyword keyword : book.getKeywords()) {
                    if (keyword.getId() == 0) {
                        long keywordId = keywordDao.save(keyword);
                        keyword.setId(keywordId);
                    }
                }

                BookDao bookDao = (BookDao) manager.getBookDao();
                long bookId = bookDao.save(book);

                if (bookId > 0) {
                    book.setId(bookId);
                    return true;
                } else {
                    return false;
                }
            });
        }
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
