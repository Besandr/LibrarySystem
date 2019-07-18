package com.library.model.data.dao;

import com.library.model.data.dto.LoanDto;

import java.util.List;

public interface LoanDtoDao {

    /**
     * Gets a list of unapproved loans
     * @return - a list with unapproved loans
     *          or the empty list if there is no any
     */
    List<LoanDto> getUnapprovedLoans();
}
