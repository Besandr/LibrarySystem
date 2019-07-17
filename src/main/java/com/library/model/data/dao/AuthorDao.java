package com.library.model.data.dao;

import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;

import java.util.List;
import java.util.Optional;

/**
 * Dao for Books entity
 */
public interface AuthorDao extends Dao<Author> {

    /**
     * Gets an author with given first and last names.
     * @param firstName - the first name of the target author
     * @param lastName - the last name of the target author
     * @return - an Optional with the target author or an
     *          empty Optional if there is no such author in DB
     */
    Optional<Author> getByName(String firstName, String lastName);

    /**
     * Gets a list with authors of a given book
     * @param book - book which authors need to be gotten
     * @return a list with authors of a given book or an empty
     *          list if there is no authors of the book
     */
    List<Author> getByBook(Book book);
}
