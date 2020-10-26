package comp3350.auctionapp.tests.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.persistence.DataAccess;

public class StubDataAccess implements DataAccess {

    private String dbName;
    private List<User> users;
    private List<Product> products;
    private List<Auction> auctions;
    private List<Bid> bids;
    private List<Rating> ratings;

    // Private class used to sort auctions by end date.
    private static class AuctionDateSorter implements Comparator<Auction> {
        @Override
        public int compare(Auction auction1, Auction auction2) {
            return auction1.compareEndDates(auction2);
        }
    }

    public StubDataAccess(String dbName) {
        this.dbName = dbName;
    }

    public boolean open(String dbPathName) {
        initializeUsers();
        initializeProducts();
        initializeAuctions();
        initializeBids();
        initializeRatings();

        System.out.println("\nOpened stub database " + dbName);
        return true; //Always able to open stub db
    }

    private void initializeUsers() {
        User user;

        users = new ArrayList<>();
        user = new User("bgates", "qwerty123");
        users.add(user);
        user = new User("ltorvalds", "qwerty123");
        users.add(user);
        user = new User("alovelace", "qwerty123");
        users.add(user);
        user = new User("mhamilton", "qwerty123");
        users.add(user);
        user = new User("tfrancis", "qwerty123");
        users.add(user);
    }

    private void initializeProducts() {
        Product product;

        products = new ArrayList<>();
        product = new Product("Computer", "This is a computer", Category.ELECTRONICS, "coco3_system.jpg");
        products.add(product);
        product = new Product("Diamond ring", "This is a shiny diamond ring", Category.JEWELLERY, "diamond_ring.jpg");
        products.add(product);
        product = new Product("MTG: Black Lotus card", "The legendary Black Lotus Card", Category.COLLECTIBLES, "black_lotus.jpg");
        products.add(product);
        product = new Product("Generic Product", "Generic Product used for pre expired auctions", Category.COLLECTIBLES, "black_lotus.jpg");
        products.add(product);
    }

