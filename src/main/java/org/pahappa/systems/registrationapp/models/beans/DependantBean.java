package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Gender;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.pahappa.systems.registrationapp.models.beans.AuthBean.getSessionUser;
import static org.pahappa.systems.registrationapp.models.beans.MainBean.container;
import static org.pahappa.systems.registrationapp.models.beans.MainBean.redirect;

@ManagedBean(name = "depBean")
@SessionScoped
public class DependantBean {
    private String username;
    private String firstname;
    private String lastname;
    private Gender gender;
    private List<Gender> gnd;
    private Date dateOfBirth;
    private User owner;
    private static final DependantService dependantService = new DependantService();
    private static final UserService userService = new UserService();
    private static Dependant dependantToEdit;
    private static User dependantOwner;


    public DependantBean() {
        setGnd(List.of(Gender.Male,Gender.Female));
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public static List<Dependant> getDependants() {
        return dependantService.getListOfDependants(false);
    }
    public static List<Dependant> getDependantsOfUser(User user) {
        return dependantService.getDependantsOfUser(user);
    }
    public static List<Dependant> getDependantsOfUser(String username) throws Exception {
        return getDependantsOfUser(userService.getUserByUsername(username));
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public static Dependant getDependantToEdit() {
        return dependantToEdit;
    }
    public static void setDependantToEdit(Dependant dependantToEdit) {
        DependantBean.dependantToEdit = dependantToEdit;
    }
    public static User getDependantOwner() {
        return dependantOwner;
    }
    public static void setDependantOwner(User dependantOwner) {
        DependantBean.dependantOwner = dependantOwner;
    }
    public List<Gender> getGnd() {
        return gnd;
    }
    public void setGnd(List<Gender> gnd) {
        this.gnd = gnd;
    }

    public static void loadDependants(User user) {
        user.setDependants(getDependantsOfUser(user));
    }

    public void addDependantToUser() {
        Dependant d = new Dependant();
        d.setUsername(username);
        d.setGender(gender);
        d.setDateOfBirth(dateOfBirth);
        d.setFirstname(firstname);
        d.setLastname(lastname);
        d.setOwner(dependantOwner);
        dependantService.registerDependant(d);
        dependantOwner = null;
        new UserBean().setupSettings();
    }

    public void resetValues() {
        setUsername(null);
        setFirstname(null);
        setLastname(null);
        setGender(null);
        setDateOfBirth(null);
    }

    public void edit(String username,String ownerUsername) {
        container(() -> {
            setDependantOwner(userService.getUserByUsername(ownerUsername));
            Dependant d = dependantService.getDependantByUsername(username);
            setDependantToEdit(d);
            setGender(d.getGender());
            redirect("/pages/dependant/view");
        });
    }
    public void delete(String username) {
        container(() -> {
            dependantService.deleteDependant(dependantService.getDependantByUsername(username));
            MainBean.refreshDependants();
            AuthBean.goHomeIfAuthenticated();
        });
    }
    public static void deleteAll() {
        dependantService.deleteAllDependants();
    }
    public void save() {
        container(() -> {
            if (!(firstname==null||firstname.isEmpty())) dependantToEdit.setFirstname(userService.validateName(firstname));
            if (!(username==null||username.isEmpty())) dependantToEdit.setUsername(userService.validateUsername(username));
            if (dateOfBirth != null) dependantToEdit.setDateOfBirth(dependantService.validateDateOfBirth(dateOfBirth));
            if (!(lastname==null||lastname.isEmpty())) dependantToEdit.setLastname(userService.validateName(lastname));
            if (gender != null) dependantToEdit.setGender(gender);
            dependantService.updateDependant(dependantToEdit);
        });
    }
}
