package comp3350.auctionapp.tests.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.persistence.DataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataAccessTests {
    private DataAccess accessor;

    @Before
    public void setup() {
        accessor = new StubDataAccess(Services.dbName);
        accessor.open("");
    }

    @After
    public void teardown() {
        accessor.close();
    }

    public static void testDataAccess(DataAccess accessor) {
        DataAccessTests testClass = new DataAccessTests();
        testClass.accessor = accessor;
        testClass.testValidUserMethodCalls();
        testClass.testInvalidUserMethodCalls();
        testClass.testValidAuctionMethodCalls();
        testClass.testInvalidAuctionMethodCalls();
        testClass.testValidBidMethodCalls();
        testClass.testInvalidBidMethodCalls();
        testClass.testValidRatingMethodCalls();
        testClass.testInvalidRatingMethodCalls();
    }

    @Test
    public void testValidUserMethodCalls() {
        System.out.println("\n***Getting valid user");
        List<User> matches = accessor.getUsers("bgates");
        assertNotNull("Should have returned a List", matches);
        assertNotNull("One user should have been found", matches);
        assertEquals("Should have username bgates", "bgates", matches.get(0).getUsername());

        System.out.println("\n***Adding valid user");
        User newUser = new User("bob123", "1234");
        assertTrue("Should have added new user", accessor.addUser(newUser));
        matches = accessor.getUsers("bob123");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Matches should be of size 1", 1, matches.size());
        assertEquals("Should have found newUser", matches.get(0), newUser);

        System.out.println("\n***Deleting valid user");
        assertTrue("Deletion should have been successful", accessor.deleteUser(newUser));
        matches = accessor.getUsers("bob123");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Matches should be of size 0", 0, matches.size());
    }

    @Test
    public void testInvalidUserMethodCalls() {
        System.out.println("\n***Getting user that doesn't exist");
        List<User> matches = accessor.getUsers("idonotexist");
        assertNotNull("Should have returned a List", matches);
        assertNotNull("No users should have been found", matches);

        System.out.println("\n***Getting null user");
        matches = accessor.getUsers(null);
        assertNotNull("Should have returned a List", matches);
        assertNotNull("No users should have been found", matches);

        System.out.println("\n***Deleting non-existent user");
        User newUser = new User("bob123", "1234");
        assertFalse("Deletion should not have been successful", accessor.deleteUser(newUser));

        System.out.println("\n***Deleting null user");
        assertFalse("Deletion should not have been successful", accessor.deleteUser(null));

        System.out.println("\n***Adding existing user user");
        newUser = new User("bgates", "qwerty123");
        assertFalse("Should not have added bgates", accessor.addUser(newUser));
        matches = accessor.getUsers("bgates");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Matches should be of size 1", 1, matches.size());

        System.out.println("\n***Adding user with same username");
        newUser = new User("bgates", "differentpw");
        assertFalse("Should not have added another bgates", accessor.addUser(newUser));
        matches = accessor.getUsers("bgates");
        assertNotNull("Matches should not be empty", matches);
        assertEquals("There should only be one bgates", 1, matches.size());
    }

    @Test
    public void testValidAuctionMethodCalls() {
        System.out.println("\n***Getting valid auction");
        List<Auction> matches = accessor.getAuctions(1001);
        assertNotNull("Matches should not be empty", matches);
        assertEquals("One auction should have been found", 1, matches.size());
        assertEquals("Should have auctionid 1001", 1001, matches.get(0).getListingID());

        System.out.println("\n***Getting auctions of a particular category");
        matches = accessor.getAuctions(null, AuctionType.ENGLISH, "");
        assertNotNull("matches shouldn't be null", matches);
        assertEquals("Should have returned 7 auctions", 7, matches.size());
        List<Integer> matchesID = new ArrayList<>();
        // Make sure that we returned the right auctions
        for (Auction auction : matches) {
            matchesID.add(auction.getListingID());
        }
        assertTrue("ID 1001 not found in returned auctions", matchesID.contains(1001));
        assertTrue("ID 1002 not found in returned auctions", matchesID.contains(1002));
        assertTrue("ID 1005 not found in returned auctions", matchesID.contains(1005));

        System.out.println("\n***Getting auctions of by user");
        matches = accessor.getAuctions(null, null, "bgates");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found 2 auctions", matches.size(), 2);
        matchesID = new ArrayList<>();
        // Making sure that we returned the right auctions
        for (Auction auction : matches) {
            matchesID.add(auction.getListingID());
        }
        assertTrue("ID 1001 not found in returned auctions", matchesID.contains(1001));
        assertTrue("ID 1010 not found in returned auctions", matchesID.contains(1010));

        System.out.println("\n***Getting auctions by category and auction type");
        matches = accessor.getAuctions(Category.ELECTRONICS, AuctionType.ENGLISH, "");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found one match", matches.size(), 1);
        // Making sure that we returned the right auction
        assertEquals("ID 1001 not found in returned auction", matches.get(0).getListingID(), 1001);

        System.out.println("\n***Getting auctions by category, auction type and username");
        matches = accessor.getAuctions(Category.ELECTRONICS, AuctionType.ENGLISH, "alovelace");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found no matches", matches.size(), 0);

        System.out.println("\n***Getting all auctions, ordered by date");
        matches = accessor.getAuctions(null, null, "");
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found 10 auctions", 10, matches.size());

        Calendar nextDate;
        Calendar prevDate = new GregorianCalendar(1970, 1, 1);
        for (Auction auction : matches) {
            nextDate = auction.getEndDate();
            assertTrue("Dates not in order!", prevDate.compareTo(nextDate) <= 0);
            prevDate = nextDate;
        }

        System.out.println("\n***Adding valid auction");
        User user = new User("bgates", "qwerty123");
        Product thing = new Product("thing", "a thing", Category.COLLECTIBLES, "image.jpg");
        Calendar startDate = new GregorianCalendar(2020, 1, 1);
        Calendar endDate = new GregorianCalendar(2021, 1, 1);
        Auction validAuction = new Auction(999, user.getUsername(), startDate, endDate, AuctionType.ENGLISH, thing, new Dollar("5.00"));
        assertTrue("Auction was not added successfully", accessor.addAuction(validAuction));
        matches = accessor.getAuctions(999);
        assertEquals("Auction was not found after being added", matches.size(), 1);

        System.out.println("\n***Removing valid auction");
        assertTrue("Auction was not removed successfully", accessor.deleteAuction(validAuction));
        matches = accessor.getAuctions(999);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Auction was still found despite being removed", 0, matches.size());
        assertFalse("Deletion returned success despite earlier successful deletion", accessor.deleteAuction(validAuction));
    }

    @Test
    public void testInvalidAuctionMethodCalls() {
        List<Auction> matches;
        User user;
        Product newProduct;
        Calendar startDate;
        Calendar endDate;
        Auction invalidAuction;

        System.out.println("\n***Getting invalid auction");
        matches = accessor.getAuctions(10101010);
        assertNotNull("Matches should not be empty", matches);
        assertEquals("No auctions should have been found", 0, matches.size());

        System.out.println("\n***Getting auctions with null query");
        matches = accessor.getAuctions(null, null, null);
        assertNotNull("Matches should contain a List", matches);
        assertEquals("No matches should have been found", 0, matches.size());

        System.out.println("\n***Adding Auction with same listingID");
        user = new User("bgates", "qwerty123");
        newProduct = new Product("thing", "a thing", Category.COLLECTIBLES, "some/path.tar.gz");
        startDate = new GregorianCalendar(2020, 1, 1);
        endDate = new GregorianCalendar(2021, 1, 1);
        invalidAuction = new Auction(1001, user.getUsername(), startDate, endDate, AuctionType.ENGLISH, newProduct, new Dollar("5.00"));
        assertFalse("Auction was added despite having a duplicate ID", accessor.addAuction(invalidAuction));
        matches = accessor.getAuctions(1001);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should only have one Auction with id 1001", 1, matches.size());

        System.out.println("\n***Adding auction with invalid username");
        newProduct = new Product("thing", "a thing", Category.COLLECTIBLES, "picture.jpg");
        startDate = new GregorianCalendar(2020, 1, 1);
        endDate = new GregorianCalendar(2021, 1, 1);
        invalidAuction = new Auction(998, "idontexist", startDate, endDate, AuctionType.ENGLISH, newProduct, new Dollar("5.00"));
        assertFalse("Auction was added despite having a user not in DB", accessor.addAuction(invalidAuction));

        System.out.println("\n***Adding null auction");
        assertFalse("Auction was added despite being null", accessor.addAuction(null));

        System.out.println("\n***Removing fake auction");
        newProduct = new Product("thing", "a thing", Category.COLLECTIBLES, "picture.jpg");
        startDate = new GregorianCalendar(2020, 1, 1);
        endDate = new GregorianCalendar(2021, 1, 1);
        Auction fakeAuction = new Auction(999, "bgates", startDate, endDate, AuctionType.ENGLISH, newProduct, new Dollar("5.00"));
        assertFalse("Deletion should not have been successful", accessor.deleteAuction(fakeAuction));

        System.out.println("\n***Removing null auction");
        assertFalse("Deletion should not have been successful", accessor.deleteAuction(null));
    }

    @Test
    public void testValidBidMethodCalls() {
        List<Bid> matches;
        Bid newBid;

        System.out.println("\n***Get bids for valid auctionID where bids exist");
        matches = accessor.getBids(1001);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found 3 bids for auction 1001", 3, matches.size());
        for (Bid bid : matches) {
            assertEquals("Should have auctionId 1001", 1001, bid.getAuctionID());
        }

        System.out.println("\n***Get bids for valid auction where no one has bid yet");
        matches = accessor.getBids(1003);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found 0 bids for auction 1003", 0, matches.size());

        System.out.println("\n***Get bids for valid user");
        matches = accessor.getBids("alovelace");
        assertNotNull("Matches should not be null", matches);
        assertEquals("1 bid should have been found", 1, matches.size());
        for (Bid bid : matches) {
            assertEquals("Bid should be from alovelace", "alovelace", bid.getBidder());
        }

        System.out.println("\n***Get bids for valid user when they've never bid");
        matches = accessor.getBids("tfrancis");
        assertNotNull("Bid list should not be null", matches);
        assertEquals("No bids should have been found", 0, matches.size());

        System.out.println("\n***Add valid bid");
        newBid = new Bid("mhamilton", new Dollar("80.00"), 1003, new GregorianCalendar(2020, 8, 1));
        assertTrue("Bid was not successfully inserted", accessor.addBid(newBid));
        matches = accessor.getBids(1003);
        assertNotNull("List of bids should not be null", matches);
        assertTrue("Bid should have been added to auction 1003", matches.contains(newBid));

        System.out.println("\n***Remove existing bid");
        assertTrue("Should have successfully deleted bid", accessor.deleteBid(newBid));
        matches = accessor.getBids(1003);
        assertNotNull("List of bids should not be null", matches);
        assertFalse("Bid should no longer exist in db", matches.contains(newBid));
    }

    @Test
    public void testInvalidBidMethodCalls() {
        List<Bid> matches;
        Bid newBid;

        System.out.println("\n***Get bids for invalid auction");
        matches = accessor.getBids(997);
        assertNotNull("Matches should not be null", matches);
        assertEquals("0 bids should have been found", 0, matches.size());

        System.out.println("\n***Get bids for invalid user");
        matches = accessor.getBids("joe");
        assertNotNull("Bid list should not be null", matches);
        assertEquals("No bids should have been found", 0, matches.size());

        System.out.println("\n***Add bid with non-existent user");
        newBid = new Bid("whoami", new Dollar("80.00"), 1003, new GregorianCalendar(2020, 8, 1));
        assertFalse("Bid was allowed to be inserted despite fake user", accessor.addBid(newBid));
        matches = accessor.getBids(1003);
        assertNotNull("List of bids should not be null", matches);
        assertFalse("Bid should not have been added to auction 1003", matches.contains(newBid));

        System.out.println("\n***Add bid with invalid auction");
        newBid = new Bid("mhamilton", new Dollar("80.00"), 999, new GregorianCalendar(2020, 8, 1));
        assertFalse("Bid was allowed to be inserted despite fake user", accessor.addBid(newBid));
        matches = accessor.getBids(999);
        assertNotNull("List of bids should not be null", matches);
        assertEquals("No bids should should exist for auction 999", 0, matches.size());

        System.out.println("\n***Remove invalid bid");
        newBid = new Bid("mhamilton", new Dollar("123.00"), 1001, new GregorianCalendar(2020, 8, 1));
        assertFalse("Removal was successful despite bid not being in DB", accessor.deleteBid(newBid));
        matches = accessor.getBids(1001);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should still contain 3 bids", 3, matches.size());

        System.out.println("\n***Remove null bid");
        assertFalse("Deletion should not have been successful", accessor.deleteBid(null));
    }

    @Test
    public void testValidRatingMethodCalls() {
        List<Rating> matches;
        Rating newRating;

        System.out.println("\n***Getting rating given valid auction ID");
        matches = accessor.getRatings(1004);
        assertNotNull("Matches should not be null", matches);
        assertEquals("Should have found 1 rating", matches.size(), 1);
        assertEquals("Rating should belong to auction 1004", 1004, matches.get(0).getAuctionID());

        System.out.println("\n***Getting rating for auction with no rating");
        matches = accessor.getRatings(1001);
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", matches.size(), 0);

        System.out.println("\n***Getting ratings given by valid user");
        matches = accessor.getRatings("bgates");
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 1", 1, matches.size());
        for (Rating rating : matches) {
            assertEquals("Should be from bgates", "bgates", rating.getRatingFrom());
        }

        System.out.println("\n***Getting ratings given by valid user if they have none");
        matches = accessor.getRatings("tfrancis");
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 0, matches.size());

        System.out.println("\n***Adding valid rating");
        newRating = new Rating(1005, "ltorvalds", 3, "test rating");
        assertTrue("Rating was not successfully inserted", accessor.addRating(newRating));
        assertEquals("Rating was not found for auction 1005", 1, accessor.getRatings(1005).size());

        System.out.println("\n***Removing existing rating");
        assertTrue("Rating was not successfully removed", accessor.deleteRating(newRating));
        assertEquals("Rating was still found for auction 1005 after removal", 0, accessor.getRatings(1005).size());
    }

    @Test
    public void testInvalidRatingMethodCalls() {
        List<Rating> matches;
        Rating newRating;

        System.out.println("\n***Getting ratings given invalid auctionID");
        matches = accessor.getRatings(999888);
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", matches.size(), 0);

        System.out.println("\n***Getting ratings for non-existent user");
        matches = accessor.getRatings("nobody");
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 0, matches.size());

        System.out.println("\n***Getting ratings for null user");
        matches = accessor.getRatings(null);
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 0, matches.size());

        System.out.println("\n***Adding rating to invalid auction");
        newRating = new Rating(9998, "ltorvalds", 3, "test rating");
        assertFalse("Rating should not have been added", accessor.addRating(newRating));
        matches = accessor.getRatings(9998);
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 0, matches.size());

        System.out.println("\n***Adding rating with invalid username");
        newRating = new Rating(1005, "monalisa", 3, "good stuff");
        assertFalse("Rating should not have been added", accessor.addRating(newRating));
        matches = accessor.getRatings("monalisa");
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 0, matches.size());

        System.out.println("\n***Adding rating to auction with rating");
        newRating = new Rating(1006, "ltorvalds", 3, "test rating");
        assertFalse("Rating was inserted despite auction 1006 already having an associated rating", accessor.addRating(newRating));
        matches = accessor.getRatings(1006);
        assertNotNull("Ratings should not be null", matches);
        assertEquals("Ratings should be of size 0", 1, matches.size());
        assertFalse("Rating should not be in matches", matches.contains(newRating));

        System.out.println("\n***Adding null rating");
        assertFalse("Rating was inserted even though it's null", accessor.addRating(null));

        System.out.println("\n***Removing rating with invalid auctionID");
        newRating = new Rating(9998, "mhamilton", 5, "Literally the best thing I have ever seen");
        assertFalse("Rating was somehow removed despite not existing in DB", accessor.deleteRating(newRating));

        System.out.println("\n***Removing rating with wrong username");
        newRating = new Rating(1007, "mhamilton2", 5, "Literally the best thing I have ever seen");
        assertFalse("Rating was somehow removed despite not existing in DB", accessor.deleteRating(newRating));

        System.out.println("\n***Removing null rating");
        assertFalse("Deletion should not have been successful", accessor.deleteRating(null));
    }
}
