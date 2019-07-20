package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.dto.LoanDto;
import com.library.model.data.entity.Book;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.Location;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoanService extends Service{

    private static final LoanService instance = new LoanService();

    public boolean saveApplyForLoan(Loan loan) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeAndClose(manager -> {
            manager.getLoanDao().save(loan);
            return true;
        });

        return checkAndCastExecutingResult(executingResult);
    }

    public List<LoanDto> getUnapprovedLoans(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<LoanDto>) daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getUnapprovedLoans());
    }

    public List<LoanDto> getActiveLoans(){

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<LoanDto>) daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoans());
    }

    public List<LoanDto> getActiveLoansByBook(Book book) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (List<LoanDto>) daoManager.executeAndClose(manager -> manager.getLoanDtoDao().getActiveLoansByBook(book));
    }

    public boolean approveLoan(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        synchronized (this) {
            Object executingResult = daoManager.executeTransaction(manager -> approveLoanCommand(manager, loanId));
            return checkAndCastExecutingResult(executingResult);
        }
    }

    public boolean returnBook(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        Object executingResult = daoManager.executeTransaction(manager -> returnBookCommand(manager, loanId));

        return checkAndCastExecutingResult(executingResult);
    }

    protected boolean approveLoanCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = (LoanDao) manager.getLoanDao();
        Optional<Loan> loan = loanDao.get(loanId);
        //check is loan with given id persistent in the DB
        //and book is not lend to user yet
        if (!loan.isPresent() || loan.get().getLoanDate() != null) {
            return false;
        }

        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBookId(), true);
        //check is there a target book on the shelves
        if (locationOptional.isPresent()) {

            loanDao.updateLoanAndExpiredDate(loanId, LocalDate.now(), LocalDate.now().plusMonths(1));
            locationDao.updateIsOccupied(locationOptional.get().getId(), false);

            return true;

        } else {
            return false;
        }
    }

    protected boolean returnBookCommand(DaoManager manager, long loanId) throws SQLException {

        LoanDao loanDao = (LoanDao) manager.getLoanDao();
        Optional<Loan> loan;
        synchronized (this) {
            loan = loanDao.get(loanId);
            //check is loan with given id persistent in the DB
            //and book is not returned by user yet
            if (!loan.isPresent() || loan.get().getReturnDate() != null) {
                return false;
            }

            loanDao.updateReturnDate(loanId, LocalDate.now());
        }

        LocationDao locationDao = (LocationDao) manager.getLocationDao();
        Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBookId(), false);
        locationDao.updateIsOccupied(locationOptional.get().getId(), true);

        return true;
    }

    public static LoanService getInstance() {
        return instance;
    }

    private LoanService(){}

}
