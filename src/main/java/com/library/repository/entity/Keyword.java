package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Keyword {

    private long id;

    private String word;

}
