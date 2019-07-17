package com.library.model.data.dao;

import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;

import java.util.List;
import java.util.Optional;

/**
 * Dao for Books entity
 */
public interface BookDao extends Dao<Book> {

    /**
     * Finds all books in a DB according to search parameters(with all ar with any of them)
     * @param author - {@code Optional} which may or may not contains one of the book authors
     * @param keyword - {@code Optional} which may or may not contains one of the book keywords
     * @param partOfTitle - string with the book title or with a part of it
     * @return - list with all books correspond to the given criteria
     */
    List<Book> getAllBookParameterized(Optional<Author> author, Optional<Keyword> keyword, String partOfTitle);

}
