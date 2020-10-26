package comp3350.auctionapp.tests.objects;

import org.junit.Test;

import comp3350.auctionapp.objects.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestUser {
    @Test
    public void testCreateNewValidUser() {
        User validUser = new User("abcd123", "qwerty123");
        assertEquals("Should have same username", "abcd123", validUser.getUsername());
        assertEquals("Should have same password", "qwerty123", validUser.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyUsername() {
        new User("", "querty123");
        fail("New user should not have been created");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullUsername() {
        new User(null, "querty123");
        fail("New user should not have been created");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyPassword() {
        new User("abcd123", "");
        fail("New user should not have been created");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullPassword() {
        new User("abcd123", null);
        fail("New user should not have been created");
    }

    @Test
    public void testSetValidPassword() {
        User validUser = new User("abcd123", "qwerty123");
        validUser.setPassword("abcd");
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullPassword() {
        User validUser = new User("abcd123", "qwerty123");
        validUser.setPassword(null);
        fail("Password should never be null");
    }

    @Test
    public void testEqualUsers() {
        User user1 = new User("user1", "qwerty123");
        User user2 = new User("user1", "qwerty1234");
        assertEquals(user1, user2);
        assertEquals(user2, user1);
    }

    @Test
    public void testUnequalUsers() {
        User user1 = new User("user1", "qwerty123");
        User user2 = new User("user2", "qwerty1234");
        assertNotEquals(user1, user2);
        assertNotEquals(user2, user1);
    }

    @Test
    public void testUnequalWithNull() {
        assertNotEquals(new User("user1", "qwerty123"), null);
    }

    @Test
    public void testUsernameIs() {
        User user = new User("user1", "qwerty123");
        assertTrue(user.hasUsername("user1"));
        assertTrue(user.hasUsername("UsEr1"));
    }

    @Test
    public void testUsernameIsNot() {
        User user = new User("user1", "qwerty123");
        assertFalse(user.hasUsername("qwerty1234"));
        assertFalse(user.hasUsername(""));
        assertFalse(user.hasUsername(null));
    }

    @Test
    public void testPasswordIs() {
        User user = new User("user1", "qwerty123");
        assertTrue(user.hasPassword("qwerty123"));
    }

    @Test
    public void testPasswordIsNot() {
        User user = new User("user1", "qwerty123");
        assertFalse(user.hasPassword("qwerty1234"));
        assertFalse(user.hasPassword("qWeRty123"));
        assertFalse(user.hasPassword(null));
    }
}
