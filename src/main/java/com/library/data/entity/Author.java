package com.library.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Author {

    private long id;

    private String firstName;

    private String lastName;
}
