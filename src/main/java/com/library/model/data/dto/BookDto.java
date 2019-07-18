package com.library.model.data.dto;

import com.library.model.data.entity.Author;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Keyword;
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
