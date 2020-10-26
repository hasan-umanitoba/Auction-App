package comp3350.auctionapp.business;

import java.util.ArrayList;
import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.persistence.DataAccess;

import static java.util.Objects.requireNonNull;

public class AccessRatings {
    private DataAccess dataAccess;

    public AccessRatings() {
        dataAccess = Services.getDataAccessService();
    }

    public List<Rating> getRatings(int auctionID) {
        return dataAccess.getRatings(auctionID);
    }

    public List<Rating> getRatings(String user) {
        requireNonNull(user, "user can't be null!");
        return dataAccess.getRatings(user);
    }

    public boolean addRating(Rating rating) {
        return rating != null && dataAccess.addRating(rating);
    }

    public boolean deleteRating(Rating rating) {
        return rating != null && dataAccess.deleteRating(rating);
    }

    // get total ratings for a user
    public int getNumberOfRatings(String username) {
        List<Rating> ratings = dataAccess.getRatings(username);

        return ratings.size();
    }

    // get all ratings of a certain value attached to a user
    public List<Rating> getRatingByValue(String username, int value) {
        // Get all  ratings with a value  particular from auction
        List<Rating> ratings = dataAccess.getRatings(username);
        List<Rating> returnRatings = new ArrayList<>();

        for (Rating rating : ratings) {
            if (rating.getRatingValue() == value) {
                returnRatings.add(rating);
            }
        }
        return returnRatings;
    }

    // get all positive ratings attached to a user
    public List<Rating> getPositiveRatings(String username) {
        List<Rating> allRatings = dataAccess.getRatings(username);
        List<Rating> returnRating = new ArrayList<>();
        for (Rating rating : allRatings) {
            if (rating.isPositiveRating()) {
                returnRating.add(rating);
            }
        }
        return returnRating;
    }

    // get all negative ratings attached to a user
    public List<Rating> getNegativeRatings(String username) {
        List<Rating> allRatings = dataAccess.getRatings(username);
        List<Rating> returnRating = new ArrayList<>();
        for (Rating rating : allRatings) {
            if (rating.isNegativeRating()) {
                returnRating.add(rating);
            }
        }
        return returnRating;
    }

    // get all neutral ratings attached to a user
    public List<Rating> getNeutralRatings(String username) {
        List<Rating> allRatings = dataAccess.getRatings(username);
        List<Rating> returnRating = new ArrayList<>();
        for (Rating rating : allRatings) {
            if (rating.isNeutralRating()) {
                returnRating.add(rating);
            }
        }
        return returnRating;
    }
}
