package comp3350.auctionapp.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessAuctions;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.tests.persistence.StubDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class TestAccessAuctions {
    private AccessAuctions accessor;

    @Before
    public void initializeAccessor() {
        Services.createDataAccess(new StubDataAccess(Services.dbName));
        accessor = new AccessAuctions();
    }

    @After
    public void teardown() {
        Services.shutDown();
    }

    @Test
    public void testGetAuctionsMatchingCategoryOnly() {
        List<Auction> auctions = accessor.getAuctions(Category.ELECTRONICS, null);
        assertNotNull("Should not be null", auctions);
        assertEquals("Should contain 1 auction", 1, auctions.size());
    }

    @Test
    public void testGetAuctionsMatchingTypeOnly() {
        List<Auction> auctions = accessor.getAuctions(null, AuctionType.ENGLISH);
        assertNotNull("Should not be null", auctions);
        assertEquals("Should contain 7 auctions", 7, auctions.size());
    }

    @Test
    public void testGetAuctionsWithNull() {
        List<Auction> auctions = accessor.getAuctions(null, null);
        assertNotNull("Should not be null", auctions);
        assertEquals("Should contain 0 auctions", 0, auctions.size());
    }

    @Test
    public void testAuctionSearchTypeCategory() {
        List<Auction> auctions = accessor.getAuctions(Category.ELECTRONICS, AuctionType.ENGLISH);
        assertNotNull("Auctions should have been found", auctions);
    }

    @Test
    public void testGetAuctionsGivenUsername() {
        List<Auction> auctions = accessor.getAuctions("bgates");
        assertNotNull("Should not be null", auctions);
        assertEquals("Should contain 2 auctions", 2, auctions.size());
    }

    @Test
    public void testGetAuctionsGivenInvalidUsername() {
        List<Auction> auctions = accessor.getAuctions("iamauser");
        assertNotNull("Should not be null", auctions);
        assertEquals("Should contain 0 auctions", 0, auctions.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAuctionsGivenNullUsername() {
        accessor.getAuctions(null);
        fail("Should not be able to get auctions with null username");
    }

    @Test
    public void testGetOpenEqualToStart() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 3", 3, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenWithFuture() {
        Calendar today = new GregorianCalendar(2020, 1, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 3", 3, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenInBetween() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 3", 3, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenEqualToEnd() {
        Calendar today = new GregorianCalendar(2020, 12, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 0", 0, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetOpenAuctionsWithNull() {
        accessor.getOpenAuctions(null);
        fail("Should not be able to return open auctions");
    }

    @Test
    public void testGetOpenWithCategory() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today, Category.COLLECTIBLES, null);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 1", 1, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenWithType() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today, null, AuctionType.ENGLISH);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 2", 2, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenWithCategoryAndType() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today, Category.JEWELLERY, AuctionType.ENGLISH);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 1", 1, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test
    public void testGetOpenWithNull() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today, null, null);
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 3", 3, auctions.size());

        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareStartDateTo(today) <= 0);
            assertTrue(nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetOpenWithFiltersNoToday() {
        accessor.getOpenAuctions(null, Category.JEWELLERY, AuctionType.ENGLISH);
    }

    @Test
    public void testGetOpenWithNoSpecifics() {
        Calendar today = new GregorianCalendar(2020, 7, 1);
        List<Auction> auctions = accessor.getOpenAuctions(today, null, null);
        assertNotNull("Should have returned a list of auctions", auctions);

        for (Auction nextAuction : auctions) {
            assertTrue("Auction should be open", nextAuction.compareStartDateTo(today) <= 0);
            assertTrue("Auction should be open", nextAuction.compareEndDateTo(today) > 0);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetOpenWithNullToday() {
        accessor.getOpenAuctions(null, null, null);
    }

    @Test
    public void testGetUserAuctionsWithValidUsername() {
        List<Auction> auctions = accessor.getAuctions("ltorvalds");
        assertNotNull("Result should not be null", auctions);
        assertEquals("Result should be of size 1", 1, auctions.size());
        assertEquals("Result should have id of 1002", 1002, auctions.get(0).getListingID());
    }

    @Test
    public void testGetUserAuctionsWithInvalidUser() {
        List<Auction> auctions = accessor.getAuctions("iamfake2020");
        assertNotNull("Result should not be null", auctions);
        assertEquals("Result should be of size 0", 0, auctions.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserAuctionsWithNull() {
        accessor.getAuctions(null);
        fail("Should not have gotten anything");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserAuctionsEmptyStr() {
        accessor.getAuctions("");
        fail("Should not have gotten anything");
    }

    @Test
    public void testAddAuctionWithInvalidUsername() {
        Calendar start = new GregorianCalendar(2020, 7, 20);
        Calendar end = new GregorianCalendar(2020, 7, 21);
        Product newProduct = new Product("my product", "my description", Category.JEWELLERY, "image");
        int auctionID = accessor.createAuctionID(newProduct, start, end, AuctionType.ENGLISH, new Dollar("0.00"));
        assertFalse("Auction should not have been added", accessor.addAuction(new Auction(auctionID, "123456789longandunique", start, end, AuctionType.ENGLISH, newProduct, new Dollar("0.00"))));
    }

    @Test
    public void testAddNewAuction() {
        Calendar start = new GregorianCalendar(2020, 7, 20);
        Calendar end = new GregorianCalendar(2020, 7, 21);
        Product newProduct = new Product("my product", "my description", Category.JEWELLERY, "image");
        int auctionID = accessor.createAuctionID(newProduct, start, end, AuctionType.ENGLISH, new Dollar("0.00"));
        Auction newAuction = new Auction(auctionID, "bgates", start, end, AuctionType.ENGLISH, newProduct, new Dollar("0.00"));

        assertTrue("Auction should have been added successfully", accessor.addAuction(newAuction));
    }

    @Test
    public void testRemoveAuction() {
        Auction auction = accessor.getAuctions("bgates").get(0);
        assertTrue("Auction should have been deleted", accessor.deleteAuction(auction));
    }

    @Test
    public void testAddExistingAuction() {
        assertFalse("Should not have been able to add existing", accessor.addAuction(accessor.getAuctions("bgates").get(0)));
    }

    @Test
    public void testAddNullAuction() {
        assertFalse("Should not have been able to add", accessor.addAuction(null));
    }

    @Test
    public void testDeleteNullAuction() {
        assertFalse("Should not have been able to add", accessor.deleteAuction(null));
    }

    @Test
    public void testGetWonEqualtoStart() {
        Calendar today = new GregorianCalendar(2019, 1, 1);
        List<Auction> auctions = accessor.getWonAuctions(today, "ltorvalds");
        assertTrue("List should be empty ", auctions.isEmpty());
        assertEquals("Should have returned a list of size 0", 0, auctions.size());
    }

    @Test
    public void testGetWonWithFuture() {
        Calendar today = new GregorianCalendar(2018, 1, 1);
        List<Auction> auctions = accessor.getWonAuctions(today, "ltorvalds");
        assertTrue("List should be empty ", auctions.isEmpty());
        assertEquals("Should have returned a list of size 0", 0, auctions.size());
    }

    @Test
    public void testGetWonInBetween() {
        Calendar today = new GregorianCalendar(2019, 5, 1);
        List<Auction> auctions = accessor.getWonAuctions(today, "ltorvalds");
        assertTrue("List should be empty ", auctions.isEmpty());
        assertEquals("Should have returned a list of size 0", 0, auctions.size());
    }

    @Test
    public void testGetWonEqualToEnd() {
        Calendar today = new GregorianCalendar(2019, 11, 1);
        List<Auction> auctions = accessor.getWonAuctions(today, "ltorvalds");
        AccessBids bids = new AccessBids();
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 2", 2, auctions.size());
        for (Auction nextAuction : auctions) {
            assertTrue(nextAuction.compareEndDateTo(today) <= 0);
            assertTrue(bids.getHighestBid(nextAuction.getListingID()).isBidder("ltorvalds"));
        }
    }

    @Test
    public void testGetWonEmptyUsername() {
        Calendar today = new GregorianCalendar(2019, 11, 1);
        List<Auction> matches = accessor.getWonAuctions(today, "");
        assertNotNull("Should not be null", matches);
        assertEquals("Should be of size 0", 0, matches.size());
    }

    @Test
    public void testGetWonNullUsername() {
        Calendar today = new GregorianCalendar(2019, 11, 1);
        List<Auction> matches = accessor.getWonAuctions(today, null);
        assertNotNull("Should not be null", matches);
        assertEquals("Should be of size 0", 0, matches.size());
    }

    @Test
    public void testGetWonInvalidUsername() {
        Calendar today = new GregorianCalendar(2019, 11, 1);
        List<Auction> auctions = accessor.getWonAuctions(today, "abcd");
        assertNotNull("Should have returned a list of auctions", auctions);
        assertEquals("Should have returned a list of size 0", 0, auctions.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWonAuctionsWithNull() {
        accessor.getWonAuctions(null, "ltorvalds");
        fail("Should not be able to return open auctions");
    }

    @Test
    public void testCreateAuctionId() {
        Product product = mock(Product.class);
        Calendar startDate = mock(Calendar.class);
        Calendar endDate = mock(Calendar.class);
        Dollar minAmount = mock(Dollar.class);
        int hash1 = accessor.createAuctionID(product, startDate, endDate, AuctionType.ENGLISH, minAmount);
        int hash2 = accessor.createAuctionID(product, startDate, endDate, AuctionType.ENGLISH, minAmount);
        assertEquals("Should have same hash value", hash1, hash2);
    }
}
