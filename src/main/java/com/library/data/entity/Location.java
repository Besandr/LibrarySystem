package com.library.data.entity;

import lombok.Data;

@Data
public class Location {

    private long id;

    private long bookId;

    private long bookcaseId;

    private int shelfNumber;

    private int cellNumber;

    private boolean isOccupied;


}
