package com.library.repository;

/**
 * {@code RuntimeException} for exceptional situations
 * during dao methods working
 */
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
