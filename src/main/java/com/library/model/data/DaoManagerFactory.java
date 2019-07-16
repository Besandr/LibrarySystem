package com.library.model.data;

/**
 * Produces new {@code DaoManager} objects
 */
public class DaoManagerFactory {

    /**
     * Factory method for creating new {@code DaoManager} objects
     * and initializing them with {@code DataSource}
     * @return new {@code DaoManager} object
     */
    public static DaoManager createDaoManager() {
        return new DaoManager(DBService.getInstance().getDataSource());
    }

    private DaoManagerFactory(){}
}
