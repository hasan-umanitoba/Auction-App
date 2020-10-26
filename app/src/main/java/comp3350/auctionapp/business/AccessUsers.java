package comp3350.auctionapp.business;

import java.util.List;

import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.objects.User;
import comp3350.auctionapp.persistence.DataAccess;

/**
 * AccessUsers
 * In charge of accessing and updating the users of the app.
 * Verifies requests are legit before sending them to the database
 */
public class AccessUsers {
    private User sessionUser;
    private DataAccess dataAccess;

    public AccessUsers() {
        dataAccess = Services.getDataAccessService();
        sessionUser = getUser("ltorvalds"); //Default user
    }

    // Methods responsible for setting and getting the current session user.
    public String getSessionUser() {
        if (sessionUser == null) {
            return "GUEST";
        }
        return sessionUser.getUsername();
    }

    // For now this is basic password string validation. Horrible security!
    public boolean setSessionUser(User newUser) {
        if (newUser != null) {
            User user = getUser(newUser.getUsername());

            if (user != null && user.hasPassword(newUser.getPassword())) {
                sessionUser = user;
                return true; //Logged in
            }

            return false; //Unable to log in
        }

        sessionUser = null;
        return true; //Logged out
    }

    // Returns first user that matches the username.
    public User getUser(String username) {
        if (username != null) {
            List<User> hits = dataAccess.getUsers(username);
            return hits.size() > 0 ? hits.get(0) : null;
        }
        return null;
    }

    //Adds valid users only
    public boolean addUser(User newUser) {
        if (newUser != null && getUser(newUser.getUsername()) == null) {
            return dataAccess.addUser(newUser);
        }

        return false; //newUser is null or already in db
    }

    //Checks the database and deletes if necessary
    public boolean deleteUser(User deleteUser) {
        if (deleteUser != null) {
            if (deleteUser.equals(sessionUser)) {
                sessionUser = null;
            }

            if (getUser(deleteUser.getUsername()) != null) {
                return dataAccess.deleteUser(deleteUser);
            }
        }
        return false;
    }
}