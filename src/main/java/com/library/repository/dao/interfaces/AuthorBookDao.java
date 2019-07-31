package com.library.repository.dao.interfaces;

import com.library.repository.entity.Author;
import com.library.repository.entity.Book;

import java.util.Set;

/**
 * Dao for interacting with author_book table
 */
public interface AuthorBookDao {

    /**
     * Inserts data for joining author and book tables.
     * Throws {@code DaoException} if inserting fails
     * @param book - book for joining
     * @param authors - set with authors for joining
     */
    void saveAuthorBookJunction(Book book, Set<Author> authors);

    /**
     * Deletes a junction between given book and given author
     * @param book - book which junction to given author need to be deleted
     * @param author - author which junction to given book need to be deleted
     */
    void deleteAuthorBookJunction(Author author, Book book);

    /**
     * Seeks for presenting books with a given author in the catalogue
     * @param author - given author
     * @return - {@code true} if there is at least one book of given
     *          author in the catalogue of {@code false} if there is
     *          no any
     */
    boolean doesAuthorHaveBooks(Author author);
}
