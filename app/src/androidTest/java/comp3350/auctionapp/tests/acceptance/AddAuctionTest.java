package comp3350.auctionapp.tests.acceptance;

import android.app.Activity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import comp3350.auctionapp.R;
import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.presentation.MainActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class AddAuctionTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initializeAccessor() {
        Services.shutDown();
        Services.createDataAccess();
    }

    @Test
    public void addValidEnglishAuction() {
        //Original auctions
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //open the navigation drawer
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.new_auction));

        //fill in the auction information
        onView(withId(R.id.product_title)).perform(typeText("Distributed textbook"));
        onView(withId(R.id.auction_description)).perform(typeText("Distributed system textbook for comp3010 and Os"));

        //onView(withId(R.id.spinner_category)).perform(click());
        onView(withId(R.id.spinner_category)).perform(click());
        waitFor();
        onData(allOf(is(instanceOf(String.class)), is("Collectibles"))).perform(click());

        onView(withId(R.id.spinner_category)).check(matches(withSpinnerText(containsString("Collectibles"))));
        //chose radio button
        onView(withId(R.id.radio_english))
                .perform(click());
        onView(withId(R.id.radio_english))
                .check(matches(isChecked()));
        onView(withId(R.id.radio_sealed)).check(matches(isNotChecked()));

        //adding amount
        closeSoftKeyboard();
        onView(withId(R.id.starting_bid)).perform(typeText("70.00"));
        //choose number of days
        closeSoftKeyboard();

        onView(withId(R.id.spinner_days)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1"))).perform(click());
        onView(withId(R.id.spinner_days)).check(matches(withSpinnerText(containsString("1"))));
        //select and image
        waitFor();
        // scroll till you find the electronics radio button
        onView(withId(R.id.radio_electronic_image)).perform(customScrollTo, click());
        waitFor();
        onView(withId(R.id.radio_electronic_image)).perform(click());
        //check that the button is checked
        onView(withId(R.id.radio_electronic_image)).check(matches(isChecked()));
        //verify that the other button are not clicked
        onView(withId(R.id.radio_collectible_image)).check(matches(isNotChecked()));
        onView(withId(R.id.radio_jewellery_image)).check(matches(isNotChecked()));

        //submit the auction
        // scroll till u find the submit button
        onView(withId(R.id.next_button)).perform(customScrollTo, click());

        //check the toast
        onView(withText("Auction successfully created!")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        //check the invidual auction has the number of days and the test
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check if it the the right listing
        onView(withText("Distributed textbook")).check(matches(isDisplayed()));
        //number of bids is 0
        onView(withText("0 bids")).check(matches(isDisplayed()));
        //check the time
        onView(withText("23h")).check(matches(isDisplayed()));
    }

    @Test
    public void addValidSealedBid() {
        //open the navigation drawer
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.new_auction));

        //fill in the auction information
        onView(withId(R.id.product_title)).perform(typeText("Acer Chromebook"));
        onView(withId(R.id.auction_description)).perform(typeText("Acer Chromebook Spin 11 CP311-H-C5PN Convertible Laptop"));

        onView(withId(R.id.spinner_category)).perform(click());
        waitFor();
        onData(allOf(is(instanceOf(String.class)), is("Collectibles"))).perform(click());

        onView(withId(R.id.spinner_category)).check(matches(withSpinnerText(containsString("Collectibles"))));
        //chose radio button
        onView(withId(R.id.radio_sealed))
                .perform(click());
        onView(withId(R.id.radio_sealed))
                .check(matches(isChecked()));
        onView(withId(R.id.radio_english)).check(matches(isNotChecked()));

        //adding amount
        closeSoftKeyboard();
        onView(withId(R.id.starting_bid)).perform(typeText("890.00"));
        //choose number of days
        closeSoftKeyboard();

        onView(withId(R.id.spinner_days)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1"))).perform(click());
        onView(withId(R.id.spinner_days)).check(matches(withSpinnerText(containsString("1"))));
        //select and image
        waitFor();

        //check that the button is checked(is using default clicked collectible)
        onView(withId(R.id.radio_collectible_image)).check(matches(isChecked()));
        //verify that the other button are not clicked
        onView(withId(R.id.radio_electronic_image)).check(matches(isNotChecked()));
        onView(withId(R.id.radio_jewellery_image)).check(matches(isNotChecked()));

        //submit the auction
        // scroll till u find the submit button
        onView(withId(R.id.next_button)).perform(customScrollTo, click());

        //check the toast

        onView(withText("Auction successfully created!")).inRoot(withDecorView(not(is(getActivityInstance().getWindow().getDecorView())))).check(matches(isDisplayed()));
        //check the individual auction has the number of days and the test
        onView(withId(R.id.listings)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // check if it the the right listing
        onView(withText("Acer Chromebook")).check(matches(isDisplayed()));
        //number of bids is 0
        onView(withText("0 bids")).check(matches(isDisplayed()));
        //check the time
        onView(withText("23h")).check(matches(isDisplayed()));
    }

    @Test
    public void addEmptyAuction() {
        // add an empty auction
        //open the navigation drawer
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.new_auction));

        waitFor();
        // scroll till u find the submit button
        onView(withId(R.id.next_button)).perform(customScrollTo, click());
        waitFor();
        onView(withId(R.id.next_button)).check(matches(isDisplayed())); //Did not submit
    }

    private void waitFor() {
        try {
            Thread.sleep(3500);
        } catch (Exception e) {
            System.out.println("Couldn't Sleep because of " + e);
        }
    }

    ViewAction customScrollTo = new ViewAction() {

        @Override
        public Matcher<View> getConstraints() {
            return CoreMatchers.allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                    isAssignableFrom(ScrollView.class),
                    isAssignableFrom(HorizontalScrollView.class),
                    isAssignableFrom(NestedScrollView.class)))
            );
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public void perform(UiController uiController, View view) {
            new ScrollToAction().perform(uiController, view);
        }
    };

    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
}