    private void initializeAuctions() {
        Auction auction;

        auctions = new ArrayList<>();
        auction = new Auction(1001, users.get(0).getUsername(), new GregorianCalendar(2020, 7, 1), new GregorianCalendar(2020, 9, 1), AuctionType.ENGLISH, products.get(0), new Dollar("5.00"));
        auctions.add(auction);
        auction = new Auction(1002, users.get(1).getUsername(), new GregorianCalendar(2020, 1, 1), new GregorianCalendar(2020, 8, 30), AuctionType.ENGLISH, products.get(1), new Dollar("6.00"));
        auctions.add(auction);
        auction = new Auction(1003, users.get(2).getUsername(), new GregorianCalendar(2020, 1, 1), new GregorianCalendar(2020, 12, 1), AuctionType.SEALEDBID, products.get(2), new Dollar("7.00"));
        auctions.add(auction);

        // PRE EXPIRED AUCTIONS: The user will not see these but they are important for rating history

        auction = new Auction(1004, users.get(3).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 12, 1), AuctionType.SEALEDBID, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1005, users.get(3).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 11, 1), AuctionType.ENGLISH, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1006, users.get(2).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 10, 1), AuctionType.SEALEDBID, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1007, users.get(2).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 10, 1), AuctionType.ENGLISH, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1008, users.get(2).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 10, 1), AuctionType.ENGLISH, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1009, users.get(2).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 10, 1), AuctionType.ENGLISH, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
        auction = new Auction(1010, users.get(0).getUsername(), new GregorianCalendar(2019, 1, 1), new GregorianCalendar(2019, 10, 1), AuctionType.ENGLISH, products.get(3), new Dollar("8.00"));
        auctions.add(auction);
    }

    private void initializeBids() {
        Bid bid;

        bids = new ArrayList<>();
        bid = new Bid(users.get(3).getUsername(), new Dollar("20.00"), auctions.get(0).getListingID(), new GregorianCalendar(2020, 7, 1));
        bids.add(bid);
        bid = new Bid(users.get(2).getUsername(), new Dollar("30.00"), auctions.get(0).getListingID(), new GregorianCalendar(2020, 7, 2));
        bids.add(bid);
        bid = new Bid(users.get(3).getUsername(), new Dollar("40.00"), auctions.get(0).getListingID(), new GregorianCalendar(2020, 7, 3));
        bids.add(bid);
        bid = new Bid(users.get(0).getUsername(), new Dollar("30.00"), auctions.get(1).getListingID(), new GregorianCalendar(2020, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(0).getUsername(), new Dollar("30.00"), auctions.get(3).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(1).getUsername(), new Dollar("30.00"), auctions.get(4).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(0).getUsername(), new Dollar("30.00"), auctions.get(5).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(3).getUsername(), new Dollar("30.00"), auctions.get(6).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(1).getUsername(), new Dollar("30.00"), auctions.get(7).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(1).getUsername(), new Dollar("30.00"), auctions.get(8).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
        bid = new Bid(users.get(3).getUsername(), new Dollar("30.00"), auctions.get(9).getListingID(), new GregorianCalendar(2019, 4, 1));
        bids.add(bid);
    }

    private void initializeRatings() {
        Rating rating;

        // auctions 1005 and 1008 were won by ltorvalds but linus has yet to leave reviews
        // as the default session user is ltorvalds this is left as an exercise to the user
        // man i've always wanted to say that. How do I get into the math textbook industry?

        ratings = new ArrayList<>();
        rating = new Rating(1004, "mhamilton", 4, "4/5 pretty good");
        ratings.add(rating);
        rating = new Rating(1006, "alovelace", 2, "2/5 not so good");
        ratings.add(rating);
        rating = new Rating(1007, "alovelace", 5, "Literally the best thing I have ever seen");
        ratings.add(rating);
        rating = new Rating(1009, "alovelace", 1, "Literally the worst thing I have ever seen");
        ratings.add(rating);
        rating = new Rating(1010, "bgates", 3, "I have complete apathy towards this");
        ratings.add(rating);
    }


    public boolean close() {
        System.out.println("Closed stub database " + dbName);
        return true; //Can always close stub db
    }

    // return auctions, matching a desired category, type and seller.
    // if any of these fields are blank, they're treated as wildcards
    public List<Auction> getAuctions(Category category, AuctionType type, String sellerName) {
        List<Auction> output = new ArrayList<>();
        Auction[] auctionsArr = auctions.toArray(new Auction[auctions.size()]);
        Arrays.sort(auctionsArr, new AuctionDateSorter());

        if (sellerName != null) {
            for (Auction auction : auctionsArr) {
                // Returns true for all auctions that match category, type, and have keyword in either name or description
                // Null fields ignored
                if ((category == null || auction.getProductCategory() == category) &&
                        (type == null || auction.getAuctionType() == type) &&
                        (sellerName.equals("") || auction.getSeller().contains(sellerName))) {

                    output.add(auction);
                }
            }
        }
        return output;
    }

    // return auction matching auctionID
    public List<Auction> getAuctions(int auctionID) {
        List<Auction> output = new ArrayList<>();
        Auction[] auctionsArr = auctions.toArray(new Auction[auctions.size()]);
        Arrays.sort(auctionsArr, new AuctionDateSorter());

        for (Auction auction : auctionsArr) {
            if (auction.getListingID() == auctionID) {
                output.add(auction);
            }
        }
        return output;
    }

    // Access by username
    public List<User> getUsers(String username) {
        List<User> matchingUsers = new ArrayList<>();
        Iterator<User> userIter = users.iterator();

        if (username != null) {
            while (userIter.hasNext()) {
                User currUser = userIter.next();

                if (currUser.hasUsername(username) || username.equals("")) {
                    matchingUsers.add(currUser);
                }
            }
        }

        return matchingUsers;
    }

    //Returns bids associated with an auction
    public List<Bid> getBids(int auctionID) {
        List<Bid> auctionBids = new ArrayList<>();

        for (Bid nextBid : bids) {
            if (nextBid.isAuction(auctionID)) {
                auctionBids.add(nextBid);
            }
        }

        return auctionBids;
    }

    //Get a User's list of bids
    public List<Bid> getBids(String user) {
        List<Bid> userBids = new ArrayList<>();

        for (Bid nextBid : bids) {
            if (nextBid.isBidder(user)) {
                userBids.add(nextBid);
            }
        }

        return userBids;
    }

    // return ratings associated with an auctionID
    public List<Rating> getRatings(int auctionID) {
        List<Rating> matchingRatings = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getAuctionID() == auctionID) {
                matchingRatings.add(rating);
            }
        }
        return matchingRatings;
    }

    // return ratings associated with an auctionID
    public List<Rating> getRatings(String username) {
        List<Rating> matchingRatings = new ArrayList<>();
        for (Rating rating : ratings) {
            if (rating.getRatingFrom().equals(username)) {
                matchingRatings.add(rating);
            }
        }
        return matchingRatings;
    }

    // Methods to add new auctions and users to the arraylists. Returns true if successful (standard from Collection class)
    public boolean addAuction(Auction auction) {
        List<Auction> matchingAuctions;
        List<User> matchingUsers;

        if (auction != null) {
            matchingAuctions = getAuctions(auction.getListingID());
            matchingUsers = getUsers(auction.getSeller());

            if (matchingAuctions.size() == 0 && matchingUsers.size() != 0 && !auctions.contains(auction)) {
                return auctions.add(auction);
            }
        }

        return false;
    }

    public boolean addUser(User user) {
        if (user != null && !users.contains(user)) {
            return users.add(user);
        }
        return false;
    }

    public boolean addBid(Bid bid) {
        if (bid != null) {
            List<User> matchingUsers = getUsers(bid.getBidder());
            List<Auction> matchingAuctions = getAuctions(bid.getAuctionID());

            if (matchingUsers.size() == 1 && matchingAuctions.size() == 1 && !bids.contains(bid)) {
                return bids.add(bid);
            }
        }
        return false;
    }

    public boolean addRating(Rating rating) {
        if (rating != null) {
            List<User> matchingUsers = getUsers(rating.getRatingFrom());
            List<Auction> matchingAuctions = getAuctions(rating.getAuctionID());
            // can't add another rating
            List<Rating> matchingRatings = getRatings(rating.getAuctionID());
            if (matchingUsers.size() == 1 && matchingAuctions.size() == 1 && matchingRatings.size() == 0) {
                return ratings.add(rating);
            }
        }
        return false;
    }

    // Methods to delete auctions and users. Returns true if the item was present and was deleted.
    // Note that a false return doesn't mean an error. It just means the thing wasn't found: these methods are not idempotent.
    public boolean deleteAuction(Auction auction) {
        int index;
        index = auctions.indexOf(auction);
        if (index >= 0) {
            auctions.remove(index);
        }
        return index >= 0;
    }

    public boolean deleteUser(User user) {
        int index;
        index = users.indexOf(user);
        if (index >= 0) {
            users.remove(index);
            return true;
        }
        return false;
    }

    public boolean deleteBid(Bid bid) {
        int index;
        index = bids.indexOf(bid);
        if (index >= 0) {
            bids.remove(index);
            return true;
        }
        return false;
    }

    public boolean deleteRating(Rating rating) {
        int index;
        index = ratings.indexOf(rating);
        if (index >= 0) {
            ratings.remove(index);
            return true;
        }
        return false;
    }
}
