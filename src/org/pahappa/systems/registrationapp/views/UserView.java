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

    private void registerUser() {
        System.out.println("Provide the following details:");
        try {
            System.out.println("First Name:");
            String firstname = scanner.nextLine().trim();
            while (firstname.length() < MIN_NAME_LENGTH) {
                System.out.println("Name entered must have at least "+MIN_NAME_LENGTH+" letters. Please try again: ");
                firstname = scanner.nextLine().trim();
            }
            System.out.println("Last Name:");
            String lastname = scanner.nextLine().trim();
            while (lastname.length() < MIN_NAME_LENGTH) {
                System.out.println("Name entered must have at least "+MIN_NAME_LENGTH+" letters. Please try again: ");
                lastname = scanner.nextLine().trim();
            }

            String username = "";
            boolean usernameIsUnique = false;
            while (!usernameIsUnique){
                System.out.println("Username:");
                username = scanner.nextLine().trim();
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

            System.out.println("Date Of Birth (dd/mm/yyyy):");
            String dateOfBirthString = scanner.nextLine().trim();
            while (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                System.out.println("The date of birth must match the format dd/mm/yyyy. Please try again: ");
                dateOfBirthString = scanner.nextLine().trim();
            }
            String[] dateData = dateOfBirthString.split("/");
            int year = Integer.parseInt(dateData[2]) - 1900;
            while (year > (Calendar.getInstance().get(Calendar.YEAR) - 1900)) {
                System.out.println("You cannot be born in the future! Plese provide a different year:");
                year = scanner.nextInt();
            }
            int month = Integer.parseInt(dateData[1]) - 1;
            while (month > 11 || month < 0) {
                System.out.println("A month cannot be less than 1 or more than 12. Please provide a different month:");
                month = scanner.nextInt() - 1;
            }
            int date = Integer.parseInt(dateData[0]);
            while (date > (month == 1 ? (year%4 == 0 ? 28 : 29) : 31) || date < 1) {
                System.out.println("A day cannot be less than 1 or more than "+(month == 1 ? (year%4 == 0 ? 28 : 29) : 31)+". Please provide a different day:");
                date = scanner.nextInt();
            }
            Date dateOfBirth = new Date(year,month,date);
            userService.registerUser(firstname, lastname, username, dateOfBirth);
        } catch (Exception e) {
            System.err.println("An error has been encountered with your inputs. Please hit r to try again.");
            String retry = scanner.nextLine().strip();
            if (retry.toLowerCase().equals("r")) {
                registerUser();
            }
        }
    }

    private void displayAllUsers() {
        HashSet<User> listOfUsers = userService.getListOfUsers();
        if (listOfUsers.isEmpty()) {
            System.out.println("The system doesn't have any users.");
            return;
        }
        System.out.println("FULL NAME\t\t@ USERNAME");
        for (User user: listOfUsers){
            System.out.println(user.getFirstname() + " " + user.getLastname() + " \t@ " + user.getUsername());
        }
    }

    private void getUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = scanner.nextLine().trim();
            while (username.length() < MIN_USERNAME_LENGTH) {
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = scanner.nextLine().trim();
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("First Name: " + user.getFirstname());
            System.out.println("Last Name: " + user.getLastname());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Date Of Birth: " + user.getDateOfBirth());

        } catch (Exception e) {
            System.err.println("An error has been encountered with your inputs. Please hit r to try again.");
            String retry = scanner.nextLine().strip();
            if (retry.toLowerCase().equals("r")) {
                getUserOfUsername();
            }
        }
    }

    private String scanOrNull() {
        String input = scanner.nextLine().trim();
        if (input == "") return null;
        return input;
    }

    private void updateUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = scanner.nextLine().trim();
            while (username.length() < MIN_USERNAME_LENGTH) {
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = scanner.nextLine().trim();
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("Press Enter to keep the prevoius values.\n");
            System.out.println("First Name ("+user.getFirstname()+"):");
            String firstname = scanOrNull();
            System.out.println("Last Name("+user.getLastname()+"):");
            String lastname = scanOrNull();

            String newUsername = null;
            boolean usernameIsUnique = false;
            while (!usernameIsUnique){
                System.out.println("Username("+user.getUsername()+"):");
                newUsername = scanOrNull();
                if (newUsername == null || (newUsername.length() >= MIN_USERNAME_LENGTH && newUsername.length() <= MAX_USERNAME_LENGTH && !userService.checkIfUsernameExists(newUsername))) {
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
            System.out.println("Date Of Birth ("+ oldDate.getDate() +"/"+ (oldDate.getMonth()+1) +"/"+ (oldDate.getYear()+1900) +"):");
            String dateOfBirthString = scanOrNull();
            Date dateOfBirth = null;
            while (dateOfBirthString != null) {
                if (!dateOfBirthString.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    System.out.println("The date of birth must match the format dd/mm/yyyy. Please try again: ");
                    dateOfBirthString = scanOrNull();
                    if (dateOfBirthString == null) break;
                    else continue;
                }
                String[] dateData = dateOfBirthString.split("/");
                int year = Integer.parseInt(dateData[2]) - 1900;
                while (year > Calendar.getInstance().get(Calendar.YEAR)) {
                    System.out.println("You cannot be born in the future! Please provide a different year:");
                    year = scanner.nextInt();
                }
                int month = Integer.parseInt(dateData[1]) - 1;
                while (month > 11 || month < 0) {
                    System.out.println("A month cannot be less than 1 or more than 12. Please provide a different month:");
                    month = scanner.nextInt() - 1;
                }
                int date = Integer.parseInt(dateData[0]);
                while (date > (month == 1 ? (year%4 == 0 ? 28 : 29) : 31) || date < 1) {
                    System.out.println("A day cannot be less than 1 or more than "+(month == 1 ? (year%4 == 0 ? 28 : 29) : 31)+". Please provide a different day:");
                    date = scanner.nextInt();
                }
                dateOfBirth = new Date(year,month,date);
            }
            if (userService.updateDetailsOfUser(username, firstname, lastname, newUsername, dateOfBirth)) {
                System.out.println("Details successfully recorded");
            } else {
                System.out.println("Did not find the user with username \""+username+"\". Maybe the details were edited by another administrator while you were also editing.");                
            }
        } catch (Exception e) {
            System.err.println("An error has been encountered with your inputs. Please hit r to try again.");
            String retry = scanner.nextLine().strip();
            if (retry.toLowerCase().equals("r")) {
                updateUserOfUsername();
            }
        }
    }

    private void deleteUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = scanner.nextLine().trim();
            while (username.length() < MIN_USERNAME_LENGTH) {
                System.out.println("Username entered must have at least "+MIN_USERNAME_LENGTH+" letters. Please try again: ");
                username = scanner.nextLine().trim();
            }
            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            userService.deleteUser(username);
            System.out.println("Successfully deleted the user @" + username);
        } catch (Exception e) {
            System.err.println("An error has been encountered with your inputs. Please hit r to try again.");
            String retry = scanner.nextLine().strip();
            if (retry.toLowerCase().equals("r")) {
                deleteUserOfUsername();
            }
        }
    }

    private void deleteAllUsers() {
        System.out.println("Are you sure? This action is irreversible!  (y/n)");
        switch (scanner.nextLine().trim().toLowerCase()) {
            case "y":
                userService.deleteAllUsers();
                System.out.println("All users successfully deleted!");
                break;
            default:
                break;
        }
    }
}
