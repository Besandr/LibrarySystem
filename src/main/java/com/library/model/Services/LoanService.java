package com.library.model.Services;

import com.library.model.data.DaoManager;
import com.library.model.data.DaoManagerFactory;
import com.library.model.data.dao.*;
import com.library.model.data.entity.Loan;
import com.library.model.data.entity.Location;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class LoanService {

    private static final LoanService instance = new LoanService();

    public boolean saveApplyForLoan(Loan loan) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        return (Boolean) daoManager.executeAndClose(manager -> saveApplyForLoanCommand(manager, loan));

    }

    public boolean approveLoan(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

        synchronized (this) {
            return (Boolean) daoManager.executeTransaction(manager -> approveLoanCommand(manager, loanId));
        }
    }

    public boolean returnBook(long loanId) {

        DaoManager daoManager = DaoManagerFactory.createDaoManager();

            return  (Boolean) daoManager.executeTransaction(manager -> returnBookCommand(manager, loanId));
    }

    protected boolean saveApplyForLoanCommand(DaoManager manager, Loan loan) throws SQLException {

        LoanDao dao =(LoanDao) manager.getLoanDao();
        dao.save(loan);
        return true;
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
        Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBook().getId(), true);
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
        Optional<Location> locationOptional = locationDao.getBookLocation(loan.get().getBook().getId(), false);
        locationDao.updateIsOccupied(locationOptional.get().getId(), true);

        return true;
    }

    static LoanService getInstance() {
        return instance;
    }

    private LoanService(){}

}
