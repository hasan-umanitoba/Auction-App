package comp3350.auctionapp.tests.objects;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAuction.class,
        TestBid.class,
        TestDollar.class,
        TestProduct.class,
        TestRating.class,
        TestUser.class
})
public class ObjectTests {
}
