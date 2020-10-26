package comp3350.auctionapp.application;

import comp3350.auctionapp.persistence.DataAccess;
import comp3350.auctionapp.persistence.HSQLDataAccess;

public class Services {
    public static final String dbName = "AuctionApp";
    private static DataAccess dataAccessService = null;
    private static String dbPathName = "database/AuctionApp";

    //Default is the HSQL database
    public static DataAccess createDataAccess() {
        if (dataAccessService == null) {
            dataAccessService = new HSQLDataAccess(dbName);
            dataAccessService.open(dbPathName);
        }
        return dataAccessService;
    }

    public static DataAccess createDataAccess(DataAccess alternativeAccess) {
        if (dataAccessService == null) {
            dataAccessService = alternativeAccess;
            dataAccessService.open(dbPathName);
        }
        return dataAccessService;
    }

    // Sets path name for the SQL database
    public static void setDBPathName(String pathName) {
        System.out.println("Setting DB path to: " + pathName);
        dbPathName = pathName;
    }

    // Cleanup code for the DB
    public static void shutDown() {
        if (dataAccessService != null) {
            dataAccessService.close();
        }
        dataAccessService = null;
    }

    public static DataAccess getDataAccessService() {
        if (dataAccessService != null) {
            return dataAccessService;
        } else {
            System.out.println("ERROR: Data Access Not Initialized");
            return null;
        }
    }

}
