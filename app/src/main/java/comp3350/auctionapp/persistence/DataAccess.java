package comp3350.auctionapp.persistence;

import java.util.List;

import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

public interface DataAccess {
    // open database for use
    boolean open(String dbPathName);

    // close database
    boolean close();

    // return auctions, matching a desired category, type and seller.
    // if any of these fields are blank, they're treated as wildcards.
    //Blank refers to (null, null, "")
    List<Auction> getAuctions(Category category, AuctionType type, String sellerName);

    // return auction matching auctionID
    List<Auction> getAuctions(int auctionID);

    // return user matching username
    List<User> getUsers(String username);

    // return bids associated with an auctionID
    List<Bid> getBids(int auctionID);

    // return bids associated with a user
    List<Bid> getBids(String username);

    // return ratings associated with an auctionID
    List<Rating> getRatings(int auctionID);

    // return ratings associated with an auctionID
    List<Rating> getRatings(String username);

    // Adders
    boolean addAuction(Auction auction);

    boolean addUser(User user);

    boolean addBid(Bid bid);

    boolean addRating(Rating rating);

    // Removers
    boolean deleteAuction(Auction auction);

    boolean deleteUser(User user);

    boolean deleteBid(Bid bid);

    boolean deleteRating(Rating rating);
}
