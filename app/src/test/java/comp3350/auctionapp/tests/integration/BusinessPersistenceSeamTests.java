package comp3350.auctionapp.tests.integration;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessAuctions;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.business.AccessRatings;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.persistence.DataAccess;
import comp3350.auctionapp.tests.persistence.StubDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BusinessPersistenceSeamTests {

    private DataAccess rawAccess;

    @Before
    public void setup() {
        //Switch to the HSQL as needed
        Services.shutDown();
        Services.createDataAccess(new StubDataAccess(Services.dbName));
        rawAccess = Services.getDataAccessService();
        assertNotNull(rawAccess);
    }

    @Test
    public void testValidAccessUsers() {
        AccessUsers au;
        System.out.println("Starting Integration test of valid AccessUsers calls to persistence");

        au = new AccessUsers();

        //Get valid user from db
        User bgates = au.getUser("bgates");
        assertNotNull("bgates should have been found", bgates);
        assertEquals("User should be bgates", "bgates", bgates.getUsername());

        //Deleting existing user from db
        au.deleteUser(bgates);
        assertNull("Should no longer be in db", au.getUser("bgates"));

        //Re-add bgates
        assertTrue("bgates should have been added", au.addUser(bgates));
        assertTrue(rawAccess.getUsers("bgates").size() > 0);
        bgates = rawAccess.getUsers("bgates").get(0);
        assertNotNull("bgates should have been found", bgates);
        assertEquals("User should be bgates", "bgates", bgates.getUsername());

        Services.shutDown();
        System.out.println("Finished Integration test of valid AccessUsers calls to persistence");
    }

    @Test
    public void testInvalidAccessUsers() {
        AccessUsers au;
        User invalidUser;

        System.out.println("\nStarting Integration test of invalid AccessUsers calls to persistence");

        au = new AccessUsers();
        invalidUser = new User("Idontexist", "qwerty123");

        //Searching for invalid user
        assertNull("Should not have found user", au.getUser(invalidUser.getUsername()));

        //Adding existing user
        User newUser = new User("bgates", "qwerty123");
        assertFalse("Should not be able to add exactly the same user", au.addUser(newUser));

        //Adding null user
        assertFalse("Should not be able to add null user", au.addUser(null));

        //Deleting null user
        assertFalse("Should not be able to delete null user", au.deleteUser(null));

        //Deleting user that doesn't exist
        assertFalse("Should not be able to delete user that doesn't exist", au.deleteUser(invalidUser));

        Services.shutDown();
        System.out.println("Finished Integration test of invalid AccessUsers calls to persistence");
    }

    @Test
    public void testValidAccessBids() {
        AccessBids ab;
        Bid validBid;
        Bid restorePreviousState;
        boolean result;

        System.out.println("\nStarting Integration test of valid AccessBids calls to persistence");

        //new accessor
        ab = new AccessBids();

        //gets the highest current bid and checks that the value is 40 dollars both ways
        validBid = ab.getHighestBid(1001);
        restorePreviousState = validBid;
        assertEquals("$40.00", validBid.toString());
        assertEquals("$40.00", ab.getHighestBid(1001).toString());

        //deletes the highest bid and ensures gets done correctly
        assertTrue(ab.deleteBid(validBid));

        //since bid validbid has not been updated should still have 40 dollars while the new highest is now 30 dollars
        assertEquals("$40.00", validBid.toString());
        assertEquals("$30.00", ab.getHighestBid(1001).toString());

        //adds the 40 dollar bid once again and checks that it was done correctly
        result = ab.addBid(validBid);
        assertTrue(result);

        //ensures both the validbid and the actual highest bid have the same value
        assertEquals("$40.00", validBid.toString());
        assertEquals("$40.00", ab.getHighestBid(1001).toString());

        //delete the highest bid again and ensure done correctly
        result = ab.deleteBid(validBid);
        assertTrue(result);

        // now get the current highest bid after the delete happened and with a null in between to ensure state has not changed
        validBid = ab.getHighestBid(1001);

        //ensures that both valid bid and highest bid have that new deleted value update
        assertEquals("$30.00", validBid.toString());
        assertEquals("$30.00", ab.getHighestBid(1001).toString());

        //restores database to previous state
        result = ab.addBid(restorePreviousState);
        assertTrue(result);
        //shut down the services as needed
        Services.shutDown();
        System.out.println("Finished Integration test of valid AccessBids calls to persistence");
    }

    @Test
    public void testInvalidAccessBids() {
        AccessBids ab;
        Bid newBid;
        List<Bid> matches;

        System.out.println("\nStarting Integration test of invalid AccessBids calls to persistence");

        ab = new AccessBids();

        //Get bids for invalid auction
        matches = ab.getAuctionBids(99999);
        assertNotNull("Null returned", matches);
        assertEquals("Found matches", 0, matches.size());

        //Get bids for invalid user
        matches = ab.getUserBids("idontexist");
        assertNotNull("Null returned", matches);
        assertEquals("Found matches", 0, matches.size());

        //Get bids for empty user
        matches = ab.getUserBids("");
        assertNotNull("Null returned", matches);
        assertEquals("Found matches", 0, matches.size());

        //Get bids for null user
        matches = ab.getUserBids(null);
        assertNotNull("Null returned", matches);
        assertEquals("Found matches", 0, matches.size());

        //Trying to get invalid auctionid highest bid
        newBid = ab.getHighestBid(1);
        assertNull(newBid);

        //Add null bid
        assertFalse("Should not have added null", ab.addBid(null));

        //Add bid with bad username
        newBid = new Bid("idontexist", new Dollar("800.00"), 1001, new GregorianCalendar());
        assertFalse("Should not have added bid", ab.addBid(newBid));
        matches = ab.getAuctionBids(1001);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 3 bids", 3, matches.size());

        //Add bid with invalid auction id
        newBid = new Bid("idontexist", new Dollar("800.00"), 9292929, new GregorianCalendar());
        assertFalse("Should not have added bid", ab.addBid(newBid));
        matches = ab.getAuctionBids(9292929);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 0 bids", 0, matches.size());

        //Delete null bid
        assertFalse("Should not have deleted anything", ab.deleteBid(null));

        //Delete non-existent bid
        assertFalse("Should not have deleted anything", ab.deleteBid(newBid));

        Services.shutDown();
        System.out.println("Finished Integration test of invalid AccessBids calls to persistence");
    }

    @Test
    public void testValidAccessAuctions() {
        AccessAuctions aa;
        List<Auction> matches;
        Auction newAuction;

        System.out.println("Starting Integration test of valid AccessAuctions calls to persistence");

        aa = new AccessAuctions();

        //Get auctions of type
        //Adding a valid new auction.
        newAuction = new Auction(6872, "tfrancis", new GregorianCalendar(2020, 7, 1), new GregorianCalendar(2020, 9, 1), AuctionType.ENGLISH, new Product("TestProduct", "TestProduct", Category.ELECTRONICS, "coco3_system.jpg"), new Dollar("115.00"));
        assertTrue("new auction should be added", aa.addAuction(newAuction));

        //Deleting the new auction from the db
        assertTrue(aa.deleteAuction(newAuction));

        Services.shutDown();
        System.out.println("Finished Integration test of valid AccessAuctions calls to persistence");
    }

    @Test
    public void testInvalidAccessAuctions() {
        AccessAuctions aa;
        aa = new AccessAuctions();
        System.out.println("Starting Integration test of Invalid AccessAuctions calls to persistence");

        //Adding an invalid new auction with a seller that doesn't exist
        Auction invalidAuction = new Auction(1000, "Idontexist", new GregorianCalendar(2020, 7, 1), new GregorianCalendar(2020, 9, 1), AuctionType.ENGLISH, new Product("TestProduct", "TestProduct", Category.ELECTRONICS, "coco3_system.jpg"), new Dollar("115.00"));
        assertFalse("failure to add new auction", aa.addAuction(invalidAuction));

        //Retrieving auctions for a seller that doesn't exist
        assertTrue("Should return empty list of auctions", aa.getAuctions("Idontexist").isEmpty());

        //Adding existing auction
        Auction existingAuction = new Auction(1001, "bgates", new GregorianCalendar(2020, 7, 1), new GregorianCalendar(2020, 9, 1), AuctionType.ENGLISH, new Product("Computer", "This is a computer", Category.ELECTRONICS, "coco3_system.jpg"), new Dollar("5.00"));
        assertFalse("Should not be able to add exactly same auction", aa.addAuction(existingAuction));

        //Adding null auction
        assertFalse("Should not be able to add null Auction", aa.addAuction(null));

        //Deleting null auction
        assertFalse("Should not be able to delete null Auction", aa.deleteAuction(null));

        //Deleting auction that doesn't exist
        assertFalse("Should not be able to delete Auction that doesn't exist", aa.deleteAuction(invalidAuction));

        Services.shutDown();
        System.out.println("Finished Integration test of Invalid AccessAuctions operations to persistence");
    }

    @Test
    public void testValidAccessRating() {
        AccessRatings ar;
        List<Rating> matches;
        System.out.println("\nStarting Integration test of valid AccessRating calls to persistence");

        ar = new AccessRatings();

        //Get valid ratings from auction
        matches = ar.getRatings(1004);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found equal matches", rawAccess.getRatings(1004).size(), matches.size());

        //Get valid ratings of user
        matches = ar.getRatings("alovelace");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found equal matches", rawAccess.getRatings("alovelace").size(), matches.size());
        assertEquals("method numberOfRatings should return db rating count", rawAccess.getRatings("alovelace").size(), ar.getNumberOfRatings("alovelace"));

        //Get ratings by value and username
        matches = ar.getRatingByValue("alovelace", 2);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 1 match", 1, matches.size());

        //Get positive ratings
        matches = ar.getPositiveRatings("alovelace");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 1 match", 1, matches.size());

        //Get negative ratings
        matches = ar.getNegativeRatings("alovelace");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 2 matches", 2, matches.size());

        //Get neutral ratings
        matches = ar.getNeutralRatings("bgates");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 1 match", 1, matches.size());

        //Add valid rating
        Rating newRating = new Rating(1005, "mhamilton", 5, "Good");
        assertTrue("Should have been successful", ar.addRating(newRating));

        //Find new rating in db
        matches = rawAccess.getRatings(1005);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 1 rating", 1, matches.size());
        matches = rawAccess.getRatings("mhamilton");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 2 ratings", 2, matches.size());

        //Remove valid rating
        assertTrue("Should have been successful", ar.deleteRating(newRating));
        matches = rawAccess.getRatings(1005);

        //Checks that it was deleted
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 0 ratings", 0, matches.size());
        matches = rawAccess.getRatings("mhamilton");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 1 rating", 1, matches.size());

        Services.shutDown();
        System.out.println("Finished Integration test of valid AccessRatings operations to persistence");
    }

    @Test
    public void testInvalidAccessRatings() {
        AccessRatings ar;
        List<Rating> matches;
        System.out.println("\nStarting Integration test of invalid AccessRating to persistence");

        ar = new AccessRatings();

        //Get ratings given invalid auction id
        matches = ar.getRatings(999999);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have equal matches", rawAccess.getRatings(999999).size(), matches.size());

        //Get ratings given invalid user
        matches = ar.getRatings("idontexist321");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have equal matches", rawAccess.getRatings("idontexist321").size(), matches.size());

        //Get ratings given invalid username
        matches = ar.getRatingByValue("idontexist", 2);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 0", 0, matches.size());

        //Get ratings given invalid value
        matches = ar.getRatingByValue("alovelace", 90);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 0", 0, matches.size());

        //Get positive ratings from invalid user
        matches = ar.getPositiveRatings("");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 0 matches", 0, matches.size());

        //Get negative ratings from invalid user
        matches = ar.getNegativeRatings("");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 0 matches", 0, matches.size());

        //Get neutral ratings from invalid user
        matches = ar.getNeutralRatings("");
        assertNotNull("Should not be null", matches);
        assertEquals("Should have found 0 matches", 0, matches.size());

        //Add rating with invalid auction id
        assertFalse("Should not have added rating", ar.addRating(new Rating(9999, "mhamilton", 5, "good")));
        matches = rawAccess.getRatings(9999);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 0 matches", 0, matches.size());

        //Add rating with invalid target
        assertFalse("Should not have added rating", ar.addRating(new Rating(1005, "wronguser", 5, "good")));
        matches = rawAccess.getRatings(1005);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 0 matches", 0, matches.size());

        //Add null rating
        assertFalse("Should not have added rating", ar.addRating(null));

        //Delete invalid rating
        assertFalse("Should not have deleted rating", ar.deleteRating(new Rating(1005, "mhamilton", 5, "5/5 good")));
        matches = rawAccess.getRatings(1005);
        assertNotNull("Should not be null", matches);
        assertEquals("Should have 0 matches", 0, matches.size());

        //Delete null rating
        assertFalse("Should not have deleted rating", ar.deleteRating(null));
        Services.shutDown();
        System.out.println("Finished Integration test of invalid AccessRatings operations to persistence");
    }
}
