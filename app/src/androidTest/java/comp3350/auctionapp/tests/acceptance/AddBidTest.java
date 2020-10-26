package comp3350.auctionapp.tests.acceptance;

import android.app.Activity;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import comp3350.auctionapp.R;
import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.presentation.MainActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class AddBidTest {
    @Rule
    public ActivityTestRule<MainActivity>  mainActivityActivityTestRule  = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void initializeAccessor() {
        Services.shutDown();
        Services.createDataAccess();
    }

    @Test
    public void testAddingBid(){
        //Original auctions
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //make the bid
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        // check if it the the right listing
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is a computer
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.amount_txt)).perform(typeText("60"));
        closeSoftKeyboard();
        onView(withId(R.id.SubmitBid)).perform(click());
        //check that the bid was added successfully buy changing the highest bid
        onView(withText("$60.00")).check(matches(isDisplayed()));
        // check the number of bids(since it is sealed bid then the amount must be greater than current highest bid
        onView(withText("4 bids")).check(matches(isDisplayed()));

        // test with a higher bid
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the diamond ring
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.amount_txt)).perform(typeText("90"));
        // close the keyboard
        closeSoftKeyboard();
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check that the bid was added successfully buy changing the highest bid
        onView(withText("$90.00")).check(matches(isDisplayed()));
        // check the number of bids(since it is sealed bid then the amount must be greater than current highest bid
        onView(withText("5 bids")).check(matches(isDisplayed()));

        // testing using the provided amounts
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is a computer
        onView(withText("Computer")).check(matches(isDisplayed()));
        // close the keyboard
        closeSoftKeyboard();
        onView(withId(R.id.basic_bid_3)).perform(click());
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check that the bid was added successfully buy changing the highest bid
        onView(withText("$500.00")).check(matches(isDisplayed()));
        // check the number of bids(since it is sealed bid then the amount must be greater than current highest bid
        onView(withText("6 bids")).check(matches(isDisplayed()));

        // make multiple bids of the same amount
        try {
            // wait for a little while
            Thread.sleep(2500);
        } catch (Exception e) {
            System.out.println("Couldn't Sleep because of " + e);
        }

        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the Computer
        onView(withText("Computer")).check(matches(isDisplayed()));
        // close the keyboard
        closeSoftKeyboard();
        onView(withId(R.id.basic_bid_3)).perform(click());
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("bid must be higher than current highest bid")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));

        // check that the bid was not added successfully by not changing the highest bid
        onView(withText("$500.00")).check(matches(isDisplayed()));
        // check the number of bids(should be the same as before)
        onView(withText("6 bids")).check(matches(isDisplayed()));

        // make an invalid  bids of the same amount
        try {
            // wait for a little while
            Thread.sleep(2500);
        } catch (Exception e) {
            System.out.println("Couldn't Sleep because of " + e);
        }

        onView(withText("Computer")).check(matches(isDisplayed()));

        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the Computer
        onView(withText("Computer")).check(matches(isDisplayed()));

        // close the keyboard(don't add a bid)
        closeSoftKeyboard();
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("bid amount cannot be empty")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //check that a bid wasn't added(highest bid still remains the same )
        onView(withText("$500.00")).check(matches(isDisplayed()));
        // check the number of bids(the number hasn't changed )
        onView(withText("6 bids")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddBidToSealed(){
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //make the bid
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the diamond ring
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.amount_txt)).perform( typeText("60"));
        // close the keyboard
        closeSoftKeyboard();
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("bid submitted")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        // check the number of bids(since it is sealed bid then the amount must be greater than current highest bid
        onView(withText("1 bid")).check(matches(isDisplayed()));

        // make a duplicate bid
        waitForToast();

        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the diamond ring
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.amount_txt)).perform( typeText("60"));
        closeSoftKeyboard();
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("Select a different bid amount")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        // check the number of bids(should be the same number of bids
        onView(withText("1 bid")).check(matches(isDisplayed()));

        // add a bid from  the suggested amount
        waitForToast();

        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the diamond ring
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        closeSoftKeyboard();
        onView(withId(R.id.basic_bid_3)).perform(click());
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("bid submitted")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        // check the number of bids(should be the same number of bids
        onView(withText("2 bids")).check(matches(isDisplayed()));

         //testing  with no values
        waitForToast();

        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        onView(withId(R.id.bid_button)).perform(click());
        // verify that it is the diamond ring
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));
        closeSoftKeyboard();
        // submit the bid
        onView(withId(R.id.SubmitBid)).perform(click());
        //check the toast
        onView(withText("bid amount cannot be empty")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        // check the number of bids(the number hasn't changed )
        onView(withText("2 bids")).check(matches(isDisplayed()));
    }

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }

    private void waitForToast(){
        try {
            Thread.sleep(2500);
        }catch (Exception e ){
            System.out.println("Couldn't Sleep because of " + e);
        }
    }
}
