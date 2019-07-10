package com.library.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Creates {@code BasicDataSource} object which provides work with
 * connection pool and configures it by properties from config file
 * of by default
 */
public class DBService {

    private static final Logger logger = LogManager.getLogger(DBService.class);

    private static BasicDataSource ds;

    static {
        initDataSource();
    }

    private static void initDataSource() {

        //Default properties
        final String DEFAULT_MAX_IDLE = "10";
        final String DEFAULT_MIN_IDLE = "5";
        final String DEFAULT_MAX_OPEN_PREPARED_STATEMENTS = "20";
        final String DEFAULT_URL = "jdbc:mysql://localhost:3306?allowMultiQueries=true&serverTimezone=UTC";
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
            logger.error("Can't read DBConnectionConfig.properties file. Configure BasicDataSource with default values");
        }

        ds = new BasicDataSource();
        ds.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle", DEFAULT_MAX_IDLE)));
        ds.setMinIdle(Integer.parseInt(properties.getProperty("minIdle", DEFAULT_MIN_IDLE)));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(properties.getProperty("maxOpenPreparedStatements", DEFAULT_MAX_OPEN_PREPARED_STATEMENTS)));
        ds.setUrl(properties.getProperty("url", DEFAULT_URL));
        ds.setUsername(properties.getProperty("username", DEFAULT_USERNAME));
        ds.setPassword(properties.getProperty("password", DEFAULT_PASSWORD));
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBService(){}

}
