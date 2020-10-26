package comp3350.auctionapp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

public class HSQLDataAccess implements DataAccess {
    private String dbName;

    private Statement st;
    private Connection connection;
    private ResultSet rs;

    private String cmdString;
    private int updateCount;
    private String result;

    public HSQLDataAccess(String dbName) {
        this.dbName = dbName;
    }

    public boolean open(String dbPath) {
        String url;
        try {
            result = null;
            // Setup for HSQL
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            url = "jdbc:hsqldb:file:" + dbPath; // stored on disk mode
            connection = DriverManager.getConnection(url, "SA", "");
            st = connection.createStatement();

        } catch (Exception e) {
            result = processSQLError(e);
        }

        if (result == null) {
            System.out.println("Opened HSQL database " + dbPath);
            return true;
        }

        return false;
    }

    public boolean close() {
        try {    // commit all changes to the database
            result = null;
            cmdString = "shutdown compact";
            st.executeQuery(cmdString);
            connection.close();
        } catch (Exception e) {
            result = processSQLError(e);
        }

        if (result == null) {
            System.out.println("Closed HSQL database " + dbName);
            return true;
        }
        return false;
    }

    // get rid of all nasty "'"
    private String sanitize(String input) {
        return input.replaceAll("'", "");
    }

    // reconstruct an auction from database data
    private Auction constructAuctionFromRow(ResultSet rs) throws SQLException {
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(rs.getTimestamp("StartDate").getTime());
        endDate.setTimeInMillis(rs.getTimestamp("EndDate").getTime());

        return new Auction(
                rs.getInt("AuctionID"),
                rs.getString("SellerName"),
                startDate,
                endDate,
                AuctionType.valueOf(rs.getString("AuctionType")),
                new Product(
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        Category.valueOf(rs.getString("ProductCategory")),
                        rs.getString("ImagePath")
                ),
                new Dollar(String.valueOf(rs.getFloat("StartingBid")))
        );
    }

    // return auctions, matching a desired category, type and seller.
    // if any of these fields are blank, they're treated as wildcards
    public List<Auction> getAuctions(Category category, AuctionType type, String sellerName) {
        ArrayList<Auction> output = new ArrayList<>();

        // Construct query conditions
        String conds = " WHERE ";
        if (category != null) {
            conds += "ProductCategory='" + category + "'";
        }

        if (type != null) {
            if (!conds.equals(" WHERE ")) {
                // add AND for additional params
                conds += " AND ";
            }
            conds += "AuctionType='" + type + "'";
        }

        if (sellerName != null && !sellerName.equals("")) {
            if (!conds.equals(" WHERE ")) {
                // add AND for additional params
                conds += " AND ";
            }
            conds += "SellerName='" + sellerName + "'";
        }

        if (conds.equals(" WHERE ")) {
            // remove conds if there were none
            conds = "";
        }

        if (sellerName != null) { //Will not execute command if sellerName is null
            cmdString = "SELECT * FROM Auctions" + conds + " ORDER BY EndDate ASC";
            try {
                rs = st.executeQuery(cmdString);
                while (rs.next()) {
                    output.add(constructAuctionFromRow(rs));
                }
                rs.close();
            } catch (Exception e) {
                processSQLError(e);
            }
        }

        return output;
    }


    public List<Auction> getAuctions(int auctionID) {
        ArrayList<Auction> output = new ArrayList<>();
        cmdString = "SELECT * FROM Auctions WHERE AuctionID=" + auctionID;

        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                output.add(constructAuctionFromRow(rs));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        return output;
    }

    public List<User> getUsers(String username) {
        ArrayList<User> output = new ArrayList<>();

        if (username != null) {

        // Construct query conditions
        String conds = username.equals("") ? "" : (" WHERE Username='" + username + "'");
        cmdString = "SELECT * FROM Users" + conds;
        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                // reconstruct the user from database data
                output.add(new User(
                        rs.getString("Username"),
                        rs.getString("Password")
                ));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        }
        return output;
    }

