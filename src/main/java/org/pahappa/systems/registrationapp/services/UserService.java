package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.exception.DateException;
import org.pahappa.systems.registrationapp.exception.NameException;
import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class UserService {
    private final HashSet<User> users;
    private final UserDAO userDAO;
    private final int MIN_USERNAME_LENGTH = 4;
    private final int MAX_USERNAME_LENGTH = 16;
    private final int MIN_NAME_LENGTH = 2;

    public UserService () {
        users = new HashSet<>();
        userDAO = new UserDAO();
    }

    public void registerUser(User user) throws Exception {
        usernameExists(user.getUsername());
        validateName(user.getFirstname());
        validateUsername(user.getUsername());
        validateDateOfBirth(user.getDateOfBirth());
        userDAO.addUser(user);
    }

    public List<User> getListOfUsers() {
        List<User> userList = userDAO.getAllUsers();
        return userList;
    }

    public User getUserByUsername(String username) throws UsernameException {
        validateUsername(username);
        return userDAO.getUserByUsername(username);
    }

    public boolean usernameExists(String username) throws UsernameException {
        return getUserByUsername(username) != null;
    }
    
    public void deleteUser(String username) throws UsernameException {
        User user = getUserByUsername(username);
        if (user != null) userDAO.deleteUser(user);
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
    }

    public boolean updateDetailsOfUser(String username, String firstname, String lastname, String newUserName, Date dateOfBirth) throws Exception {
        User user = getUserByUsername(username);
        if (user == null) return false;
        users.remove(user);
        user.setFirstname(firstname != null ? validateName(firstname) : user.getFirstname());
        user.setLastname(lastname != null ? lastname : user.getLastname());
        user.setDateOfBirth(dateOfBirth != null ? validateDateOfBirth(dateOfBirth) : user.getDateOfBirth());
        user.setUsername(newUserName != null ? validateUsername(newUserName) : user.getUsername());
        userDAO.updateUser(user);
        return true;
    }

    /* Validators */
    /** Returns the validted Name */
    public String validateName(String name) throws NameException {
        if (name.length() < MIN_NAME_LENGTH) {
            throw new NameException("A name must have at least "+MIN_NAME_LENGTH+" letters.");
        } else if (!name.matches("^[a-zA-Z]*")) {
            throw new NameException("A name should have letters only");
        } else return name;
    }
    /** Returns the validated Username */
    public String validateUsername(String username) throws UsernameException {
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new UsernameException("Username is too short. Minimum is "+MIN_USERNAME_LENGTH+" characters.");
        } else if (username.length() > MAX_USERNAME_LENGTH) {
            throw new UsernameException("Username is too long. Maximum is "+MAX_USERNAME_LENGTH+" characters.");
        }
        if (!username.matches("^[a-zA-Z0-9][a-zA-Z0-9_]*")) {
            throw new UsernameException("Invalid characters in username. Usernames start with a letter, and can only allow letters, numbers and underscores.");
        }
        return username;
        
    }
    /** Returns a valid date */
    @SuppressWarnings("deprecation")
    public Date validateDateOfBirth(Date dateOfBirth) throws DateException {
        if (Calendar.getInstance().getTime().before(dateOfBirth)) {
            throw new DateException("You cannot be born in the future. Please choose another date.");                    
        } else if (dateOfBirth.before(new Date(0,1,1))) {
            throw new DateException("You cannot be born before 1900. Please choose another date.");                    
        }
        return dateOfBirth;
    }
    /** Returns a valid date */
    public Date validateDateOfBirth(String dateOfBirthString) throws Exception {
        if (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            throw new DateException("The date of birth must match the format dd/mm/yyyy.");
        }
        SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = dFormat.parse(dateOfBirthString);
        return validateDateOfBirth(dateOfBirth);
    }
}
