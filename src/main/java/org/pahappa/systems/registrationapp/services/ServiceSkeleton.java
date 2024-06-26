package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.exception.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class ServiceSkeleton {

    /* Validators */
    /** @return The validated Name */
    public String validateName(String name) throws NameException {
        int MIN_NAME_LENGTH = 2;
        if (name.length() < MIN_NAME_LENGTH) {
            throw new NameException("A name must have at least "+ MIN_NAME_LENGTH +" letters.");
        } else if (!name.matches("^[a-zA-Z]*")) {
            throw new NameException("A name should have letters only");
        } else return name;
    }
    /** @return The validated Username */
    public String validateUsername(String username) throws UsernameException {
        int MIN_USERNAME_LENGTH = 4;
        int MAX_USERNAME_LENGTH = 16;
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new UsernameException("Username is too short. Minimum is "+ MIN_USERNAME_LENGTH +" characters.");
        } else if (username.length() > MAX_USERNAME_LENGTH) {
            throw new UsernameException("Username is too long. Maximum is "+ MAX_USERNAME_LENGTH +" characters.");
        }
        if (!username.matches("^[a-zA-Z0-9][a-zA-Z0-9_]*")) {
            throw new UsernameException("Invalid characters in username. Usernames start with a letter, and can only allow letters, numbers and underscores.");
        }
        return username;
    }
    /** @return A valid date */
    @SuppressWarnings("deprecation")
    public Date validateDateOfBirth(Date dateOfBirth) throws DateException {
        if (Calendar.getInstance().getTime().before(dateOfBirth)) {
            throw new DateException("You cannot be born in the future. Please choose another date.");
        } else if (dateOfBirth.before(new Date(0, Calendar.JANUARY,1))) {
            throw new DateException("You cannot be born before 1900. Please choose another date.");
        }
        return dateOfBirth;
    }
    /** @return A valid date */
    public Date validateDateOfBirth(String dateOfBirthString) throws Exception {
        if (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            throw new DateException("The date of birth must match the format dd/mm/yyyy.");
        }
        SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = dFormat.parse(dateOfBirthString);
        return validateDateOfBirth(dateOfBirth);
    }

    public void isQuitting(String input) throws ExitException {
        if (input.equalsIgnoreCase("n")) throw new ExitException();
    }
}
