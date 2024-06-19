package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.exception.ExitException;
import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Gender;
import org.pahappa.systems.registrationapp.services.DependantService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DependantView {
    DependantService dependantService;
    Scanner scanner;
    UserView userView;
    
    public DependantView() {
        dependantService = new DependantService();
        scanner = new Scanner(System.in);
        userView = new UserView();
    }

    public void displayMenu() {
        System.out.println("\nDependant Menu:");
        System.out.println("1. Add a dependant to a user");
        System.out.println("2. List dependants of a user");
        System.out.println("3. Delete a dependant");
//        System.out.println("4. Update user details of username");
//        System.out.println("5. Delete User of username");
//        System.out.println("6. Delete all users");
//        System.out.println("7. ");
//        System.out.println("8. ");
        System.out.println("Anything Else: Exit");
        try{
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (choice) {
                case 1:
                    addDependant();
                    break;
                case 2:
                    listUserDependants();
                    break;
                case 3:
                    deleteDependant();
                    break;
                default:
                    System.out.println("Returning to User Menu");
                    break;
            }
        }catch (Exception e){
            System.out.println("Returning to User Menu");
        }
    }

    private void deleteDependant() {
        try {
            Dependant dependant = getDependantOfUsername();
            if (dependant != null) {
                dependantService.deleteDependant(dependant);
            } else throw new Exception("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userView.retry()) deleteDependant();
        }
    }

    private void addDependant() {
        try {
            User user = userView.getUserOfUsername();
            if (user != null) {
                registerDependant(user);
            } else throw new Exception("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userView.retry()) addDependant();
        }
    }

    private void registerDependant(User user) {
        System.out.println("\nProvide the following details for the dependant:");
        Dependant newDependant = new Dependant();
        String firstname;
        System.out.println("First Name (required):");
        while (true) {
            try {
                firstname = dependantService.validateName(scanner.nextLine().replaceAll("[ \u00a0]*",""));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Last Name:");
        String lastname = scanner.nextLine().replaceAll("[ \u00a00-9]*","");

        System.out.println("Gender (default=m) (m/f)");
        newDependant.setGender(Gender.Male);
        if (spacesCleaner(scanner.nextLine()).equalsIgnoreCase("f"))
            newDependant.setGender(Gender.Female);

        System.out.println("Username (required):");
        String username;
        while (true) {
            try {
                username = dependantService.validateUsername(spacesCleaner(scanner.nextLine()));
                if (!dependantService.usernameExists(username)) break;
                throw new UsernameException("The username you have provided already exists");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Date Of Birth (required) (dd/mm/yyyy):");
        Date dateOfBirth;
        while (true) {
            try {
                dateOfBirth = dependantService.validateDateOfBirth(scanner.nextLine().strip());
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        newDependant.setFirstname(firstname);
        newDependant.setLastname(lastname);
        newDependant.setUsername(username);
        newDependant.setDateOfBirth(dateOfBirth);
        newDependant.setOwner(user);
        dependantService.registerDependant(newDependant);
    }

    private void listUserDependants() {
        try {
            User user = userView.getUserOfUsername();
            if (user != null) {
                List<Dependant> dependantList = dependantService.getDependantsOfUser(user);
                System.out.printf("Dependants for [%s]:%n",user.getUsername());
                for (Dependant dependant: dependantList) {
                    System.out.printf("%s%n", dependant);
                }
            }
            else throw new Exception("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userView.retry()) listUserDependants();
        }
    }

    protected Dependant getDependantOfUsername() {
        try {
            userView.quitMessage();
            dependantService.isQuitting(spacesCleaner(scanner.nextLine()));
            System.out.println("Provide the username below:");
            String username;
            Dependant dependant;
            while (true) {
                try {
                    username = spacesCleaner(scanner.nextLine());
                    dependant = dependantService.getDependantByUsername(username);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (dependant == null) {
                System.out.println("There is no dependant with the username \"" + username + "\"");
            } else {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println("First Name:    " + dependant.getFirstname());
                System.out.println("Last Name:     " + dependant.getLastname());
                System.out.println("Username:      " + dependant.getUsername());
                System.out.println("Gender:        " + dependant.getGender());
                System.out.println("Date Of Birth: " + df.format(dependant.getDateOfBirth()));
            }
            return dependant;
        } catch (ExitException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String spacesCleaner(String s) {return s.strip().replaceAll("[ \u00a0]*","");}
}
