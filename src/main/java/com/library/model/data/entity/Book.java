package com.library.model.data.entity;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class Book {

    private long id;

    private String title;

    private Set<Author> authors;

    private int year;

    private String description;

    private Set<Keyword> keywords;

}