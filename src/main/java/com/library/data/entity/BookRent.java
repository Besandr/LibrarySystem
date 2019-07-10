package com.library.data.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BookRent {

    private long id;

    private long bookId;

    private long userId;

    private Date applyDate;

    private Date takenDate;

    private Date expiredDate;

    private Date returnDate;

}
