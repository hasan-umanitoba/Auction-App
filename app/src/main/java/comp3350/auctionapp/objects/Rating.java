package comp3350.auctionapp.objects;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.util.Objects.requireNonNull;

/**
 * Rating
 * Stores information about a single rating. Will be used in iteration 3.
 */
public class Rating {
    // Threshold that decides whether a rating is positive, negative or neutral
    private static final int RATING_THRESHOLD = 3;

    //Limits the Rating's value
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final int MAX_DESC_LENGTH = 1000;

    private int ratingValue;
    private String writtenReview;
    private String ratingTarget;
    private int auctionID;
    private Calendar date;

    public Rating(int auctionID, String ratingTarget, int value, String review) {
        validateConstruction(ratingTarget, value, review);

        this.ratingValue = value;
        this.writtenReview = review;
        this.ratingTarget = ratingTarget;
        this.auctionID = auctionID;
        date = new GregorianCalendar();
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public String getWrittenReview() {
        return writtenReview;
    }

    public String getRatingFrom() {
        return ratingTarget;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public boolean isPositiveRating() {
        return this.ratingValue > RATING_THRESHOLD;
    }

    public boolean isNegativeRating() {
        return this.ratingValue < RATING_THRESHOLD;
    }

    public boolean isNeutralRating() {
        return this.ratingValue == RATING_THRESHOLD;
    }

    public Calendar getDate() {
        //Should never be returning reference to actual date
        return (Calendar) date.clone();
    }

    private void validateConstruction(String ratingTarget, int value, String review) {
        requireNonNull(ratingTarget, "Rating must be directed at someone and not null!");
        requireNonNull(review, "Can't have a null review!");

        if (value < MIN_RATING || value > MAX_RATING) {
            throw new IllegalArgumentException("The rating should be between 1 - 5");
        } else if (ratingTarget.isEmpty()) {
            throw new IllegalArgumentException("'' is not a valid user");
        } else if (review.length() > MAX_DESC_LENGTH) {
            throw new IllegalArgumentException("Review must be shorter!");
        }
    }
}