package org.pahappa.systems.registrationapp.views;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

public class UserView {

    private final Scanner scanner;
    private final UserService userService;

    public UserView(){
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
    }


    public void displayMenu() {
        System.out.println("********* User Registration System *********");
        boolean running = true;
        //seed a user (quick testing purposes)
        User seededUser = new User();
        seededUser.setFirstname("The");
        seededUser.setLastname("Admin");
        seededUser.setUsername("admin");
        seededUser.setDateOfBirth(new Date(100,1,1));
        try {
            userService.registerUser(seededUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                System.out.println("Invalid choice. Please try again.\n");
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
        return input.replaceAll("[ \u00a0]",""); //space & nbsp
    }

    private void registerUser() {
        System.out.println("Provide the following details:");
        try {
            User newUser = new User();
            String firstname = "";
            System.out.println("First Name (required):");
            while (true) {
                try {
                    firstname = userService.validateName(spacesCleaner(scanner.nextLine().strip()));
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Last Name:");
            String lastname = spacesCleaner(scanner.nextLine().strip());
            if (lastname.contains("[0-9]*")) {
                lastname = lastname.replaceAll("[0-9]*","");
            }

            System.out.println("Username (required):");
            String username = null;
            while (true) {
                try {
                    username = userService.validateUsername(spacesCleaner(scanner.nextLine().strip()));
                    if (!userService.usernameExists(username)) break;
                    throw new UsernameException("The username you have provided already exists");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Date Of Birth (required) (dd/mm/yyyy):");
            Date dateOfBirth = null;
            while (true) {
                try {
                    dateOfBirth = userService.validateDateOfBirth(scanner.nextLine().strip());
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            newUser.setFirstname(firstname);
            newUser.setLastname(lastname);
            newUser.setUsername(username);
            newUser.setDateOfBirth(dateOfBirth);
            userService.registerUser(newUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) registerUser();
        }
    }

    private void displayAllUsers() {
        List<User> listOfUsers = userService.getListOfUsers();
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
            String username = null;
            User user;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine().strip());
                    user = userService.getUserByUsername(username);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("First Name:    " + user.getFirstname());
            System.out.println("Last Name:     " + user.getLastname());
            System.out.println("Username:      " + user.getUsername());
            System.out.println("Date Of Birth: " + user.getDateOfBirth().toString().replaceFirst("00:00:00 UTC ", ""));

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            String username = null;
            User user = null;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine().strip());
                    user = userService.getUserByUsername(username);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            System.out.println("Press Enter to keep the (previous) values.\n");
            System.out.println("First Name ("+user.getFirstname()+"):");
            String firstname = "";
            while (true) {
                firstname = scanOrNull();
                if (firstname == null) break;
                try {
                    firstname = userService.validateName(firstname);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Last Name("+user.getLastname()+"):");
            String lastname = scanOrNull();

            System.out.println("Username("+user.getUsername()+"):");
            String newUsername = null;
            while (true){
                newUsername = scanOrNull();
                if (newUsername == null) break;
                try {
                    newUsername = userService.validateUsername(newUsername);
                    if (!userService.usernameExists(newUsername)) break;
                    throw new UsernameException("The username you have provided already exists");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            Date oldDate = user.getDateOfBirth();
            String dateOfBirthString = "";
            Date dateOfBirth = null;
            System.out.println("Date Of Birth ("+ oldDate.getDate() +"/"+ (oldDate.getMonth()+1) +"/"+ (oldDate.getYear()+1900) +"):");
            while (true) {
                dateOfBirthString = scanOrNull();
                if (dateOfBirthString == null) break;
                try {
                    dateOfBirth = userService.validateDateOfBirth(dateOfBirthString);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (userService.updateDetailsOfUser(username, firstname, lastname, newUsername, dateOfBirth)) {
                System.out.println("Details successfully recorded");
            } else {
                System.out.println("Did not find the user with username \""+username+"\". Maybe the details were edited by another administrator while you were also editing.");                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) updateUserOfUsername();
        }
    }

    private void deleteUserOfUsername() {
        try {
            System.out.println("Provide the username below:");
            String username = null;
            User user = null;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine().strip());
                    user = userService.getUserByUsername(username);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
                return;
            }
            userService.deleteUser(username);
            System.out.println("Successfully deleted the user \"" + username + "\"");
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
