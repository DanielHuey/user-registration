package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.models.User;

import java.util.Date;
import java.util.HashSet;

public class UserService {
    private final HashSet<User> users = new HashSet<>();

    public void registerUser(String firstname, String lastname, String username, Date dateOfBirth){
        if (checkIfUsernameExists(username)) return;
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setDateOfBirth(dateOfBirth);
        users.add(user);
    }

    public HashSet<User> getListOfUsers() {
        return users;
    }

    public User getUserByUsername(String username) {
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean checkIfUsernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    public void deleteAllUsers() {
        users.clear();
    }

    public void deleteUser(String username) {
        User user = getUserByUsername(username);
        if (user != null) users.remove(user);
    }

    public void updateDetailsOfUser(String username, String firstname, String lastname, String newUserName, Date dateOfBirth) {
        User user = getUserByUsername(username);
        if (user == null) return;
        users.remove(user);
        user.setFirstname(firstname != null ? firstname : user.getFirstname());
        user.setLastname(lastname != null ? lastname : user.getLastname());
        user.setDateOfBirth(dateOfBirth != null ? dateOfBirth : user.getDateOfBirth());
        user.setUsername(newUserName != null ? newUserName : user.getUsername());
        users.add(user);
    }
}
