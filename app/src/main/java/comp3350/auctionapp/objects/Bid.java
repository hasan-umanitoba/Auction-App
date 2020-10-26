package comp3350.auctionapp.objects;

import java.util.Calendar;

import static java.util.Objects.requireNonNull;

/**
 * Bid
 * Class that stores information about a single bid in an Auction
 */
public class Bid {
    private String bidder;
    private Dollar bidAmount;
    private int auctionID;
    private Calendar date;

    public Bid(String bidder, Dollar amount, int auctionID, Calendar date) {
        if (amount == null) {
            throw new NullPointerException("Amount can't be null");
        } else if (amount.isNegative()) {
            throw new IllegalArgumentException("Can't make a negative bid!");
        } else if (amount.isZero()) {
            throw new IllegalArgumentException("Can't make a bid of $0.00!");
        }
        this.bidAmount = amount;
        this.bidder = requireNonNull(bidder, "Bidder can't be null");
        this.auctionID = auctionID;
        this.date = requireNonNull(date);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Bid) {
            return bidder.toLowerCase().equals((((Bid) other).bidder.toLowerCase())) &&
                    bidAmount.equals(((Bid) other).bidAmount) &&
                    auctionID == ((Bid) other).auctionID &&
                    date.equals(((Bid) other).date);
        }
        return false;
    }

    @Override
    public String toString() {
        return bidAmount.toString();
    }

    public String getBidAmount() {
        return bidAmount.getBidAmount();
    }

    public String getBidder() {
        return bidder;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public Calendar getDate() {
        return (Calendar) date.clone();
    }

    public int compareBidAmounts(Bid otherBid) {
        if (otherBid == null) {
            throw new IllegalArgumentException("Other Bid should not be empty!");
        }
        return this.bidAmount.compareTo(otherBid.bidAmount);
    }

    public int compareBidDates(Bid otherBid) {
        if (otherBid == null) {
            throw new IllegalArgumentException("Other Bid should not be empty!");
        }
        return this.date.compareTo(otherBid.date);
    }

    public boolean isBidder(String username) {
        return username != null && this.bidder.equals(username.toLowerCase());
    }

    public boolean isAuction(int auctionID) {
        return this.auctionID == auctionID;
    }
}
