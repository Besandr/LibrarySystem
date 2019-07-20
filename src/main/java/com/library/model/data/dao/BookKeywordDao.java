package com.library.model.data.dao;

import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;

import java.util.Set;

/**
 * Dao for interacting with book_keyword table
 */
public interface BookKeywordDao {

    /**
     * Inserts data for joining keyword and book tables.
     * Throws {@code DBException} if inserting fails
     * @param book - book for joining
     * @param keywords - set with keywords for joining
     */
    void saveBookKeywordsJunction(Book book, Set<Keyword> keywords);

    /**
     * Seeks books which has given keyword and return {@code true}
     * if there are any.
     * @param keyword - given keyword
     * @return - {@code true} if there is at least one book with given keyword
     *           {@code false} if there is no any
     */
    boolean doesKeywordBelongToBook(Keyword keyword);

    /**
     * Deletes a junction between given book and given keyword
     * @param book - book which junction to given keyword need to be deleted
     * @param keyword - keyword which junction to given book need to be deleted
     */
    void deleteBookKeywordJunction(Keyword keyword, Book book);
}
