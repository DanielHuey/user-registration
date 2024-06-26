package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.exception.*;
import org.pahappa.systems.registrationapp.models.User;

import java.util.Date;
import java.util.List;

public class UserService extends ServiceSkeleton {
    private final UserDAO userDAO;

    public UserService () {
        userDAO = new UserDAO();
    }

    public void registerUser(User user) throws Exception {
        validateUsername(user.getUsername());
        if (usernameExists(user.getUsername())) throw new UsernameException("This username has been taken already.");
        validateEmail(user.getEmail());
        if (emailExists(user.getEmail())) throw new EmailException("This email is already part of the system.");
        validateName(user.getFirstname());
        validateDateOfBirth(user.getDateOfBirth());
        userDAO.addUser(user);
    }

    /** Returns a soft list of users (where deleted = false)*/
    public List<User> getListOfUsers() {
        return getListOfUsers(true);
    }

    /** @param softList True: A list of users with the deleted flag set to false. False: All users in the table */
    public List<User> getListOfUsers(boolean softList) {
        return userDAO.getAllUsers(softList);
    }

    public User getUserByUsername(String username) throws UsernameException {
        validateUsername(username);
        return userDAO.getUserByUsername(username);
    }

    public boolean usernameExists(String username) throws UsernameException {
        return getUserByUsername(username) != null;
    }

    public User getUserByEmail(String email) throws EmailException {
        validateEmail(email);
        return userDAO.getUserByEmail(email);
    }

    public boolean emailExists(String email) throws EmailException {
        return getUserByEmail(email) != null;
    }

    public void deleteUser(String username) throws UsernameException {
        deleteUser(username, true);
    }

    public void deleteUser(String username, boolean softDelete) throws UsernameException {
        User user = getUserByUsername(username);
        if (user != null) userDAO.deleteUser(user, softDelete);
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
    }

    public boolean updateDetailsOfUser(String username, String firstname, String lastname, String newUserName, Date dateOfBirth) throws Exception {
        User user = getUserByUsername(username);
        if (user == null) return false;
        user.setFirstname(firstname != null ? validateName(firstname) : user.getFirstname());
        user.setLastname(lastname != null ? lastname : user.getLastname());
        user.setDateOfBirth(dateOfBirth != null ? validateDateOfBirth(dateOfBirth) : validateDateOfBirth(user.getDateOfBirth()));
        user.setUsername(newUserName != null ? validateUsername(newUserName) : user.getUsername());
        userDAO.updateUser(user);
        return true;
    }

    /* Validators */
    /**
     *
     */
    private void validateEmail(String email) throws EmailException {
        if (!email.matches("^[a-zA-Z0-9_.]*@[a-z]*\\.[a-z]*")) {
            throw new EmailException("Invalid characters in email.");
        }
    }
}
