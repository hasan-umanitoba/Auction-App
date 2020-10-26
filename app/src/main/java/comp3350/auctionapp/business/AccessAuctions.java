package comp3350.auctionapp.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;
import comp3350.auctionapp.persistence.DataAccess;

import static java.util.Objects.requireNonNull;

/**
 * AccessAuctions
 * In charge of accessing and updating the Auctions of the app.
 * Verifies parameters are valid before sending them to the database
 */
public class AccessAuctions {
    private DataAccess dataAccess;

    public AccessAuctions() {
        dataAccess = Services.getDataAccessService();
    }

    // Accessor methods for auctions. Returns auctions filtered by given parameters.
    private List<Auction> getAuctions() {
        return dataAccess.getAuctions(null, null, "");
    }

    //Currently do not care about getting auctions by category, type AND seller
    public List<Auction> getAuctions(Category category, AuctionType type) {
        if (category == null && type == null) {
            return new ArrayList<>(); //Empty as we never want to return all auctions
        }
        return dataAccess.getAuctions(category, type, "");
    }

    //Gets all auctions related to particular user
    public List<Auction> getAuctions(String user) {
        requireNonNull(user, "User can't be null!");

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User can't be empty string!");
        }

        return dataAccess.getAuctions(null, null, user);
    }

    //Will return auctions who have not yet closed
    public List<Auction> getOpenAuctions(Calendar today) {
        return getOpenAuctions(today, null, null);
    }

    public List<Auction> getWonAuctions(Calendar today, String user) { // Returns auctions won by a user
        requireNonNull(today, "Today can't be null!");
        AccessBids bids = new AccessBids();
        AccessRatings ratings = new AccessRatings(); // make sure the won auction doesn't already have a rating
        List<Auction> closedAuctions = getClosedAuctions(today);
        List<Auction> auctionsWon = new ArrayList<>();

        if (user != null) {
            for (Auction nextAuction : closedAuctions) {
                Bid highestBid = bids.getHighestBid(nextAuction.getListingID());
                if (highestBid != null && highestBid.isBidder(user) && ratings.getRatings(nextAuction.getListingID()).size() == 0) {
                    auctionsWon.add(nextAuction);
                }
            }
        }

        return auctionsWon;
    }

    private List<Auction> getClosedAuctions(Calendar today) { // Returns auctions which are closed.
        requireNonNull(today, "Today can't be null!");
        List<Auction> allAuctions = getAuctions();
        List<Auction> auctionsClosed = new ArrayList<>();
        if (allAuctions != null) {
            for (Auction nextAuction : allAuctions) {
                if (nextAuction.compareEndDateTo(today) <= 0) {
                    auctionsClosed.add(nextAuction);
                }
            }
        }
        return auctionsClosed;
    }

    public List<Auction> getOpenAuctions(Calendar today, Category category, AuctionType type) {
        requireNonNull(today, "Today can't be null!");
        List<Auction> allAuctions = getAuctions();
        List<Auction> openAuctions = new ArrayList<>();

        if (allAuctions != null) {
            for (Auction nextAuction : allAuctions) {
                if (nextAuction.compareEndDateTo(today) > 0) {
                    if (category == null && type == null) {
                        openAuctions.add(nextAuction); //Just add
                    } else if (category != null && type != null) {
                        if (nextAuction.getProductCategory() == category && nextAuction.getAuctionType() == type) {
                            openAuctions.add(nextAuction); //Add if both category and type matches
                        }
                    } else if (category != null) {
                        if (nextAuction.getProductCategory() == category) {
                            openAuctions.add(nextAuction); ///Add if both category matches
                        }
                    } else {
                        if (nextAuction.getAuctionType() == type) {
                            openAuctions.add(nextAuction); //Add if type matches
                        }
                    }
                }
            }
        }

        return openAuctions;
    }

    // Returns whether an auction was added successfully
    public boolean addAuction(Auction newAuction) {
        return newAuction != null && dataAccess.addAuction(newAuction);
    }

    // Note: will return false if auction is not present in database
    public boolean deleteAuction(Auction deletedAuction) {
        return deletedAuction != null && dataAccess.deleteAuction(deletedAuction);
    }

    public int createAuctionID(Product auctionProduct, Calendar startDate, Calendar endDate, AuctionType type, Dollar minAmount) {
        //This hash method is good enough for our purposes
        return Objects.hash(auctionProduct, startDate, endDate, type, minAmount);
    }
}
