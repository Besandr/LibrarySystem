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
     * @param authorId - id of the book author
     * @param keywordId - id of the book keyword
     * @param partOfTitle - string with the book title or with a part of it
     * @return - list with all books correspond to the given criteria
     */
    List<Book> getAllBookParameterized(long authorId, long keywordId, String partOfTitle);

}
