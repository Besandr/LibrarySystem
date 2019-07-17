package com.library.model.data.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Loan {

    private long id;

    private Book book;

    private User user;

    private LocalDate applyDate;

    private LocalDate loanDate;

    private LocalDate expiredDate;

    private LocalDate returnDate;

}
