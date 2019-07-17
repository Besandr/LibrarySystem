package com.library.model.data.dao;

import com.library.model.data.entity.Author;

import java.util.Optional;

public interface AuthorDao extends Dao<Author> {

    /**
     * Tries to find an author with given first and last names
     * @param firstName - the first name of the target author
     * @param lastName - the last name of the target author
     * @return - an Optional with the target author or an
     *          empty Optional if there is no such author in DB
     */
    Optional<Author> getByName(String firstName, String lastName);
}
