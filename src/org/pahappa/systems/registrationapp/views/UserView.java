package org.pahappa.systems.registrationapp.views;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class UserView {

    private final Scanner scanner;

    public UserView(){
        this.scanner = new Scanner(System.in);
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
            String firstname = scanner.nextLine();
            System.out.println("Last Name:");
            String lastname = scanner.nextLine();
            System.out.println("Username:");
            String username = "";
            boolean usernameIsUnique = false;
            while (!usernameIsUnique){
                username = scanner.nextLine();
                //check for unique username
                usernameIsUnique = true;
            }
            System.out.println("Date Of Birth (dd/mm/yyyy):");
            String dateOfBirthString = scanner.nextLine();
            String[] dateData = dateOfBirthString.split("/");
            int year = Integer.parseInt(dateData[2]) - 1900;
            int month = Integer.parseInt(dateData[1]) - 1;
            int date = Integer.parseInt(dateData[0]);
            Date dateOfBirth = new Date(year,month,date);
        } catch (Exception e) {
            System.err.println("An error has been encountered with your inputs. Please try again.");
            registerUser();
        }
    }

    private void displayAllUsers() {
    }

    private void getUserOfUsername() {
    }

    private void updateUserOfUsername() {
    }

    private void deleteUserOfUsername() {
    }

    private void deleteAllUsers() {
    }
}
