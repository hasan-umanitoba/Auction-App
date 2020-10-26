package comp3350.auctionapp.tests.business;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAccessAuctions.class,
        TestAccessBids.class,
        TestAccessRatings.class,
        TestAccessUsers.class,
        TestCalculate.class
})
public class BusinessTests {
}
