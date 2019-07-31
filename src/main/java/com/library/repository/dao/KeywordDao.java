package com.library.repository.dao;

import com.library.repository.entity.Keyword;

import java.util.List;
import java.util.Optional;

/**
 * Dao for Keyword entity
 */
public interface KeywordDao extends Dao<Keyword> {

    /**
     * Tries to find a keyword in a DB by given word
     * @param word - the word which must be in a keyword row
     * @return - an Optional with the target keyword or an
     *          empty Optional if there is no such keyword in DB
     */
    Optional<Keyword> getByWord(String word);

    /**
     * Gets a list with keywords of a given book
     * @param bookId - ID of book which authors need to be gotten
     * @return a list with keywords of a given book or an empty
     *          list if there is no keywords of the book
     */
    List<Keyword> getByBookId(long bookId);
}
