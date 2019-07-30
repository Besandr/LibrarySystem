package com.library.repository.dto;

import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.entity.Keyword;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * DTO version of {@code Book}
 */
@Data
@Builder
public class BookDto {

    private Book book;

    private Set<Author> authors;

    private Set<Keyword> keywords;

}
