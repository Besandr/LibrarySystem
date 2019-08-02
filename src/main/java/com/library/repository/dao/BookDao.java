package com.library.repository.dao;

import com.library.repository.entity.Book;

import java.util.List;

/**
 * Dao for Books entity
 */
public interface BookDao extends Dao<Book> {

    /**
     * Finds all books in a DB according to search parameters(with all ar with any of them)
     * @param authorId - id of the book author
     * @param keywordId - id of the book keyword
     * @param partOfTitle - string with the book title or with a part of it
     * @param limit the number of loans returned
     * @param offset the number of loans returned
     * @return - list with all books correspond to the given criteria
     */
    List<Book> getAllBookParameterized(long authorId, long keywordId, String partOfTitle, int limit, int offset);

    /**
     * Counts amount of all books which fits target parameters
     * @param authorId - id of the book author
     * @param keywordId - id of the book keyword
     * @param partOfTitle - string with the book title or with a part of it
     * @return amount of all books which fits target parameters
     */
    long getBooSearchResultCount(long authorId, long keywordId, String partOfTitle);
}
