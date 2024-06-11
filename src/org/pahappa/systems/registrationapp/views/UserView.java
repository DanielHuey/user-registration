package org.pahappa.systems.registrationapp.views;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

public class UserView {

    private final Scanner scanner;
    private final UserService userService;
    private final int MIN_USERNAME_LENGTH = 4;
    private final int MAX_USERNAME_LENGTH = 16;
    private final int MIN_NAME_LENGTH = 2;

    public UserView(){
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
    }


    public void displayMenu() {
        System.out.println("********* User Registration System *********");
        boolean running = true;
        //seed a user (quick testing purposes)
        userService.registerUser("The", "Admin", "admin", new Date(100,1,1));

        while (running) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Register a user");
            System.out.println("2. Display all users");
            System.out.println("3. Get a user of username");
            System.out.println("4. Update user details of username");
            System.out.println("5. Delete User of username");
            System.out.println("6. Delete all users");
            System.out.println("7. Exit");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        displayAllUsers();
                        break;
                    case 3:
                        getUserOfUsername();
                        break;
                    case 4:
                        updateUserOfUsername();
                        break;
                    case 5:
                        deleteUserOfUsername();
                        break;
                    case 6:
                        deleteAllUsers();
                        break;
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }catch (Exception e){
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume the newline character
            }
        }
    }

    /** helper function for handling user retries */
    private boolean retry() {
        System.out.println("An error has been encountered with your inputs. Please hit r to try again.");
        String retry = scanner.nextLine().strip();
        return retry.toLowerCase().equals("r");
    }

    /** Removes all spaces from the input string */
    private String spacesCleaner(String input) {
        return input
        .replaceAll(" ","") //space
        .replaceAll(" ",""); // Alt+0160 chracter, looks like space
    }

    private void registerUser() {
        System.out.println("Provide the following details:");
        try {
            String firstname = "";
            System.out.println("First Name:");
            while (true) {
                firstname = spacesCleaner(scanner.nextLine().strip());
                if (firstname.length() < MIN_NAME_LENGTH) {
                    System.out.println("Name entered must have at least "+MIN_NAME_LENGTH+" letters. Please try again: ");
                } else if (!firstname.matches("^[a-zA-Z]*")) {
                    System.out.println("A name should have letters only");
                } else break;
            }
            System.out.println("Last Name:");
            String lastname = spacesCleaner(scanner.nextLine().strip()).replaceAll("[0-9]*","");

            String username = "";
            boolean usernameIsUnique = false;
            while (!usernameIsUnique){
                System.out.println("Username:");
                username = spacesCleaner(scanner.nextLine().strip());
                if (!username.matches("^[a-zA-Z0-9][a-zA-Z0-9_]*")) {
                    System.out.println("Invalid characters in username. Usernames start with a letter, and can only allow letters, numbers and underscores.");
                    continue;
                }
                if (username.length() >= MIN_USERNAME_LENGTH && username.length() <= MAX_USERNAME_LENGTH && !userService.checkIfUsernameExists(username)) {
                    usernameIsUnique = true;
                    continue;
                }
                if (username.length() < MIN_USERNAME_LENGTH) {
                    System.out.print("Username is too short. Minimum is "+MIN_USERNAME_LENGTH+" characters. Try another ");
                } else if (username.length() > MAX_USERNAME_LENGTH) {
                    System.out.print("Username is too long. Maximum is "+MAX_USERNAME_LENGTH+" characters. Try another ");
                } else System.out.print("This username is already in use. New "); 
            }

            boolean validDate = false;
            Date dateOfBirth = null;
            while (!validDate) {
                System.out.println("Date Of Birth (dd/mm/yyyy):");
                String dateOfBirthString = scanner.nextLine().strip();
                while (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    System.out.println("The date of birth must match the format dd/mm/yyyy. Please try again: ");
                    dateOfBirthString = scanner.nextLine().strip();
                }
                String[] dateData = dateOfBirthString.split("/");
                int year = Integer.parseInt(dateData[2]) - 1900;
                int month = Integer.parseInt(dateData[1]) - 1;
                int date = Integer.parseInt(dateData[0]);
                dateOfBirth = new Date(year,month,date);
                if (Calendar.getInstance().getTime().before(dateOfBirth)) {
                    System.err.println("You cannot be born in the future. Please choose another date.");                    
                } else if (dateOfBirth.before(new Date(0,1,1))) {
                    System.err.println("You cannot be born before 1900. Please choose another date.");                    
                } else {
                    validDate = true;
                }
            }
            userService.registerUser(firstname, lastname, username, dateOfBirth);
        } catch (Exception e) {
            if (retry()) registerUser();
        }
    }

    private void displayAllUsers() {
        HashSet<User> listOfUsers = userService.getListOfUsers();
        if (listOfUsers.isEmpty()) {
            System.out.println("The system doesn't have any users.");
            return;
        }
        System.out.println("FULL NAME\t\t\"USERNAME\"");
        for (User user: listOfUsers){
            System.out.println(user.getFirstname() + " " + user.getLastname() + " \t\t\"" + user.getUsername() + "\"");
        }
    }

    private void getUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = spacesCleaner(scanner.nextLine().strip());
            while (username.length() < MIN_USERNAME_LENGTH) {
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = spacesCleaner(scanner.nextLine().strip());
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("First Name: " + user.getFirstname());
            System.out.println("Last Name: " + user.getLastname());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Date Of Birth: " + user.getDateOfBirth().toString().replaceFirst("00:00:00 UTC ", ""));

        } catch (Exception e) {
            if (retry()) getUserOfUsername();
        }
    }

    /** returns a string or null */
    private String scanOrNull() {
        String input = scanner.nextLine().strip();
        if (input == "") return null;
        return input;
    }

    private void updateUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = spacesCleaner(scanner.nextLine().strip());
            while (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
                if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*")) System.out.println("Invalid characters in username. Usernames start with a letter, and can only allow letters, numbers and underscores.");
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = spacesCleaner(scanner.nextLine().strip());
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("Press Enter to keep the (prevoius) values.\n");
            System.out.println("First Name ("+user.getFirstname()+"):");
            String firstname = "";
            while (firstname != null) {
                firstname = scanOrNull();
                if (firstname == null) break;
                if (firstname.length() < MIN_NAME_LENGTH) {
                    System.out.println("Name entered must have at least "+MIN_NAME_LENGTH+" letters. Please try again: ");
                } else if (!firstname.matches("^[a-zA-Z]*")) {
                    System.out.println("A name should have letters only");
                } else break;
            }
            System.out.println("Last Name("+user.getLastname()+"):");
            String lastname = scanOrNull();

            String newUsername = null;
            boolean usernameIsUnique = false;
            while (!usernameIsUnique){
                System.out.println("Username("+user.getUsername()+"):");
                newUsername = scanOrNull();
                if (newUsername == null) break;
                if (!newUsername.matches("^[a-zA-Z0-9][a-zA-Z0-9_]*")) {
                    System.out.println("Invalid characters in username. Usernames start with a letter, and can only allow letters, numbers and underscores.");
                    continue;
                }
                if (newUsername.length() >= MIN_USERNAME_LENGTH && newUsername.length() <= MAX_USERNAME_LENGTH && !userService.checkIfUsernameExists(newUsername)) {
                    usernameIsUnique = true;
                    continue;
                }
                if (newUsername.length() < MIN_USERNAME_LENGTH) {
                    System.out.print("New username is too short. Minimum is "+MIN_USERNAME_LENGTH+" characters. Try another ");
                } else if (newUsername.length() > MAX_USERNAME_LENGTH) {
                    System.out.print("New username is too long. Maximum is "+MAX_USERNAME_LENGTH+" characters. Try another ");
                } else System.out.print("This username is already in use. New "); // Word loop
            }
            Date oldDate = user.getDateOfBirth();
            String dateOfBirthString = "";
            boolean validDate = false;
            Date dateOfBirth = null;
            outer: while (!validDate) {
                while (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    System.out.println("Date Of Birth ("+ oldDate.getDate() +"/"+ (oldDate.getMonth()+1) +"/"+ (oldDate.getYear()+1900) +"):");
                    dateOfBirthString = scanOrNull();
                    if (dateOfBirthString == null) {
                        break outer;
                    }
                    System.out.println("The date of birth must match the format dd/mm/yyyy. Please try again: ");
                }
                String[] dateData = dateOfBirthString.split("/");
                int year = Integer.parseInt(dateData[2]) - 1900;
                int month = Integer.parseInt(dateData[1]) - 1;
                int date = Integer.parseInt(dateData[0]);
                dateOfBirth = new Date(year,month,date);
                if (Calendar.getInstance().getTime().before(dateOfBirth)) {
                    System.err.println("You cannot be born in the future. Please choose another date.");                    
                } else if (dateOfBirth.before(new Date(0,1,1))) {
                    System.err.println("You cannot be born before 1900. Please choose another date.");                    
                } else {
                    validDate = true;
                }
            }
            if (userService.updateDetailsOfUser(username, firstname, lastname, newUsername, dateOfBirth)) {
                System.out.println("Details successfully recorded");
            } else {
                System.out.println("Did not find the user with username \""+username+"\". Maybe the details were edited by another administrator while you were also editing.");                
            }
        } catch (Exception e) {
            if (retry()) updateUserOfUsername();
        }
    }

    private void deleteUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = scanner.nextLine().strip();
            while (username.length() < MIN_USERNAME_LENGTH) {
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = scanner.nextLine().strip();
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            userService.deleteUser(username);
            System.out.println("Successfully deleted the user \"" + username + "\"");
        } catch (Exception e) {
            if (retry()) deleteUserOfUsername();
        }
    }

    private void deleteAllUsers() {
        System.out.println("Are you sure? This action is irreversible!  (y/n)");
        switch (scanner.nextLine().strip().toLowerCase()) {
            case "y":
                userService.deleteAllUsers();
                System.out.println("All users successfully deleted!");
                break;
            default:
                break;
        }
    }
}
