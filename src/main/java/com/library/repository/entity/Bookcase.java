package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bookcase {

    private long id;

    private int shelfQuantity;

    private int cellQuantity;

}
