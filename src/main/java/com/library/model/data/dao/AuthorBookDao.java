package com.library.model.data.dao;

import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;

import java.util.Set;

/**
 * Dao for interacting with author_book table
 */
public interface AuthorBookDao {

    /**
     * Inserts data for joining author and book tables.
     * Throws {@code DBException} if inserting fails
     * @param book - book for joining
     * @param authors - set with authors for joining
     */
    void saveAuthorBookJunction(Book book, Set<Author> authors);
}
