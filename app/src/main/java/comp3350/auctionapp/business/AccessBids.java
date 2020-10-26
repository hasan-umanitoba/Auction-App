package comp3350.auctionapp.business;

import java.util.Iterator;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.persistence.DataAccess;

/**
 * AccessBids
 * Manages the adding and retrieving of Bids
 */
public class AccessBids {

    private DataAccess dataAccess;

    public AccessBids() {
        dataAccess = Services.getDataAccessService();
    }

    //Returns all bids for particular auction
    public List<Bid> getAuctionBids(int auctionID) {
        return dataAccess.getBids(auctionID);
    }

    //Gets all bids made by particular user
    public List<Bid> getUserBids(String username) {
        return dataAccess.getBids(username);
    }

    //Gets highest bid in an auction
    public Bid getHighestBid(int auctionID) {
        List<Bid> bids = dataAccess.getBids(auctionID);
        Iterator<Bid> bidsIter = bids.iterator();
        Bid highest = null;

        while (bidsIter.hasNext()) {
            Bid nextBid = bidsIter.next();

            if (highest == null || highest.compareBidAmounts(nextBid) < 0) {
                highest = nextBid;
            }
        }
        return highest;
    }

    public boolean addBid(Bid newBid) {
        return newBid != null && dataAccess.addBid(newBid);
    }

    // Note: will return false if auction is not present in database
    public boolean deleteBid(Bid deletedBid) {
        return deletedBid != null && dataAccess.deleteBid(deletedBid);
    }
}
