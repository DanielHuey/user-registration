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
import java.util.Scanner;

public class DependantView {
    DependantService dependantService;
    Scanner scanner;
    
    public DependantView() {
        dependantService = new DependantService();
        scanner = new Scanner(System.in);
    }
    
    protected void registerDependant(User user) {
        System.out.println("Provide the following details:");
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

        System.out.println("Gender (default=m)");
        newDependant.setGender(Gender.Male);
        if (scanner.nextLine().replaceAll("[ \u00a0]*","").equalsIgnoreCase("f"))
            newDependant.setGender(Gender.Female);

        System.out.println("Username (required):");
        String username;
        while (true) {
            try {
                username = dependantService.validateUsername(scanner.nextLine().replaceAll("[ \u00a0]",""));
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

    private Dependant getDependantOfUsername() {
        try {
            quitMessage();
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

    private void quitMessage() {System.out.println("Proceed with selected action? (y/n): ");}

    private String spacesCleaner(String s) {return s.strip().replaceAll("[ \u00a0]*","");}
}
