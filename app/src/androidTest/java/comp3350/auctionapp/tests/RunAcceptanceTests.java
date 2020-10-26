package comp3350.auctionapp.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.auctionapp.tests.acceptance.AddAuctionTest;
import comp3350.auctionapp.tests.acceptance.AddBidTest;
import comp3350.auctionapp.tests.acceptance.AddRatingTest;
import comp3350.auctionapp.tests.acceptance.ViewAuctionsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ViewAuctionsTest.class,
        AddBidTest.class,
        AddRatingTest.class,
        AddAuctionTest.class,
})
public class RunAcceptanceTests {
}
