package comp3350.auctionapp.tests.integration;

import org.junit.Test;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.persistence.DataAccess;
import comp3350.auctionapp.persistence.HSQLDataAccess;
import comp3350.auctionapp.tests.persistence.DataAccessTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DataAccessHSQLDBTest {
    @Test
    public void testDataAccess() {
        DataAccess accessor;
        Services.shutDown();

        System.out.println("\nStarting DataAccess integration tests");
        accessor = Services.createDataAccess();
        DataAccessTests.testDataAccess(accessor);
        Services.shutDown();
        System.out.println("Finished DataAccess integration tests");
    }

    @Test
    public void testInvalidOpenAndClose() {
        //Tests when opening and closing is not valid
        DataAccess accessor;
        Services.shutDown();
        accessor = new HSQLDataAccess(Services.dbName);
        assertFalse(accessor.open(""));
        assertFalse(accessor.close());
    }

    @Test
    public void testCallWhenNotOpen() {
        DataAccess accessor;
        Services.shutDown();
        accessor = new HSQLDataAccess(Services.dbName);
        //Adding when not open
        assertNotNull(accessor.getBids(1001));
        assertNotNull(accessor.getBids("mhamilton"));
        assertNotNull(accessor.getAuctions(1001));
        assertNotNull(accessor.getUsers("mhamilton"));
        assertNotNull(accessor.getAuctions(Category.JEWELLERY, AuctionType.ENGLISH, ""));
        assertNotNull(accessor.getRatings(1004));
        assertNotNull(accessor.getRatings("bgates"));

        //Deleting when not open
        assertFalse(accessor.deleteRating(mock(Rating.class)));
        assertFalse(accessor.deleteBid(mock(Bid.class)));
        assertFalse(accessor.deleteUser(mock(User.class)));
        assertFalse(accessor.deleteAuction(mock(Auction.class)));
    }
}
