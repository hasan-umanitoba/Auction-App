package comp3350.auctionapp.tests.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BusinessPersistenceSeamTests.class,
        DataAccessHSQLDBTest.class
})
public class IntegrationTests {
}
