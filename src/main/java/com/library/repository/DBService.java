package com.library.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Creates {@code BasicDataSource} object which provides work with
 * connection pool and configures it by properties from config file
 * of by default
 */
public class DBService {

    private static final Logger log = LogManager.getLogger(DBService.class);

    private static DBService instance = new DBService();
    private static BasicDataSource ds;

    static {
        registerDriver();
        initDataSource();
    }

    private static void initDataSource() {

        //Default properties
        final String DEFAULT_MAX_IDLE = "10";
        final String DEFAULT_MIN_IDLE = "5";
        final String DEFAULT_MAX_OPEN_PREPARED_STATEMENTS = "20";
        final String DEFAULT_URL = "jdbc:mysql://localhost:3306?allowMultiQueries=true&serverTimezone=";
        final String DEFAULT_USERNAME = "andrey";
        final String DEFAULT_PASSWORD = "fancyPa55w0rd";

        Properties properties = new Properties();

        try (InputStream inputStream = DBService.class.getClassLoader().getResourceAsStream("DBConnectionConfig.properties")) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException();
            }

        } catch (IOException e) {
            log.error("Can't read DBConnectionConfig.properties file. Configure BasicDataSource with default values", e);
        }

        ds = new BasicDataSource();
        ds.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle", DEFAULT_MAX_IDLE)));
        ds.setMinIdle(Integer.parseInt(properties.getProperty("minIdle", DEFAULT_MIN_IDLE)));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(properties.getProperty("maxOpenPreparedStatements", DEFAULT_MAX_OPEN_PREPARED_STATEMENTS)));
        // connection url appended with JVM default timezone for synchronizing timezones between JVM and MySql server
        ds.setUrl(properties.getProperty("url", DEFAULT_URL) + TimeZone.getDefault().getID());
        ds.setUsername(properties.getProperty("username", DEFAULT_USERNAME));
        ds.setPassword(properties.getProperty("password", DEFAULT_PASSWORD));
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Registers the database connection driver
     */
    private static void registerDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    DataSource getDataSource(){
        return ds;
    }

    public static DBService getInstance(){
        return instance;
    }

    private DBService(){}

}
