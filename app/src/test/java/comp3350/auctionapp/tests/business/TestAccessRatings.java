package comp3350.auctionapp.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessRatings;
import comp3350.auctionapp.objects.Rating;
import comp3350.auctionapp.tests.persistence.StubDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestAccessRatings {
    private AccessRatings accessor;

    @Before
    public void initializeAccessor() {
        Services.createDataAccess(new StubDataAccess(Services.dbName));
        accessor = new AccessRatings();
    }

    @After
    public void teardown() {
        Services.shutDown();
    }

    @Test
    public void testGetRatingsOfAuction() {
        List<Rating> ratings = accessor.getRatings(1004);
        assertNotNull("should not be null", ratings);
        assertEquals("should be 1 ", 1, ratings.size());
    }

    @Test
    public void testGetRatingWithInvalidAuctionId() {
        List<Rating> ratings = accessor.getRatings(100000009);
        assertNotNull("should not be null", ratings);
        assertEquals("Should be 0 ", 0, ratings.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetRatingWithNullUser() {
        accessor.getRatings(null);
        fail("Should not be able to get null reviews");
    }

    @Test
    public void testGetRatingsWithValidUsername() {
        List<Rating> ratings = accessor.getRatings("bgates");
        assertNotNull("should not be null", ratings);
        assertEquals("Should be 1", 1, ratings.size());
    }

    @Test
    public void testGetRatingsWithInvalidUser() {
        List<Rating> ratings = accessor.getRatings("pepsi");
        assertNotNull("should not be null", ratings);
        assertEquals("Should be 0 ", 0, ratings.size());
    }

    @Test
    public void testGetRatingsWithEmptyUser() {
        List<Rating> ratings = accessor.getRatings("");
        assertNotNull("should not be null", ratings);
        assertEquals("Should be 0 ", 0, ratings.size());
    }

    @Test
    public void testGetRatingValuesForUser() {
        List<Rating> ratingValues = accessor.getRatingByValue("bgates", 3);
        assertNotNull("Should not be null", ratingValues);
        assertEquals("should be 1", 1, ratingValues.size());
    }

    @Test
    public void testGetRatingValuesWithInvalidUser() {
        List<Rating> ratingValues = accessor.getRatingByValue("Dr Pepper himself", 4);
        assertNotNull("Should not be null", ratingValues);
        assertEquals("should be 0", 0, ratingValues.size());
    }

    @Test
    public void testGetRatingValuesWithInvalidValues() {
        List<Rating> ratingValues = accessor.getRatingByValue("alovelace", -2);
        assertNotNull("Should not be null", ratingValues);
        assertEquals("should be 0", 0, ratingValues.size());
    }

    @Test
    public void testGetRatingValuesWithEmptyUsername() {
        List<Rating> ratingValues = accessor.getRatingByValue("", 2);
        assertNotNull("Should not be null", ratingValues);
        assertEquals("should be 0", 0, ratingValues.size());
    }

    @Test
    public void testAddAndRemoveValidRating() {
        Rating rating = new Rating(1005, "ltorvalds", 5, "nice");
        assertTrue("Rating has been added successfully", accessor.addRating(rating));
        assertTrue("Rating should be deleted", accessor.deleteRating(rating));
    }

    @Test
    public void testDeleteNullRating() {
        assertFalse("Rating should be deleted", accessor.deleteRating(null));
    }

    @Test
    public void testAddNullRatings() {
        assertFalse(accessor.addRating(null));
    }

    @Test
    public void testAddRatingFromInvalidUser() {
        assertFalse(accessor.addRating(new Rating(1004, "avocadoman", 2, "faulty electronic")));
    }

    @Test
    public void testAddRatingToInvalidAuction() {
        assertFalse(accessor.addRating(new Rating(1000202, "bgates", 2, "doesnt exist")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRatingWithInvalidRatingValue() {
        accessor.addRating(new Rating(1004, "bgates", -1, "could be better"));
        accessor.addRating(new Rating(1004, "bgates", 6, "really good"));
        fail("Should not be able to add rating of invalid value ");
    }

    @Test
    public void testGetPositiveRatings() {
        List<Rating> positiveRatings = accessor.getPositiveRatings("mhamilton");
        assertNotNull("This should not be null", positiveRatings);
        assertEquals("This should be 1", 1, positiveRatings.size());
    }

    @Test
    public void testGetNegativeRatings() {
        List<Rating> negativeRatings = accessor.getNegativeRatings("alovelace");
        assertNotNull("This should not be null", negativeRatings);
        assertEquals("This should be 2", 2, negativeRatings.size());
    }

    @Test
    public void testGetNeutralRatings() {
        List<Rating> neutralRatings = accessor.getNeutralRatings("bgates");
        assertNotNull("This should not be null", neutralRatings);
        assertEquals("This should be 1 ", 1, neutralRatings.size());
    }

    @Test
    public void testGetNumberOfRatings() {
        assertEquals("This should be 1", 1, accessor.getNumberOfRatings("mhamilton"));
        assertEquals("This should be 0", 0, accessor.getNumberOfRatings("tfrancis"));
    }

    @Test
    public void testGetNumberOfRatingsWithInvalidUser() {
        assertEquals("This should be 0 ", 0, accessor.getNumberOfRatings("literally who"));
    }

    @Test
    public void testGetPositiveRatingsWithInvalidUser() {
        List<Rating> positiveRatings = accessor.getPositiveRatings("literally who");
        assertNotNull("This should not be null", positiveRatings);
        assertEquals("This should be 0 ", 0, positiveRatings.size());
    }

    @Test
    public void testGetNegativeRatingsWithInvalidUser() {
        List<Rating> negativeRatings = accessor.getNegativeRatings("literally who");
        assertNotNull("This should not be null", negativeRatings);
        assertEquals("This should be 0 ", 0, negativeRatings.size());
    }

    @Test
    public void testGetNeutralRatingsWithInvalidUser() {
        List<Rating> neutralRatings = accessor.getNeutralRatings("literally who");
        assertNotNull("This should not be null", neutralRatings);
        assertEquals("This should be 0 ", 0, neutralRatings.size());
    }

    @Test
    public void testGetPositiveRatingsWithEmpty() {
        List<Rating> neutralRatings = accessor.getPositiveRatings("");
        assertNotNull("This should not be null", neutralRatings);
        assertEquals("This should be 0 ", 0, neutralRatings.size());
    }

    @Test
    public void testGetNeutralRatingsWithEmpty() {
        List<Rating> neutralRatings = accessor.getNeutralRatings("");
        assertNotNull("This should not be null", neutralRatings);
        assertEquals("This should be 0 ", 0, neutralRatings.size());
    }

    @Test
    public void testGetNegativeRatingsWithEmpty() {
        List<Rating> neutralRatings = accessor.getNegativeRatings("");
        assertNotNull("This should not be null", neutralRatings);
        assertEquals("This should be 0 ", 0, neutralRatings.size());
    }
}
