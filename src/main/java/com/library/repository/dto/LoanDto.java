package com.library.repository.dto;

import com.library.repository.entity.Author;
import com.library.repository.entity.Book;
import com.library.repository.entity.Loan;
import com.library.repository.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO version of {@code Loan}
 */
@Data
@Builder
public class LoanDto {

    private Loan loan;

    private Book book;

    private User user;

    private List<Author> authors;

    private int bookQuantity;

}
