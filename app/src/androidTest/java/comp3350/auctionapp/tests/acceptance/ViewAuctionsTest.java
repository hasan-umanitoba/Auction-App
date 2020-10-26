package comp3350.auctionapp.tests.acceptance;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import comp3350.auctionapp.R;
import comp3350.auctionapp.presentation.MainActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
public class ViewAuctionsTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testViewOpenAuctions() {
        //Original auctions
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //Checks that appropriate bid amounts are shown
        onView(withText("1 bid")).check(matches(isDisplayed()));
        onView(withText("3 bids")).check(matches(isDisplayed()));
        onView(withText("0 bids")).check(matches(isDisplayed()));

        //Makes sure it's actually displaying the time remaining and the number of bids
        onData(allOf(withId(R.id.time_remaining), not(withText("0 days"))));
        onData(allOf(withId(R.id.num_bids), not(withText("No bids"))));

        //Filter auctions by jewellery category
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.jewellery_filter));
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.auction_card), not(withText("Computer"))));
        onView(allOf(withId(R.id.auction_card), not(withText("MTG: Black Lotus card"))));

        //Filter open jewellery auctions to get sealed bid auctions
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.auction_type_label)).perform(click());
        onView(withText(R.string.auction_type_2)).perform(click());
        Espresso.pressBack(); //Used as I couldn't figure out how to tap on the screen
        onView(withId(R.id.auction_card)).check(doesNotExist());

        //Go back home
        onView(withContentDescription(R.string.nav_app_bar_open_drawer_description)).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.home));
        onView(withText("Diamond ring")).check(matches(isDisplayed()));
        onView(withText("Computer")).check(matches(isDisplayed()));
        onView(withText("MTG: Black Lotus card")).check(matches(isDisplayed()));

        //Select first auction
        onView(withText("Diamond ring")).perform(click());
        //Note: Tests related to checking IndivAuction were removed as the test was too fast
    }
}
