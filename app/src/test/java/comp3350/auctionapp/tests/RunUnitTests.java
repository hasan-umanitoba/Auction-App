package comp3350.auctionapp.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.auctionapp.tests.business.BusinessTests;
import comp3350.auctionapp.tests.objects.ObjectTests;
import comp3350.auctionapp.tests.persistence.PersistenceTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ObjectTests.class,
        BusinessTests.class,
        PersistenceTests.class
})

public class RunUnitTests {
}
