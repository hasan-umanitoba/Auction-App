package comp3350.auctionapp.tests.objects;

import org.junit.Test;

import java.util.Calendar;

import comp3350.auctionapp.objects.Rating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRating {
    @Test
    public void testCreateValidRating() {
        Rating newRating = new Rating(2010, "Bob", 2, "Ok experience");

        assertEquals("Should have auctionID 2010", 2010, newRating.getAuctionID());
        assertEquals("Should have been written by Bob", newRating.getRatingFrom(), "Bob");
        assertEquals("Should have rating of 2", 2, newRating.getRatingValue());
        assertEquals("Should have same written review", "Ok experience", newRating.getWrittenReview());
    }

    @Test(expected = NullPointerException.class)
    public void testNullRatingFrom() {
        new Rating(2010, null, 2, "Ok experience");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUsername() {
        new Rating(2010, "", 2, "Ok experience");
    }

    @Test(expected = NullPointerException.class)
    public void testNullReview() {
        new Rating(2010, "Bob", 2, null);
    }

    @Test
    public void testEmptyReview() {
        Rating newRating = new Rating(2010, "Bob", 2, "");
        assertEquals("Should have auctionID 2010", 2010, newRating.getAuctionID());
        assertEquals("Should have been written by Bob", newRating.getRatingFrom(), "Bob");
        assertEquals("Should have rating of 2", 2, newRating.getRatingValue());
        assertEquals("Should have same written review", "", newRating.getWrittenReview());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUser() {
        new Rating(2010, "", 2, "This is my review");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLowRating() {
        new Rating(2010, "Bob", 0, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooHighRating() {
        new Rating(2010, "Bob", 6, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateLongRatingReview() {
        String longReview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pharetra lacinia ultricies. Sed facilisis nec ipsum et lacinia. Pellentesque fermentum vitae magna eu venenatis. Nam efficitur risus tellus, quis consectetur ex dignissim a. Quisque nisl risus, tempus nec tempor nec, porttitor eu sem. Phasellus mollis eu felis at bibendum. Phasellus ut mauris imperdiet, feugiat magna vitae, bibendum nisl. Fusce nec turpis lorem. Pellentesque et tortor sit amet sapien ornare sodales eget sit amet ipsum. In tincidunt ante sed arcu tempor, et dictum velit sagittis.\n" +
                "\n" +
                "Proin scelerisque, felis vel varius tempus, metus leo accumsan turpis, eget tempus metus enim non purus. Aliquam bibendum diam eros, et pulvinar libero aliquam at. Nunc sed sodales purus. Ut tincidunt, augue vitae eleifend ultrices, nunc mauris rutrum velit, in dictum ante metus nec orci. Curabitur nec arcu tincidunt, tincidunt leo sit amet, ultrices ligula. Etiam non leo sit amet arcu vulputate posuere. Maecenas tincidunt libero at placerat facilisis. Donec quis nunc nisi.";
        new Rating(2010, "Bob", 2, longReview);
    }

    @Test
    public void testIsPositiveRating() {
        assertTrue(new Rating(2010, "Bob", 5, "Review").isPositiveRating());
    }

    @Test
    public void testIsNegativeRating() {
        assertTrue(new Rating(2010, "Bob", 1, "Review").isNegativeRating());
    }

    @Test
    public void testIsNeutralRating() {
        assertTrue(new Rating(2010, "Bob", 3, "Review").isNeutralRating());
    }

    @Test
    public void testModifyDate() {
        Rating newRating = new Rating(2010, "Bob", 5, "Review");
        Calendar ratingDate = newRating.getDate();
        Calendar ratingDateClone = (Calendar) ratingDate.clone();
        ratingDate.add(Calendar.WEEK_OF_MONTH, 2);
        assertEquals("Should be equal", ratingDateClone, newRating.getDate());
    }
}