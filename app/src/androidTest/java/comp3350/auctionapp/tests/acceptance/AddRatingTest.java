package comp3350.auctionapp.tests.acceptance;

import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import comp3350.auctionapp.R;
import comp3350.auctionapp.presentation.MainActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class AddRatingTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testAddingRating() {

        //check the rating of the seller before adding our own
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //sleep to give viewer a chance to see that the rating is 2.7 to begin with
        try {
            // wait for a little while
            Thread.sleep(2500);
        }catch (Exception e ){
            System.out.println("Couldn't Sleep because of " + e);
        }
        //checks that it is 2.7 stars as well as it is on the right auction (lovelaces auction)
        onView(withText("2.7 STARS")).check(matches(isDisplayed()));
        onView(withText("alovelace")).check(matches(isDisplayed()));
        //goes back to homescreen
        pressBack();

        //check we made it back to home screen alright
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //goes to wonAuctions page
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.won_auction));

        //checks no longer on the main page only has the ones needing rating
        onView(allOf(withId(R.id.auction_card), not(withText("Diamond ring"))));
        onView(allOf(withId(R.id.auction_card), not(withText("Computer"))));
        onView(allOf(withId(R.id.auction_card), not(withText("MTG: Black Lotus card"))));

        //Click to get go to rating page and make a rating
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //clicks on setting 3 stars for user and opens keyboard
        onView(withId(R.id.rating_bar)).perform(click());
        onView(withId(R.id.user_review)).perform(click());

        //write review and close keyboard
        onView(withId(R.id.user_review)).perform( typeText("Good experience, seller has great communication! Would have liked faster delivery though."));
        closeSoftKeyboard();

        //submit
        onView(withId(R.id.submit_rating)).perform(click());

        //so now we want to check if the rating of the seller was updated in the entire system
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //sleep to give viewer a chance to see that the rating was updated from 2.7 to 2.8
        try {
            // wait for a little while
            Thread.sleep(2500);
        }catch (Exception e ){
            System.out.println("Couldn't Sleep because of " + e);
        }
        //checks that it is now 2.8 stars as well as it is on the right auction (lovelaces auction)
        onView(withText("2.8 STARS")).check(matches(isDisplayed()));
        onView(withText("alovelace")).check(matches(isDisplayed()));
    }
}
