package comp3350.auctionapp.objects;

import java.io.Serializable;
import java.util.Calendar;

import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

import static java.util.Objects.requireNonNull;

/**
 * Auction
 * Stores information about a single Auction
 */
public class Auction implements Serializable {
    private int listingID;
    private AuctionType auctionType;
    private Dollar askingAmt;
    private String seller;
    private Product product;
    private Calendar start;
    private Calendar end;

    // Big constructor but everything is necessary
    public Auction(int listingID, String seller, Calendar startDate, Calendar endDate, AuctionType auctionType, Product product, Dollar askingAmt) {
        validateConstruction(seller, startDate, endDate, auctionType, product, askingAmt);

        this.listingID = listingID;
        this.seller = seller;
        this.start = startDate;
        this.end = endDate;
        this.auctionType = auctionType;
        this.product = product;
        this.askingAmt = askingAmt;
    }

    public int getListingID() {
        return listingID;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public Calendar getStartDate() {
        return (Calendar) start.clone();
    }

    public Calendar getEndDate() {
        return (Calendar) end.clone();
    }

    public String getSeller() {
        return seller;
    }

    public String minBidAmountToString() {
        return askingAmt.toString();
    }

    public String getMinBidAmount() {
        return askingAmt.getBidAmount();
    }

    public Category getProductCategory() {
        return product.getCategory();
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductDescription() {
        return product.getDescription();
    }

    @Override
    public boolean equals(Object otherAuction) {
        if (otherAuction instanceof Auction) {
            return this.listingID == ((Auction) otherAuction).listingID &&
                    this.seller.equals(((Auction) otherAuction).seller) &&
                    this.auctionType == ((Auction) otherAuction).auctionType &&
                    this.start.equals(((Auction) otherAuction).start) &&
                    this.end.equals(((Auction) otherAuction).end) &&
                    this.product.equals(((Auction) otherAuction).product);
        }
        return false;
    }

    public int compareEndDates(Auction other) {
        if (other == null) {
            throw new IllegalArgumentException("Other auction can't be null!");
        }
        return this.end.compareTo(other.end);
    }

    public int compareEndDateTo(Calendar otherDate) {
        if (otherDate == null) {
            throw new IllegalArgumentException("Must have other Date");
        }
        return this.end.compareTo(otherDate);
    }

    public int compareStartDates(Auction other) {
        if (other == null) {
            throw new IllegalArgumentException("Other auction can't be null!");
        }
        return this.start.compareTo(other.start);
    }

    public int compareStartDateTo(Calendar otherDate) {
        if (otherDate == null) {
            throw new IllegalArgumentException("Must have other Date");
        }
        return this.start.compareTo(otherDate);
    }

    public String getProductImage() {
        return product.getImageFileName();
    }

    private void validateConstruction(String seller, Calendar startDate, Calendar endDate, AuctionType auctionType, Product product, Dollar startingBid) {
        requireNonNull(seller, "Seller can't be null!");
        requireNonNull(startDate, "Start date can't be null!");
        requireNonNull(endDate, "End date can't be null!");
        requireNonNull(auctionType, "Auction type can't be null!");
        requireNonNull(product, "Product can't be null!");
        requireNonNull(startingBid, "Asking price can't be null");

        if (seller.isEmpty()) {
            throw new IllegalArgumentException("Seller can't be an empty string!");
        } else if (startDate.compareTo(endDate) > 0) {
            throw new IllegalArgumentException("Start date must occur before end date!");
        }
    }

    public int compareMinBidAmount(Dollar otherAmount) {
        return this.askingAmt.compareTo(otherAmount);
    }
}