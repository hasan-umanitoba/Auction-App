package comp3350.auctionapp.tests.objects;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestBid {
    private Dollar validDollar;
    private Dollar negativeDollar;
    private Dollar zeroDollar;
    private Dollar otherValidNumber;

    @Before
    public void createDollarMock() {
        validDollar = mock(Dollar.class);
        negativeDollar = mock(Dollar.class);
        otherValidNumber = mock(Dollar.class);
        zeroDollar = mock(Dollar.class);

        when(validDollar.toString()).thenReturn("$20.00");
        when(validDollar.getBidAmount()).thenReturn("20.00");
        when(negativeDollar.isNegative()).thenReturn(true);
        when(zeroDollar.isZero()).thenReturn(true);
    }

    @Test
    public void testCreateValidBid() {
        Calendar today = new GregorianCalendar();
        Bid newBid = new Bid("bidder", validDollar, 1234, today);
        assertEquals("Should have same bidder", "bidder", newBid.getBidder());
        assertEquals("Should have same bid amount", "$20.00", newBid.toString());
        assertEquals("Should have same auctionID", 1234, newBid.getAuctionID());
        assertEquals("Should have same dates", today, newBid.getDate());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBidWithNullBidder() {
        new Bid(null, validDollar, 1234, new GregorianCalendar());
        fail("Should not be able to make a new bid with a null bidder");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBidWithNullAmount() {
        new Bid("bidder", null, 1234, new GregorianCalendar());
        fail("Should not be able to make a new bid with a null amount");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBidWithNegativeAmount() {
        new Bid("bidder", negativeDollar, 1234, new GregorianCalendar());
        fail("Should not be able to bid with a negative amount");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBidWithZeroAmount() {
        new Bid("bidder", zeroDollar, 1234, new GregorianCalendar());
        fail("Should not be able to bid with an amount of $0.00");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBidWithNullDate() {
        new Bid("bidder", null, 1234, null);
        fail("Should not be able to make a new bid with a null date");
    }

    @Test
    public void testEqualBids() {
        GregorianCalendar today = new GregorianCalendar();
        Bid newBid = new Bid("user123", validDollar, 123, today);
        Bid identicalBid = new Bid("user123", validDollar, 123, today);
        Bid uppercaseUser = new Bid("USER123", validDollar, 123, today);
        assertEquals("Should be newBid = identicalBid", newBid, identicalBid);
        assertEquals("Should be identicalBid = newBid", identicalBid, newBid);
        assertEquals("Should be newBid = uppercaseUser", newBid, uppercaseUser);
        assertEquals("Should be uppercaseUser = newBid", uppercaseUser, newBid);
    }

    @Test
    public void testUnequalBidders() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        Bid differentUsername = new Bid("user1234", validDollar, 123, new GregorianCalendar());
        assertNotEquals("newBid should not equal differentUsername", newBid, differentUsername);
        assertNotEquals("differentUsername should not equal newBid", differentUsername, newBid);
    }

    @Test
    public void testUnequalDollarAmounts() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        Bid differentBid = new Bid("user123", otherValidNumber, 123, new GregorianCalendar());
        //To note: This is really just testing that equals is called to compare Dollars
        assertNotEquals(newBid, differentBid);
        assertNotEquals(differentBid, newBid);
    }

    @Test
    public void testUnequalAuctionIDs() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        Bid differentBid = new Bid("user123", validDollar, 1234, new GregorianCalendar());
        assertNotEquals("newBid should not equal differentBid", newBid, differentBid);
        assertNotEquals("differentBid should not equal newBid", differentBid, newBid);
    }

    @Test
    public void testUnequalDates() {
        Bid oldBid = new Bid("username", validDollar, 4444, new GregorianCalendar(2000, 1, 1));
        Bid newBid = new Bid("username", validDollar, 4444, new GregorianCalendar());
        assertNotEquals("Should be unequal", oldBid, newBid);
        assertNotEquals("Should be unequal", newBid, oldBid);
    }

    @Test
    public void testNotEqualToObject() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        assertNotEquals(newBid, new Object());
    }

    @Test
    public void testCompareBidAmounts() {
        Bid bid1 = new Bid("user123", validDollar, 123, new GregorianCalendar());
        Bid bid2 = new Bid("user123", validDollar, 123, new GregorianCalendar());
        Bid bid3 = new Bid("user123", otherValidNumber, 123, new GregorianCalendar());
        bid1.compareBidAmounts(bid2);
        bid2.compareBidAmounts(bid1);
        verify(validDollar, times(2)).compareTo(validDollar);
        bid1.compareBidAmounts(bid3);
        bid3.compareBidAmounts(bid1);
        verify(validDollar).compareTo(otherValidNumber);
        verify(otherValidNumber).compareTo(validDollar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareBidAmountWithNull() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        newBid.compareBidAmounts(null);
        fail("Should not be able to compare against null");
    }

    @Test
    public void testCompareEqualBidDates() {
        GregorianCalendar today = new GregorianCalendar();
        Bid newBid = new Bid("user123", validDollar, 123, today);
        Bid newBid2 = new Bid("user1234", validDollar, 1123, today);
        assertEquals("Equal bid dates", 0, newBid.compareBidDates(newBid2));
    }

    @Test
    public void testCompareUnequalBidDates() {
        Bid oldBid = new Bid("username", validDollar, 4444, new GregorianCalendar(2000, 1, 1));
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        assertTrue("oldBid < newBid", oldBid.compareBidDates(newBid) < 0);
        assertTrue("newBid > oldBid", newBid.compareBidDates(oldBid) > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareBidDateWithNull() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        newBid.compareBidDates(null);
        fail("Should not be able to compare against null");
    }

    @Test
    public void testValidIsBidder() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        assertTrue("Username should be user123", newBid.isBidder("user123"));
        assertTrue("Username should be user123", newBid.isBidder("UsEr123"));
        assertFalse("Username should not be ''", newBid.isBidder(""));
    }

    @Test
    public void testIsBidderWithNull() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        assertFalse(newBid.isBidder(null));
    }

    @Test
    public void testIsAuctionWithValid() {
        Bid newBid = new Bid("user123", validDollar, 123, new GregorianCalendar());
        assertTrue(newBid.isAuction(123));
        assertFalse(newBid.isAuction(1234));
        assertFalse(newBid.isAuction(0));
        assertFalse(newBid.isAuction(-123));
    }

    @Test
    public void testModifyDate() {
        Calendar today = new GregorianCalendar();
        Calendar todayClone = (Calendar) today.clone();
        Bid newBid = new Bid("user123", validDollar, 123, today);
        newBid.getDate().add(Calendar.MONTH, 2);
        assertEquals("Should remain the same as before", todayClone, newBid.getDate());
    }

    @Test
    public void testGetBidAmount() {
        Calendar today = new GregorianCalendar();
        Bid newBid = new Bid("bidder", validDollar, 1234, today);
        assertEquals("20.00", newBid.getBidAmount());
    }
}
