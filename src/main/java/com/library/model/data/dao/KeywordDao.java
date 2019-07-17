package com.library.model.data.dao;

import com.library.model.data.entity.Keyword;

import java.util.Optional;

public interface KeywordDao extends Dao<Keyword> {

    /**
     * Tries to find a keyword in a DB by given word
     * @param word - the word which must be in a keyword row
     * @return - an Optional with the target keyword or an
     *          empty Optional if there is no such keyword in DB
     */
    Optional<Keyword> getByWord(String word);
}
