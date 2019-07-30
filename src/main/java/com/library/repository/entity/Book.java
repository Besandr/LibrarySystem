package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {

    private long id;

    private String title;

    private int year;

    private String description;

}