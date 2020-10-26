package comp3350.auctionapp.tests.objects;

import org.junit.Test;

import comp3350.auctionapp.objects.Dollar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestDollar {
    @Test
    public void testCreateValidDollar() {
        assertEquals("$5.00", new Dollar("5.00").toString());
    }

    @Test
    public void testCreateDollarNoDecimal() {
        assertEquals("$5.00", new Dollar("5").toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateInvalidNullDollar() {
        new Dollar(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidDollarEmptyStr() {
        new Dollar("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDollarWithInvalidString() {
        new Dollar("abcd");
    }

    @Test
    public void testCreateNegativeDollar() {
        assertEquals("-$10.00", new Dollar("-10.00").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        new Dollar("10.20.0");
    }

    @Test
    public void testRoundUp() {
        assertEquals("Should be $10.21", "$10.21", new Dollar("10.206").toString());
    }

    @Test
    public void testRoundDown() {
        assertEquals("Should be $10.21", "$10.21", new Dollar("10.206").toString());
    }

    @Test
    public void testEqualDollars() {
        Dollar dollar1 = new Dollar("10.00");
        Dollar dollar2 = new Dollar("10.00");
        assertEquals(dollar1, dollar2);
    }

    @Test
    public void testUnequalDollars() {
        Dollar dollar1 = new Dollar("10.00");
        Dollar dollar2 = new Dollar("10.01");
        assertNotEquals(dollar1, dollar2);
    }

    @Test
    public void testEqualToObject() {
        assertNotEquals(new Dollar("10.00"), new Object());
    }

    @Test
    public void testComparisonBetweenDollars() {
        Dollar dollar1 = new Dollar("10.00");
        Dollar dollar2 = new Dollar("10.01");
        Dollar dollar3 = new Dollar("10.00");
        assertTrue("Dollar1 < Dollar2", dollar1.compareTo(dollar2) < 0);
        assertTrue("Dollar1 > Dollar2", dollar2.compareTo(dollar1) > 0);
        assertEquals("Dollar3 == Dollar1", 0, dollar3.compareTo(dollar1));
        assertEquals("Dollar1 == Dollar3", 0, dollar1.compareTo(dollar3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullComparison() {
        new Dollar("10.00").compareTo(null);
    }

    @Test
    public void testDollarIsNegative() {
        assertTrue("Dollar should be negative", new Dollar("-10.00").isNegative());
    }

    @Test
    public void testDollarIsNotNegative() {
        assertFalse("Dollar should not be negative", new Dollar("10.00").isNegative());
    }

    @Test
    public void testIsZero() {
        assertTrue(new Dollar("0.00").isZero());
    }

    @Test
    public void testIsNotZero() {
        assertFalse(new Dollar("0.01").isZero());
    }

    @Test
    public void testGetBidAmount() {
        assertEquals("10.00", new Dollar("10.00").getBidAmount());
    }
}