    public List<Bid> getBids(int auctionID) {
        ArrayList<Bid> output = new ArrayList<>();
        cmdString = "SELECT * FROM Bids WHERE AuctionID=" + auctionID + " ORDER BY Amount DESC";
        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                // reconstruct the user from database data
                Calendar bidDate = new GregorianCalendar();
                bidDate.setTimeInMillis(rs.getTimestamp("BidDate").getTime());
                output.add(new Bid(
                        rs.getString("Username"),
                        new Dollar(String.valueOf(rs.getFloat("Amount"))),
                        rs.getInt("AuctionID"), bidDate
                ));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        return output;
    }

    public List<Bid> getBids(String username) {
        ArrayList<Bid> output = new ArrayList<>();
        cmdString = "SELECT * FROM Bids WHERE Username='" + username + "'";
        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                // reconstruct the user from database data
                Calendar bidDate = new GregorianCalendar();
                bidDate.setTimeInMillis(rs.getTimestamp("BidDate").getTime());
                output.add(new Bid(
                        rs.getString("Username"),
                        new Dollar(String.valueOf(rs.getFloat("Amount"))),
                        rs.getInt("AuctionID"), bidDate
                ));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        return output;
    }

    // return ratings associated with an auctionID
    public List<Rating> getRatings(int auctionID) {
        ArrayList<Rating> output = new ArrayList<>();
        cmdString = "SELECT * FROM Ratings WHERE AuctionID='" + auctionID + "'";
        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                // reconstruct the user from database data
                output.add(new Rating(
                        rs.getInt("AuctionID"),
                        rs.getString("RatingFrom"),
                        rs.getInt("Amount"),
                        rs.getString("Review")
                ));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        return output;
    }

    // return ratings associated with an auctionID
    public List<Rating> getRatings(String username) {
        ArrayList<Rating> output = new ArrayList<>();
        cmdString = "SELECT * FROM Ratings WHERE RatingFrom='" + username + "'";
        try {
            rs = st.executeQuery(cmdString);
            while (rs.next()) {
                // reconstruct the user from database data
                output.add(new Rating(
                        rs.getInt("AuctionID"),
                        rs.getString("RatingFrom"),
                        rs.getInt("Amount"),
                        rs.getString("Review")
                ));
            }
            rs.close();
        } catch (Exception e) {
            processSQLError(e);
        }
        return output;
    }

    public boolean addAuction(Auction auction) {
        result = null;

        if (auction != null) {
            List<User> existingUsers = getUsers(auction.getSeller());

            if (existingUsers != null && existingUsers.size() == 1) {
                String values;
                try {
                    Timestamp startDate = new Timestamp(auction.getStartDate().getTimeInMillis());
                    Timestamp endDate = new Timestamp(auction.getEndDate().getTimeInMillis());
                    values = "" + auction.getListingID() + ","
                            + "'" + auction.getSeller() + "',"
                            + "'" + auction.getAuctionType() + "',"
                            + auction.getMinBidAmount() + ","
                            + "'" + startDate.toString() + "',"
                            + "'" + endDate.toString() + "',"
                            + "'" + sanitize(auction.getProductName()) + "',"
                            + "'" + sanitize(auction.getProductDescription()) + "',"
                            + "'" + auction.getProductCategory().toString() + "',"
                            + "'" + auction.getProductImage() + "'";
                    cmdString = "INSERT INTO Auctions Values(" + values + ")";
                    updateCount = st.executeUpdate(cmdString);
                    result = checkWarning(st, updateCount);
                } catch (Exception e) {
                    result = processSQLError(e);
                }

                if (result == null) {
                    return true;
                }
            }
        }
        System.out.println("HSQL Failure on auction insert:\n" + result);
        return false;
    }

    public boolean addUser(User user) {
        String values;
        result = null;

        if (user != null) {
            try {
                values = "'" + user.getUsername() + "','" + user.getPassword() + "'";
                cmdString = "INSERT INTO Users Values(" + values + ")";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }
            if (result == null) {
                return true;
            }
        }
        System.out.println("HSQL Failure on user insert:\n" + result);
        return false;
    }

