package comp3350.auctionapp.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.tests.persistence.StubDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestAccessBids {
    private AccessBids accessBids;

    @Before
    public void createAccessBids() {
        Services.createDataAccess(new StubDataAccess(Services.dbName));
        accessBids = new AccessBids();
    }

    @After
    public void teardown() {
        Services.shutDown();
    }

    @Test
    public void testGetAuctionBidsWithMultipleHits() {
        List<Bid> matchingAuctions = accessBids.getAuctionBids(1001);
        assertNotNull("Should not be null", matchingAuctions);
        assertEquals("Should have 3 hits", 3, matchingAuctions.size());
        assertEquals("Should be from user mhamilton", "mhamilton", matchingAuctions.get(0).getBidder());
        assertEquals("Should be from user alovelace", "alovelace", matchingAuctions.get(1).getBidder());
        assertEquals("Should be from user mhamilton", "mhamilton", matchingAuctions.get(2).getBidder());
    }

    @Test
    public void testGetAuctionBidsWithOneHit() {
        List<Bid> matchingAuctions = accessBids.getAuctionBids(1002);
        assertNotNull("Should not be null", matchingAuctions);
        assertEquals("Should have 1 hit", 1, matchingAuctions.size());
        assertEquals("Should be from user bgates", "bgates", matchingAuctions.get(0).getBidder());
    }

    @Test
    public void testGetAuctionWithZeroBids() {
        List<Bid> matchingAuctions = accessBids.getAuctionBids(1003);
        assertNotNull("Should not be null", matchingAuctions);
        assertEquals("Should have returned 0 bids", 0, matchingAuctions.size());
    }

    @Test
    public void testGetBidsFromInvalidAuction() {
        List<Bid> matchingAuctions = accessBids.getAuctionBids(1);
        assertNotNull("Result should not be null", matchingAuctions);
        assertEquals("Should have returned 0 bids", 0, matchingAuctions.size());
    }

    @Test
    public void testGetUserBidsWithMultipleHits() {
        List<Bid> matchingBids = accessBids.getUserBids("mhamilton");
        assertNotNull("Result should not be null", matchingBids);
        assertEquals("Result should have 4 hits", 4, matchingBids.size());
        // Make sure that we returned the right auctions
        List<Integer> matchesID = new ArrayList<>();
        for (Bid bid : matchingBids) {
            matchesID.add(bid.getAuctionID());
        }
        assertTrue("ID 1001 not found in returned auctions", matchesID.contains(1001));
        assertTrue("ID 1007 not found in returned auctions", matchesID.contains(1007));
        assertTrue("ID 1010 not found in returned auctions", matchesID.contains(1010));
    }

    @Test
    public void testGetUserBidsWithOneHit() {
        List<Bid> matchingAuctions = accessBids.getUserBids("alovelace");
        assertNotNull("Result should not be null", matchingAuctions);
        assertEquals("Result should have 1 hit", 1, matchingAuctions.size());
        assertEquals("Only bid should be from auction 1001", 1001, matchingAuctions.get(0).getAuctionID());
    }

    @Test
    public void testGetUserBidsWithZeroBids() {
        List<Bid> matchingAuctions = accessBids.getUserBids("tfrancis");
        assertNotNull("Result should not be null", matchingAuctions);
        assertEquals("Result should have 0 hits", 0, matchingAuctions.size());
    }

    @Test
    public void testGetUserBidsFromInvalidUser() {
        List<Bid> matchingAuctions = accessBids.getUserBids("invalid");
        assertNotNull("Result should not be null", matchingAuctions);
        assertEquals("Result should be of size 0", 0, matchingAuctions.size());
    }

    @Test
    public void testGetHighestBidOfMany() {
        Bid highestBid = accessBids.getHighestBid(1001);
        assertNotNull(highestBid);
        assertEquals("Should have amount of $40.00", "$40.00", highestBid.toString());
    }

    @Test
    public void testGetSingleHighestBid() {
        Bid highestBid = accessBids.getHighestBid(1002);
        assertNotNull(highestBid);
        assertEquals("Should have amount of $30.00", "$30.00", highestBid.toString());
    }

    @Test
    public void testGetHighestBidOfNoBids() {
        Bid highestBid = accessBids.getHighestBid(1003);
        assertNull(highestBid);
    }

    @Test
    public void testGetHighestBidOfInvalidAuction() {
        Bid highestBid = accessBids.getHighestBid(1);
        assertNull(highestBid);
    }

    @Test
    public void testAddValidBid() {
        Bid bid = new Bid("tfrancis", new Dollar("10.00"), 1001, new GregorianCalendar());
        // Will not be worrying about whether it's actually higher than the highest bid right now
        assertTrue("Bid should have been added", accessBids.addBid(bid));
    }

    @Test
    public void testRemoveValidBid() {
        assertTrue("Bid should have been removed", accessBids.deleteBid(accessBids.getHighestBid(1001)));
    }

    @Test
    public void testAddBidWithInvalidUsername() {
        assertFalse("Bid should not have been added", accessBids.addBid(new Bid("billygates", new Dollar("10.00"), 1001, new GregorianCalendar())));
    }

    @Test
    public void testAddBidWithInvalidAuctionID() {
        boolean addedBid = accessBids.addBid(new Bid("bgates", new Dollar("10.00"), 79, new GregorianCalendar()));
        assertFalse("Should not be able to add bids without valid id", addedBid);
    }

    @Test
    public void testAddNullBid() {
        assertFalse("Should not have added bid", accessBids.addBid(null));
    }
}
