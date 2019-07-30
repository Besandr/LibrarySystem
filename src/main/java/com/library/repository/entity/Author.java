package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {

    private long id;

    private String firstName;

    private String lastName;
}
