package comp3350.auctionapp.tests.objects;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestAuction {
    private Product product;
    private Dollar askingPrice;

    @Before
    public void setup() {
        product = mock(Product.class);
        askingPrice = mock(Dollar.class);

        when(product.getName()).thenReturn("My product");
        when(product.getDescription()).thenReturn("This is a description");
        when(product.getCategory()).thenReturn(Category.COLLECTIBLES);
        when(product.getImageFileName()).thenReturn("picture.jpg");
        when(askingPrice.getBidAmount()).thenReturn("10.00");
    }

    @Test
    public void testCreateValidAuction() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertEquals("Should have same seller", "bob123", newAuction.getSeller());
        assertEquals("Should have same auction type", AuctionType.ENGLISH, newAuction.getAuctionType());
        assertEquals("Should have same start date", new GregorianCalendar(2020, 1, 1), newAuction.getStartDate());
        assertEquals("Should have same end date", new GregorianCalendar(2020, 2, 2), newAuction.getEndDate());
        assertEquals("Should have same starting bid", askingPrice.toString(), newAuction.minBidAmountToString());
        assertEquals("Should have same auctionID", 1, newAuction.getListingID());
    }

    @Test
    public void testGetProductInfo() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        newAuction.getProductName();
        verify(product).getName();
        newAuction.getProductCategory();
        verify(product).getCategory();
        newAuction.getProductImage();
        verify(product).getImageFileName();
        newAuction.getProductDescription();
        verify(product).getDescription();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullSeller() {
        new Product("Title", "description", Category.COLLECTIBLES, "image");
        new Auction(
                1,
                null,
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullStartDate() {
        new Auction(
                1,
                "bob123",
                null,
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullEndDate() {
        new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                null,
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullAuctionType() {
        new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                null,
                product,
                askingPrice
        );

    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullProduct() {
        new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                null,
                askingPrice
        );
    }

    @Test(expected = NullPointerException.class)
    public void testCreateAuctionWithNullStartingBid() {
        new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullSeller() {
        new Auction(
                1,
                "",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAuctionWithStartAfterEnd() {
        new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 2, 2),
                new GregorianCalendar(2020, 1, 1),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
    }

    @Test
    public void testEqualAuctions() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        //Same reference to product and minBidAmount as the equals method does not check
        Auction newAuction2 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertEquals("Should be equal", newAuction1, newAuction2);
        assertEquals("Should be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalListingIDs() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                2,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalSellers() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                1,
                "steven",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalAuctionTypes() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.SEALEDBID,
                product,
                askingPrice
        );

        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalStartDates() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 2),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalEndDates() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                2,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 3),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalProducts() {
        Product otherProduct = mock(Product.class);

        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                2,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 3),
                AuctionType.ENGLISH,
                otherProduct,
                askingPrice
        );

        //Verifies that equals is called between Products
        assertNotEquals("Should not be equal", newAuction1, newAuction2);
        assertNotEquals("Should not be equal", newAuction2, newAuction1);
    }

    @Test
    public void testUnequalWithObject() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertNotEquals("Should not be equal", newAuction1, new Object());
    }

    @Test
    public void testCompareStartDatesAgainstEqual() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertEquals("newAuction1 start = newAuction2 start", 0, newAuction1.compareStartDates(newAuction2));
        assertEquals("newAuction1 start = newAuction2 start", 0, newAuction2.compareStartDates(newAuction1));
    }

    @Test
    public void testCompareStartDatesUnequal() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction oldAuction = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2010, 1, 2),
                new GregorianCalendar(2010, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction futureAuction = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2022, 1, 2),
                new GregorianCalendar(2022, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertTrue("newAuction after oldAuction", newAuction.compareStartDates(oldAuction) > 0);
        assertTrue("newAuction after oldAuction", oldAuction.compareStartDates(newAuction) < 0);
        assertTrue("newAuction before futureAuction", newAuction.compareStartDates(futureAuction) < 0);
        assertTrue("newAuction before futureAuction", futureAuction.compareStartDates(newAuction) > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareStartDatesWithNull() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        newAuction.compareStartDates(null);
        fail("Should not be able to compare with null");
    }

    @Test
    public void testCompareEndDatesAgainstEqual() {
        Auction newAuction1 = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction newAuction2 = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertEquals("newAuction1 end = newAuction2 end", 0, newAuction1.compareEndDates(newAuction2));
        assertEquals("newAuction1 end = newAuction2 end", 0, newAuction2.compareEndDates(newAuction1));
    }

    @Test
    public void testCompareEndDatesUnequal() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction ended = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2010, 1, 1),
                new GregorianCalendar(2010, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        Auction endingAfter = new Auction(
                2,
                "bob1234",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2030, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );

        assertTrue("newAuction after ended", newAuction.compareEndDates(ended) > 0);
        assertTrue("newAuction after ended", ended.compareEndDates(newAuction) < 0);
        assertTrue("newAuction before endingAfter", newAuction.compareEndDates(endingAfter) < 0);
        assertTrue("newAuction before endingAfter", endingAfter.compareEndDates(newAuction) > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareEndDatesWithNull() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        newAuction.compareEndDates(null);
        fail("Should not be able to compare with null");
    }

    @Test
    public void testCompareStartDateToEqual() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertEquals("Should be the same", 0, newAuction.compareStartDateTo(new GregorianCalendar(2020, 1, 1)));
    }

    @Test
    public void testCompareStartDateToOldDate() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertTrue("Should be newer than date", newAuction.compareStartDateTo(new GregorianCalendar(2010, 1, 1)) > 0);
    }

    @Test
    public void testCompareStartDateToFutureDate() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertTrue("Should be older than date", newAuction.compareStartDateTo(new GregorianCalendar(2030, 1, 1)) < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareStartDateToNull() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        newAuction.compareStartDateTo(null);
        fail("Should have not created a new auction");
    }

    @Test
    public void testCompareEndDateToEqual() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertEquals("Should be the same", 0, newAuction.compareEndDateTo(new GregorianCalendar(2020, 2, 2)));
    }

    @Test
    public void testCompareEndDateToOldDate() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertTrue("Should be newer than date", newAuction.compareEndDateTo(new GregorianCalendar(2010, 1, 1)) > 0);
    }

    @Test
    public void testCompareEndDateToFutureDate() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertTrue("Should be older than date", newAuction.compareEndDateTo(new GregorianCalendar(2030, 1, 1)) < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareEndDateToNull() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        newAuction.compareEndDateTo(null);
        fail("Should not be able to compare with null");
    }

    @Test
    public void testModifyStartDate() {
        Product newProduct = new Product("Title", "description", Category.COLLECTIBLES, "image");
        Dollar minBidAmount = new Dollar("0.00");
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                newProduct,
                minBidAmount
        );

        Calendar startDate = newAuction.getStartDate();
        startDate.set(Calendar.MONTH, 12);
        assertNotEquals(newAuction.getStartDate(), startDate);
        assertEquals(newAuction.getStartDate(), new GregorianCalendar(2020, 1, 1));
    }

    @Test
    public void testModifyEndDate() {
        Product newProduct = new Product("Title", "description", Category.COLLECTIBLES, "image");
        Dollar minBidAmount = new Dollar("0.00");
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                newProduct,
                minBidAmount
        );

        Calendar startDate = newAuction.getStartDate();
        startDate.set(Calendar.MONTH, 12);
        assertNotEquals(newAuction.getStartDate(), startDate);
        assertEquals(newAuction.getStartDate(), new GregorianCalendar(2020, 1, 1));
    }

    @Test
    public void testGetMinBidAmount() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        assertEquals("10.00", newAuction.getMinBidAmount());
    }

    @Test
    public void testCompareMinBidAmount() {
        Auction newAuction = new Auction(
                1,
                "bob123",
                new GregorianCalendar(2020, 1, 1),
                new GregorianCalendar(2020, 2, 2),
                AuctionType.ENGLISH,
                product,
                askingPrice
        );
        Dollar otherDollarAmt = mock(Dollar.class);
        newAuction.compareMinBidAmount(otherDollarAmt);
        verify(askingPrice).compareTo(otherDollarAmt); //Just cares if method called
        newAuction.compareMinBidAmount(null);
        verify(askingPrice).compareTo(null);
    }
}
