package com.example.ecommerce;
import com.example.ecommerce.entity.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public int getUserId() { return currentUser != null ? (int) currentUser.getId() : -1; }
}
