package com.library.model.data.dto;

import com.library.model.data.entity.Book;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * DTO version of {@code Loan}
 */
@Data
@Builder
public class LoanDto {

    private Loan loan;

    private Book book;

    private User user;

}