    public boolean addBid(Bid bid) {
        String values;
        result = null;

        if (bid != null) {
            try {
                Timestamp bidDate = new Timestamp(bid.getDate().getTimeInMillis());
                values = "" + bid.getAuctionID() + ",'" + bid.getBidder()
                        + "'," + bid.toString().substring(1) + ",'"
                        + bidDate.toString() + "'";
                cmdString = "INSERT INTO Bids Values(" + values + ")";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }
            if (result == null) {
                return true;
            }
        }
        System.out.println("HSQL Failure on bid insert:\n" + result);
        return false;
    }

    public boolean addRating(Rating rating) {
        result = null;

        if (rating != null) {
            List<User> users = getUsers(rating.getRatingFrom());

            if (users != null && users.size() == 1) {
                String values;
                result = null;
                try {
                    values = "" + rating.getAuctionID() + ",'" + rating.getRatingFrom()
                            + "'," + rating.getRatingValue() + ",'"
                            + rating.getWrittenReview() + "'";
                    cmdString = "INSERT INTO Ratings Values(" + values + ")";
                    updateCount = st.executeUpdate(cmdString);
                    result = checkWarning(st, updateCount);
                } catch (Exception e) {
                    result = processSQLError(e);
                }
                if (result == null) {
                    return true;
                }
            }
        }
        System.out.println("HSQL Failure on rating insert:\n" + result);
        return false;
    }

    public boolean deleteAuction(Auction auction) {
        result = null;
        if (auction != null) {
            try {
                cmdString = "DELETE FROM Auctions WHERE AuctionID=" + auction.getListingID()
                        + " AND SellerName='" + auction.getSeller() + "'";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }
            if (result == null) {
                return true;
            }
        } else {
            result = "NULL AUCTION";
        }
        System.out.println("HSQL Failure on auction delete:\n" + result);
        return false;
    }

    public boolean deleteUser(User user) {
        result = null;

        if (user != null) {
            try {
                cmdString = "DELETE FROM Users WHERE Username='" + user.getUsername() + "'";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }

            if (result == null) {
                return true;
            }
        }
        System.out.println("HSQL Failure on user delete:\n" + result);
        return false;
    }

    public boolean deleteBid(Bid bid) {
        result = null;

        if (bid != null) {
            try {
                cmdString = "DELETE FROM Bids WHERE AuctionID=" + bid.getAuctionID() +
                        " AND Username='" + bid.getBidder() +
                        "' AND Amount=" + bid.toString().substring(1) + "";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }

            if (result == null) {
                return true;
            }
        }

        System.out.println("HSQL Failure on bid delete:\n" + result);
        return false;
    }

    public boolean deleteRating(Rating rating) {
        result = null;

        if (rating != null) {
            try {
                cmdString = "DELETE FROM Ratings WHERE AuctionID=" + rating.getAuctionID() +
                        " AND RATINGFROM='" + rating.getRatingFrom() + "'";
                updateCount = st.executeUpdate(cmdString);
                result = checkWarning(st, updateCount);
            } catch (Exception e) {
                result = processSQLError(e);
            }

            if (result == null) {
                return true;
            }
        }

        System.out.println("HSQL Failure on rating delete:\n" + result);
        return false;
    }

    // "Re-purposed" this from the sample project
    private String checkWarning(Statement st, int updateCount) {
        String result = null;

        try {
            SQLWarning warning = st.getWarnings();
            if (warning != null) {
                result = warning.getMessage();
            }
        } catch (Exception e) {
            result = processSQLError(e);
        }
        if (updateCount != 1) {
            result = "Tuple not inserted correctly.";
        }
        return result;
    }

    public String processSQLError(Exception e) {
        String result = "*** SQL Error: " + e.getMessage();
        e.printStackTrace();

        return result;
    }
}
