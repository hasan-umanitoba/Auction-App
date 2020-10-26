package comp3350.auctionapp.objects;

import static java.util.Objects.requireNonNull;

/**
 * User
 * Holds information about a particular User
 */
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        //Check username
        if (username == null) {
            throw new NullPointerException("Null username!");
        } else if (username.isEmpty()) {
            throw new IllegalArgumentException("Empty username!");
        }

        //Check password
        if (password == null) {
            throw new NullPointerException("Null password!");
        } else if (password.isEmpty()) {
            throw new IllegalArgumentException("Empty password!");
        }
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = requireNonNull(password, "Password can't be null!");
    }

    public boolean hasUsername(String username) {
        return username != null && this.username.equals(username.toLowerCase());
    }

    //Bad security-wise but works for now
    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.username.equals(((User) obj).username);
        }
        return false;
    }
}