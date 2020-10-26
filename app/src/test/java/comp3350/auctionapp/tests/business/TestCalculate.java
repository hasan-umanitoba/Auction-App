package comp3350.auctionapp.tests.business;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.business.Calculate;
import comp3350.auctionapp.objects.Rating;

import static org.junit.Assert.assertEquals;

public class TestCalculate {
    private List<Rating> ratings;

    @Test
    public void testOutputManyDaysRemaining() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 0),
                new GregorianCalendar(2020, 7, 31, 11, 12));
        assertEquals("91d", output);
    }

    @Test
    public void testOutputSmallDaysRemaining() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 0),
                new GregorianCalendar(2020, 5, 20, 11, 12));
        assertEquals("19d", output);
    }

    @Test
    public void testOutputHoursRemaining() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 0),
                new GregorianCalendar(2020, 5, 1, 12, 12));
        assertEquals("2h", output);
    }

    @Test
    public void testOutputMinutesRemaining() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 29),
                new GregorianCalendar(2020, 5, 1, 10, 59));
        assertEquals("0h 30m", output);
    }

    @Test
    public void testOutputSecondsRemaining() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 20, 1),
                new GregorianCalendar(2020, 5, 1, 10, 20, 11));
        assertEquals("0m 10s", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartAfterEnd() {
        Calculate.formatTimeDifference(
                new GregorianCalendar(2022, 5, 1, 10, 20, 1),
                new GregorianCalendar(2020, 5, 1, 10, 20, 11));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStart() {
        Calculate.formatTimeDifference(
                null,
                new GregorianCalendar(2020, 5, 1, 10, 20, 11));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEnd() {
        Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 5, 1, 10, 20, 11), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlotOfDays() {
        Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 1, 1, 1, 1, 1),
                new GregorianCalendar(2021, 1, 1, 1, 1, 1));
    }

    @Test
    public void testStartEndSame() {
        String output = Calculate.formatTimeDifference(
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 1, 1));
        assertEquals("DONE", output);
    }

    @Test
    public void testCalculateValidAverage() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        assertEquals("Should equal 3.0", 0, Double.compare(2.8, Calculate.ratingAverage(ratings)));
    }

    @Test
    public void testCalculateAverageOfZero() {
        ratings = new ArrayList<>();
        assertEquals("Should equal 0.0", 0, Double.compare(0.0, Calculate.ratingAverage(ratings)));
    }

    @Test
    public void testCalculateNullAverage() {
        assertEquals("Should equal 0.0", 0, Double.compare(0.0, Calculate.ratingAverage(null)));
    }

    @Test
    public void testCalculateValidPositivePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        assertEquals("Should equal 40", 40, Calculate.positiveRatingsAsPercent(ratings));
    }

    @Test
    public void testCalculateZeroPositivePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        assertEquals("Should equal 0", 0, Calculate.positiveRatingsAsPercent(ratings));
    }

    @Test
    public void testAllPositivePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        assertEquals("Should equal 100", 100, Calculate.positiveRatingsAsPercent(ratings));
    }

    @Test
    public void testPositivePercentEmptyList() {
        ratings = new ArrayList<>();
        assertEquals("Should equal 0", 0, Calculate.positiveRatingsAsPercent(ratings));
    }

    @Test
    public void testPositivePercentNull() {
        assertEquals("Should equal 0", 0, Calculate.positiveRatingsAsPercent(null));
    }

    @Test
    public void testCalculateValidNegativePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        assertEquals("Should equal 40", 40, Calculate.negativeRatingsAsPercent(ratings));
    }

    @Test
    public void testCalculateZeroNegativePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        assertEquals("Should equal 0", 0, Calculate.negativeRatingsAsPercent(ratings));
    }

    @Test
    public void testAllNegativePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 1, ""));
        assertEquals("Should equal 100", 100, Calculate.negativeRatingsAsPercent(ratings));
    }

    @Test
    public void testNegativePercentEmptyList() {
        ratings = new ArrayList<>();
        assertEquals("Should equal 0", 0, Calculate.negativeRatingsAsPercent(ratings));
    }

    @Test
    public void testNegativePercentNull() {
        assertEquals("Should equal 0", 0, Calculate.negativeRatingsAsPercent(null));
    }

    @Test
    public void testCalculateNeutralPositivePercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        assertEquals("Should equal 20", 40, Calculate.positiveRatingsAsPercent(ratings));
    }

    @Test
    public void testCalculateZeroNeutralPercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 1, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 2, ""));
        ratings.add(new Rating(1, "person", 4, ""));
        ratings.add(new Rating(1, "person", 5, ""));
        assertEquals("Should equal 0", 0, Calculate.neutralRatingsAsPercent(ratings));
    }

    @Test
    public void testAllNeutralPercent() {
        ratings = new ArrayList<>();
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        ratings.add(new Rating(1, "person", 3, ""));
        assertEquals("Should equal 100", 100, Calculate.neutralRatingsAsPercent(ratings));
    }

    @Test
    public void testNeutralPercentEmptyList() {
        ratings = new ArrayList<>();
        assertEquals("Should equal 0", 0, Calculate.neutralRatingsAsPercent(ratings));
    }

    @Test
    public void testNeutralPercentNull() {
        assertEquals("Should equal 0", 0, Calculate.neutralRatingsAsPercent(null));
    }
}