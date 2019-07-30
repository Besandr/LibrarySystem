package com.library.repository.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

    private long id;

    private long bookId;

    private long bookcaseId;

    private int shelfNumber;

    private int cellNumber;

    private boolean isOccupied;


}
