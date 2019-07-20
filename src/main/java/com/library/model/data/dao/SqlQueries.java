package com.library.model.data.dao;

public class SqlQueries {

    public static final String SAVE_AUTHOR_QUERY = "INSERT INTO library_system.author (first_name, last_name) VALUES (?, ?);";
    public static final String DELETE_AUTHOR_QUERY = "DELETE FROM library_system.author WHERE author_id = ?;";
    public static final String UPDATE_AUTHOR_QUERY = "UPDATE library_system.author SET first_name = ?, last_name = ? WHERE author_id = ?;";
    public static final String ALL_AUTHORS_QUERY = "SELECT * FROM library_system.author ORDER BY last_name;";
    public static final String GET_AUTHOR_QUERY = "SELECT * FROM library_system.author WHERE author_id = ?;";
    public static final String GET_AUTHORS_BY_BOOK_QUERY = "SELECT * FROM library_system.author AS a\n" +
            "INNER JOIN library_system.author_book AS ab ON a.author_id = ab.author_id\n" +
            "WHERE ab.book_id = ?;";
    public static final String GET_AUTHOR_BY_NAME_QUERY = "SELECT * FROM library_system.author WHERE first_name = ? AND last_name = ?;";

    public static final String SAVE_BOOK_QUERY = "INSERT INTO library_system.book (title, year, description) VALUES (?, ?, ?);";
    public static final String DELETE_BOOK_QUERY = "DELETE FROM library_system.book WHERE book_id = ?;";
    public static final String UPDATE_BOOK_QUERY = "UPDATE library_system.book SET title = ?, year = ?, description = ? WHERE book_id = ?;";
    public static final String ALL_BOOKS_QUERY_HEAD_PART = "SELECT * FROM library_system.book AS b";
    public static final String ALL_BOOKS_QUERY_AUTHOR_PART = "INNER JOIN library_system.author_book AS ab ON ab.author_id = ? AND ab.book_id = b.book_id";
    public static final String ALL_BOOKS_QUERY_KEYWORD_PART = "INNER JOIN library_system.book_keyword AS bk ON bk.keyword_id = ? AND bk.book_id = b.book_id";
    public static final String ALL_BOOKS_QUERY_TAIL_PART = "WHERE title LIKE ? ORDER BY b.year DESC;";
    public static final String GET_BOOK_QUERY = "SELECT * FROM library_system.book WHERE book_id = ?;";

    public static final String INSERT_BOOK_KEYWORD_QUERY = "INSERT INTO library_system.book_keyword VALUES (?, ?);";
    public static final String DELETE_BOOK_KEYWORD_QUERY = "DELETE FROM library_system.book_keyword WHERE book_id = ? AND keyword_id = ?;";
    public static final String COUNT_BOOKS_WITH_KEYWORD_QUERY = "SELECT COUNT(*) FROM library_system.book_keyword WHERE keyword_id = ?;";


    public static final String INSERT_AUTHOR_BOOK_QUERY = "INSERT INTO library_system.author_book VALUES (?, ?);";
    public static final String DELETE_AUTHOR_BOOK_QUERY = "DELETE FROM library_system.author_book WHERE author_id = ? AND book_id = ?;";
    public static final String COUNT_BOOKS_OF_AUTHOR_QUERY = "SELECT COUNT(*) FROM library_system.author_book WHERE author_id = ?;";


    public static final String SAVE_KEYWORD_QUERY = "INSERT INTO library_system.keyword (word) VALUES (?);";
    public static final String DELETE_KEYWORD_QUERY = "DELETE FROM library_system.keyword WHERE keyword_id = ?;";
    public static final String UPDATE_KEYWORD_QUERY = "UPDATE library_system.keyword SET word = ? WHERE keyword_id = ?;";
    public static final String ALL_KEYWORDS_QUERY = "SELECT * FROM library_system.keyword ORDER BY word;";
    public static final String GET_KEYWORD_QUERY = "SELECT * FROM library_system.keyword WHERE keyword_id = ?;";
    public static final String GET_KEYWORD_BY_WORD_QUERY = "SELECT * FROM library_system.keyword WHERE word = ?;";
    public static final String GET_KEYWORDS_BY_BOOK_QUERY = "SELECT * FROM library_system.keyword AS k\n" +
            "INNER JOIN library_system.book_keyword AS bk ON k.keyword_id = bk.keyword_id\n" +
            "WHERE bk.book_id = ?;";

    public static final String SAVE_BOOKCASE_QUERY = "INSERT INTO library_system.bookcase (shelf_quantity, cell_quantity) VALUES (?, ?);";
    public static final String DELETE_BOOKCASE_QUERY = "DELETE FROM library_system.bookcase WHERE bookcase_id = ?;";
    public static final String UPDATE_BOOKCASE_QUERY = "UPDATE library_system.bookcase SET shelf_quantity = ?, cell_quantity = ? WHERE bookcase_id = ?;";
    public static final String ALL_BOOKCASES_QUERY = "SELECT * FROM library_system.bookcase;";
    public static final String GET_BOOKCASE_QUERY = "SELECT * FROM library_system.bookcase WHERE bookcase_id = ?;";

