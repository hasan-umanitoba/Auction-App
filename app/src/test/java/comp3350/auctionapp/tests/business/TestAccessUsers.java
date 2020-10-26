package comp3350.auctionapp.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.tests.persistence.StubDataAccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestAccessUsers {
    private AccessUsers accessor;

    @Before
    public void initializeAccessor() {
        Services.shutDown();
        Services.createDataAccess(new StubDataAccess(Services.dbName));
        accessor = new AccessUsers();
    }

    @After
    public void closeDatabaseAccess() {
        Services.shutDown();
    }

    @Test
    public void testUserSearchHit() {
        User target = accessor.getUser("bgates");
        assertNotNull("User should have been found", target);
        assertTrue("Wrong user was returned", target.hasUsername("bgates"));
        System.out.println("***testUserSearchHit was successful***");
    }

    @Test
    public void testUserSearchMiss() {
        User target = accessor.getUser("literally nobody 45376e738r");
        assertNull("No user should have been found", target);
        System.out.println("***testUserSearchMiss was successful***");
    }

    @Test
    public void testUserSearchNull() {
        assertNull(accessor.getUser(null));
        System.out.println("***testUserSearchNull was successful***");
    }

    @Test
    public void testSetNullSessionUser() {
        assertTrue("Should have been successful", accessor.setSessionUser(null));
        System.out.println("***testSetNullSessionUser was successful***");
    }

    @Test
    public void testAddNullUser() {
        assertFalse("Should not have been successful", accessor.addUser(null));
        System.out.println("***testAddNullUser was successful***");
    }
    @Test
    public void testValidSessionUserFunctionality(){
        //Getting current session user
        String sessionUser = accessor.getSessionUser();
        assertNotNull("There should be a session user", sessionUser);
        assertEquals("Should be user ltorvalds", "ltorvalds", sessionUser);

        User bgates = accessor.getUser("bgates");
        assertTrue("Should have set bgates as the session user", accessor.setSessionUser(bgates));
        assertNotNull("Session user should not be null", accessor.getSessionUser());
        assertEquals("Session user should be bgates", bgates.getUsername(), accessor.getSessionUser());

        assertTrue("Should have set null as the session user", accessor.setSessionUser(null));
        assertEquals("Session user should be GUEST", "GUEST", accessor.getSessionUser());

        User newSessionUser = new User("ltorvalds", "qwerty123");
        assertTrue("Should have set ltorvalds as the session user", accessor.setSessionUser(newSessionUser));
        assertNotNull("Session user should not be null", accessor.getSessionUser());
        assertEquals("Session user should be ltorvalds", sessionUser, accessor.getSessionUser());

        //Deleting session user then adding them back
        assertTrue("Session user should have been deleted", accessor.deleteUser(newSessionUser));
        assertEquals("Should have GUEST session user", "GUEST", accessor.getSessionUser());
        assertFalse("Should not be able to set session user", accessor.setSessionUser(newSessionUser));
        assertTrue("Should have re-added session user", accessor.addUser(newSessionUser));
        assertTrue("Should now be able to set session user", accessor.setSessionUser(newSessionUser));
    }
    @Test
    public void testInvalidSessionUserFunctionality(){
        String sessionUser;
        User invalidUser = new User("Idontexist", "qwerty123");
        //Trying to set session user to invalid user
        assertFalse("Should not have changed session user", accessor.setSessionUser(invalidUser));
        sessionUser = accessor.getSessionUser();
        assertNotNull("Session user should not be null", sessionUser);
        assertEquals("Session user should be ltorvalds", "ltorvalds", sessionUser);

        //Trying to set session user to one with wrong password
        User userWrongPass = new User("bgates", "wrongpassword");
        assertFalse("Should not have changed session user", accessor.setSessionUser(userWrongPass));
        sessionUser = accessor.getSessionUser();
        assertNotNull("Session user should not be null", sessionUser);
        assertEquals("Session user should be ltorvalds", "ltorvalds", sessionUser);
    }
}
