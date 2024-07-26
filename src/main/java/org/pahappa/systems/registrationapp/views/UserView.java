package org.pahappa.systems.registrationapp.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Role;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.swing.text.DateFormatter;

public class UserView {

    private final Scanner scanner;
    private final UserService userService;
//    private final DependantView dependantView = new DependantView();

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
            System.out.println("7. Dependant Menu");
            System.out.println("8. Exit");
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
                        DependantView.displayMenu();
                        break;
                    case 8:
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
    boolean retry() {
        System.out.println("An error has been encountered with your inputs. Please hit r to try again.");
        String retry = scanner.nextLine().strip();
        return retry.equalsIgnoreCase("r");
    }

    /** Removes all spaces from the input string */
    private String spacesCleaner(String input) {
        return input.strip().replaceAll("[ \u00a0]",""); //space & nbsp
    }

    private void registerUser() {
        try {
            quitMessage();
            userService.isQuitting(spacesCleaner(scanner.nextLine()));
            System.out.println("Provide the following details:");
            User newUser = new User();
            String firstname;
            System.out.println("First Name (required):");
            while (true) {
                try {
                    firstname = userService.validateName(spacesCleaner(scanner.nextLine()));
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("Last Name:");
            String lastname = spacesCleaner(scanner.nextLine()).replaceAll("[0-9]*","");

            System.out.println("Username (required):");
            String username;
            while (true) {
                try {
                    username = userService.validateUsername(spacesCleaner(scanner.nextLine()));
                    if (!userService.usernameExists(username)) break;
                    throw new UsernameException("The username you have provided already exists");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Date Of Birth (required) (dd/mm/yyyy):");
            Date dateOfBirth;
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
        } catch (ExitException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) registerUser();
        }
    }

    void quitMessage() {System.out.println("Proceed with selected action? (y/n): ");}

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

    User getUserOfUsername() {
        try {
            quitMessage();
            userService.isQuitting(spacesCleaner(scanner.nextLine()));
            System.out.println("Provide the username below:");
            String username;
            User user;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine());
                    user = userService.getUserByUsername(username);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (user == null) {
                System.out.println("There is no user with the username \"" + username + "\"");
            } else {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println("First Name:    " + user.getFirstname());
                System.out.println("Last Name:     " + user.getLastname());
                System.out.println("Username:      " + user.getUsername());
                System.out.println("Date Of Birth: " + df.format(user.getDateOfBirth()));
            }
            return user;
        } catch (ExitException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) getUserOfUsername();
        }
        return null;
    }

    /** returns a string or null */
    private String scanOrNull() {
        String input = scanner.nextLine().strip();
        if (input.isEmpty()) return null;
        return input;
    }

    private void updateUserOfUsername() {
        try {
            quitMessage();
            userService.isQuitting(spacesCleaner(scanner.nextLine()));
            System.out.println("Provide the username below:");
            String username;
            User user;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine());
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
            String firstname;
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
            lastname = lastname != null ? lastname.replaceAll("[0-9]*","") : null;

            System.out.println("Username("+user.getUsername()+"):");
            String newUsername;
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
            Date oldDate = userService.validateDateOfBirth(user.getDateOfBirth());
            String dateOfBirthString;
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
            if (userService.updateDetailsOfUser(username, firstname, lastname, newUsername, dateOfBirth,"","", Role.Default)) {
                System.out.println("Details successfully recorded");
            } else {
                System.out.println("Did not find the user with username \""+username+"\". Maybe the details were edited by another administrator while you were also editing.");                
            }
        } catch (ExitException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) updateUserOfUsername();
        }
    }

    private void deleteUserOfUsername() {
        try {
            quitMessage();
            userService.isQuitting(spacesCleaner(scanner.nextLine()));
            System.out.println("Provide the username below:");
            String username;
            User user;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine());
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
        } catch (ExitException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (retry()) deleteUserOfUsername();
        }
    }

    private void deleteAllUsers() {
        System.out.println("Are you sure? This action is irreversible!  (y/n)");
        if (scanner.nextLine().strip().equalsIgnoreCase("y")) {
            userService.deleteAllUsers();
            System.out.println("All users successfully deleted!");
        }
    }
}