    public static final String SAVE_LOCATION_QUERY =
            "INSERT INTO library_system.location (bookcase_id, shelf_number, cell_number) VALUES (?, ?, ?);";
    public static final String DELETE_LOCATION_QUERY = "DELETE FROM library_system.location WHERE location_id = ?;";
    public static final String SAVE_BOOK_TO_LOCATION_QUERY = "UPDATE library_system.location SET book_id = ?, is_occupied = TRUE WHERE location_id = ?;";
    public static final String DELETE_BOOK_FROM_ALL_LOCATIONS_QUERY = "UPDATE library_system.location SET book_id = NULL, is_occupied = FALSE WHERE book_id = ?;";
    public static final String UPDATE_LOCATION_OCCUPIED_QUERY = "UPDATE library_system.location SET is_occupied = ? WHERE location_id = ?;";
    public static final String ALL_LOCATIONS_QUERY = "SELECT * FROM library_system.location;";
    public static final String GET_FREE_LOCATIONS_QUERY = "SELECT * FROM library_system.location WHERE book_id IS NULL AND is_occupied = false;";
    public static final String GET_LOCATION_QUERY = "SELECT * FROM library_system.location WHERE location_id = ?;";
    public static final String GET_BOOK_LOCATION_QUERY = "SELECT * FROM library_system.location WHERE book_id = ? AND is_occupied = ? LIMIT 1;";
    public static final String GET_BOOK_QUANTITY_QUERY = "SELECT COUNT(*) FROM library_system.location WHERE book_id = ? AND is_occupied = TRUE;";



    public static final String SAVE_ROLE_QUERY = "INSERT INTO library_system.role (name) VALUES (?);";
    public static final String DELETE_ROLE_QUERY = "DELETE FROM library_system.role WHERE role_id = ?;";
    public static final String UPDATE_ROLE_QUERY = "UPDATE library_system.role SET name = ? WHERE role_id = ?;";
    public static final String ALL_ROLES_QUERY = "SELECT * FROM library_system.role;";
    public static final String GET_ROLE_QUERY = "SELECT * FROM library_system.role WHERE role_id = ?;";

    public static final String SAVE_USER_QUERY = "INSERT INTO library_system.user (email, password, phone, first_name, last_name, role_id)" +
            " VALUES (?, ?, ?, ?, ?, ?);";
    public static final String DELETE_USER_QUERY = "DELETE FROM library_system.user WHERE user_id = ?;";
    public static final String UPDATE_USER_INFO_QUERY = "UPDATE library_system.user SET " +
            "email = ?," +
            "password = ?, " +
            "phone = ?, " +
            "first_name = ?, " +
            "last_name = ? " +
            "WHERE user_id = ?;";
    public static final String UPDATE_USER_KARMA_QUERY = "UPDATE library_system.user SET karma = ? WHERE user_id = ?;";
    public static final String UPDATE_USER_ROLE_QUERY = "UPDATE library_system.user SET role_id = ? WHERE user_id = ?;";
    public static final String ALL_USERS_QUERY = "SELECT * FROM library_system.user;";
    public static final String GET_USER_QUERY = "SELECT * FROM library_system.user WHERE user_id = ?;";

    public static final String SAVE_LOAN_QUERY = "INSERT INTO library_system.loan (book_id, user_id, apply_date) VALUES (?, ?, ?);";
    public static final String DELETE_LOAN_QUERY = "DELETE FROM library_system.loan WHERE loan_id = ?;";
    public static final String UPDATE_LOAN_AND_EXPIRED_DATE_QUERY = "UPDATE library_system.loan " +
            "SET loan_date = ?, expired_date = ? WHERE loan_id = ?;";
    public static final String UPDATE_RETURN_DATE_QUERY = "UPDATE library_system.loan SET return_date = ? WHERE loan_id = ?;";
    public static final String ALL_LOANS_QUERY = "SELECT * FROM library_system.loan ORDER BY expired_date;";
    public static final String GET_LOAN_QUERY = "SELECT * FROM library_system.loan WHERE loan_id = ?;";


    public static final String GET_UNAPPROVED_LOANS_QUERY = "SELECT * FROM library_system.loan AS l \n" +
            "INNER JOIN library_system.book AS b ON l.book_id = b.book_id\n" +
            "INNER JOIN library_system.user AS u ON l.user_id = u.user_id\n" +
            "WHERE COALESCE (loan_date, expired_date, return_date) IS NULL;";

    public static final String GET_ACTIVE_LOANS_QUERY = "SELECT * FROM library_system.loan AS l \n" +
            "INNER JOIN library_system.book AS b ON l.book_id = b.book_id\n" +
            "INNER JOIN library_system.user AS u ON l.user_id = u.user_id\n" +
            "WHERE loan_date IS NOT NULL AND return_date IS NULL;";

    public static final String GET_ACTIVE_LOANS_BY_BOOK_QUERY = "SELECT * FROM library_system.loan AS l \n" +
            "INNER JOIN library_system.book AS b ON l.book_id = b.book_id\n" +
            "INNER JOIN library_system.user AS u ON l.user_id = u.user_id\n" +
            "WHERE loan_date IS NOT NULL AND return_date IS NULL\n" +
            "AND l.book_id = ?;";
}
